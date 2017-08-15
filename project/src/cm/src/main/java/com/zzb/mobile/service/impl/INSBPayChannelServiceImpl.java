package com.zzb.mobile.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.cninsure.core.utils.LogUtil;
import com.zzb.cm.dao.*;
import com.zzb.cm.entity.*;
import com.zzb.conf.dao.*;
import com.zzb.conf.entity.*;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.cninsure.system.dao.INSCCodeDao;
import com.cninsure.system.entity.INSCCode;
import com.zzb.cm.service.INSBQuoteinfoService;
import com.zzb.cm.service.INSBQuotetotalinfoService;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.service.INSBPayChannelService;
/*
 * 支付通道接口
 * 
 */

@Service
public class INSBPayChannelServiceImpl extends BaseServiceImpl<INSBPaychannel> implements INSBPayChannelService {
	@Resource
	private INSBPaychannelDao insbPaychannelDao;
	@Resource
	private INSBOrderpaymentDao insbOrderpaymentDao;
	@Resource
	private INSBOrderDao insbOrderDao;
	@Resource
	private INSBQuoteinfoService quoteInfoService;
	@Resource
	private INSBQuotetotalinfoService quotetotalInfoService;
	@Resource
	private INSBBankcardconfDao insbBankcardconfDao;
	@Resource
	private INSCCodeDao inscCodeDao;
	@Resource
	private INSBPaychannelmanagerDao insbPaychannelmanagerDao;
	@Resource
	private INSBCarowneinfoDao insbCarowneinfoDao;
	@Resource
	private INSBInsuredhisDao insuredhisDao;//被保人
	@Resource
	private INSBApplicanthisDao insbApplicanthisDao;//投保人
	@Resource
	private INSBPersonDao insbPersonDao;
	@Resource
	private INSBCarmodelinfohisDao insbCarmodelinfohisDao;
	@Resource
	private  INSBCarinfohisDao carinfohisDao;
	@Resource
	private INSBWorkflowsubDao insbWorkflowsubDao;
	@Resource
	private INSBChannelDao insbChannelDao;
	@Resource
	private INSBWorkflowsubtrackDao insbWorkflowsubtrackDao;

