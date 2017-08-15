package com.zzb.conf.service.impl;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.service.INSCCodeService;
import com.zzb.app.model.ImageManagerModel;
import com.zzb.cm.controller.vo.InsRecordNumberVO;
import com.zzb.cm.dao.*;
import com.zzb.cm.entity.*;
import com.zzb.cm.service.INSBQuoteinfoService;
import com.zzb.conf.dao.INSBOrderpaymentDao;
import com.zzb.conf.dao.INSBPolicyitemDao;
import com.zzb.conf.dao.INSBProviderDao;
import com.zzb.conf.entity.*;
import com.zzb.conf.service.INSBPolicyitemService;
import com.zzb.conf.service.INSBWorkflowmainService;
import com.zzb.conf.service.INSBWorkflowsubService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
@Transactional
public class INSBPolicyitemServiceImpl extends BaseServiceImpl<INSBPolicyitem> implements
		INSBPolicyitemService {
	@Resource
	private INSBPolicyitemDao insbPolicyitemDao;
	@Resource 
	private INSBOrderDao insbOrderDao;
	@Resource
	private INSBCarinfoDao insbcarDao;
	@Resource
	private INSBFilebusinessDao insbFileBusinessDao;
	@Resource
	private INSBFilelibraryDao insbFileLibraryDao;
	@Resource
	private INSBOrderpaymentDao orderpaymentDao;
	@Resource
	private INSBProviderDao insbProviderDao;
    @Resource
    private INSBCarkindpriceDao insbCarkindpriceDao;
    @Resource
    private INSBCarinfohisDao insbCarinfohisDao;
    @Resource
    INSBFilebusinessDao insbFilebusinessDao;
    @Resource
    INSBFilelibraryDao insbFilelibraryDao;

    @Override
	protected BaseDao<INSBPolicyitem> getBaseDao() {
		return insbPolicyitemDao;
	}
	
	private static ResourceBundle resourceBundle;
	static {
		// 读取相关的配置
		resourceBundle = ResourceBundle
				.getBundle("config/policyno");
	}
	
	/**
	 * 通过任务id查询保单信息
	 */
	@Override
	public Map<String, Object> getPolicyNumInfo(String taskId) {
		Map<String, Object> temp = new HashMap<String, Object>();
		temp.put("taskid", taskId);
		INSBPolicyitem policyitem = new INSBPolicyitem();
		policyitem.setTaskid(taskId);
		List<INSBPolicyitem> policyList = insbPolicyitemDao.selectList(policyitem);
		if(policyList!=null && policyList.size()>0){
			for (INSBPolicyitem plo : policyList) {
				if("0".equals(plo.getRisktype())){
					if(plo.getPolicyno()!=null){
						temp.put("businessPolicyNum", plo.getPolicyno());
					}
					if(plo.getPaynum()!=null){
						temp.put("payNum", plo.getPaynum());
					}
					if(plo.getCheckcode()!=null){
						temp.put("checkCode", plo.getCheckcode());
					}
					if(plo.getProposalformno()!=null){
						temp.put("businessProposalFormNo", plo.getProposalformno());
					}
				}else if("1".equals(plo.getRisktype())){
					if(plo.getPolicyno()!=null){
						temp.put("strongPolicyNum", plo.getPolicyno());
					}if(plo.getProposalformno()!=null){
						temp.put("strongProposalFormNo", plo.getProposalformno());
					}
				}
			}
		}
		return temp;
	}
	
	/**
	 * 修改投保单信息
	 * @param insRecordNumber
	 */
	@Override
	public String editPolicyNumInfo(InsRecordNumberVO insRecordNumber) {
		Date date = new Date();
		//得到订单id
		Map<String,Object> maporder = new HashMap<String,Object>();
		maporder.put("taskid", insRecordNumber.getTaskid());
		maporder.put("inscomcode", insRecordNumber.getInscomcode());              //保险公司id
		INSBOrder order = insbOrderDao.selectOrderByTaskId(maporder);
		INSBOrderpayment insbOrderpayment=new INSBOrderpayment();
		insbOrderpayment.setOrderid(order.getId());
		insbOrderpayment =orderpaymentDao.selectOne(insbOrderpayment);
		if(insbOrderpayment!=null){
			insbOrderpayment.setInsurecono(insRecordNumber.getPayNum());
			orderpaymentDao.updateById(insbOrderpayment);
		}
		if(order!=null){
			//得到投保单信息
			INSBPolicyitem policyitem = new INSBPolicyitem();
			policyitem.setOrderid(order.getId());
			List<INSBPolicyitem> policyList = insbPolicyitemDao.selectList(policyitem);
			INSBPolicyitem plo=null; 
			if(policyList!=null && policyList.size()>0){
				for (int i = 0; i < policyList.size(); i++) {
					plo= policyList.get(i);
					if(plo!=null){
						plo.setOperator(insRecordNumber.getOperator());
						plo.setModifytime(date);
						if("0".equals(plo.getRisktype())){
							//商业险保单号修改
							plo.setPolicyno(insRecordNumber.getBusinessPolicyNum());
							plo.setPolicyno(insRecordNumber.getStrongPolicyNum());
							plo.setCheckcode(insRecordNumber.getCheckCode());
						}else if("1".equals(plo.getRisktype())){
							//交强险保单号修改
							plo.setPolicyno(insRecordNumber.getBusinessPolicyNum());
							plo.setPolicyno(insRecordNumber.getStrongPolicyNum());
							plo.setCheckcode(insRecordNumber.getCheckCode());
						}
						insbPolicyitemDao.updateById(plo);
					}
				}
				
			}
		}
		return "success";
	}
	
	/**
	 * 根据 代理人信息查询保单图像list
	 * @see com.zzb.conf.service.INSBPolicyitemService#getImageInfoList(java.lang.String)
	 */
	@Override
	public String getImageInfoList(String agentid) {
		List<Map<String, Object>> image = new ArrayList<Map<String,Object>>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		JSONObject jsonObject = null;
		try {
			List<Map<String, Object>> imageList = insbPolicyitemDao.getImageInfoList(agentid);
			for (Map<String, Object> m: imageList) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", m.get("id"));//保单id
				map.put("carlicenseno", m.get("carlicenseno"));//车牌号
				map.put("insuredname", m.get("insuredname"));//被保人
				map.put("createtime", m.get("createtime"));//创建时间
				image.add(map);
			}
			resultMap.put("result", image);
			jsonObject = JSONObject.fromObject(resultMap);
		} catch (Exception e) {
			resultMap.put("result", "error");
			jsonObject = JSONObject.fromObject(resultMap);
		}
		return jsonObject.toString();
	}
	
	/** 根据投保单id查找车主和车辆信息
	 * @see com.zzb.conf.service.INSBPolicyitemService#getImageInfo(java.lang.String)
	 */
	@Override
	public String getImageInfo(String policyid) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			ImageManagerModel model = insbPolicyitemDao.queryModelList(policyid);
			if(model!=null){
				result.put("STATUS", "0");
				result.put("MESSAGE", "操作成功");
				Map<String, Object> albuminfo = new HashMap<String, Object>(); 
				albuminfo.put("ACCESSID", "img_com");//？？
				albuminfo.put("BUCKET", "img_com");//？？
				albuminfo.put("ORGID", "1244191733");//？？
				albuminfo.put("AGENTCODE", "620005858");//？？
				albuminfo.put("BIZNO", "a98a8348868148719ef5e00b970ff723");//？？
				albuminfo.put("CAROWNER", model.getCarowner());//车主
				albuminfo.put("PLATENUM", model.getCarowner());//车牌号
				albuminfo.put("VINNO", model.getPlatenum());//车架号
				albuminfo.put("RECOGNIZEE", model.getRecognizee());//被保人
				albuminfo.put("AGENTNAME", model.getAgentname());//代理人名称
				result.put("ALBUMINFO", albuminfo);
			}else{
				result.put("STATUS", "0");
				result.put("MESSAGE", "没有数据");
			}
		} catch (Exception e) {
			result.put("STATUS", "-1");
			result.put("MESSAGE", "操作失败");
		}
		List<Map<Object, Object>> imageList = insbPolicyitemDao.queryImageList(policyid);
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		for (Map<Object,Object> m :imageList  ) {
			Map<String, Object> IMAGELIST = new HashMap<String, Object>();
			IMAGELIST.put("KEY", m.get("codetype"));
			IMAGELIST.put("URL", m.get("filepath"));
			IMAGELIST.put("NAME", m.get("codename"));
			list.add(IMAGELIST);
		}
		result.put("IMAGELIST", list);
		JSONObject jsonObject = JSONObject.fromObject(result);
		System.out.println(jsonObject.toString()+"imageAPI");
		return jsonObject.toString();
	}
	/** 
	 * 添加影像信息
	 * @see com.zzb.conf.service.INSBPolicyitemService#cos(com.zzb.app.model.ImageManagerModel)
	 */
	@Override
	public String cos(ImageManagerModel model) {
		Map<String, String> result = new HashMap<String, String>();
		Date date = new Date();
		try {
			String operator = insbPolicyitemDao.selectPolicyitemByTaskId(model.getTaskid()).getAgentname();
//			车辆表信息
			INSBCarinfo carinfo = new INSBCarinfo();
			String carinfoid = UUIDUtils.random();
			carinfo.setId(carinfoid);
			carinfo.setOwnername(model.getCarowner());//车主
			carinfo.setCarlicenseno(model.getPlatenum());//车牌号
			carinfo.setVincode(model.getVinno());//车架号
			carinfo.setTaskid(model.getTaskid());//暂时没有
			carinfo.setCreatetime(date);
			carinfo.setOperator(operator);
			insbcarDao.insert(carinfo);
//			保单表信息
			INSBPolicyitem po = new INSBPolicyitem();
			String poid = UUIDUtils.random();
			po.setId(poid);
			po.setCarinfoid(carinfoid);//车辆信息表的id
			po.setInsuredname(model.getRecognizee());//被保人
			po.setAgentname(model.getAgentname());//代理人名称
			po.setTaskid(model.getTaskid());//任务id
			po.setRisktype("1");//1交强险0商业险
			po.setCreatetime(date);
			po.setOperator(operator);
			insbPolicyitemDao.insert(po);
//			影像表信息（两张）
			INSBFilelibrary lib = new INSBFilelibrary();
			String filelibraryid = UUIDUtils.random();
			lib.setId(filelibraryid);
			lib.setFiletype(model.getKey());//影像类型
			lib.setFilepath(model.getUrl());//影像地址
			lib.setFilename("filename");//应该是有的
			lib.setCreatetime(date);
			lib.setOperator(operator);
			this.replaceHttpToHttps(lib);
			insbFileLibraryDao.insert(lib);
			INSBFilebusiness bus = new INSBFilebusiness();
			bus.setId(UUIDUtils.random());
			bus.setFilelibraryid(filelibraryid);//取lib表的id
			bus.setCode(poid);// 取保单表的id
			bus.setType("1");//??
			bus.setCreatetime(date);
			bus.setOperator(operator);
			insbFileBusinessDao.insert(bus);
			result.put("STATUS", "0");
			result.put("MESSAGE","操作成功");
			JSONObject jsonObject = JSONObject.fromObject(result);
			return jsonObject.toString();
		} catch (Exception e) {
			result.put("STATUS", "1");
			result.put("MESSAGE","操作失败");
			JSONObject jsonObject = JSONObject.fromObject(result);
			return jsonObject.toString();
		}
		
	}
	
	public void replaceHttpToHttps(INSBFilelibrary insbFilelibrary){
		String filepath = insbFilelibrary.getFilepath();
		String smallfilepath = insbFilelibrary.getSmallfilepath();
		if( !StringUtil.isEmpty(filepath) ){
			if(!filepath.startsWith("https")){
				filepath.replace("http", "https");
				insbFilelibrary.setFilepath(filepath);
			}
		}
		if( !StringUtil.isEmpty(smallfilepath) ){
			if(!smallfilepath.startsWith("https")){
				smallfilepath.replace("http", "https");
				insbFilelibrary.setSmallfilepath(smallfilepath);
			}
		}
	}

	@Override
	public String updatePolicyNumInfo(String ciPolicyNo, String biPolicyNo, String taskid,String inscomcode) {
        LogUtil.info("==修改保单号：taskid=" + taskid + ",inscomcode=" + inscomcode + ",ciPolicyNo：" + ciPolicyNo + "，biPolicyNo：" + biPolicyNo);

        Map<String,Object> result = new HashMap<String,Object>();
		//去前后空格
		if(biPolicyNo!=null){
			biPolicyNo = biPolicyNo.trim();
		}
		if(ciPolicyNo!=null){
			ciPolicyNo = ciPolicyNo.trim();
		}
        if(StringUtils.isEmpty(biPolicyNo) && StringUtils.isEmpty(ciPolicyNo)){
            //没有填写数据不作处理
            result.put("status", "fail");
            result.put("msg", "没有填写保单号数据！");
            return JSONObject.fromObject(result).toString();
        }

        //组织查询参数
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("taskid", taskid);
		params.put("inscomcode", inscomcode);
		List<INSBPolicyitem> findp = null;
		List<String> pnoList = null;
		String policynoLength = null;
		INSBProvider provider = insbProviderDao.selectFatherProvider(inscomcode);
		if(provider!=null){
			policynoLength = resourceBundle.getString("policyno.length."+provider.getId());
			if(!StringUtils.isEmpty(policynoLength)){
				pnoList = Arrays.asList(policynoLength.split(","));
			}
		}

		//校验商业险保单号
		if(!StringUtils.isEmpty(biPolicyNo)){
			//校验保单号长度
			if(pnoList!=null && (!pnoList.contains(biPolicyNo.length()+""))){
				result.put("status", "fail");
				result.put("msg", "商业险保单号长度应为"+policynoLength+"！");

                LogUtil.error("==修改保单号：taskid=" + taskid + ",inscomcode=" + inscomcode + ",商业险保单号长度不合法:" + biPolicyNo.length());
                return JSONObject.fromObject(result).toString();
			}
			//hxx 保单号重复校验去掉
			//校验是否重复保单号
//			findp = insbPolicyitemDao.getListByPolicyno(biPolicyNo, taskid);
//			if(findp!=null && findp.size()>0){
//				result.put("status", "fail");
//				result.put("msg", "商业险保单号已存在！");
//
//                LogUtil.error("==修改保单号：taskid=" + taskid + ",inscomcode=" + inscomcode + ",商业险保单号已存在");
//				return JSONObject.fromObject(result).toString();
//			}
		}
		//校验交强险保单号
		if(!StringUtils.isEmpty(ciPolicyNo)){
			//校验保单号长度
			if(pnoList!=null && (!pnoList.contains(ciPolicyNo.length()+""))){
				result.put("status", "fail");
				result.put("msg", "交强险保单号长度应为"+policynoLength+"！");

                LogUtil.error("==修改保单号：taskid=" + taskid + ",inscomcode=" + inscomcode + ",交强险保单号长度不合法:" + ciPolicyNo.length());
				return JSONObject.fromObject(result).toString();
			}
			//hxx 保单号重复校验去掉
			//校验是否重复保单号
//			findp = insbPolicyitemDao.getListByPolicyno(ciPolicyNo, taskid);
//			if(findp!=null && findp.size()>0){
//				result.put("status", "fail");
//				result.put("msg", "交强险保单号已存在！");
//
//                LogUtil.error("==修改保单号：taskid=" + taskid + ",inscomcode=" + inscomcode + ",交强险保单号已存在");
//				return JSONObject.fromObject(result).toString();
//			}
		}
		INSBWorkflowmain queryInsbWorkflowmain = new INSBWorkflowmain();
		queryInsbWorkflowmain.setInstanceid(taskid);
		queryInsbWorkflowmain = insbWorkflowmainService.queryOne(queryInsbWorkflowmain);
		if(!StringUtil.isEmpty(queryInsbWorkflowmain)){
			if(!"23".equals(queryInsbWorkflowmain.getTaskcode())&&!"27".equals(queryInsbWorkflowmain.getTaskcode())){
				INSCCode queryInscCode = new INSCCode();
				queryInscCode.setCodetype("workflowNodelName");
				queryInscCode.setCodevalue(queryInsbWorkflowmain.getTaskcode());
				queryInscCode = inscCodeService.queryOne(queryInscCode);
				if(!StringUtil.isEmpty(queryInscCode)){
					result.put("msg", "该任务当前流程节点是【" + queryInscCode.getCodename() + "】,不能修改投保单号！");
				}else{
					result.put("msg", "该任务当前流程节点不能修改投保单号！");
				}
				result.put("status", "fail");
				return JSONObject.fromObject(result).toString();
			}
		}

        //数据修改
        List<INSBPolicyitem> policyitemList = insbPolicyitemDao.selectPolicyitemByInscomTask(params);
        for (INSBPolicyitem temp : policyitemList) {
            temp.setModifytime(new Date());
            if("0".equals(temp.getRisktype())){
                if(!StringUtils.isEmpty(biPolicyNo)){
                    LogUtil.info("==商业险=修改单号=taskid=" + taskid + ",inscomcode=" + inscomcode + ",商业险保单号由：" + temp.getPolicyno() + "改为：" + biPolicyNo);
                    temp.setPolicyno(biPolicyNo);
                    insbPolicyitemDao.updateById(temp);
                }
            }else if("1".equals(temp.getRisktype())){
                if(!StringUtils.isEmpty(ciPolicyNo)){
                    LogUtil.info("==交强险=修改单号=taskid=" + taskid + ",inscomcode=" + inscomcode + ",交强险保单号由：" + temp.getPolicyno() + "改为：" + ciPolicyNo);
                    temp.setPolicyno(ciPolicyNo);
                    insbPolicyitemDao.updateById(temp);
                }
            }

        }

        result.put("status", "success");
        return JSONObject.fromObject(result).toString();
	}
	@Override
	public String updatePolicyNumInfo1(String ciPolicyNo, String biPolicyNo, String taskid,String inscomcode) {
		LogUtil.info("==修改保单号：taskid=" + taskid + ",inscomcode=" + inscomcode + ",ciPolicyNo：" + ciPolicyNo + "，biPolicyNo：" + biPolicyNo);
		
		Map<String,Object> result = new HashMap<String,Object>();
		//去前后空格
		if(biPolicyNo!=null){
			biPolicyNo = biPolicyNo.trim();
		}
		if(ciPolicyNo!=null){
			ciPolicyNo = ciPolicyNo.trim();
		}
		if(StringUtils.isEmpty(biPolicyNo) && StringUtils.isEmpty(ciPolicyNo)){
			//没有填写数据不作处理
			result.put("status", "fail");
			result.put("msg", "没有填写保单号数据！");
			return JSONObject.fromObject(result).toString();
		}
		
		//组织查询参数
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("taskid", taskid);
		params.put("inscomcode", inscomcode);
		List<INSBPolicyitem> findp = null;
		List<String> pnoList = null;
		String policynoLength = null;
		INSBProvider provider = insbProviderDao.selectFatherProvider(inscomcode);
		if(provider!=null){
			policynoLength = resourceBundle.getString("policyno.length."+provider.getId());
			if(!StringUtils.isEmpty(policynoLength)){
				pnoList = Arrays.asList(policynoLength.split(","));
			}
		}
		
		//校验商业险保单号
		if(!StringUtils.isEmpty(biPolicyNo)){
			//校验保单号长度
			if(pnoList!=null && (!pnoList.contains(biPolicyNo.length()+""))){
				result.put("status", "fail");
				result.put("msg", "商业险保单号长度应为"+policynoLength+"！");
				
				LogUtil.error("==修改保单号：taskid=" + taskid + ",inscomcode=" + inscomcode + ",商业险保单号长度不合法:" + biPolicyNo.length());
				return JSONObject.fromObject(result).toString();
			}
			//hxx 保单号重复校验去掉
			//校验是否重复保单号
//			findp = insbPolicyitemDao.getListByPolicyno(biPolicyNo, taskid);
//			if(findp!=null && findp.size()>0){
//				result.put("status", "fail");
//				result.put("msg", "商业险保单号已存在！");
//
//                LogUtil.error("==修改保单号：taskid=" + taskid + ",inscomcode=" + inscomcode + ",商业险保单号已存在");
//				return JSONObject.fromObject(result).toString();
//			}
		}
		//校验交强险保单号
		if(!StringUtils.isEmpty(ciPolicyNo)){
			//校验保单号长度
			if(pnoList!=null && (!pnoList.contains(ciPolicyNo.length()+""))){
				result.put("status", "fail");
				result.put("msg", "交强险保单号长度应为"+policynoLength+"！");
				
				LogUtil.error("==修改保单号：taskid=" + taskid + ",inscomcode=" + inscomcode + ",交强险保单号长度不合法:" + ciPolicyNo.length());
				return JSONObject.fromObject(result).toString();
			}
			//hxx 保单号重复校验去掉
			//校验是否重复保单号
//			findp = insbPolicyitemDao.getListByPolicyno(ciPolicyNo, taskid);
//			if(findp!=null && findp.size()>0){
//				result.put("status", "fail");
//				result.put("msg", "交强险保单号已存在！");
//
//                LogUtil.error("==修改保单号：taskid=" + taskid + ",inscomcode=" + inscomcode + ",交强险保单号已存在");
//				return JSONObject.fromObject(result).toString();
//			}
		}
		
		//数据修改
		List<INSBPolicyitem> policyitemList = insbPolicyitemDao.selectPolicyitemByInscomTask(params);
		for (INSBPolicyitem temp : policyitemList) {
			temp.setModifytime(new Date());
			if("0".equals(temp.getRisktype())){
				if(!StringUtils.isEmpty(biPolicyNo)){
					LogUtil.info("==商业险=修改单号=taskid=" + taskid + ",inscomcode=" + inscomcode + ",商业险保单号由：" + temp.getPolicyno() + "改为：" + biPolicyNo);
					temp.setPolicyno(biPolicyNo);
					insbPolicyitemDao.updateById(temp);
				}
			}else if("1".equals(temp.getRisktype())){
				if(!StringUtils.isEmpty(ciPolicyNo)){
					LogUtil.info("==交强险=修改单号=taskid=" + taskid + ",inscomcode=" + inscomcode + ",交强险保单号由：" + temp.getPolicyno() + "改为：" + ciPolicyNo);
					temp.setPolicyno(ciPolicyNo);
					insbPolicyitemDao.updateById(temp);
				}
			}
			
		}
		
		result.put("status", "success");
		return JSONObject.fromObject(result).toString();
	}

    @Resource INSCCodeService inscCodeService;
	@Resource INSBWorkflowsubService insbWorkflowsubService;
	@Resource INSBWorkflowmainService insbWorkflowmainService;
	@Resource INSBQuoteinfoService insbQuoteinfoService;
	@Override
	public String updateProposalNumInfo(String ciproposalno, String biproposalno,
			String taskid,String inscomcode) {
		Map<String,Object> result = new HashMap<String,Object>();
		//去前后空格
		if(biproposalno!=null){
			biproposalno = biproposalno.trim();
		}
		if(ciproposalno!=null){
			ciproposalno = ciproposalno.trim();
		}
		//组织查询参数
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("taskid", taskid);
		params.put("inscomcode", inscomcode);
		List<INSBPolicyitem> findp = null;
		INSBQuoteinfo dataInsbQuoteinfo = insbQuoteinfoService.getByTaskidAndCompanyid(taskid, inscomcode);
		if(!StringUtil.isEmpty(dataInsbQuoteinfo)){
			INSBWorkflowsub queryInsbWorkflowsub = new INSBWorkflowsub();
			queryInsbWorkflowsub.setInstanceid(dataInsbQuoteinfo.getWorkflowinstanceid());
			queryInsbWorkflowsub = insbWorkflowsubService.queryOne(queryInsbWorkflowsub);
			if(!StringUtil.isEmpty(queryInsbWorkflowsub)){
				INSBWorkflowmain insbWorkflowmain = insbWorkflowmainService.selectByInstanceId(queryInsbWorkflowsub.getMaininstanceid());
				if(!"18".equals(queryInsbWorkflowsub.getTaskcode())&&!"7".equals(queryInsbWorkflowsub.getTaskcode())&&!"8".equals(queryInsbWorkflowsub.getTaskcode())
						// 增加人工承保时可以修改投保单号功能
						&&!"27".equals(insbWorkflowmain.getTaskcode())){
					INSCCode queryInscCode = new INSCCode();
					queryInscCode.setCodetype("workflowNodelName");
					queryInscCode.setCodevalue(queryInsbWorkflowsub.getTaskcode());
					queryInscCode = inscCodeService.queryOne(queryInscCode);
					if(!StringUtil.isEmpty(queryInscCode)){
						result.put("msg", "该任务当前流程节点是【" + queryInscCode.getCodename() + "】,不能修改投保单号！");
					}else{
						result.put("msg", "该任务当前流程节点不能修改投保单号！");
					}
					result.put("status", "fail");
					return JSONObject.fromObject(result).toString();
				}
			}
		}
		//校验商业险投保单号
//		if(!StringUtils.isEmpty(biproposalno)){
//			findp = insbPolicyitemDao.getListByProposalformno(biproposalno, taskid);
//			if(findp!=null && findp.size()>0){
//				result.put("status", "fail");
//				result.put("msg", "商业险投保单号已存在！");
//				return JSONObject.fromObject(result).toString();
//			}
//		}
		//校验交强险投保单号
//		if(!StringUtils.isEmpty(ciproposalno)){
//			findp = insbPolicyitemDao.getListByProposalformno(ciproposalno, taskid);
//			if(findp!=null && findp.size()>0){
//				result.put("status", "fail");
//				result.put("msg", "交强险投保单号已存在！");
//				return JSONObject.fromObject(result).toString();
//			}
//		}

        //数据修改
        List<INSBPolicyitem> policyitemList = insbPolicyitemDao.selectPolicyitemByInscomTask(params);
        for (INSBPolicyitem temp : policyitemList) {
            temp.setModifytime(new Date());
            if("0".equals(temp.getRisktype())){
                if(!StringUtils.isEmpty(biproposalno)){
                    LogUtil.info("==商业险=修改单号=taskid=" + taskid + ",inscomcode=" + inscomcode + ",商业险投保单号由：" + temp.getProposalformno() + "改为：" + biproposalno);
                    temp.setProposalformno(biproposalno);
                    insbPolicyitemDao.updateById(temp);
                }
            }else if("1".equals(temp.getRisktype())){
                if(!StringUtils.isEmpty(ciproposalno)){
                    LogUtil.info("==交强险=修改单号=taskid=" + taskid + ",inscomcode=" + inscomcode + ",交强险投保单号由：" + temp.getProposalformno() + "改为：" + ciproposalno);
                    temp.setProposalformno(ciproposalno);
                    insbPolicyitemDao.updateById(temp);
                }
            }

        }
        result.put("status", "success");
        return JSONObject.fromObject(result).toString();
	}
	@Override
	public List<INSBPolicyitem> selectByInscomTask(Map<String, Object> map) {
		return insbPolicyitemDao.selectPolicyitemByInscomTask(map);
	}
	
	/**
	 * 通过任务id,inscomcode查询保单信息
	 */
	@Override
	public Map<String, Object> getPolicyNumInfo2(String taskId,String inscomcode) {
		Map<String, Object> temp = new HashMap<String, Object>();
		temp.put("taskid", taskId);
		temp.put("inscomcode", inscomcode);
//		INSBPolicyitem policyitem = new INSBPolicyitem();
//		policyitem.setTaskid(taskId);
//		List<INSBPolicyitem> policyList = insbPolicyitemDao.selectList(policyitem);
		List<INSBPolicyitem> policyList = selectByInscomTask(temp);
		String hasbusi = "false";
		String hasstr = "false";
		if(policyList!=null && policyList.size()>0){
			for (INSBPolicyitem plo : policyList) {
				if("0".equals(plo.getRisktype())){
					hasbusi = "true";
					if(plo.getPolicyno()!=null){
						temp.put("businessPolicyNum", plo.getPolicyno());
					}
					if(plo.getPaynum()!=null){
						temp.put("payNum", plo.getPaynum());
					}
					if(plo.getCheckcode()!=null){
						temp.put("checkCode", plo.getCheckcode());
					}
					if(plo.getProposalformno()!=null){
						temp.put("businessProposalFormNo", plo.getProposalformno());
					}
				}else if("1".equals(plo.getRisktype())){
					hasstr = "true";
					if(plo.getPolicyno()!=null){
						temp.put("strongPolicyNum", plo.getPolicyno());
					}if(plo.getProposalformno()!=null){
						temp.put("strongProposalFormNo", plo.getProposalformno());
					}
				}
			}
		}
		temp.put("hasbusi", hasbusi);
		temp.put("hasstr", hasstr);
		return temp;
	}
	@Override
	public List<INSBPolicyitem> getListByParam(Map<String, Object> param) {
		return insbPolicyitemDao.getListByParam(param);
	}
	@Override
	public Map<String, Object> policcyitembyagentid(Map<String, Object> data) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<Object, Object>> infoList = insbPolicyitemDao.policcyitembyagentid(data);
		map.put("total", insbPolicyitemDao.policcyitembyagentidcount(data));
		map.put("rows", infoList);
		return map;
	}

    /**
     * 保单号检验
     * @param ciPolicyNo 交强险单号
     * @param biPolicyNo 商业险单号
     * @param taskid 任务id
     * @param inscomcode 保险公司编码
     * @return map
     */
    @Override
    public Map<String, Object> verifyPolicyno(String ciPolicyNo, String biPolicyNo, String taskid, String inscomcode) {
        Map<String, Object> result = new HashMap<String, Object>();
        if(StringUtils.isEmpty(biPolicyNo) && StringUtils.isEmpty(ciPolicyNo)){
            //没有填写数据不作处理
            result.put("status", "fail");
            result.put("msg", "没有填写投保单号数据！");
            return result;
        }
        //查询此任务中的保险公司已选择的险别记录
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("taskid",taskid);
        paramMap.put("inscomcode",inscomcode);
        //查询商业险
        paramMap.put("inskindtype",0);
        List<INSBCarkindprice> bussiCarkindpriceList = insbCarkindpriceDao.selectCarkindpriceList(paramMap);
        if(bussiCarkindpriceList!=null&&bussiCarkindpriceList.size()>0){
            if (StringUtils.isEmpty(biPolicyNo)){
                result.put("status", "fail");
                result.put("msg", "商业险投保单号不能为空");
                return result;
            }
        }
        //快速续保
        INSBCarinfohis carinfohis = new INSBCarinfohis();
        carinfohis.setTaskid(taskid);
        carinfohis.setInscomcode(inscomcode);
        carinfohis = insbCarinfohisDao.selectOne(carinfohis);
        if(carinfohis!=null&&"1".equals(carinfohis.getInsureconfigsameaslastyear())){
            if (StringUtils.isEmpty(biPolicyNo)){
                result.put("status", "fail");
                result.put("msg", "商业险投保单号不能为空");
                return result;
            }
        }
        //查询交强险
        paramMap.put("inskindtype",2);
        List<INSBCarkindprice> strCarkindpriceList = insbCarkindpriceDao.selectCarkindpriceList(paramMap);
        if(strCarkindpriceList!=null&&strCarkindpriceList.size()>0){
            if (StringUtils.isEmpty(ciPolicyNo)){
                result.put("status", "fail");
                result.put("msg", "交强险投保单号不能为空");
                return result;
            }
        }
        result.put("status", "success");
        result.put("msg", "");
        return result;
    }
    
    public String getElecPolicyFilePath(String taskId, String type){
    	String filePath = null;
    	INSBFilebusiness fileBusiness = new INSBFilebusiness();
		fileBusiness.setCode(taskId);
		fileBusiness.setType(type);
		fileBusiness = insbFilebusinessDao.selectOne(fileBusiness);
		if(fileBusiness != null){
			INSBFilelibrary fileLib = insbFilelibraryDao.selectById(fileBusiness.getFilelibraryid());
			if(fileLib != null)
				filePath = fileLib.getFilepath();
		}
		return filePath;
    }

}