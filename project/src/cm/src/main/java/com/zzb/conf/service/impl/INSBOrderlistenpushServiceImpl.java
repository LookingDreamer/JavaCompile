package com.zzb.conf.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.common.ConfigUtil;
import com.common.LZGUtil;
import com.lzgapi.order.model.LzgOrderSaveModel;
import com.lzgapi.order.model.LzgPolicyModel;
import com.lzgapi.order.service.LzgOrderService;
import com.zzb.cm.dao.INSBInsuredDao;
import com.zzb.cm.dao.INSBOrderDao;
import com.zzb.cm.dao.INSBPersonDao;
import com.zzb.cm.entity.INSBInsured;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.cm.entity.INSBPerson;
import com.zzb.conf.dao.INSBOrderlistenpushDao;
import com.zzb.conf.dao.INSBPolicyitemDao;
import com.zzb.conf.entity.INSBOrderlistenpush;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.conf.service.INSBOrderlistenpushService;

@Service
@Transactional
public class INSBOrderlistenpushServiceImpl extends
		BaseServiceImpl<INSBOrderlistenpush> implements
		INSBOrderlistenpushService {
	@Resource
	private INSBOrderlistenpushDao insbOrderlistenpushDao;

	@Resource
	private LzgOrderService lzgOrderService;
	@Resource
	private INSBOrderDao insbOrderDao;
	@Resource
	private INSBPolicyitemDao insbPolicyitemDao;
	@Resource
	private INSBInsuredDao insbInsuredDao;
	@Resource
	private INSBPersonDao insbPersonDao;

	@Override
	protected BaseDao<INSBOrderlistenpush> getBaseDao() {
		return insbOrderlistenpushDao;
	}

	@Override
	public void updateAddFail(INSBOrderlistenpush orderfail) {
		LogUtil.info("=====懒掌柜定时推送信息表=====" + orderfail.getLzgid() + "==");
		try {
			// 更新
			orderfail.setModifytime(new Date());
			insbOrderlistenpushDao.updateById(orderfail);
			LzgOrderSaveModel model = transFormOrder(orderfail);
			// 调用懒掌柜接口新增接口
			String LZGStr = LZGUtil.saveOrupdateLZGorder(model);
			// 懒掌柜返回值
			Map<String, String> returnFlag = JSONObject.fromObject(LZGStr);
			if (!returnFlag.isEmpty()) {
				orderfail.setMessage(returnFlag.get("message"));
				orderfail.setStatus(returnFlag.get("status"));
				insbOrderlistenpushDao.updateById(orderfail);
			} else {
				orderfail.setStatus("FAILED");
				orderfail.setMessage("接口调用返回null");
				insbOrderlistenpushDao.updateById(orderfail);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public LzgOrderSaveModel transFormOrder(INSBOrderlistenpush order) {
		LzgOrderSaveModel model = new LzgOrderSaveModel();
		if (order != null) {
			// 根据任务号去订单表查数据
			INSBOrder ovo = new INSBOrder();
			ovo.setTaskid(order.getTaskid());
			INSBOrder o = insbOrderDao.selectList(ovo).get(0);
			model.setId(order.getLzgid());
			model.setOrderno(order.getTaskid());
			model.setPlantform(Integer.valueOf(ConfigUtil
					.getPropString("lzg.zzb.platform.code")));
			model.setQuantity(1);// 数量
			if (o != null) {
				model.setPrice(o.getTotalpaymentamount());
				model.setTotalprice(o.getTotalpaymentamount());
			}
			model.setStatus("0");// 刚生成的订单状态都是未完成的
			INSBPolicyitem vo = new INSBPolicyitem();
			vo.setTaskid(order.getTaskid());
			List<INSBPolicyitem> listPol = insbPolicyitemDao.selectList(vo);
			INSBInsured insured = insbInsuredDao.selectInsuredByTaskId(order
					.getTaskid());
			INSBPerson insp = new INSBPerson();
			if (insured != null && !StringUtil.isEmpty(insured.getPersonid())) {
				insp = insbPersonDao.selectById(insured.getPersonid());
			}
			for (INSBPolicyitem pli : listPol) {
				LzgPolicyModel pm = new LzgPolicyModel();
				pm.setContNo(pli.getPolicyno());
				pm.setSupplierCode(pli.getInscomcode());
				pm.setRiskCode(pli.getInscomcode());
				pm.setSignDate(dateToString(pli.getInsureddate()));
				pm.setCvaliDate(dateToString(pli.getStartdate()));
				pm.setCinvaliDate(dateToString(pli.getEnddate()));
				pm.setContPrem(pli.getDiscountCharge().toString());
				pm.setAmnt(pli.getAmount().toString());
				pm.setInsuredIdno(insp.getIdcardno());// 被保人证件号
				pm.setInsuredName(insp.getName());// 被保人姓名
				model.getPolicylist().add(pm);
			}
		}
		return model;
	}

	public String dateToString(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
		return df.format(date);
	}

	@Override
	public INSBOrderlistenpush queryOrderListen(Map<String, String> param) {
		return insbOrderlistenpushDao.queryOrderListen(param);
	}

	@Override
	public List<INSBOrderlistenpush> queryListBytype(String type) {
		return insbOrderlistenpushDao.queryListBytype(type);
	}

	public void save4Again(String mainTaskId, String subTaskId,
			String taskName, String status) {
		lzgOrderService.save4Again(mainTaskId, subTaskId, taskName, status);
	}
}