	private boolean checkZhongan(String prvid,String taskId) {
		//需求1—— 小于8年车龄的车
		INSBCarinfohis carinfohisParam = new INSBCarinfohis();
		carinfohisParam.setTaskid(taskId);
		carinfohisParam.setInscomcode(prvid);
		INSBCarinfohis carinfohis = carinfohisDao.selectOne(carinfohisParam);
		if(carinfohis == null || carinfohis.getRegistdate() == null || StringUtil.isEmpty(new java.text.SimpleDateFormat("yyyyMM").format(carinfohis.getRegistdate())) ){
			LogUtil.info("众安支付校验-车龄：" + taskId +  ",prvid=" + prvid + "校验失败，车辆历史信息为空");
			return  false;
		}

		//车辆初登日期 日期格式YYYYMMDD
		String registDateStr = new java.text.SimpleDateFormat("yyyyMM").format(carinfohis.getRegistdate());
		LogUtil.info("众安支付校验-车龄：" + taskId +  ",prvid=" + prvid + "初登日期为："+ registDateStr);
		if(registDateStr.length() < 6 || calculateCarAge(registDateStr)>= 8){
			LogUtil.info("众安支付校验-车龄：" + taskId +  ",prvid=" + prvid + "校验失败，车龄为"+calculateCarAge(registDateStr) +"大于或等于8年");
			return  false;
		}

		//校验是否为非营业车  16\12\11\10\1
		LogUtil.info("众安支付校验-车辆使用性质：" + taskId +  ",prvid=" + prvid + "校验车辆使用性质：" + carinfohis.getCarproperty());
		if(carinfohis.getCarproperty() == null || StringUtil.isEmpty(carinfohis.getCarproperty()) || isBusinessCar(carinfohis.getCarproperty())){
			LogUtil.info("众安支付校验-车辆使用性质：" + taskId +  ",prvid=" + prvid + "校验失败，车辆使用性质为:"+carinfohis.getCarproperty() );
			return  false;
		}

		//需求2—— 车座数校验 -- 七座及以下的车
		Map<String, Object> carModelInfoHisMap = new HashMap<>();
		carModelInfoHisMap.put("taskid",taskId);
		carModelInfoHisMap.put("inscomcode",prvid);
		INSBCarmodelinfohis carmodelinfohis = insbCarmodelinfohisDao.selectModelInfoByTaskIdAndPrvId(carModelInfoHisMap);

		if(carmodelinfohis == null || carmodelinfohis.getSeat() == null ){
			LogUtil.info("众安支付校验-座位数：" + taskId +  ",prvid=" + prvid + "校验失败，座位数为空");
			return false;
		}
		LogUtil.info("众安支付校验-座位数：" + taskId +  ",prvid=" + prvid + "座位数为："+ carmodelinfohis.getSeat());
		if(carmodelinfohis.getSeat() > 7 || carmodelinfohis.getSeat() < 0){
			LogUtil.info("众安支付校验-座位数：" + taskId +  ",prvid=" + prvid + "校验失败，座位数大于7座");
			return false;
		}

		INSBCarowneinfo carowneinfo=new INSBCarowneinfo();
		carowneinfo.setTaskid(taskId);
		//车主校验
		INSBCarowneinfo carowneinfoTemp=insbCarowneinfoDao.selectOne(carowneinfo);
		if(carowneinfoTemp==null)
		{
			LogUtil.info("众安支付校验-车主：" + taskId + ",prvid=" + prvid + "校验失败,车主查询结果为空");
			return false;
		}
		INSBPerson person=insbPersonDao.selectById(carowneinfoTemp.getPersonid());
		LogUtil.info("众安支付校验-车主："+taskId+",prvid="+prvid+" ,personId:"+carowneinfoTemp.getPersonid()+"，查询结果:"+person);

		if(person==null  || person.getIdcardno() == null || person.getIdcardtype() == null || person.getIdcardtype()!=0)
		{
			LogUtil.info("众安支付校验-车主："+taskId+",prvid="+prvid+"校验失败");
			return false;
		}
		String idcard=person.getIdcardno();
		INSBInsuredhis insuredhis=new INSBInsuredhis();
		insuredhis.setTaskid(taskId);
		insuredhis.setInscomcode(prvid);

		//被保人校验
		INSBInsuredhis insuredhisNew = insuredhisDao.selectOne(insuredhis);
		if (insuredhisNew == null) {
			LogUtil.info("众安支付校验-被保人："+taskId+",prvid="+prvid+"校验失败,被保人查询结果为空");
			return false;
		}
		INSBPerson personTmp = insbPersonDao.selectById(insuredhisNew.getPersonid());
		LogUtil.info("众安支付校验-被保人："+taskId+",prvid="+prvid+" ,personId:"+insuredhisNew.getPersonid()+"，查询结果:"+personTmp);
		if(personTmp==null  || personTmp.getIdcardtype() == null || personTmp.getIdcardtype()!=0 || ! idcard.equals(personTmp.getIdcardno()))
		{
			LogUtil.info("众安支付校验-被保人："+taskId+",prvid="+prvid+"校验失败，证件类型不对或者号码与车主不符合");
			return false;
		}

		//投保人校验
		INSBApplicanthis example=new INSBApplicanthis();
		example.setTaskid(taskId);
		example.setInscomcode(prvid);
		INSBApplicanthis applicanthisTemp= insbApplicanthisDao.selectOne(example);
		if (applicanthisTemp == null) {
			LogUtil.info("众安支付校验-投保人："+taskId+",prvid="+prvid+"校验失败,投保人查询结果为空");
			return false;
		}
		INSBPerson personApp=insbPersonDao.selectById(applicanthisTemp.getPersonid());
		LogUtil.info("众安支付校验-投保人："+taskId+",prvid="+prvid+" ,personId:"+applicanthisTemp.getPersonid()+"，查询结果:"+personApp);
		if(personApp==null  ||personApp.getIdcardtype() == null || personApp.getIdcardtype()!=0 || ! idcard.equals(personApp.getIdcardno()))
		{
			LogUtil.info("众安支付校验-投保人："+taskId+",prvid="+prvid+"校验失败，证件类型不对或者号码与车主不符合");
			return false;
		}

		return true;

	}

