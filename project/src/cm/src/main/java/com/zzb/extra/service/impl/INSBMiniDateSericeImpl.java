package com.zzb.extra.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.extra.dao.INSBMiniDateDao;
import com.zzb.extra.entity.INSBMiniDate;
import com.zzb.extra.service.INSBMiniDateService;
/**
 * 
 * @author Shigw
 * -----------------
 */
@Service
@Transactional
public class INSBMiniDateSericeImpl extends BaseServiceImpl<INSBMiniDate> implements INSBMiniDateService {

	@Resource
	private INSBMiniDateDao insbMinDateDao;
	

	@Override
	public int updateById(INSBMiniDate date) {
		// TODO Auto-generated method stub
		return insbMinDateDao.updateById(date);
	}


	@Override
	public int getWorkday(final int day) {

		int tmpDate = 0;
		int dateDiff = 0;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
//		gc.add(gc.DATE, 1);
		c.add(Calendar.DAY_OF_MONTH, 1);
//		INSBMiniDate date = new INSBMiniDate();
		Map<String, Object> map = new HashMap<>();
		

		while (tmpDate < day) {
			map.put("datestr", sf.format(c.getTime()));
			
			INSBMiniDate date =  insbMinDateDao.selectDateByDatestr(map);
			/**
			 * 如果用户没有配置日期，则默认为五天
			 */
			if(date == null) {
				
				return 5;
			}
			else if(!date.getWeekday().equals("星期日") && !date.getWeekday().equals("星期六")) {
				
				if(!date.getDatetype().equals("02")) {

				
					dateDiff++;
					tmpDate++;
					c.add(Calendar.DAY_OF_MONTH, 1);

//					System.out.println("工作日      "+sf.format(c.getTime()));

				} else {
					c.add(Calendar.DAY_OF_MONTH, 1);
					dateDiff++;
//					System.out.println("节假日       " + sf.format(c.getTime()));
				}
			} else {
				if(date.getDatetype().equals("01")) {
					tmpDate++;
					dateDiff++;
					c.add(Calendar.DAY_OF_MONTH, 1);
//					System.out.println("工作日      "+sf.format(c.getTime()));
				}else {
					
					c.add(Calendar.DAY_OF_MONTH, 1);
					dateDiff++;
//					System.out.println("节假日       " + sf.format(c.getTime()));
				}
			}
//			

		}
		c.add(Calendar.DAY_OF_MONTH, -1);
		System.out.println(sf.format(c.getTime()));
		
		return dateDiff;
	
	}

	@Override
	public int insertDate(INSBMiniDate date) {
		// TODO Auto-generated method stub
		return insbMinDateDao.insertDate(date);
	}

	@Override
	public List<INSBMiniDate> selectByDatetype(String datetype) {
		// TODO Auto-generated method stub
		return insbMinDateDao.selectByDatetype(datetype);
	}

	@Override
	public List<INSBMiniDate> selectDate(Map map) {
		// TODO Auto-generated method stub
		return insbMinDateDao.selectDate(map);
	}

	@Override
	public INSBMiniDate selectDateByDatestr(Map map) {
		// TODO Auto-generated method stub
		return insbMinDateDao.selectDateByDatestr(map);
	}

	@Override
	public Map<String, Object> queryMiniDateList(Map<String, Object> map) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<Object, Object>> rows = insbMinDateDao.queryMiniDateList(map);
		
		long total = insbMinDateDao.queryCountMiniDate(map);
		
		resultMap.put("rows", rows);
		
		resultMap.put("total", total);
		
		return resultMap;
	}


	@Override
	protected BaseDao<INSBMiniDate> getBaseDao() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void deleteByYear(String year) {
		// TODO Auto-generated method stub
		
		insbMinDateDao.deleteByYear(year);
		
	}

}
