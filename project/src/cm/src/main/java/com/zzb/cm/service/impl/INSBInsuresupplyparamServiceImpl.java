package com.zzb.cm.service.impl;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.cninsure.core.utils.JsonUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.service.INSCCodeService;
import com.zzb.cm.entity.INSBInsuredhis;
import com.zzb.cm.entity.INSBPerson;
import com.zzb.cm.service.INSBInsuredhisService;
import com.zzb.cm.service.INSBPersonService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSBInsuresupplyparamDao;
import com.zzb.cm.entity.INSBInsuresupplyparam;
import com.zzb.cm.service.INSBInsuresupplyparamService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class INSBInsuresupplyparamServiceImpl extends BaseServiceImpl<INSBInsuresupplyparam> implements
		INSBInsuresupplyparamService {
	@Resource
	private INSBInsuresupplyparamDao insbInsuresupplyparamDao;
	@Resource
	private INSCCodeService inscCodeService;
	@Resource
	INSBInsuredhisService insbInsuredhisService;
	@Resource
	INSBPersonService insbPersonService;

	private List<INSCCode> certKinds = null;

	@PostConstruct
	private void initData() {
		if (certKinds == null) {
			certKinds = Collections.unmodifiableList(inscCodeService.queryINSCCodeByCode("CertKinds","CertKinds"));
		}
	}

	public List<INSCCode> getCertKinds() {
		return certKinds;
	}

	@Override
	protected BaseDao<INSBInsuresupplyparam> getBaseDao() {
		return insbInsuresupplyparamDao;
	}

	private void processItem(INSBInsuresupplyparam item, String taskid, String inscomcode, String operator) {
		if (item == null) return;

		item.setOperator(operator);
		item.setTaskid(taskid);
		item.setInscomcode(inscomcode);

		if (StringUtil.isEmpty(item.getItemoptionallist()) && item.getItemoptions() != null && !item.getItemoptions().isEmpty()) {
			item.setItemoptionallist(JsonUtil.getJsonString(item.getItemoptions()));
		}

		if (StringUtil.isEmpty(item.getIteminputtype())) {
			item.setIteminputtype("text");
		}
	}

	/**
	 * 插入数据，插入原则：根据任务号、投保公司编码查询数据表中已有的数据，
	 * 不在已有数据中，新增；
	 * 在已有数据中，不增不改不删除；
	 * 已有数据中存在 当前数据中(insuresupplyparamList) 没有的数据，删除
	 * @param insuresupplyparamList
	 * @param taskid
	 * @param inscomcode
	 * @return
	 */
	public int insertByTask(List<INSBInsuresupplyparam> insuresupplyparamList, String taskid, String inscomcode) {
		LogUtil.info(taskid + "," + inscomcode + "补充数据项：" + com.common.JsonUtil.serialize(insuresupplyparamList));
		if (StringUtil.isEmpty(taskid) || StringUtil.isEmpty(inscomcode)) {
			LogUtil.error("任务号或投保公司编码为空："+ taskid + "," + inscomcode);
			return 0;
		}
		if (insuresupplyparamList == null || insuresupplyparamList.isEmpty()) {
			LogUtil.error("数据为空："+ taskid + "," + inscomcode);
			return 0;
		}

		//删除不在insuresupplyparamList中的数据
		List<INSBInsuresupplyparam> paramList = insbInsuresupplyparamDao.getByTask(taskid, inscomcode);
		int result = 0;

		for (INSBInsuresupplyparam supplyparam : paramList) {
			if (!insuresupplyparamList.contains(supplyparam)) {
				insbInsuresupplyparamDao.deleteById(supplyparam.getId());
				result += 1;
			}
		}

		for (INSBInsuresupplyparam param : insuresupplyparamList) {
			if (!paramList.contains(param)) {
				processItem(param, taskid, inscomcode, "admin");
				insbInsuresupplyparamDao.insert(param);
				result += 1;
			}
		}

		return result;
	}

	/**
	 * 更新数据，列表元素数据格式为：["数据项一编码|||数据项一的值","数据项二编码|||数据项二的值"]
	 * 一个数据项一个元素，内容包含数据项编码和前端录入的值，编码与值之间用三竖线（|||）分隔
	 * 表中没数据不更新，不增加，因为缺少其他数据项类型等数据
	 * @param insuresupplyparamList
	 * @param taskid
	 * @param inscomcode
	 * @return
	 */
	public int updateByTask(List<String> insuresupplyparamList, String taskid, String inscomcode, String operator) {
		LogUtil.info(taskid + "," + inscomcode + "更新补充数据项：" + com.common.JsonUtil.serialize(insuresupplyparamList));
		if (StringUtil.isEmpty(taskid) || StringUtil.isEmpty(inscomcode)) {
			LogUtil.error("任务号或投保公司编码为空："+ taskid + "," + inscomcode);
			return 0;
		}
		if (insuresupplyparamList == null || insuresupplyparamList.isEmpty()) {
			LogUtil.error("数据为空："+ taskid + "," + inscomcode);
			return 0;
		}

		List<INSBInsuresupplyparam> paramList = insbInsuresupplyparamDao.getByTask(taskid, inscomcode);
		if (paramList.isEmpty()) return 0;

		String datas[] = null;
		String insuredEmail = null;
		String insuredMobile = null;
		int result = 0;

		for (String data : insuresupplyparamList) {
			if (StringUtil.isEmpty(data)) continue;

			datas = data.split("\\|\\|\\|");
			if (datas.length < 2) continue;

			for (INSBInsuresupplyparam supplyparam : paramList) {
				if (supplyparam.getItemcode().equalsIgnoreCase(datas[0])) {
					if("insuredEmail".equalsIgnoreCase(supplyparam.getItemcode())){
						insuredEmail = datas[1];
					}
					if("insuredMobile".equalsIgnoreCase(supplyparam.getItemcode())){
						insuredMobile = datas[1];
					}
					supplyparam.setItemvalue(datas[1]);
					supplyparam.setOperator(operator);
					insbInsuresupplyparamDao.updateById(supplyparam);
					result += 1;
					break;
				}
			}
		}

		//需求1579更新补充数据项是时候、把被保人的手机号、邮箱信息也更新
		INSBInsuredhis insuredhis = new INSBInsuredhis();
		insuredhis.setTaskid(taskid);
		insuredhis.setInscomcode(inscomcode);
		insuredhis = insbInsuredhisService.queryOne(insuredhis);
		if (insuredhis != null && StringUtil.isNotEmpty(insuredhis.getPersonid())) {
			INSBPerson person = insbPersonService.queryById(insuredhis.getPersonid());
			person.setOperator(operator);
			if(insuredEmail!=null) person.setEmail(insuredEmail);
			if(insuredMobile!=null) person.setCellphone(insuredMobile);
			if(insuredEmail!=null || insuredMobile!=null) {//没修改的选项、不做更新
				insbPersonService.updateById(person);
			}
		}

		return result;
	}

	/**
	 * 保存数据，数据项有值的更新（表中不存在则新增），没有值的删除
	 * @param insuresupplyparamList
	 * @param taskid
	 * @param inscomcode
	 * @return
	 */
	public int saveByTask(List<INSBInsuresupplyparam> insuresupplyparamList, String taskid, String inscomcode, String operator) {
		LogUtil.info(taskid + "," + inscomcode + "保存补充数据项：" + com.common.JsonUtil.serialize(insuresupplyparamList));
		if (StringUtil.isEmpty(taskid) || StringUtil.isEmpty(inscomcode)) {
			LogUtil.error("任务号或投保公司编码为空："+ taskid + "," + inscomcode);
			return 0;
		}
		if (insuresupplyparamList == null || insuresupplyparamList.isEmpty()) {
			LogUtil.error("数据为空："+ taskid + "," + inscomcode);
			return 0;
		}

		List<INSBInsuresupplyparam> paramList = insbInsuresupplyparamDao.getByTask(taskid, inscomcode);
		int result = 0;
		String insuredEmail = null;
		String insuredMobile = null;
		//需求1579更新补充数据项是时候、把被保人的手机号、邮箱信息也更新
		INSBInsuredhis insuredhis = new INSBInsuredhis();
		insuredhis.setTaskid(taskid);
		insuredhis.setInscomcode(inscomcode);
		insuredhis = insbInsuredhisService.queryOne(insuredhis);

		for (INSBInsuresupplyparam data : insuresupplyparamList) {
			boolean exist = false;

			for (INSBInsuresupplyparam supplyparam : paramList) {
				if (supplyparam.getItemcode().equalsIgnoreCase(data.getItemcode())) {
					exist = true;
					if("insuredEmail".equalsIgnoreCase(supplyparam.getItemcode())){
						insuredEmail = data.getItemvalue();
					}
					if("insuredMobile".equalsIgnoreCase(supplyparam.getItemcode())){
						insuredMobile = data.getItemvalue();
					}
					if (StringUtil.isEmpty(data.getItemvalue())) {
						insbInsuresupplyparamDao.deleteById(supplyparam.getId());
						result += 1;
					} else {
						supplyparam.setItemvalue(data.getItemvalue());
						supplyparam.setOperator(operator);
						insbInsuresupplyparamDao.updateById(supplyparam);
						result += 1;
					}
				}
			}

			if (!exist && StringUtil.isNotEmpty(data.getItemvalue()) && StringUtil.isNotEmpty(data.getItemcode())) {
				processItem(data, taskid, inscomcode, operator);
				if("insuredEmail".equalsIgnoreCase(data.getItemcode())){
					insuredEmail = data.getItemvalue();
				}
				if("insuredMobile".equalsIgnoreCase(data.getItemcode())){
					insuredMobile = data.getItemvalue();
				}
				insbInsuresupplyparamDao.insert(data);
				result += 1;
			}
		}

		if (insuredhis != null && StringUtil.isNotEmpty(insuredhis.getPersonid())) {
			INSBPerson person = insbPersonService.queryById(insuredhis.getPersonid());
			person.setOperator(operator);
			if(insuredEmail!=null) person.setEmail(insuredEmail);
			if(insuredMobile!=null) person.setCellphone(insuredMobile);
			if(insuredEmail!=null || insuredMobile!=null) {//没修改的选项、不做更新
				insbPersonService.updateById(person);
			}
		}

		return result;
	}

	public Map<String, String> getParamsByTask(String taskid, String inscomcode, boolean editable) {
		Map<String, String> result = new HashMap<>();
		if (StringUtil.isEmpty(taskid) || StringUtil.isEmpty(inscomcode)) {
			LogUtil.error("任务号或投保公司编码为空："+ taskid + "," + inscomcode);
			return result;
		}

		List<INSBInsuresupplyparam> paramList = insbInsuresupplyparamDao.getByTask(taskid, inscomcode);
		if (paramList.isEmpty()) return result;

		for (INSBInsuresupplyparam supplyparam : paramList) {
			result.put(supplyparam.getItemcode()+"_code", supplyparam.getItemcode());
			result.put(supplyparam.getItemcode()+"_inputtype", supplyparam.getIteminputtype());
			result.put(supplyparam.getItemcode()+"_value", supplyparam.getItemvalue());

			if (!editable && supplyparam.getItemcode().endsWith("DocumentType")) {
				for (INSCCode code : certKinds) {
					if (code.getCodevalue().equals(supplyparam.getItemvalue())) {
						result.put(supplyparam.getItemcode()+"_value", code.getCodename());
						break;
					}
				}
			}
		}

		return result;
	}
}