	private static int calculateCarAge(String firstLogDate){
		String listedYear =firstLogDate.substring(0, 4);
		String listedMonth =firstLogDate.substring(4, 6);
		int listedYaerInt = Integer.valueOf(listedYear);
		int listedYaerMonth = Integer.valueOf(listedMonth);
		System.out.println("listedYear = " + listedYear);
		System.out.println("listedMonth = " + listedMonth);
		System.out.println("listedYear of int  = " + Integer.valueOf(listedYear));
		System.out.println("listedMonth of int  = " + Integer.valueOf(listedMonth));

		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		System.out.println("year of now = " + year);
		System.out.println("month of now = " + month);
		int carAge = 0;
		if((month - listedYaerMonth) < 0 ){
			year = year - 1;
		}
		carAge = year - listedYaerInt;
		System.out.println("carAge = " + carAge );
		return carAge;
	}

	/**
	 * 检查是否为营业车
	 * */
	private static Boolean isBusinessCar(String carProperty){
		Boolean flag = true;
		if(carProperty.equals("1") || carProperty.equals("16") ||carProperty.equals("12") ||carProperty.equals("11")
				||carProperty.equals("10") )  flag = false;
		return  flag;
	}

	/*
	 * 显示支付通道列表信息,入参供应商id
	 */
	@Override
	public String showPayChannel(String deptcode,String prvid,String taskId, String subInstanceId, String clientType) {
		CommonModel model = new CommonModel();

		Map<String,Object> body = new HashMap<String,Object>();
		try{
			/*
			 * 调用接口，完成显示列表
			 */
            List<Map<String,Object>> payChannelNew = new ArrayList<Map<String,Object>>();
            if (!org.springframework.util.StringUtils.isEmpty(clientType)) {
                List<INSBPaychannel> paychannels = insbPaychannelDao.selectPayChannelListByAgreementId(deptcode,prvid);//03-31 7480 直接获取对象Paychannel

                for(INSBPaychannel paychannel:paychannels){
                    Map<String,Object> payChanneluse = new HashMap<String,Object>();
                    if(paychannel.getCostratedescribe()!=null){
                        payChanneluse.put("isHasCostRate", "1");//标记有优惠
                    }else{
                        payChanneluse.put("isHasCostRate", "0"); //标记没有优惠
                    }
                    payChanneluse.put("chargepoundagefalg",paychannel.getChargepoundagefalg());//标记手续费
                    payChanneluse.put("clientpayflag", paychannel.getClientpayflag());//标记直接支付
                    payChanneluse.put("paychannelname",paychannel.getPaychannelname()); //支付渠道名称
                    payChanneluse.put("id",paychannel.getId());     //支付渠道id
                    payChanneluse.put("sort",StringUtil.isEmpty(paychannel.getSort()) ? 0 : Integer.parseInt(paychannel.getSort()));
                    boolean flag=true;
                    if("28".equals(paychannel.getId())&& ! StringUtils.isEmpty(taskId))//众安支付条件判断
                    {
                        flag=checkZhongan(prvid,taskId);
                    }
                    // 通过终端类型判断是否显示支付方式，如果未配置，则不显示；如果是微信端，则只显示微信支付（比如诚泰微信支付，联通微信支付）
                    boolean flag2 = false;
                    if (isClientSupported(paychannel, clientType)) {
						if (paychannel.isOnlyedionlineunderwriting()) {
							flag2 = isEdiUnderWriting(subInstanceId);
						} else {
							flag2 = true;
						}
					} else {
                        flag2 = false;
                    }

                    if (flag && flag2) {
                        payChannelNew.add(payChanneluse);
                    }

                }
                Collections.sort(payChannelNew, new Comparator<Map<String,Object>>() {
                    @Override
                    public int compare(Map<String,Object> o1, Map<String,Object> o2) {
                        return ((Integer)o2.get("sort")).compareTo ((Integer)o1.get("sort"));
                    }
                });
            }

			body.put("paychannel", payChannelNew);
			model.setBody(body);
			model.setMessage("支付通道列表查询成功");
			model.setStatus("success");
			return JSONObject.fromObject(model).toString();
		}catch(Exception e){
			e.printStackTrace();
			model.setMessage("支付通道列表查询失败");
			model.setStatus("fail");
			return JSONObject.fromObject(model).toString();
		}

	}

