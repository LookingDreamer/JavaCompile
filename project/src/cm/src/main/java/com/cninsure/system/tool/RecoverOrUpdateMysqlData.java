package com.cninsure.system.tool;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.StringUtil;
import com.common.HttpClientUtil;

public class RecoverOrUpdateMysqlData {

	public static void main(String[] args) throws Exception {
		//小数据量用
		//execute("2016-12-01 00:00:00", "2017-01-01 00:00:00", 1, 31);
		
		
		/* 大数据量课根据时间段执行，可多线程同时执行
		 * 另外运行在jvm的堆/栈 内存调大点,run->run configurations->Arguments -Xms1024m -Xmx1024m
		 */
		//runables();
		
		
		//同步工作流和CM的节点(taskcode)状态
		String url="http://10.68.4.1/cm/workflow/startTask"; //开发环境
		//String url="http://cittest.cisg.cn/cm/workflow/startTask";//测试环境
		//String url="http://cm.uat.52zzb.com/cm/workflow/startTask";//准生产生产环境
		//String url="https://m.52zzb.com/cm/workflow/startTask";//生产环境
		List<String> mainTaskIds = new ArrayList<String>();
		mainTaskIds.add("1484905");
		syncWorkflowAndCmTaskCode(mainTaskIds, url);
	}
	
	/**
	 * 大数据量，多线程执行,可定时执行
	 * 如果数据量太大 间隔天数小点
	 * 另外配置 内存调大点,run->run configurations->RecoverOrUpdateMysqlData 找到Arguments下VM Argument：-Xms1024m -Xmx1024m
	 * 
	 */
	public static void runables(){
		Runnable task1 = new Runnable() {
			@Override
			public void run() {
				updateNoEqlCompleteMfytime("2016-12-01 00:00:00", "2016-12-02 00:00:00", "Run[1]");
			}
		};
		ScheduledExecutorService scheExecutor1 = Executors.newSingleThreadScheduledExecutor();
		scheExecutor1.schedule(task1, 0, TimeUnit.HOURS); //如果延迟执行，把0改成1,2,3....
		scheExecutor1.shutdown();
		
		//下面可以定义多个线程task2,task3....
	}
	
