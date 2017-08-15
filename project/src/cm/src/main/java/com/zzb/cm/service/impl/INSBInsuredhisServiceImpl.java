package com.zzb.cm.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import javax.annotation.Resource;

import com.common.ConstUtil;
import com.zzb.cm.dao.INSBInsuresupplyparamDao;
import com.zzb.cm.entity.INSBInsuresupplyparam;
import com.zzb.cm.service.INSBInsuresupplyparamService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.entity.INSCUser;
import com.zzb.cm.Interface.service.ExternalInterFaceService;
import com.zzb.cm.dao.INSBInsuredhisDao;
import com.zzb.cm.dao.INSBPersonDao;
import com.zzb.cm.entity.INSBInsuredhis;
import com.zzb.cm.entity.INSBPerson;
import com.zzb.cm.service.INSBInsuredhisService;
import com.zzb.mobile.model.IDCardModel;

import net.sf.json.JSONObject;

@Service
@Transactional
public class INSBInsuredhisServiceImpl extends BaseServiceImpl<INSBInsuredhis> implements
		INSBInsuredhisService {
	@Resource
	private INSBInsuredhisDao insbInsuredhisDao;
	@Resource
	private INSBPersonDao insbPersonDao;
	@Resource
    ExternalInterFaceService externalInterFaceService;
	@Resource
	private INSBInsuresupplyparamDao insbInsuresupplyparamDao;
	@Resource
	private INSBInsuresupplyparamService insbInsuresupplyparamService;

	@Override
	protected BaseDao<INSBInsuredhis> getBaseDao() {
		return insbInsuredhisDao;
	}
	
	@Override
	public String cmApplyPin(INSCUser user, String taskid, String inscomcode){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		IDCardModel idcardmodel = new IDCardModel();
		idcardmodel.setTaskid(taskid);
		idcardmodel.setInscomcode(inscomcode);
		idcardmodel.setAgentid(user.getUsercode());
		Map<String, String> result = externalInterFaceService.checkIDCardAndGetPinCode(idcardmodel, "CM");

		//本地调试专用，直接成功
		/*Map<String, String> result = new HashedMap();
		result.put("status","success");*/

		if(result.containsKey("status") && result.get("status").equals("success")){
			/*Date start = new Date();
			boolean chaoshi = true;
			INSBInsuredhis queryTemp =  new INSBInsuredhis();
			queryTemp.setTaskid(taskid);
			queryTemp.setInscomcode(inscomcode);
			INSBInsuredhis insuredhis = null;
			String temp = null;
			try {
				LogUtil.info(taskid + "=taskid, 支付或二支CM工作台正在申请验证码中.........");
				//设置30s超时
				while(new Date().getTime() - start.getTime() <= 30000L){
					Thread.sleep(10000); //只查询3次
					insuredhis = insbInsuredhisDao.selectOne(queryTemp);
					if(insuredhis == null){
						returnMap.put("status", "fail");
						returnMap.put("errmsg", "没有找到单方被保人信息!");
						return JSONObject.fromObject(returnMap).toString();
					}
					temp = insuredhis.getApplystatus();
					if(StringUtil.isNotEmpty(temp) && Integer.parseInt(temp) > 0){
						chaoshi = false;
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(chaoshi){
				returnMap.put("status", "chaoshi");
				LogUtil.error("支付或二支CM工作台申申请验证码超时!taskid" + taskid + ", inscomcode=" + inscomcode + ", oprator=" + user.getUsercode() + ",applystatus=" + insuredhis.getApplystatus());
			} else {
				//成功申请后修改操作人信息
//				insuredhis.setOperator(user.getUsercode());
//				insbInsuredhisDao.updateById(insuredhis);   //先注释掉看看是不是这个引起的
				LogUtil.info("支付或二支CM工作台申请验证码成功!taskid" + taskid + ", inscomcode=" + inscomcode + ", oprator=" + user.getUsercode() + ",applystatus=" + insuredhis.getApplystatus());
				returnMap.put("status", "success");
				returnMap.put("applystatus", insuredhis.getApplystatus());
			}*/
			returnMap.put("status", "success");
			returnMap.put("applystatus", ConstUtil.PIN_APPLING);
			
		} else {
			returnMap.put("status", "fail");
			returnMap.put("errmsg", result.get("errormsg"));
			LogUtil.info("支付或二支CM工作台申请验证码请求精灵返回失败!taskid" + taskid);
		}
		return JSONObject.fromObject(returnMap).toString();
	}
	
	@Override
	public String saveIDCardInfo(INSCUser user, String taskid, String inscomcode, String name, String idcardno,
			String sex, String nation, String birthday, String address, String authority, String expdate, String cellphone, String expstartdate, String expenddate, String email){
		Map<String, String> returnMap = new HashMap<String, String>();
		INSBInsuredhis insbinsuredhis = new INSBInsuredhis();
		insbinsuredhis.setTaskid(taskid);
		insbinsuredhis.setInscomcode(inscomcode);
		insbinsuredhis = insbInsuredhisDao.selectOne(insbinsuredhis);
		INSBPerson person = null;
		if(insbinsuredhis == null){
			person = new INSBPerson();
			person.setCreatetime(new Date());
		} else {
			person = insbPersonDao.selectById(insbinsuredhis.getPersonid());
		}
		try {
			//修改被保人信息的时候、更新补充数据项的表
			List<INSBInsuresupplyparam> insuresupplyparamList = new ArrayList<INSBInsuresupplyparam>();
			INSBInsuresupplyparam insbInsuresupplyparam;
			if(!email.equalsIgnoreCase(person.getEmail())){			//修改过才做操作
				insbInsuresupplyparam = new INSBInsuresupplyparam();
				insbInsuresupplyparam.setItemcode("insuredEmail");
				insbInsuresupplyparam.setItemname("被保人邮箱");
				insbInsuresupplyparam.setItemvalue(email);
				insuresupplyparamList.add(insbInsuresupplyparam);
			}
			if(!cellphone.equalsIgnoreCase(person.getCellphone())){
				insbInsuresupplyparam = new INSBInsuresupplyparam();
				insbInsuresupplyparam.setItemcode("insuredMobile");
				insbInsuresupplyparam.setItemname("被保人手机号码");
				insbInsuresupplyparam.setItemvalue(cellphone);
				insuresupplyparamList.add(insbInsuresupplyparam);
			}
			if(!email.equalsIgnoreCase(person.getEmail()) || !cellphone.equalsIgnoreCase(person.getCellphone())){
				insbInsuresupplyparamService.saveByTask(insuresupplyparamList, taskid, inscomcode, user.getOperator());
			}

			person.setOperator(user.getUsercode());
			person.setName(name);
			person.setIdcardtype(0);
			person.setIdcardno(idcardno);
			person.setNation(nation);
			person.setGender(sex.equals("1") ? 1 : 0);
			person.setBirthday((StringUtil.isEmpty(birthday) ? null : DateUtil.parse(birthday)));
			person.setAddress(address);
			person.setAuthority(authority);
			person.setExpdate(expdate);
			person.setCellphone(cellphone);
			person.setExpstartdate(StringUtil.isEmpty(expstartdate) ? null : DateUtil.parse(expstartdate));//后台新增保存起始日期
			person.setExpenddate(StringUtil.isEmpty(expenddate) ? null : DateUtil.parse(expenddate));
			person.setEmail(email);
			int flag = insbPersonDao.updateById(person);

			if (insbinsuredhis == null) {
				insbinsuredhis = new INSBInsuredhis();
				insbinsuredhis.setOperator(user.getUsercode());
				insbinsuredhis.setCreatetime(new Date());
				insbinsuredhis.setTaskid(taskid);
				insbinsuredhis.setInscomcode(inscomcode);
				insbinsuredhis.setPersonid(person.getId());
				insbInsuredhisDao.updateById(insbinsuredhis);
			}

			if (flag == 1) {
				returnMap.put("status", "success");
			} else {
				returnMap.put("status", "fail");
			}
		}catch (Exception e){
			returnMap.put("status", "fail");
			LogUtil.info("%s被保人信息更新入库失败", taskid);
			e.printStackTrace();
		}
		return JSONObject.fromObject(returnMap).toString();
	}

}