	public String showPayChannelForChn(String channelId,String prvid,String taskId, String subInstanceId, String clientType){
		CommonModel model = new CommonModel();

		Map<String,Object> body = new HashMap<String,Object>();
		try{
			/*
			 * 调用接口，完成显示列表
			 */
			List<Map<String,Object>> payChannelNew = new ArrayList<Map<String,Object>>();
			if (!org.springframework.util.StringUtils.isEmpty(clientType)) {
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("channelId",channelId);
				params.put("providerid",prvid);
				//获取渠道配置的支付方式
				List<INSBPaychannel> paychannels = insbChannelDao.showPayChannelForChn(params);
				for(INSBPaychannel paychannel:paychannels){
					Map<String,Object> payChanneluse = new HashMap<String,Object>();
					if(paychannel.getCostratedescribe()!=null){
						payChanneluse.put("isHasCostRate", "1");//标记有优惠
					}else{
						payChanneluse.put("isHasCostRate", "0"); //标记没有优惠
					}
					payChanneluse.put("chargepoundagefalg",paychannel.getChargepoundagefalg());//标记手续费
					payChanneluse.put("clientpayflag", paychannel.getClientpayflag());//标记直接支付
					payChanneluse.put("paychannelname",paychannel.getPaychannelname()); //支付渠道名称
					payChanneluse.put("id",paychannel.getId());     //支付渠道id
					payChanneluse.put("sort",StringUtil.isEmpty(paychannel.getSort()) ? 0 : Integer.parseInt(paychannel.getSort()));
					boolean flag=true;
					if("28".equals(paychannel.getId())&& ! StringUtils.isEmpty(taskId))//众安支付条件判断
					{
						flag=checkZhongan(prvid,taskId);
					}
					// 通过终端类型判断是否显示支付方式，如果未配置，则不显示；如果是微信端，则只显示微信支付（比如诚泰微信支付，联通微信支付）
					boolean flag2 = false;
					if (isClientSupported(paychannel, clientType)) {
						if (paychannel.isOnlyedionlineunderwriting()) {
							flag2 = isEdiUnderWriting(subInstanceId);
						} else {
							flag2 = true;
						}
					} else {
						flag2 = false;
					}

					if (flag && flag2) {
						payChannelNew.add(payChanneluse);
					}

				}
				Collections.sort(payChannelNew, new Comparator<Map<String,Object>>() {
					@Override
					public int compare(Map<String,Object> o1, Map<String,Object> o2) {
						return ((Integer)o2.get("sort")).compareTo ((Integer)o1.get("sort"));
					}
				});
			}

			body.put("paychannel", payChannelNew);
			model.setBody(body);
			model.setMessage("支付通道列表查询成功");
			model.setStatus("success");
			return JSONObject.fromObject(model).toString();
		}catch(Exception e){
			e.printStackTrace();
			model.setMessage("支付通道列表查询失败");
			model.setStatus("fail");
			return JSONObject.fromObject(model).toString();
		}
	}

	private boolean isClientSupported(INSBPaychannel paychannel, String clientType) {
		return paychannel.getClienttypes() != null &&
				Arrays.asList(paychannel.getClienttypes().trim().toLowerCase().split(",")).contains(clientType.trim().toLowerCase());
	}

	private String getTaskcode(Map<String,Object> data) {
		if (data == null || data.isEmpty()) return "";
		return String.valueOf(data.get("taskcode"));
	}

	//自核或核保查询成功 转人工流程轨迹序列匹配模式
	private String[] r = {"^.+,40(,18)?(,38,18)+,20$", "^.+,40(,38)?(,18,38)+,20$"};