	/** 
	 * 执行
	 * @param startDatetimeStr 开始日期
	 * @param endDatetimeStr 结束日期
	 * @param startDay 从第startDay天开始
	 * @param endDay 到endDay天结束
	 */
	public static void execute(String startDatetimeStr, String endDatetimeStr, int startDay, int endDay) {
		try {
			Runnable task = new Runnable() {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				java.util.Date startdate = sdf.parse(startDatetimeStr);
				java.util.Date enddate = sdf.parse(endDatetimeStr);
				java.util.Date addOneDayDate = DateUtil.addDay(sdf.parse(startDatetimeStr), 1);

				@Override
				public void run() {
					for (int i = startDay; i <= endDay; i++) {
						if (addOneDayDate.getTime() > enddate.getTime())
							break;
						updateData(sdf.format(startdate), sdf.format(addOneDayDate), "Run[" + i + "]");
						try {
							Thread.sleep((long) 60 * 1000);
							startdate = DateUtil.addDay(startdate, 1);
							addOneDayDate = DateUtil.addDay(startdate, 1);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			};

			ScheduledExecutorService scheExecutor = Executors.newSingleThreadScheduledExecutor();
			scheExecutor.schedule(task, 0, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 修改数据
	 * @param sttime
	 * @param edtime
	 * @param flag
	 */
	public static void updateData(String sttime, String edtime, String flag){
		String datesql = " and createtime between '" + sttime + "' and '" + edtime + "' order by createtime desc";
		String completedSql = "SELECT * from dw_zzb_insbworkflowsubtrackdetail_copy20170109 where taskcode='20' and taskstate='Completed' and operator!='admin' " + datesql;
		String readySql = "select * from dw_zzb_insbworkflowsubtrackdetail_copy20170109 where taskstate in ('Reserved','Ready') ";
		String orderbySql = " ORDER BY createtime desc limit 1";
		String updateSql = "update dw_zzb_insbworkflowsubtrackdetail_copy20170109 set modifytime='";
		Connection conn = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int count = 1;
		java.util.Date taskstarttime = new java.util.Date();
		System.out.println(flag + "=====开始执行===" + sdf.format(taskstarttime));
		
		try {
			conn = getConnection();
			if(conn == null){
				System.out.println("链接数据库失败!");
			}
			PreparedStatement pst = conn.prepareStatement(completedSql);
			ResultSet rs = pst.executeQuery();
			Timestamp datetime = null;
			Date createtime = null;
			String operator = null;
			String mainId = null;
			ResultSet readyRs = null;
			String taskcode = null;
			String instanceid = null;
			int result = 0;
			
			Timestamp modifytimestamp = null;
			String id = null;
			if(rs != null){
				while(rs.next()){
					datetime = rs.getTimestamp("createtime");
					operator = rs.getString("operator");
					mainId = rs.getString("maininstanceid");
					taskcode = rs.getString("taskcode");
					instanceid = rs.getString("instanceid");
					if(datetime ==null || operator == null || mainId == null || instanceid == null){
						continue;
					}
					
					createtime = new Date(datetime.getTime());
					pst = conn.prepareStatement((readySql + "and instanceid='" + instanceid + "' and taskcode='" + taskcode + "' and createtime <= '" + sdf.format(createtime) + "' and operator='" + operator + "' and maininstanceid='" + mainId + "' " + orderbySql));
					readyRs = pst.executeQuery();
					
					if (readyRs.next()) {
						modifytimestamp = readyRs.getTimestamp("modifytime");
						// 如果相同就不修改
						if (modifytimestamp == null || createtime.getTime() != modifytimestamp.getTime()){
							id = readyRs.getString("id");
							pst = conn.prepareStatement((updateSql + sdf.format(createtime) + "' where id='" + id + "' and instanceid='" + instanceid+"' and maininstanceid='" + mainId + "' and taskcode='" + taskcode + "' and operator='" + operator + "'"));
							result = pst.executeUpdate();
							System.out.println("---" + flag + "--" + count + "----result=" + result + "----id=" + id + ", mainId=" + mainId + ",time:" + sdf.format(createtime));
							count++;
						}
					}
				}
			}
			if(rs != null)
				rs.close();
			if(readyRs != null)
				readyRs.close();
			if(pst != null)
				pst.close();
			if(conn != null)
				conn.close();
			System.out.println(flag + "=====结束执行===开始时间:" + sdf.format(taskstarttime) +"----结束时间："+ sdf.format(new java.util.Date()));
		} catch (Exception e) {
			e.printStackTrace();
			
			if(conn != null)
				try {
					conn.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
		}
	}

	/**
	 * 没有匹配Complete数据的modifytime修改为NULL
	 * @param sttime
	 * @param edtime
	 * @param flag
	 */
	public static void updateNoEqlCompleteMfytime(String sttime, String edtime, String flag){
		String datesql = " and modifytime between '" + sttime + "' and '" + edtime + "' order by modifytime desc";
		String completedSql = "select * from dw_zzb_insbworkflowsubtrackdetail_copy20170109 where taskcode='20' and modifytime IS NOT NULL " + datesql;
		String readySql = "select * from dw_zzb_insbworkflowsubtrackdetail_copy20170109 where taskstate='Completed' ";
		String updateSql = "update dw_zzb_insbworkflowsubtrackdetail_copy20170109 set modifytime=NULL where id='";
		Connection conn = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int upcount = 0;
		int nocount = 0;
		java.util.Date taskstarttime = new java.util.Date();
		System.out.println(flag + "=====开始执行===" + sdf.format(taskstarttime));
		
		try {
			conn = getConnection();
			if(conn == null){
				System.out.println("链接数据库失败!");
			}
			PreparedStatement pst = conn.prepareStatement(completedSql);
			ResultSet rs = pst.executeQuery();
			Timestamp mdftime = null;
			Date modifytime = null;
			String operator = null;
			String mainId = null;
			String taskcode = null;
			ResultSet readyRs = null;
			int result = 0;
			String instanceid = null;
			String id = null;
			if(rs != null){
				while(rs.next()){
					mdftime = rs.getTimestamp("modifytime");
					operator = rs.getString("operator");
					mainId = rs.getString("maininstanceid");
					taskcode = rs.getString("taskcode");
					instanceid = rs.getString("instanceid");
					id = rs.getString("id");
					if(mdftime ==null || operator == null || taskcode == null || mainId == null || instanceid == null || id == null){
						continue;
					}
					
					modifytime = new Date(mdftime.getTime());
					pst = conn.prepareStatement((readySql + "and instanceid='" + instanceid + "' and maininstanceid='" + mainId + "' and taskcode='" + taskcode + "' and createtime = '" + sdf.format(modifytime) + "' and operator='" + operator + "' limit 1"));
					readyRs = pst.executeQuery();
					
					if(!readyRs.next()){
						upcount++;
						
						pst = conn.prepareStatement((updateSql + id + "' and instanceid='" + instanceid + "' and maininstanceid='" + mainId + "' and taskcode='" + taskcode +"' and operator='" + operator + "'"));
						result = pst.executeUpdate();
						
						//根据日志Time看进度
						System.out.println("-" + flag +"--" + upcount + "---result=" + result + "--id=" + id + ", maininstanceid=" + mainId + ", Time=" + modifytime);
					} else {
						nocount++;
					}
				}
				
				System.out.println("-" + flag + "----------Yes--" + nocount);
			}
			if(rs != null)
				rs.close();
			if(readyRs != null)
				readyRs.close();
			if(pst != null)
				pst.close();
			if(conn != null)
				conn.close();
			System.out.println(flag + "=====结束执行===开始时间:" + sdf.format(taskstarttime) +"----结束时间："+ sdf.format(new java.util.Date()));
		} catch (Exception e) {
			e.printStackTrace();
			
			if(conn != null)
				try {
					conn.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
		}
	}
	
	/**
	 * 同步工作流和CM的节点(taskcode)状态
	 * @param mainTaskIds
	 * @param cmurl 如开发环境：http://10.68.4.1/cm/workflow/startTask
	 */
	public static void syncWorkflowAndCmTaskCode(List<String> mainTaskIds, String cmurl){
		String selectSql = "select * from insbworkflowsub where maininstanceid='";
		String orderBySql = " order by createtime desc limit 1";
		String selectTaskSql = "select * from task where processInstanceId=";
		String taskOrderBySql = " order by createdOn desc limit 1";
		Connection conn = null;
		try {
			conn = getConnection();
			if(conn == null){
				System.out.println("链接数据库失败!");
			}
			
			PreparedStatement pst = null;
			ResultSet rs = null;
			ResultSet readyRs = null;
			
			//table insbworkflowsub
			String instanceid = null;
			String mainId = null;
			String subtaskcode = null;
			String taskState = null;
			
			//table task
			String taskName = null;
			String status = null;
			String taskcode = null;
			
			for(String mainTaskId : mainTaskIds){
				pst = conn.prepareStatement(selectSql + mainTaskId + "'" + orderBySql);
				rs = pst.executeQuery();
				if(rs != null){
					while(rs.next()){
						instanceid = rs.getString("instanceid");
						mainId = rs.getString("maininstanceid");
						subtaskcode = rs.getString("taskcode");
						taskState = rs.getString("taskstate");
						if(mainId == null || instanceid == null || taskState == null){
							continue;
						}
						
						pst = conn.prepareStatement(selectTaskSql + instanceid + taskOrderBySql);
						readyRs = pst.executeQuery();
						
						if (readyRs.next()) {
							status = readyRs.getString("status");
							taskName = readyRs.getString("name");
							taskcode = getCodeByName(taskName);
							if(!StringUtil.isEmpty(status) && "Exited".equals(status) && !"Closed".equals(taskState)){
								if(addTaskToPool(cmurl, mainId, instanceid, subtaskcode, taskName, "Closed")){
									System.out.println("Sync Exited Data！ mainTaskId=" + mainTaskId + ", instanceid=" + instanceid +", taskState(" + taskState +") 修改为 Closed");
								}
							} 
							else if(!StringUtil.isEmpty(subtaskcode) && !StringUtil.isEmpty(taskcode) && !taskcode.equals(subtaskcode)){
								if(addTaskToPool(cmurl, mainId, instanceid, taskcode, taskName, status)){
									System.out.println("Sync taskcode Data！ mainTaskId=" + mainTaskId + ", instanceid=" + instanceid +", table_insbworkflowsub_taskcode=" + subtaskcode + "修改为 table_task_taskcode=" + taskcode);
								}
							}
						}
					}
				}
			}
			if(rs != null)
				rs.close();
			if(readyRs != null)
				readyRs.close();
			if(pst != null)
				pst.close();
			if(conn != null)
				conn.close();
		} catch (Exception e) {
			e.printStackTrace();
			
			if(conn != null)
				try {
					conn.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
		}
	}
	public static String getCodeByName(String name){
		Map<String, String> codeMap = new HashMap<String, String>();
		codeMap.put("前端提交报价", "1");
		codeMap.put("报价", "2");
		codeMap.put("EDI报价", "3");
		codeMap.put("精灵报价", "4");
		codeMap.put("人工调整", "6");
		codeMap.put("人工规则报价", "7");
		codeMap.put("人工报价", "8");
		codeMap.put("报价退回修改", "13");
		codeMap.put("前端提交核保", "14");
		codeMap.put("快速续保", "15");
		codeMap.put("EDI核保暂存", "16");
		codeMap.put("精灵核保暂存", "17");
		codeMap.put("人工核保", "18");
		codeMap.put("核保退回修改", "19");
		codeMap.put("支付", "20");
		codeMap.put("二次支付确认", "21");
		codeMap.put("打单", "23");
		codeMap.put("配送", "24");
		codeMap.put("EDI承保", "25");
		codeMap.put("精灵承保", "26");
		codeMap.put("人工承保", "27");
		codeMap.put("承保退回", "28");
		codeMap.put("完成", "29");
		codeMap.put("关闭", "30");
		codeMap.put("人工回写", "31");
		codeMap.put("规则报价", "32");
		codeMap.put("结束", "33");
		codeMap.put("拒绝承保", "34");
		codeMap.put("暂停支付", "36");
		codeMap.put("关闭", "37");
		codeMap.put("核保查询", "38");
		codeMap.put("反向关闭", "39");
		codeMap.put("EDI自动核保", "40");
		codeMap.put("精灵自动核保", "41");
		codeMap.put("承保政策限制", "51");
		codeMap.put("等待报价请求", "52");
		codeMap.put("平台查询", "53");
		return codeMap.get(name);
	}
	
	public static boolean addTaskToPool(String cmurl, String mainTaskId, String subTaskId, String taskCode, String taskName,String taskStatus){
		Map<String, String> param = new HashMap<String, String>();
		param.put("mainInstanceId", mainTaskId);
		param.put("subInstanceId", subTaskId);
		param.put("providerId", null);
		param.put("taskName", taskName);
		param.put("taskStatus",taskStatus);
		param.put("dataFlag", "2");
		param.put("taskCode", taskCode);
		System.out.println("入调度池:" + param.toString());
		
		String result = HttpClientUtil.doGet(cmurl, param);
		if(result != null){
			return true;
		}
		return false;
	}
	
	/**
	 * 数据库链接Connection
	 * @return
	 */
	public static Connection getConnection(){
		String db_drivername = "com.mysql.jdbc.Driver";
		String db_url = "jdbc:mysql://10.68.3.63:3306/zzb_pro?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false";
		String db_username = "public";
		String db_password = "public";
        Connection conn = null;
        try
        {
            Class.forName(db_drivername);
            conn = DriverManager.getConnection(db_url, db_username, db_password);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return conn;
    }
}
