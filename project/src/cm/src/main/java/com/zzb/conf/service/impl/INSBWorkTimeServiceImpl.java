package com.zzb.conf.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cninsure.core.utils.LogUtil;
import com.common.ModelUtil;
import com.zzb.conf.dao.*;
import com.zzb.conf.entity.*;
import org.jivesoftware.smackx.pubsub.GetItemsRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.service.INSCDeptService;
import com.zzb.conf.service.INSBWorkTimeService;
import com.zzb.model.WorkTimeModel;

@Service
@Transactional
public class INSBWorkTimeServiceImpl extends BaseServiceImpl<INSBWorktime>
		implements INSBWorkTimeService {

	@Resource
	private INSBWorktimeDao insbWorktimeDao;

	@Resource
	private INSBWorktimeareaDao insbWorktimeareaDao;

	@Resource
	private INSBHolidayareaDao insbHolidayareaDao;

	@Resource
	private INSCDeptService deptService;
	@Override
	protected BaseDao<INSBWorktime> getBaseDao() {
		// TODO Auto-generated method stub
		return insbWorktimeDao;
	}

	// 总页数
	private int getTotalPage(long total, int pageSize) {
		// 总条数/每页显示的条数=总页数
		int size = (int) (total / pageSize);
		// 最后一页的条数
		int mod = (int) (total % pageSize);
		if (mod != 0)
			size++;
		return total == 0 ? 1 : size;
	}

	// 分页展示 时间管理
	@Override
	public Map<Object, Object> queryworktimelistByPage(int currentPage,String onlyfuture2) {
		int pageSize = 3;
		long total = insbWorktimeDao.selectCount();
		int totalPage = getTotalPage(total, pageSize);
		int currentPageTrue = currentPage == 0 ? 1 : currentPage;

		Map<Object, Object> resultMap = new HashMap<Object, Object>();
		Map<Object, Object> map = new HashMap<Object, Object>();

		resultMap.put("currentPage", currentPageTrue);
		resultMap.put("totalPage", totalPage);
		resultMap.put("total", total);

		int limit = (currentPage - 1) * pageSize;
		map.put("limit", limit);
		map.put("pageSize", pageSize);

		List<Map<Object, Object>> newrowList = new ArrayList<Map<Object, Object>>();
		List<INSBWorktime> worktimeList = insbWorktimeDao.selectByPage(map);
		for (int i = 0; i < worktimeList.size(); i++) {
			Map<Object, Object> wtmap = new HashMap<Object, Object>();

			String deptid = worktimeList.get(i).getInscdeptid();
			INSCDept d = deptService.queryById(deptid);
			String deptname="";
			if(d!=null){
				deptname = deptService.queryById(deptid).getComname();
			}

			String insbworktimeid = worktimeList.get(i).getId();
			Map<String, String> map1 = new HashMap<String, String>(); // 查询工作时间参数
			Map<String, String> map2 = new HashMap<String, String>(); // 查询工作时间提醒时间参数
			Map<String, String> map3 = new HashMap<String, String>(); // 查询节假日时间参数
			Map<String, String> map4 = new HashMap<String, String>(); // 查询节假日值班时间参数
			if (insbworktimeid != null) {
				map1.put("insbworktimeid", insbworktimeid);
				map1.put("isremindtime", "0");
			}
			if (insbworktimeid != null) {
				map2.put("insbworktimeid", insbworktimeid);
				map2.put("isremindtime", "1");
			}
			if (insbworktimeid != null) {
				map3.put("insbworktimeid", insbworktimeid);
				map3.put("isonduty", "0");
				map3.put("onlyfuture", onlyfuture2);
			}
			if (insbworktimeid != null) {
				map4.put("insbworktimeid", insbworktimeid);
				map4.put("isonduty", "1");
				map4.put("onlyfuture", onlyfuture2);
			}
			List<INSBWorktimearea> worktimeAreaList = insbWorktimeareaDao
					.selectByWorktimeId(map1); // 工作时间list
			List<INSBWorktimearea> remindWorkTimeAreaList = insbWorktimeareaDao
					.selectByWorktimeId(map2); // 工作时间提醒时间list
			List<INSBHolidayarea> holidaylist = insbHolidayareaDao
					.selectByWorktimeId(map3); // 节假日list
			List<INSBHolidayarea> holidayworklist = insbHolidayareaDao
					.selectByWorktimeId(map4); // 节假日值班list

			String paytimetype = worktimeList.get(i).getPaytimetype();
			
			wtmap.put("insbworktimeid", insbworktimeid);
			wtmap.put("deptid", deptid);
			wtmap.put("deptname", deptname);
			wtmap.put("paytimetype", paytimetype);
			wtmap.put("noworkwords", worktimeList.get(i).getNoworkwords());
			wtmap.put("workwords", worktimeList.get(i).getWorkwords());
			wtmap.put("networkstate", worktimeList.get(i).getNetworkstate());
			if (worktimeAreaList != null) {
				wtmap.put("workdaylist", worktimeAreaList); // 工作时间list
			}
			if (remindWorkTimeAreaList != null) {
				wtmap.put("workdayremindList", remindWorkTimeAreaList);
			}
			if (holidaylist != null) {
				wtmap.put("holidaylist", holidaylist); // 节假日list
			}
			if (holidayworklist != null) {
				wtmap.put("holidayworklist", holidayworklist); // 节假日值班时间段list
			}

			newrowList.add(wtmap);
		}

		resultMap.put("rowList", newrowList);
		return resultMap;
	}

	// 通过选择机构【查询】
	@Override
	public Map<Object, Object> queryOneBydeptid(String inscdeptid) {
		Map<Object, Object> resultMap = new HashMap<Object, Object>();
		List<Map<Object, Object>> newrowList = new ArrayList<Map<Object, Object>>();
		Map<Object, Object> wtmap = new HashMap<Object, Object>();
		/*
		 * INSBWorktime worktime = new INSBWorktime();
		 * worktime.setInscdeptid(inscdeptid); INSBWorktime insbWorktime =
		 * insbWorktimeDao.selectOne(worktime);
		 */

		INSBWorktime insbWorktime = insbWorktimeDao.selectOneBydeptId(inscdeptid);
		if (insbWorktime != null && !insbWorktime.equals(null)) {

			String deptname = deptService.queryById(insbWorktime.getInscdeptid()).getComname();

			String insbworktimeid = insbWorktime.getId();
			Map<String, String> map1 = new HashMap<String, String>(); // 查询工作时间参数
			Map<String, String> map2 = new HashMap<String, String>(); // 查询工作时间提醒时间参数
			Map<String, String> map3 = new HashMap<String, String>(); // 查询节假日时间参数
			Map<String, String> map4 = new HashMap<String, String>(); // 查询节假日值班时间参数
			if (insbworktimeid != null) {
				map1.put("insbworktimeid", insbworktimeid);
				map1.put("isremindtime", "0");
			}
			if (insbworktimeid != null) {
				map2.put("insbworktimeid", insbworktimeid);
				map2.put("isremindtime", "1");
			}
			if (insbworktimeid != null) {
				map3.put("insbworktimeid", insbworktimeid);
				map3.put("isonduty", "0");
			}
			if (insbworktimeid != null) {
				map4.put("insbworktimeid", insbworktimeid);
				map4.put("isonduty", "1");
			}
			List<INSBWorktimearea> worktimeAreaList = insbWorktimeareaDao
					.selectByWorktimeId(map1); // 工作时间list
			List<INSBWorktimearea> remindWorkTimeAreaList = insbWorktimeareaDao
					.selectByWorktimeId(map2); // 工作时间提醒时间list
			List<INSBHolidayarea> holidaylist = insbHolidayareaDao
					.selectByWorktimeId(map3); // 节假日list
			List<INSBHolidayarea> holidayworklist = insbHolidayareaDao
					.selectByWorktimeId(map4); // 节假日值班list

			String paytimetype = insbWorktime.getPaytimetype();
			/*String paytype = insbWorktime.getPaytimetype();
			String paytimetype = "";
			if (paytype == "0" || "0".equals(paytype)) {
				paytimetype = "每天工作时间";
			}
			if (paytype == "1" || "1".equals(paytype)) {
				paytimetype = "报价有效期前一个工作日的工作时间";
			}*/
			wtmap.put("insbworktimeid", insbworktimeid);
			wtmap.put("deptid", inscdeptid);
			wtmap.put("deptname", deptname);
			wtmap.put("paytimetype", paytimetype);
			wtmap.put("noworkwords", insbWorktime.getNoworkwords());
			wtmap.put("workwords", insbWorktime.getWorkwords());
			wtmap.put("networkstate", insbWorktime.getNetworkstate());
			if (worktimeAreaList != null) {
				wtmap.put("workdaylist", worktimeAreaList); // 工作时间list
			}
			if (remindWorkTimeAreaList != null) {
				wtmap.put("workdayremindList", remindWorkTimeAreaList);
			}
			if (holidaylist != null) {
				wtmap.put("holidaylist", holidaylist); // 节假日list
			}
			if (holidayworklist != null) {
				wtmap.put("holidayworklist", holidayworklist); // 节假日值班时间段list
			}

			newrowList.add(wtmap);
			resultMap.put("currentPage", 1);
			resultMap.put("totalPage", 1);
			resultMap.put("total", 1);
			resultMap.put("rowList", newrowList);
		}
		if (insbWorktime == null) {
			wtmap.put("deptname", "无数据");
			wtmap.put("paytimetype", "无数据");
			wtmap.put("noworkwords", "无数据");
			wtmap.put("workwords", "无数据");
			
			List<INSBWorktimearea> worktimeAreaList = new ArrayList<INSBWorktimearea>();
			List<INSBWorktimearea> remindWorkTimeAreaList = new ArrayList<INSBWorktimearea>();
			List<INSBHolidayarea> holidaylist = new ArrayList<INSBHolidayarea>();
			List<INSBHolidayarea> holidayworklist = new ArrayList<INSBHolidayarea>();
			
			wtmap.put("workdaylist", worktimeAreaList); // 工作时间list
			wtmap.put("workdayremindList", remindWorkTimeAreaList	);
			wtmap.put("holidaylist", holidaylist); // 节假日list
			wtmap.put("holidayworklist", holidayworklist); // 节假日值班时间段list
			resultMap.put("currentPage", 0);
			resultMap.put("totalPage", 0);
			resultMap.put("total", 0);
			newrowList.add(wtmap);
			resultMap.put("rowList", newrowList);
		}
		return resultMap;
	}
	
	/**
	 * 通过选择机构【查询】
	 */
	@Override
	public Map<Object, Object> queryOneFuture(Map<Object,Object> map) {
		Map<Object, Object> resultMap = new HashMap<Object, Object>();
		List<Map<Object, Object>> newrowList = new ArrayList<Map<Object, Object>>();
		Map<Object, Object> wtmap = new HashMap<Object, Object>();
		
		List<Map<Object,Object>> wtimeAndWeekListMap = insbWorktimeDao.selectOneFuture(map); //普通信息和周集合
		System.out.println(wtimeAndWeekListMap+"sssssssssssssssddd");
		System.out.println(wtimeAndWeekListMap.size());
		if(wtimeAndWeekListMap.size() > 0 && null != wtimeAndWeekListMap && !"".equals(wtimeAndWeekListMap)){
			Map<Object, Object> htmap = new HashMap<Object, Object>();
			htmap.put("insbworktimeid", wtimeAndWeekListMap.get(0).get("id"));
			htmap.put("future", map.get("future"));
			List<Map<Object,Object>> holidayListMap = insbHolidayareaDao.selectFuture(htmap);
			System.out.println(holidayListMap);
			
			List<INSBWorktimearea> worktimeAreaList = new ArrayList<INSBWorktimearea>();
			List<INSBWorktimearea> remindWorkTimeAreaList = new ArrayList<INSBWorktimearea>();
			List<INSBHolidayarea> holidaylist = new ArrayList<INSBHolidayarea>();
			List<INSBHolidayarea> holidayworklist = new ArrayList<INSBHolidayarea>();
			
			if(null != wtimeAndWeekListMap){
				for(Map<Object, Object> wta : wtimeAndWeekListMap){
					if (wta.get("isremindtime") == null) continue;
					if(wta.get("isremindtime").equals("0")){
						INSBWorktimearea iwta = new INSBWorktimearea();
						iwta.setWorkdaystart(String.valueOf(wta.get("workdaystart")));
						iwta.setWorkdayend(String.valueOf(wta.get("workdayend")));
						iwta.setDaytimestart(String.valueOf(wta.get("daytimestart")));
						iwta.setDaytimeend(String.valueOf(wta.get("daytimeend")));
						worktimeAreaList.add(iwta);
					}
					if(wta.get("isremindtime").equals("1")){
						INSBWorktimearea rwta = new INSBWorktimearea();
						rwta.setWorkdaystart(String.valueOf(wta.get("workdaystart")));
						rwta.setWorkdayend(String.valueOf(wta.get("workdayend")));
						rwta.setDaytimestart(String.valueOf(wta.get("daytimestart")));
						rwta.setDaytimeend(String.valueOf(wta.get("daytimeend")));
						remindWorkTimeAreaList.add(rwta);
					}
				}
			}
			if(null != holidayListMap){
				for(Map<Object, Object> hmap : holidayListMap){
					if(hmap.get("isonduty").equals("0")){
						INSBHolidayarea ihda = new INSBHolidayarea();
						ihda.setDatestart(String.valueOf(hmap.get("datestart")));
						ihda.setDateend(String.valueOf(hmap.get("dateend")));
						ihda.setDaytimestart(String.valueOf(hmap.get("daytimestart")));
						ihda.setDaytimeend(String.valueOf(hmap.get("daytimeend")));
						holidaylist.add(ihda);
					}
					if(hmap.get("isonduty").equals("1")){
						INSBHolidayarea ihdaw = new INSBHolidayarea();
						ihdaw.setDatestart(String.valueOf(hmap.get("datestart")));
						ihdaw.setDateend(String.valueOf(hmap.get("dateend")));
						ihdaw.setDaytimestart(String.valueOf(hmap.get("daytimestart")));
						ihdaw.setDaytimeend(String.valueOf(hmap.get("daytimeend")));
						holidayworklist.add(ihdaw);
						
					}
				}
			}
			
			wtmap.put("insbworktimeid", wtimeAndWeekListMap.get(0).get("id"));
			wtmap.put("deptid", wtimeAndWeekListMap.get(0).get("deptid"));
			wtmap.put("deptname", wtimeAndWeekListMap.get(0).get("shortname"));
			wtmap.put("paytimetype", wtimeAndWeekListMap.get(0).get("paytimetype"));
			wtmap.put("noworkwords", wtimeAndWeekListMap.get(0).get("noworkwords"));
			wtmap.put("workwords", wtimeAndWeekListMap.get(0).get("workwords"));
			wtmap.put("networkstate", wtimeAndWeekListMap.get(0).get("networkstate"));
			
			wtmap.put("workdaylist", worktimeAreaList); // 工作时间list
			wtmap.put("workdayremindList", remindWorkTimeAreaList);
			wtmap.put("holidaylist", holidaylist); // 节假日list
			wtmap.put("holidayworklist", holidayworklist); // 节假日值班时间段list
			newrowList.add(wtmap);
			resultMap.put("rowList", newrowList);
			resultMap.put("currentPage", 0);
			resultMap.put("totalPage", 0);
			resultMap.put("total", 0);
		}else{
			wtmap.put("deptname", "该机构无数据,请添加");
			wtmap.put("noworkwords", "该机构无数据,请添加");
			wtmap.put("workwords", "该机构无数据,请添加");
			
			resultMap.put("currentPage", 0);
			resultMap.put("totalPage", 0);
			resultMap.put("total", 0);
			newrowList.add(wtmap);
			resultMap.put("rowList", newrowList);
		}
		
		return resultMap;
	}

	//编辑修改保存 || 添加
	@Override
	public void updateOrAdd(WorkTimeModel model) {
		
		INSBWorktime worktime= insbWorktimeDao.selectById(model.getInsbworktimeid());
		INSBWorktime worktime2= insbWorktimeDao.selectOneBydeptId(model.getInscdeptid()); //新增 worktime，通过deptid查看，是否已经存在
		
		if(worktime!=null){
			worktime.setModifytime(new Date());
			if(worktime.getNoworkwords()!=model.getNoworkwords()){
				worktime.setNoworkwords(model.getNoworkwords());
			} 
			if(worktime.getPaytimetype()!=model.getPaytimetype()){
				worktime.setPaytimetype(model.getPaytimetype());
			}
			if(worktime.getWorkwords()!=model.getWorkwords()){
				worktime.setWorkwords(model.getWorkwords());
			}
			if(worktime.getNetworkstate()!=model.getNetworkstate()){
				worktime.setNetworkstate(model.getNetworkstate());
			}
		    int updatei=insbWorktimeDao.updateById(worktime);
			System.out.println("修改worktime条数："+updatei);
			
			//通过insbworktimeareaId 修改或添加----工作时间
			if(model.getWorkTimeArea()!=null){
				//批量新增
				List<INSBWorktimearea> addList = new ArrayList<INSBWorktimearea>();
				INSBWorktimearea insbworktimearea=new INSBWorktimearea();
				for(int i=0;i<model.getWorkTimeArea().size();i++){
					String insbworktimeareaId=model.getWorkTimeArea().get(i).getId();
					if(insbworktimeareaId!=null){
						insbworktimearea=insbWorktimeareaDao.selectById(insbworktimeareaId);
						insbworktimearea.setWorkdaystart(model.getWorkTimeArea().get(i).getWorkdaystart());
						insbworktimearea.setWorkdayend(model.getWorkTimeArea().get(i).getWorkdayend());
						insbworktimearea.setDaytimestart(model.getWorkTimeArea().get(i).getDaytimestart());
						insbworktimearea.setDaytimeend(model.getWorkTimeArea().get(i).getDaytimeend());
						insbworktimearea.setModifytime(new Date());
						int updatej=insbWorktimeareaDao.updateById(insbworktimearea);
						System.out.println("修改insbworktimearea的条数："+updatej++);
					} else {
						insbworktimearea.setId(model.getWorkTimeArea().get(i).getId());
						insbworktimearea.setOperator("1");
						insbworktimearea.setCreatetime(new Date());
						insbworktimearea.setModifytime(null);
						insbworktimearea.setNoti(null);
						insbworktimearea.setInsbworktimeid(model.getInsbworktimeid());
						insbworktimearea.setWorkdaystart(model.getWorkTimeArea().get(i).getWorkdaystart());
						insbworktimearea.setWorkdayend(model.getWorkTimeArea().get(i).getWorkdayend());
						insbworktimearea.setDaytimestart(model.getWorkTimeArea().get(i).getDaytimestart());
						insbworktimearea.setDaytimeend(model.getWorkTimeArea().get(i).getDaytimeend());
						insbworktimearea.setIsremindtime("0");
						addList.add(insbworktimearea);
						insbWorktimeareaDao.insert(insbworktimearea);
					}					
				}
				//insbWorktimeareaDao.insertInBatch(addList);
			}
			//通过holidayareaId 修改或添加----节假日时间
			else if(model.getHolidayArea()!=null){
				//批量新增
				List<INSBHolidayarea> addList = new ArrayList<INSBHolidayarea>();
				
				INSBHolidayarea insbholidayarea = new INSBHolidayarea();
				for(int i=0;i<model.getHolidayArea().size();i++){
					String insbholidayareaId=model.getHolidayArea().get(i).getId();
					if(insbholidayareaId!=null){
						insbholidayarea=insbHolidayareaDao.selectById(insbholidayareaId);
						insbholidayarea.setDatestart(model.getHolidayArea().get(i).getDatestart());
						insbholidayarea.setDateend(model.getHolidayArea().get(i).getDateend());
						insbholidayarea.setDaytimestart(model.getHolidayArea().get(i).getDaytimestart());
						insbholidayarea.setDaytimeend(model.getHolidayArea().get(i).getDaytimeend());
						insbholidayarea.setIsonduty("0");
						insbholidayarea.setModifytime(new Date());
						int updatehj=insbHolidayareaDao.updateById(insbholidayarea);
						System.out.println("更新了holidayarea一条"+updatehj++);
					}else{
//						insbholidayarea.setId(model.getHolidayWorkArea().get(i).getId());
						insbholidayarea.setId(null);
						insbholidayarea.setOperator("1");
						insbholidayarea.setCreatetime(new Date());
						insbholidayarea.setModifytime(null);
						insbholidayarea.setNoti(null);
						insbholidayarea.setInsbworktimeid(model.getInsbworktimeid());
						insbholidayarea.setDatestart(model.getHolidayArea().get(i).getDatestart());
						insbholidayarea.setDateend(model.getHolidayArea().get(i).getDateend());
						insbholidayarea.setDaytimestart(model.getHolidayArea().get(i).getDaytimestart());
						insbholidayarea.setDaytimeend(model.getHolidayArea().get(i).getDaytimeend());
						insbholidayarea.setIsonduty("0");
						addList.add(insbholidayarea);
						insbHolidayareaDao.insert(insbholidayarea);
					}					
				}
				//insbHolidayareaDao.insertInBatch(addList);
			}
			//通过holidayareaId 修改或添加----节假日值班时间
			else if(model.getHolidayWorkArea()!=null){
				//批量新增
				List<INSBHolidayarea> addList = new ArrayList<INSBHolidayarea>();
				INSBHolidayarea insbholidayarea = new INSBHolidayarea();
				for(int i=0;i<model.getHolidayWorkArea().size();i++){
					String insbholidayareaId=model.getHolidayWorkArea().get(i).getId();
					if(insbholidayareaId!=null){
						insbholidayarea=insbHolidayareaDao.selectById(insbholidayareaId);
						insbholidayarea.setDatestart(model.getHolidayWorkArea().get(i).getDatestart());
						insbholidayarea.setDateend(model.getHolidayWorkArea().get(i).getDateend());
						insbholidayarea.setDaytimestart(model.getHolidayWorkArea().get(i).getDaytimestart());
						insbholidayarea.setDaytimeend(model.getHolidayWorkArea().get(i).getDaytimeend());
						insbholidayarea.setIsonduty("1");
						insbholidayarea.setModifytime(new Date());
						int updatehj=insbHolidayareaDao.updateById(insbholidayarea);
						System.out.println("更新了holidayarea一条:"+updatehj++);
					}else{
						insbholidayarea.setId(model.getHolidayWorkArea().get(i).getId());
						insbholidayarea.setOperator("1");
						insbholidayarea.setCreatetime(new Date());
						insbholidayarea.setModifytime(null);
						insbholidayarea.setNoti(null);
						insbholidayarea.setInsbworktimeid(model.getInsbworktimeid());
						insbholidayarea.setDatestart(model.getHolidayWorkArea().get(i).getDatestart());
						insbholidayarea.setDateend(model.getHolidayWorkArea().get(i).getDateend());
						insbholidayarea.setDaytimestart(model.getHolidayWorkArea().get(i).getDaytimestart());
						insbholidayarea.setDaytimeend(model.getHolidayWorkArea().get(i).getDaytimeend());
						insbholidayarea.setIsonduty("1");
						addList.add(insbholidayarea);
						insbHolidayareaDao.insert(insbholidayarea);
					}					
				}
				//insbHolidayareaDao.insertInBatch(addList);
			}
			//通过insbworktimeareaId 修改或添加----工作时间提醒remindWorkTimeArea
			else if(model.getRemindWorkTimeArea()!=null){
				//批量新增
				List<INSBWorktimearea> addList = new ArrayList<INSBWorktimearea>();
				
				INSBWorktimearea insbworktimearea=new INSBWorktimearea();
				for(int i=0;i<model.getRemindWorkTimeArea().size();i++){
					String insbworktimeareaId=model.getRemindWorkTimeArea().get(i).getId();
					if(insbworktimeareaId!=null){
						insbworktimearea=insbWorktimeareaDao.selectById(insbworktimeareaId);
						insbworktimearea.setWorkdaystart(model.getRemindWorkTimeArea().get(i).getWorkdaystart());
						insbworktimearea.setWorkdayend(model.getRemindWorkTimeArea().get(i).getWorkdayend());
						insbworktimearea.setDaytimestart(model.getRemindWorkTimeArea().get(i).getDaytimestart());
						insbworktimearea.setDaytimeend(model.getRemindWorkTimeArea().get(i).getDaytimeend());
						insbworktimearea.setModifytime(new Date());
						int updatej=insbWorktimeareaDao.updateById(insbworktimearea);
						System.out.println("修改insbworktimearea的条数："+updatej++);
					} else {
						insbworktimearea.setId(model.getRemindWorkTimeArea().get(i).getId());
						insbworktimearea.setOperator("1");
						insbworktimearea.setCreatetime(new Date());
						insbworktimearea.setModifytime(null);
						insbworktimearea.setNoti(null);
						insbworktimearea.setInsbworktimeid(model.getInsbworktimeid());
						insbworktimearea.setWorkdaystart(model.getRemindWorkTimeArea().get(i).getWorkdaystart());
						insbworktimearea.setWorkdayend(model.getRemindWorkTimeArea().get(i).getWorkdayend());
						insbworktimearea.setDaytimestart(model.getRemindWorkTimeArea().get(i).getDaytimestart());
						insbworktimearea.setDaytimeend(model.getRemindWorkTimeArea().get(i).getDaytimeend());
						insbworktimearea.setIsremindtime("1");
						addList.add(insbworktimearea);
						insbWorktimeareaDao.insert(insbworktimearea);
					}					
				}
				//insbWorktimeareaDao.insertInBatch(addList);
			}
			
			
		} else if(worktime==null && worktime2==null){
			INSBWorktime worktime1=new INSBWorktime();
			worktime1.setOperator("1");
			worktime1.setCreatetime(new Date());
			worktime1.setModifytime(null);
			worktime1.setNoti(null);
			worktime1.setInscdeptid(model.getInscdeptid());
			worktime1.setPaytimetype(model.getPaytimetype());
			worktime1.setNoworkwords(model.getNoworkwords());
			worktime1.setWorkwords(model.getWorkwords());
			insbWorktimeDao.insert(worktime1);
			
			if(model.getWorkTimeArea()!=null){
				//批量新增
				List<INSBWorktimearea> addList = new ArrayList<INSBWorktimearea>();
				INSBWorktimearea insbworktimearea1=new INSBWorktimearea();
				for(int i=0;i<model.getWorkTimeArea().size();i++){
					insbworktimearea1.setId(model.getWorkTimeArea().get(i).getId());
					insbworktimearea1.setOperator("1");
					insbworktimearea1.setCreatetime(new Date());
					insbworktimearea1.setModifytime(null);
					insbworktimearea1.setNoti(null);
					
					insbworktimearea1.setInsbworktimeid(worktime1.getId());
					
					insbworktimearea1.setWorkdaystart(model.getWorkTimeArea().get(i).getWorkdaystart());
					insbworktimearea1.setWorkdayend(model.getWorkTimeArea().get(i).getWorkdayend());
					insbworktimearea1.setDaytimestart(model.getWorkTimeArea().get(i).getDaytimestart());
					insbworktimearea1.setDaytimeend(model.getWorkTimeArea().get(i).getDaytimeend());
					insbworktimearea1.setIsremindtime("0");
					addList.add(insbworktimearea1);
					insbWorktimeareaDao.insert(insbworktimearea1);
				}
				//insbWorktimeareaDao.insertInBatch(addList);
			}
		} else if(worktime2.getInscdeptid()==model.getInscdeptid()){
			String str="该机构工作时间设置已经存在！";
			System.out.println(str);
		}
		
	}

    /**
     * 指定时间是否工作时间：-1 报价已过期，0 非工作时间，1 工作时间
     * @param time
     * @param deptid
     * @param payDate
     * @return
     */
	@Override
	public int inWorkTime(Date time, String deptid, Date payDate) {
		if (payDate == null) {
			LogUtil.info("1");
			return 1;
		}

		INSCDept dept = deptService.queryById(deptid);
		INSBWorktime insbWorktime;
		int times=0;

		//逐层查上级机构
		do{
			insbWorktime = new INSBWorktime();
			insbWorktime.setInscdeptid(dept.getId());
			insbWorktime = insbWorktimeDao.selectOne(insbWorktime);
			if(insbWorktime==null) {
				dept = deptService.queryById(dept.getUpcomcode());
				if(dept==null){
					LogUtil.info("2");
					return 1;
				}
				if(dept.getComgrade()==null||dept.getComgrade().equals("00")){
					LogUtil.info("3");
					return 1;
				}
			}
			times++;
		} while (insbWorktime==null && times<10);

		if(insbWorktime == null) {
			LogUtil.info("4");
			return 1;
		}
		if(times>8) {
			LogUtil.info("5");
			return 1;
		}

		LogUtil.info("inWorkTime deptid:" + insbWorktime.getInscdeptid() + " paytimetype:" + insbWorktime.getPaytimetype() + " state:" + insbWorktime.getNetworkstate() +
				" payDate:" +payDate + " now:" +time);

		if("0".equals(insbWorktime.getNetworkstate())){
			return 0;
		}

		Map<String, String> map1 = new HashMap<String, String>(); // 查询工作时间参数
		Map<String, String> map2 = new HashMap<String, String>(); // 查询节假日时间参数
		Map<String, String> map3 = new HashMap<String, String>(); // 查询节假日值班时间参数
		if (insbWorktime.getId() != null) {
			map1.put("insbworktimeid", insbWorktime.getId());
			map1.put("isremindtime", "0");

			map2.put("insbworktimeid", insbWorktime.getId());
			map2.put("isonduty", "0");
			map2.put("onlyfuture", null);

			map3.put("insbworktimeid", insbWorktime.getId());
			map3.put("isonduty", "1");
			map3.put("onlyfuture", null);
		} else {
			LogUtil.info("未配置工作时间");
			return 0;
		}

		List<INSBWorktimearea> worktimeAreaList = insbWorktimeareaDao.selectByWorktimeId(map1); // 工作时间list
		List<INSBHolidayarea> holidaylist = insbHolidayareaDao.selectByWorktimeId(map2); // 节假日list
		List<INSBHolidayarea> holidayworklist = insbHolidayareaDao.selectByWorktimeId(map3); // 节假日值班list

		return detectWorktime(time, payDate, insbWorktime, worktimeAreaList, holidayworklist, holidaylist);
	}

    /**
     * 指定时间是否工作时间：-1 报价已过期，0 非工作时间，1 工作时间
     * @param time
     * @param payDate
     * @param insbWorktime
     * @param worktimeAreaList
     * @param holidayworklist
     * @param holidaylist
     * @return
     */
	private int detectWorktime(Date time, Date payDate, INSBWorktime insbWorktime, List<INSBWorktimearea> worktimeAreaList, List<INSBHolidayarea> holidayworklist, List<INSBHolidayarea> holidaylist) {
		Calendar payCalendar = Calendar.getInstance();
		payCalendar.setTime(payDate);
		payCalendar.set(Calendar.HOUR_OF_DAY, 23);
		payCalendar.set(Calendar.MINUTE, 59);
		payCalendar.set(Calendar.SECOND, 59);
		payDate = payCalendar.getTime();

		long timeInLong = time.getTime();
		long payDateInLong = payDate.getTime();

		if (timeInLong > payDateInLong) {
			LogUtil.info("报价已过期");
			return -1;
		}

		for (INSBHolidayarea insbHolidayarea : holidayworklist) {
			LogUtil.info("inWorkTime getIsonduty:" + insbHolidayarea.getIsonduty() +
					" getDaystart:"+insbHolidayarea.getDatestart() + " getDayend:"+insbHolidayarea.getDateend() +
					" getDaytimestart:"+insbHolidayarea.getDaytimestart() + " getDaytimeend:"+insbHolidayarea.getDaytimeend());

			//这里不够严谨，应该按每天的开始和结束时间
			Date daytimestart = DateUtil.parseDateTime(insbHolidayarea.getDatestart()+ " "+ insbHolidayarea.getDaytimestart() + ":00");
			Date daytimeend = DateUtil.parseDateTime(insbHolidayarea.getDateend()+ " "+insbHolidayarea.getDaytimeend() + ":00");
			long daytimestartInLong = daytimestart.getTime(), daytimeendInLong = daytimeend.getTime();

			//节假日值班时间
			if(daytimestartInLong<=timeInLong && daytimeendInLong>=timeInLong){
				LogUtil.info("6");
				return 1;
			}

			//当前时间与报价有效期之间有工作时间
			if ("1".equals(insbWorktime.getPaytimetype())) {
				if ((timeInLong <= daytimestartInLong && payDateInLong >= daytimestartInLong) ||
						(timeInLong >= daytimestartInLong && timeInLong <= daytimeendInLong)) {
					LogUtil.info("7");
					return 1;
				}
			}
		}

		Boolean inholidaytime=false;
		for (INSBHolidayarea insbHolidayarea : holidaylist) {
			LogUtil.info("inWorkTime getIsonduty:" + insbHolidayarea.getIsonduty() +
					" getDaystart:"+insbHolidayarea.getDatestart() + " getDayend:"+insbHolidayarea.getDateend() +
					" getDaytimestart:"+insbHolidayarea.getDaytimestart() + " getDaytimeend:"+insbHolidayarea.getDaytimeend());

			Date daytimestart = DateUtil.parseDateTime(insbHolidayarea.getDatestart()+ " "+insbHolidayarea.getDaytimestart() + ":00");
			Date daytimeend = DateUtil.parseDateTime(insbHolidayarea.getDateend()+" "+insbHolidayarea.getDaytimeend() + ":00");
			//节假日时间
			if(daytimestart.getTime()<=timeInLong && daytimeend.getTime()>=timeInLong){
				inholidaytime=true;
				break;
			}
		}

		boolean inworktimearea = false;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(time);

		if ("1".equals(insbWorktime.getPaytimetype())) {//当前时间与报价有效期之间有工作时间
			int dayBetween = ModelUtil.daysBetween(time, payDate);
			int hit = 0, currhit = 0;
			String daytimeend = null;

			for (int i=0; i <= dayBetween; i+=1) {
				if (i > 0) {
					//天数递增直到报价有效期那天
					calendar.add(Calendar.DAY_OF_MONTH, i);
				}

				//星期几
				int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)-1;
				if (dayOfWeek == 0) {
					dayOfWeek = 7;
				}

				for (INSBWorktimearea worktimearea : worktimeAreaList) {
					LogUtil.info("inWorkTime getWorkdaystart:"+worktimearea.getWorkdaystart() + " getWorkdayend:"+worktimearea.getWorkdayend()
							+ " getDaytimestart:"+worktimearea.getDaytimestart() + " getDaytimeend:"+worktimearea.getDaytimeend());

					if(dayOfWeek>=Integer.parseInt(worktimearea.getWorkdaystart()) && dayOfWeek<=Integer.parseInt(worktimearea.getWorkdayend())){
						if (i == 0) {
							//当前时间就在工作日期
							currhit = 1;
							daytimeend = worktimearea.getDaytimeend();
						} else {
							//当前时间与报价有效期之间至少有一个工作时间（报价有效期是到23:59:59）
							hit += 1;
							break;
						}
					}
				}

				if (hit > 0) {
					break;
				}
			}

			if (hit > 0) {
				inworktimearea = true;
			} else if (currhit == 1) {
				//当前时间就在工作日期的情况下，要判断具体工作时间的小时分钟数
				int secondInHour = getSecondsInHour(calendar);
				inworktimearea = hasWorktime(secondInHour, daytimeend);
			}

			LogUtil.info("inWorkTime 1 inworktimearea:" + inworktimearea + " inholidaytime:" + inholidaytime);
			if(inworktimearea && !inholidaytime) {//在节假日值班日内或在上班日内但不在节假日内
				LogUtil.info("8");
				return 1;
			}

		} else {//每天工作时间
			int secondInHour = getSecondsInHour(calendar);

			//星期几
			int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)-1;
			if (dayOfWeek == 0) {
				dayOfWeek = 7;
			}

			for (INSBWorktimearea worktimearea : worktimeAreaList) {
				LogUtil.info("inWorkTime getWorkdaystart:"+worktimearea.getWorkdaystart() + " getWorkdayend:"+worktimearea.getWorkdayend()
						+ " getDaytimestart:"+worktimearea.getDaytimestart() + " getDaytimeend:"+worktimearea.getDaytimeend());

				if(dayOfWeek>=Integer.parseInt(worktimearea.getWorkdaystart()) && dayOfWeek<=Integer.parseInt(worktimearea.getWorkdayend())){
					if (inWorkingtime(secondInHour, worktimearea.getDaytimestart(), worktimearea.getDaytimeend())) {
						inworktimearea = true;
					}
				}
			}

			LogUtil.info("inWorkTime 0 inworktimearea:" + inworktimearea + " inholidaytime:" + inholidaytime);
			if(inworktimearea && !inholidaytime) {//在节假日值班日内或在上班日内但不在节假日内
				LogUtil.info("9");
				return 1;
			}
		}

		LogUtil.info("10");
		return 0;
	}

	private int getSecondsInHour(Calendar calendar) {
		return calendar.get(Calendar.HOUR_OF_DAY)*3600 + calendar.get(Calendar.MINUTE)*60 + calendar.get(Calendar.SECOND);
	}

	private boolean hasWorktime(int secondInHour, String daytimeend) {
		String[] endtimes = daytimeend.split(":");

		int endtime = 0;

		if (endtimes.length > 1) {
			endtime = Integer.parseInt(endtimes[0])*3600 + Integer.parseInt(endtimes[1])*60;
		} else if (endtimes.length == 1) {
			endtime = Integer.parseInt(endtimes[0])*3600;
		}

		return endtime >= secondInHour;
	}

	private boolean inWorkingtime(int secondInHour, String daytimestart, String daytimeend) {
		String[] starttimes = daytimestart.split(":");
		String[] endtimes = daytimeend.split(":");

		int starttime = 0, endtime = 0;

		if (starttimes.length > 1) {
			starttime = Integer.parseInt(starttimes[0])*3600 + Integer.parseInt(starttimes[1])*60;
		} else if (starttimes.length == 1) {
			starttime = Integer.parseInt(starttimes[0])*3600;
		}
		if (endtimes.length > 1) {
			endtime = Integer.parseInt(endtimes[0])*3600 + Integer.parseInt(endtimes[1])*60;
		} else if (endtimes.length == 1) {
			endtime = Integer.parseInt(endtimes[0])*3600;
		}

		return starttime <= secondInHour && endtime >= secondInHour;
	}

	/*public static void main(String[] args) {
		Date time = ModelUtil.conbertStringToDate("2017-01-06 15:31:00");
		Date payDate = ModelUtil.conbertStringToNyrDate("2017-01-06");
		INSBWorktime insbWorktime = new INSBWorktime();
		insbWorktime.setPaytimetype("1");

		List<INSBWorktimearea> worktimeAreaList = new ArrayList<>();
		INSBWorktimearea worktimearea = new INSBWorktimearea();
		worktimearea.setWorkdaystart("1");
		worktimearea.setWorkdayend("3");
		worktimearea.setDaytimestart("09:00");
		worktimearea.setDaytimeend("17:25");
		worktimeAreaList.add(worktimearea);
		INSBWorktimearea worktimearea1 = new INSBWorktimearea();
		worktimearea1.setWorkdaystart("5");
		worktimearea1.setWorkdayend("6");
		worktimearea1.setDaytimestart("10:00");
		worktimearea1.setDaytimeend("15:30");
		worktimeAreaList.add(worktimearea1);

		List<INSBHolidayarea> holidayworklist = new ArrayList<>();
		INSBHolidayarea holidaywork = new INSBHolidayarea();
		holidaywork.setDatestart("2017-01-04");
		holidaywork.setDateend("2017-01-04");
		holidaywork.setDaytimestart("10:00");
		holidaywork.setDaytimeend("16:00");
		holidaywork.setIsonduty("1");
		holidayworklist.add(holidaywork);

		List<INSBHolidayarea> holidaylist = new ArrayList<>();
		INSBHolidayarea holidayarea = new INSBHolidayarea();
		holidayarea.setDatestart("2017-01-01");
		holidayarea.setDateend("2017-01-02");
		holidayarea.setDaytimestart("00:00");
		holidayarea.setDaytimeend("23:55");
		holidayarea.setIsonduty("0");
		holidaylist.add(holidayarea);

		INSBWorkTimeServiceImpl service = new INSBWorkTimeServiceImpl();
		System.out.println(service.detectWorktime(time, payDate, insbWorktime, worktimeAreaList, holidayworklist, holidaylist));
	}*/
}