	// 广东平台可能存在两种核保方式: edi核保成功后转人工核保
	private boolean isEdiUnderWriting(String instanceId) {
		//按创建时间从小到大的排序
		List<Map<String,Object>> subtracks = insbWorkflowsubtrackDao.selectAllTrack(instanceId);
		if (subtracks == null || subtracks.size() < 2) {
			LogUtil.error("流程数据异常");
			return false;
		}

		String tracks = "," + subtracks.stream().map(this::getTaskcode).collect(Collectors.joining(","));
		LogUtil.info("%s当前流程轨迹：%s", instanceId, tracks);

		//修正核保轮询完成直接到支付的时候，两个状态的创建时间相同，导致查询出来的数据顺序不正确的情况(bug-8813)
		if (tracks.endsWith(",20,38")) {
			tracks = tracks.replaceFirst(",20,38$", ",38,20");
			LogUtil.info("%s修正流程轨迹：%s", instanceId, tracks);
		}

		//自核或自核查询返回成功
		if (tracks.endsWith(",40,20") || tracks.endsWith(",40,38,20")) return true;

		//转过人工
		String underWritingSuccessType = null;
		INSBWorkflowsub sub = insbWorkflowsubDao.selectByInstanceId(instanceId);
		if (sub != null) {
			underWritingSuccessType = sub.getUnderwritingsuccesstype();
		}
		LogUtil.info("%s核保查询轨迹：%s", instanceId, underWritingSuccessType);

		//是否(自动)核保(查询)成功的
		boolean querySuccess = false;

        if (StringUtil.isEmpty(underWritingSuccessType)) {
        	return querySuccess;
        }

		int trackIndex = -1;
		if (tracks.matches(r[0])) {
			trackIndex = subtracks.size() - 3;
		} else if (tracks.matches(r[1])) {
			trackIndex = subtracks.size() - 2;
		} else if (tracks.endsWith(",40,18,20")) { //自核转人工，需要判断是否自核成功
			trackIndex = subtracks.size() - 3;
		}

		if (trackIndex > 0) {
			String trackid = subtracks.get(trackIndex).get("id")+"";
			//核保查询的
			if (underWritingSuccessType.contains(","+getTaskcode(subtracks.get(trackIndex))+"#"+trackid)) {
				querySuccess = true;
			}
		}

		return querySuccess;
	}

	private boolean isFairyUnderWriting(String instanceId) {
        INSBWorkflowsub sub = insbWorkflowsubDao.selectByInstanceId(instanceId);
        if (sub == null) {
            return false;
        }
        return "41".equals(sub.getUnderwritingsuccesstype());
	}

	private boolean isHumanUnderWriting(String instanceId) {
        INSBWorkflowsub sub = insbWorkflowsubDao.selectByInstanceId(instanceId);
        if (sub == null) {
            return false;
        }
        return "18".equals(sub.getUnderwritingsuccesstype());
	}

