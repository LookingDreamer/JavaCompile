package com.cninsure.system.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.quartz.manager.exe.Executer;
import com.cninsure.core.quartz.util.IPUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.system.dao.INSCCodeDao;
import com.cninsure.system.dao.INSCScheduleDao;
import com.cninsure.system.dao.INSCTaskDao;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.entity.INSCSchedule;
import com.cninsure.system.entity.INSCTask;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCCodeService;
import com.cninsure.system.service.INSCScheduleService;

@Service
@Transactional
public class INSCScheduleServiceImpl extends BaseServiceImpl<INSCSchedule>
		implements INSCScheduleService {
	@Resource
	private INSCScheduleDao inscScheduleDao;

	@Resource
	private INSCCodeDao inscCodeDao;

	@Resource
	private INSCTaskDao inscTaskDao;
	
	@Resource
	private INSCCodeService inscCodeService;

	@Override
	protected BaseDao<INSCSchedule> getBaseDao() {
		return inscScheduleDao;
	}

	@Override
	public String executebyids(List<String> ids) {
		StringBuffer msgsb = new StringBuffer();
		Map<String, String> map = new HashMap<String, String>();
		if (ids != null && ids.size() > 0 && !ids.isEmpty()) {
			List<INSCSchedule> stateList = inscScheduleDao
					.getScheduleStateListByIds(ids);
			int i = 0, j = 0;
			Integer state = null;
			String ip = null;
			for (INSCSchedule entity : stateList) {
				ip = entity.getIp();
				if (IPUtil.isLocalHostIp(ip)) {
					state = entity.getState();
					if (state != null && state.intValue() == 2) {
						i++;
					}
				} else {
					j++;
				}
			}
			if (i > 0) {
				msgsb.append("您选择的计划中有").append(i).append("个计划为停用，请启用后再执行！");
				map.put("fail", msgsb.toString());
			}
			if (j > 0) {
				msgsb.append("您选择的计划中有").append(j)
						.append("个计划为非本地执行计划，请选择本地执行计划后再执行！");
				map.put("fail", msgsb.toString());
			}

			if (i == 0 && j == 0) {
				StringBuffer sb = new StringBuffer();
				for (String id : ids) {
					if (StringUtils.isNotBlank(id)) {
						sb.append("'").append(id).append("',");
					}
				}
				String idsStr = sb.toString();
				if (idsStr != null && idsStr.length() > 1) {
					idsStr = idsStr.substring(0, idsStr.length() - 1);
					Executer executer = new Executer();
					executer.execute(idsStr, true);
					msgsb.append("操作成功！");
					map.put("success", msgsb.toString());
				}
			}
		} else {
			msgsb.append("操作失败，请重试！");
			map.put("fail", msgsb.toString());
		}
		JSONObject obj = JSONObject.fromObject(map);
		return obj.toString();
	}

	@Override
	public String getSchedule(String id) {
		String result = "";
		if (id != null) {
			INSCSchedule entity = this.queryById(id);
			if (entity != null) {
				JSONObject obj = JSONObject.fromObject(entity);
				result = obj.toString();
			}
		}
		return result;
	}

	@Override
	public String deleteByIds(List<String> ids) {
		if (ids != null && ids.size() > 0 && !ids.isEmpty()) {
			this.deleteByIdInBatch(ids);
		}
		return "redirect:/schedule/list";
	}

	private Date getNextExeDate(INSCSchedule schedule) {
		Date date = new Date();
		if (schedule != null) {
			Integer iscron = schedule.getIscron();
			Integer period = schedule.getPeriod();
			String periodunit = schedule.getPeriodunit();
			Date starttime = schedule.getStarttime();
			LogUtil.debug("起始时间：" + starttime);
			if (iscron != null && period != null
					&& StringUtils.isNotBlank(periodunit) && starttime != null) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(starttime);
				if (iscron.intValue() == 1) {
					if (periodunit.equals("1")) {
						calendar.add(Calendar.MINUTE, period);
					} else if (periodunit.equals("2")) {
						calendar.add(Calendar.HOUR_OF_DAY, period);
					} else if (periodunit.equals("3")) {
						calendar.add(Calendar.DAY_OF_YEAR, period);
					} else if (periodunit.equals("4")) {
						calendar.add(Calendar.MONTH, period);
					}

					date = calendar.getTime();
				} else if (iscron.intValue() == 2) {
					LogUtil.debug("cron方式的下次运行时间算法待定...");
				}

			}

		}
		LogUtil.debug("下次时间：" + date);
		return date;
	}

	@Override
	public String saveOrUpdateSchedule(INSCUser user, INSCSchedule schedule) {
		int flag = 0;
		String id = schedule.getId();
		String tasktypeid = schedule.getTasktypeid();
		String taskid = schedule.getTaskid();
		String tasktypename = "";
		if (StringUtils.isNotBlank(tasktypeid)) {
			INSCCode tasktype = inscCodeDao.selectById(tasktypeid);
			tasktypename = tasktype.getCodename();
		}
		String taskname = "";
		if (StringUtils.isNotBlank(taskid)) {
			INSCTask task = inscTaskDao.selectById(taskid);
			taskname = task.getTaskname();
		}

		schedule.setTasktypename(tasktypename);
		schedule.setTaskname(taskname);
		schedule.setNexttime(getNextExeDate(schedule));
		schedule.setOperator(user.getUsercode());
		if (StringUtils.isBlank(id)) {
			schedule.setCreatetime(new Date());
			this.insert(schedule);
			flag = 1;
		} else {
			INSCSchedule oldSchedule = this.queryById(id);
			schedule.setCreatetime(oldSchedule.getCreatetime());
			schedule.setModifytime(new Date());
			this.updateById(schedule);
			flag = 1;
		}

		JSONObject obj = new JSONObject();
		obj.accumulate("flag", flag);
		return obj.toString();
	}

	@Override
	public Map<String, Object> getEditInfo(String scheduleId,int offset,int limit) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<INSCCode> taskTypeList = inscCodeService.queryINSCCodeByCode("cmtasktimingtype", "cmtasktimingtype");
		map.put("taskTypeList", taskTypeList);
		Map<String, Object> Params=new HashMap<String, Object>();
	    Params.put("offset", offset);
	    Params.put("limit", limit);
		List<INSCTask> taskList = inscTaskDao.selectAll(Params);
		map.put("taskList", taskList);
		INSCSchedule scheEntity = this.queryById(scheduleId);
		map.put("scheEntity", scheEntity);
		return map;
	}

	@Override
	public String getScheduleList(Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		long count = inscScheduleDao.selectPagingCount(map);
		List<Map<Object, Object>> list = inscScheduleDao.selectScheduleListPaging(map);
		resultMap.put("total", count);
		resultMap.put("rows", list);
		
		JSONObject obj = JSONObject.fromObject(resultMap);
		return obj.toString();
	}

}