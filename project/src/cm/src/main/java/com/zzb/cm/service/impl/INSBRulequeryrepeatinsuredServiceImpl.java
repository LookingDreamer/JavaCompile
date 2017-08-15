package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import com.common.ModelUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSBRulequeryrepeatinsuredDao;
import com.zzb.cm.entity.INSBRulequeryrepeatinsured;
import com.zzb.cm.service.INSBRulequeryrepeatinsuredService;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class INSBRulequeryrepeatinsuredServiceImpl extends BaseServiceImpl<INSBRulequeryrepeatinsured> implements
		INSBRulequeryrepeatinsuredService {
	@Resource
	private INSBRulequeryrepeatinsuredDao insbRulequeryrepeatinsuredDao;

	@Override
	protected BaseDao<INSBRulequeryrepeatinsured> getBaseDao() {
		return insbRulequeryrepeatinsuredDao;
	}


	//排序，返回的列表中大的在前面
	public void sortPolicies(List<INSBRulequeryrepeatinsured> policieslist) {
		if (policieslist == null || policieslist.isEmpty()) return;

		policieslist.sort(new Comparator<INSBRulequeryrepeatinsured>() {
			@Override
			public int compare(INSBRulequeryrepeatinsured o1, INSBRulequeryrepeatinsured o2) {
				Date date1 = ModelUtil.conbertStringToNyrDate(o1.getPolicyendtime());
				Date date2 = ModelUtil.conbertStringToNyrDate(o2.getPolicyendtime());

				if (date1 == null && date2 == null) return 0;
				if (date1 == null) return 1;
				if (date2 == null) return -1;

				return ModelUtil.compareDate(date2, date1);
			}
		});
	}

	public static void main(String[] args) {
		/*List<INSBRulequeryrepeatinsured> policieslist = new ArrayList<>();
		INSBRulequeryrepeatinsured a = new INSBRulequeryrepeatinsured();
		a.setPolicyendtime("2016-12-14");
		INSBRulequeryrepeatinsured b = new INSBRulequeryrepeatinsured();
		b.setPolicyendtime("2016-12-13");
		INSBRulequeryrepeatinsured c = new INSBRulequeryrepeatinsured();
		c.setPolicyendtime("2017-10-29");
		INSBRulequeryrepeatinsured d = new INSBRulequeryrepeatinsured();
		d.setPolicyendtime("2015-11-01");
		policieslist.add(a);
		policieslist.add(b);
		policieslist.add(c);
		policieslist.add(d);

		sortPolicies(policieslist);
		System.out.println(policieslist);*/
	}
}