	/*
	 * 选择某一种的支付通道作为支付方式
	 */
	@Override
	public String choosePayChannel(String id,String agentnum,String processInstanceId,String inscomcode) {
		CommonModel model = new CommonModel();
		try{
			Map<String, Object> maporder = new HashMap<String, Object>();
			maporder.put("taskid", processInstanceId);
			maporder.put("inscomcode", inscomcode); // 保险公司id
			INSBOrder order = insbOrderDao.selectOrderByTaskId(maporder);
			if (order == null) {
				model.setStatus("fail");
				model.setMessage("订单不存在！");
				return JSONObject.fromObject(model).toString();
			} else {
				// 把选择的支付方式存到insbOrder表中
				order.setPaymentmethod(id);
				insbOrderDao.updateById(order);
			}
			model.setStatus("success");
			model.setMessage("选择支付方式成功");
			return JSONObject.fromObject(model).toString();
		}catch(Exception e){
			e.printStackTrace();
			model.setStatus("fail");
			model.setMessage("选择支付方式失败");
			return JSONObject.fromObject(model).toString();
		}

	}
	@Override
	public String choosePayChannel1(String id,String agentnum,String processInstanceId,String inscomcode) {
		CommonModel model = new CommonModel();
		try{
			Map<String,Object> maporder = new HashMap<String,Object>();
			maporder.put("taskid", processInstanceId);
			maporder.put("inscomcode", inscomcode);              //保险公司id
			INSBOrder order = insbOrderDao.selectOrderByTaskId(maporder);
			if(order==null){
				model.setStatus("fail");
				model.setMessage("订单不存在！");
				return JSONObject.fromObject(model).toString();
			}
			//查询子流程id
			INSBQuoteinfo insbQuoteinfo = quoteInfoService.getByTaskidAndCompanyid(processInstanceId, inscomcode);

			//是否存在数据
			INSBOrderpayment orderPaymentTemp = new INSBOrderpayment();
			//orderPaymentTemp.setOrderid(order.getId());
			orderPaymentTemp.setSubinstanceid(insbQuoteinfo.getWorkflowinstanceid());

			orderPaymentTemp = insbOrderpaymentDao.selectOne(orderPaymentTemp);

			//报价子表金额
			//总表中数据
			INSBQuotetotalinfo insbQuotetotalinfo=new INSBQuotetotalinfo();
			insbQuotetotalinfo.setTaskid(processInstanceId);
			insbQuotetotalinfo=quotetotalInfoService.queryOne(insbQuotetotalinfo);
			//报价子表中的数据
			INSBQuoteinfo insbQuoteinfo1=new INSBQuoteinfo();
			insbQuoteinfo1.setQuotetotalinfoid(insbQuotetotalinfo.getId());
			insbQuoteinfo1.setInscomcode(inscomcode);
			//INSBQuoteinfo insbQuoteinfo1=insbQuoteinfoDao.selectOne(insbQuoteinfo);
			Double totalpaymentamount = quoteInfoService.queryOne(insbQuoteinfo1).getQuotediscountamount();


			if(orderPaymentTemp==null){
			INSBQuotetotalinfo temp =new INSBQuotetotalinfo();
			temp.setTaskid(processInstanceId);
			INSBQuotetotalinfo total = quotetotalInfoService.queryOne(temp);
			INSBQuoteinfo subtemp =new INSBQuoteinfo();
			subtemp.setQuotetotalinfoid(total.getId());
			subtemp.setInscomcode(inscomcode);
			INSBQuoteinfo subinfo = quoteInfoService.queryOne(subtemp);

			INSBOrderpayment orderPayment = new INSBOrderpayment();
			orderPayment.setTaskid(processInstanceId);   //设置任务id
			orderPayment.setOperator(agentnum);      //操作员
			orderPayment.setSubinstanceid(subinfo.getWorkflowinstanceid());
			orderPayment.setOrderid(order.getId());         //订单表id
			orderPayment.setPaychannelid(id); //支付通道id
//				orderPayment.setPaychanneltype(payChannel.getSupportterminal());// 支付渠道类型--支持终端
//				orderPayment.setPaystyle(payChannel.getPaytype());               //支付方式--支付类型
			BigDecimal totalpaymentamountDecimal = new BigDecimal(String.valueOf(totalpaymentamount==null?0:totalpaymentamount));
			BigDecimal totaldeliveryamountDecimal = new BigDecimal(String.valueOf(order.getTotaldeliveryamount()==null?0:order.getTotaldeliveryamount()));
			orderPayment.setAmount(totalpaymentamountDecimal.add(totaldeliveryamountDecimal)
					.setScale(2, RoundingMode.HALF_UP).doubleValue());
			//支付金额（实付金额）+配送费  ，判断是否为空
			orderPayment.setCurrencycode(order.getCurrency());       //币种编码
			orderPayment.setPayresult("00");         //支付状态：待支付-00，正在支付-01, 支付完成-02 等等
			orderPayment.setCreatetime(new Date());  //创建时间
			String random = UUIDUtils.random();
			orderPayment.setPaymentransaction(random);
			insbOrderpaymentDao.insert(orderPayment);  //插入一条数据
			model.setBody(orderPayment.getPaymentransaction());
			}else{
				orderPaymentTemp.setOperator(agentnum);      //操作员
				orderPaymentTemp.setPaychannelid(id); //支付通道id
				orderPaymentTemp.setModifytime(new Date());  //修改时间
				insbOrderpaymentDao.updateById(orderPaymentTemp);
				model.setBody(orderPaymentTemp.getPaymentransaction());
			}
			model.setStatus("success");
			model.setMessage("选择支付方式成功");
			return JSONObject.fromObject(model).toString();
		}catch(Exception e){
			e.printStackTrace();
			model.setStatus("fail");
			model.setMessage("选择支付方式失败");
			return JSONObject.fromObject(model).toString();
		}

	}

