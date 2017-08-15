package com.zzb.cm.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.UUIDUtils;
import com.zzb.cm.dao.INSBOrdernumberDao;
import com.zzb.cm.entity.INSBOrdernumber;
import com.zzb.cm.service.INSBOrdernumberService;

@Service
@Transactional
public class INSBOrdernumberServiceImpl extends BaseServiceImpl<INSBOrdernumber> implements
		INSBOrdernumberService {
	/**
	 * 生成订单号时对象锁
	 */
	public static final String LOCK = "table_insbordernumber_lock";
	@Resource
	private INSBOrdernumberDao insbOrdernumberDao;

	@Override
	protected BaseDao<INSBOrdernumber> getBaseDao() {
		return insbOrdernumberDao;
	}

	/**
	 * 得到订单生成号
	 */
	@Override
	public String getGeneralOrdernumber(Date date) {
		String generalOrdernumber = null;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		INSBOrdernumber ordernumber = null;
		try {
			if(date!=null){
				int number = 1;
				String[] dateList = sdf.format(date).split("-");
				synchronized (LOCK){
					ordernumber = insbOrdernumberDao.selectByDate(dateList[0]);
					if(ordernumber!=null){
						number = ordernumber.getTransaction()+1;
						ordernumber.setTransaction(number);
						insbOrdernumberDao.updateById(ordernumber);
					}else{
						ordernumber = new INSBOrdernumber();
						ordernumber.setId(UUIDUtils.random());
						ordernumber.setDate(dateList[0]);
						ordernumber.setPrfix("");
						ordernumber.setTransaction(number);
						insbOrdernumberDao.insert(ordernumber);
					}
				}
				String str = String.format("%016d", number);
				generalOrdernumber = ordernumber.getPrfix()+dateList[0]+str;
			}else{
				throw new RuntimeException("date is null,can not generate ordernumber!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return generalOrdernumber;
	}

}