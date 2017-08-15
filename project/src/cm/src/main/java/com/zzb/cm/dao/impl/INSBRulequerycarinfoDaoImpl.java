package com.zzb.cm.dao.impl;

import com.cninsure.core.utils.StringUtil;
import com.zzb.cm.dao.INSBRulequeryclaimsDao;
import com.zzb.cm.dao.INSBRulequeryotherinfoDao;
import com.zzb.cm.dao.INSBRulequeryrepeatinsuredDao;
import com.zzb.cm.entity.*;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.dao.INSBRulequerycarinfoDao;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class INSBRulequerycarinfoDaoImpl extends BaseDaoImpl<INSBRulequerycarinfo> implements
		INSBRulequerycarinfoDao {

	@Resource
	private INSBRulequeryrepeatinsuredDao insbRulequeryrepeatinsuredDao;
	@Resource
	private INSBRulequeryclaimsDao insbRulequeryclaimsDao;
	@Resource
	private INSBRulequeryotherinfoDao insbRulequeryotherinfoDao;

	@Override
	public INSBLastyearinsureinfo queryLastYearClainInfo(String taskid) {
		INSBLastyearinsureinfo lastyearinsureinfo = new INSBLastyearinsureinfo();
		if (StringUtil.isEmpty(taskid)) {
			return lastyearinsureinfo;
		}
		lastyearinsureinfo.setTaskid(taskid);

		/*INSBRulequerycarinfo car = new INSBRulequerycarinfo();
		car.setTaskid(taskid);
		car = this.selectOne(car);
		if (car != null) {

		}*/

		List<INSBRulequeryrepeatinsured> insbRulequeryrepeatinsuredList= insbRulequeryrepeatinsuredDao.selectPolicy(taskid);
		for (INSBRulequeryrepeatinsured r : insbRulequeryrepeatinsuredList) {
			if (r == null ) continue;
			if (StringUtil.isEmpty(r.getRisktype())) continue;
			if (r.getRisktype().equals("0")) {
				lastyearinsureinfo.setSypolicyno(r.getPolicyno());
				if (StringUtil.isEmpty(lastyearinsureinfo.getSupplierid())) {
					lastyearinsureinfo.setSupplierid(r.getInscorpcode());
				}
				if (StringUtil.isEmpty(lastyearinsureinfo.getSuppliername())) {
					lastyearinsureinfo.setSuppliername(r.getInscorpname());
				}
				lastyearinsureinfo.setSystartdate(r.getPolicystarttime());
				lastyearinsureinfo.setSyenddate(r.getPolicyendtime());
			} else
			if (r.getRisktype().equals("1")) {
				lastyearinsureinfo.setJqpolicyno(r.getPolicyno());
				if (StringUtil.isEmpty(lastyearinsureinfo.getSupplierid())) {
					lastyearinsureinfo.setSupplierid(r.getInscorpcode());
				}
				if (StringUtil.isEmpty(lastyearinsureinfo.getSuppliername())) {
					lastyearinsureinfo.setSuppliername(r.getInscorpname());
				}
				lastyearinsureinfo.setJqstartdate(r.getPolicystarttime());
				lastyearinsureinfo.setJqenddate(r.getPolicyendtime());
			}
		}

		List<INSBRulequeryclaims> rulequeryclaimsList= insbRulequeryclaimsDao.selectPolicy(taskid);
		List<INSBRulequeryclaims> syList = new ArrayList<INSBRulequeryclaims>();
		List<INSBRulequeryclaims> jqList = new ArrayList<INSBRulequeryclaims>();
		for (INSBRulequeryclaims r : rulequeryclaimsList) {
			if (r == null ) continue;
			if (StringUtil.isEmpty(r.getRisktype())) continue;
			if (r.getRisktype().equals("0")) {
				if (StringUtil.isEmpty(lastyearinsureinfo.getInscomcode())) {
					lastyearinsureinfo.setInscomcode(r.getInscorpcode());
				}
				if (StringUtil.isEmpty(lastyearinsureinfo.getSupplierid()) && StringUtil.isNotEmpty(r.getInscorpcode())) {
					lastyearinsureinfo.setSupplierid(r.getInscorpcode());
				}
				//task 765 在平台信息中的“上年商业险投保公司”为空的情况下，如果理赔信息中查到了上年商业险投保公司，则将该值填入平台信息中。
				if (StringUtil.isEmpty(lastyearinsureinfo.getSuppliername()) && StringUtil.isNotEmpty(r.getInscorpname())) {
					lastyearinsureinfo.setSuppliername(r.getInscorpname());
				}
				syList.add(r);
			} else
			if (r.getRisktype().equals("1")) {
				if (StringUtil.isEmpty(lastyearinsureinfo.getInscomcode())) {
					lastyearinsureinfo.setInscomcode(r.getInscorpcode());
				}
				if (StringUtil.isEmpty(lastyearinsureinfo.getSupplierid()) && StringUtil.isNotEmpty(r.getInscorpcode())) {
					lastyearinsureinfo.setSupplierid(r.getInscorpcode());
				}
				//task 765 在平台信息中的“上年商业险投保公司”为空的情况下，如果理赔信息中查到了上年商业险投保公司，则将该值填入平台信息中。
				if (StringUtil.isEmpty(lastyearinsureinfo.getSuppliername()) && StringUtil.isNotEmpty(r.getInscorpname())) {
					lastyearinsureinfo.setSuppliername(r.getInscorpname());
				}
				jqList.add(r);
			}
		}

		lastyearinsureinfo.setSyclaims(JSONArray.fromObject(syList).toString());
		lastyearinsureinfo.setJqclaims(JSONArray.fromObject(jqList).toString());


		INSBRulequeryotherinfo other = new INSBRulequeryotherinfo();
		other.setTaskid(taskid);
		other = insbRulequeryotherinfoDao.selectOne(other);
		if (other != null) {
			lastyearinsureinfo.setSyclaimrate(StringUtil.isEmpty(other.getBwcommercialclaimrate()) ? 0.0 : Double.valueOf(other.getBwcommercialclaimrate()));
			lastyearinsureinfo.setSyclaimtimes(other.getClaimtimes());

			lastyearinsureinfo.setJqclaimtimes(other.getCompulsoryclaimtimes());
			lastyearinsureinfo.setJqclaimrate(0.0);

			lastyearinsureinfo.setFirstinsuretype(other.getFirstinsuretype());
			lastyearinsureinfo.setSylastclaimsum(other.getBwlastclaimsum());
			lastyearinsureinfo.setJqlastclaimsum(other.getBwlastcompulsoryclaimsum());
			lastyearinsureinfo.setTrafficoffence(0);
			lastyearinsureinfo.setTrafficoffencediscount(other.getTrafficoffencediscount());

			lastyearinsureinfo.setNoclaimdiscountcoefficient(String.valueOf(other.getNoclaimdiscountcoefficient()));
			lastyearinsureinfo.setNoclaimdiscountcoefficientreasons(String.valueOf(other.getNoclaimdiscountcoefficientreasons()));

			lastyearinsureinfo.setCompulsoryclaimrate(other.getCompulsoryclaimrate());
			lastyearinsureinfo.setBwcompulsoryclaimtimes(other.getBwcompulsoryclaimtimes());
			lastyearinsureinfo.setCompulsoryclaimratereasons(other.getCompulsoryclaimratereasons());

			lastyearinsureinfo.setBwcommercialclaimtimes(other.getBwcommercialclaimtimes());
			lastyearinsureinfo.setSflag("");
			lastyearinsureinfo.setJqrepeatinsurance("");
			lastyearinsureinfo.setRepeatinsurance("");
			lastyearinsureinfo.setLoyaltyreasons(other.getLoyaltyreasons());
		}

		return lastyearinsureinfo;
	}
}