	/*
	 * 显示某一种支付渠道详细信息
	 */
	@Override
	public String showPayChannelDetail(String id) {
		CommonModel model = new CommonModel();
		Map<String,Object> body = new HashMap<String,Object>();
		try{
			/*
			 * 显示支付通道详细信息
			 */
			INSBPaychannel payChannel =  insbPaychannelDao.selectById(id);
			body.put("poundageratio", payChannel.getPoundageratio());    //手续费率比
			body.put("paychannelname", payChannel.getPaychannelname()); //通道名称
			body.put("chargepoundagefalg", payChannel.getChargepoundagefalg());//是否收取手续费
			body.put("clientpayflag", payChannel.getClientpayflag());//是否支持客户端付款
			body.put("costratedescribe", payChannel.getCostratedescribe());//费率信息描述
			/*
			 *银行单笔金额等信息 
			 */
			List<Map<String,Object>> creditcard = new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> depositcard = new ArrayList<Map<String,Object>>();
			/*
			 * 调用服务，查询信用卡与储蓄卡的消费信息
			 */
			List<INSBBankcardconf> bankcardconf = insbBankcardconfDao.queryBankcardConfInfo(id);
			for(INSBBankcardconf conftemp: bankcardconf){
				/*
				 * 信用卡部分
				 */
				Map<String,Object> creditcardnew = new HashMap<String,Object>();
				if(StringUtil.isEmpty(conftemp.getInsbcodevalue()))continue;
				INSCCode code = inscCodeDao.selectBankList(conftemp.getInsbcodevalue());
				creditcardnew.put("bankname", code.getCodename());  //通过数据字典返回银行名称
				creditcardnew.put("singleamount", conftemp.getCreditlimit());  //单笔额度
				creditcardnew.put("singledayamount", conftemp.getCreditdaylimit());  //单日额度
				creditcard.add(creditcardnew);
				/*
				 * 储蓄卡部分
				 */
				Map<String,Object> creditcardnewc = new HashMap<String,Object>();
				creditcardnewc.put("bankname", code.getCodename());  //通过数据字典返回银行名称
				creditcardnewc.put("singleamount", conftemp.getCashlimit());  //单笔额度
				creditcardnewc.put("singledayamount", conftemp.getCashdaylimit());  //单日额度
				depositcard.add(creditcardnewc);
			}
			body.put("creditcard", creditcard);//信用卡银行信息
			body.put("depositcard", depositcard);//储蓄卡银行信息
			model.setBody(body);
			model.setMessage("支付通道方式详细信息查询成功");
			model.setStatus("success");
			return JSONObject.fromObject(model).toString();
		}catch(Exception e){
			e.printStackTrace();
			model.setMessage("支付通道方式详细信息查询失败");
			model.setStatus("fail");
			return JSONObject.fromObject(model).toString();
		}
	}

	@Override
	protected BaseDao<INSBPaychannel> getBaseDao() {
		return null;
	}
	@Override
	public String queryPayChannelAccount(String deptid, String prvid) {
		CommonModel model = new CommonModel();
		Map<String,Object> body = new HashMap<String,Object>();
		try {
			INSBPaychannelmanager manager = new INSBPaychannelmanager();
			Map<String, String> param = new HashMap<String, String>();
			param.put("deptid", deptid);
			param.put("providerid", prvid);
			manager = insbPaychannelmanagerDao.queryManager(param);
			if(manager!=null){
				body.put("settlementno", manager.getSettlementno());//结算商户号
				body.put("terraceno", manager.getTerraceno());//快钱的平台商户号
				body.put("settlementnoname", manager.getSettlementnoname());//结算商户号名称
				body.put("terracenoname", manager.getTerracenoname());//快钱的平台商户号名称
				model.setStatus("success");
				model.setBody(body);
				model.setMessage("商户号查询成功");
				return JSONObject.fromObject(model).toString();
			}else{
				model.setStatus("fail");
				model.setMessage("根据此供应商和网点找不到商户号");
				return JSONObject.fromObject(model).toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.setStatus("fail");
			model.setMessage("商户号查询失败");
			return JSONObject.fromObject(model).toString();
		}
	}

	@Override
	public String getNewVersion() {
		CommonModel model = new CommonModel();
		try {
			Map<String,Object> types = new HashMap<String,Object>();
			Map<String, String> para = new HashMap<String, String>();
			para.put("codetype", "MobileVersion");
			para.put("parentcode", "MobileVersion");
			List<INSCCode> codelist = inscCodeDao.selectINSCCodeByCode(para);
			if(codelist!=null&&codelist.size()>0){
				for (int i = 0; i < codelist.size(); i++) {
					if("Android".equals(codelist.get(i).getProp3())){
						Map<String,Object> map = new HashMap<String,Object>();
						map.put("versionNum", codelist.get(i).getCodevalue());
						map.put("versionContents", codelist.get(i).getCodename());
						map.put("version", codelist.get(i).getProp4());
						map.put("path", codelist.get(i).getProp2());
						map.put("time", dateToString(codelist.get(i).getModifytime()));
						types.put("Android", map);
					}else if ("appStore".equals(codelist.get(i).getProp3())) {
						Map<String,Object> map = new HashMap<String,Object>();
						map.put("versionNum", codelist.get(i).getCodevalue());
						map.put("versionContents", codelist.get(i).getCodename());
						map.put("version", codelist.get(i).getProp4());
						map.put("path", codelist.get(i).getProp2());
						map.put("time", dateToString(codelist.get(i).getModifytime()));
						types.put("appStore", map);
					}else if("EnterpriseEdition".equals(codelist.get(i).getProp3())){
						Map<String,Object> map = new HashMap<String,Object>();
						map.put("versionNum", codelist.get(i).getCodevalue());
						map.put("versionContents", codelist.get(i).getCodename());
						map.put("version", codelist.get(i).getProp4());
						map.put("path", codelist.get(i).getProp2());
						map.put("time", dateToString(codelist.get(i).getModifytime()));
						types.put("EnterpriseEdition", map);
					}
				}
				model.setStatus("success");
				model.setBody(types);
				model.setMessage("版本号查询成功");
				return JSONObject.fromObject(model).toString();
			}else{
				model.setStatus("fail");
				model.setMessage("未找到数据");
				return JSONObject.fromObject(model).toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.setStatus("fail");
			model.setMessage("版本查询失败");
			return JSONObject.fromObject(model).toString();
		}
	}
	public String dateToString(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
		return df.format(date);
	}



	public static  void main(String args[]){
//		String dtStr1 = "200706";
//		System.out.println("dtStr1 length = " + dtStr1.length());
//		System.out.println( dtStr1.length() < 6 );
//		System.out.println(calculateCarAge(dtStr1));
//
//		System.out.println("============================");
//
//		String dtStr2 = "200812";
//		System.out.println(calculateCarAge(dtStr2));
//
//
//		System.out.println("============================");
//
//		String dtStr3 = "200912";
//		System.out.println(calculateCarAge(dtStr3));

		/*System.out.println("12 = "+ isBusinessCar("12"));

		System.out.println("14 ="+isBusinessCar("14"));

		System.out.println("1 ="+isBusinessCar("1"));*/

		/*List<Map<String,Object>> subtracks = new ArrayList<>();
		Map<String,Object> m1 = new HashMap<>(); m1.put("taskcode", "40");
		Map<String,Object> m2 = new HashMap<>(); m2.put("taskcode", "38");
		Map<String,Object> m3 = new HashMap<>(); m3.put("taskcode", "18");
		Map<String,Object> m4 = new HashMap<>(); m4.put("taskcode", "19");
		Map<String,Object> m5 = new HashMap<>(); m5.put("taskcode", "18");
		Map<String,Object> m6 = new HashMap<>(); m6.put("taskcode", "20");
		subtracks.add(m1);
		subtracks.add(m2);
		subtracks.add(m3);
		subtracks.add(m4);
		subtracks.add(m5);
		subtracks.add(m6);
		System.out.println(subtracks.stream().map(INSBPayChannelServiceImpl::getTaskcode).collect(Collectors.joining(",")));*/

		/*//String r = "^.+,40(,18)?(,38,18)+,20$";
		String r = "^.+,40(,38)?(,18,38)+,20$";
		String[] s = {"s,40,38,18,20", "s,40,38,18,38,18,20", " ,40,18,38,18,20", " ,40,38,18,38,20", " ,40,18,38,20", " ,40,20", " ,40,38,20"};
		for (String ss : s) {
			boolean m = ss.matches(r);
			System.out.println(m+": "+ss);
		}*/
	}
}
