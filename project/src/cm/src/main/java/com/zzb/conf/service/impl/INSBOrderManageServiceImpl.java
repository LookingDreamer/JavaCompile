package com.zzb.conf.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cninsure.system.service.INSCDeptService;
import com.zzb.conf.service.INSBUsercommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCDept;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.conf.dao.INSBGroupmembersDao;
import com.zzb.conf.dao.INSBOrderManageDao;
import com.zzb.conf.dao.INSBOrderpaymentDao;
import com.zzb.conf.entity.INSBOrderpayment;
import com.zzb.conf.service.INSBOrderManageService;
import com.zzb.model.OrderQueryModel;

@Service
@Transactional
public class INSBOrderManageServiceImpl extends BaseServiceImpl<INSBOrder>
		implements INSBOrderManageService { 
	@Resource
	private INSBGroupmembersDao insbGroupmembersDao;
	@Resource
	private INSBOrderManageDao insbOrderManageDao;
	@Resource
	private INSBOrderpaymentDao orderpaymentDao;
	@Resource
	private INSCDeptDao inscDeptDao;
	@Resource
	private INSBUsercommentService insbUsercommentService;
	@Resource
	private INSCDeptService inscDeptService;


	@Override
	protected BaseDao<INSBOrder> getBaseDao() {
		return insbOrderManageDao;
	}

	// 总页数
	private int getTotalPage(long total,int pageSize) {
		// 总条数/每页显示的条数=总页数
		int size = (int) (total / pageSize);
		// 最后一页的条数
		int mod = (int) (total % pageSize);
		if (mod != 0)
			size++;
		return total == 0 ? 1 : size;
	}

	@Override
	public Map<String, Object> queryOrderList(OrderQueryModel queryModel,String deptid) {
		int pageSize = 10;
		int currentPage = queryModel.getCurrentPage() == 0?1:queryModel.getCurrentPage();

		String platforminnercode = null;
		INSCDept dept = inscDeptDao.selectById(deptid);
		if(dept!=null&&dept.getParentcodes()!=null){
			queryModel.setParentcodes(dept.getDeptinnercode());
			queryModel.setComcode(dept.getComcode());
			platforminnercode = inscDeptService.getPlatformInnercode(dept.getDeptinnercode());
		}
		Map<String, Object> map = BeanUtils.toMap(queryModel);
		if (StringUtils.isNotEmpty(queryModel.getStartdate())) {
			map.put("startdate", queryModel.getStartdate() + " 00:00:00");
		}
		if (StringUtils.isNotEmpty(queryModel.getEnddate())) {
			map.put("enddate", queryModel.getEnddate() + " 23:59:59");
		}
		if (platforminnercode != null) {
			map.put("platforminnercode", platforminnercode);
		}
		//LogUtil.info(map);
		long total = insbOrderManageDao.queryOrderPagingListCount(map);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<Object, Object>> resultList = null, tempcarTaskList = null, syList = null;
		
		//开始位置
		int limit = (currentPage - 1) * pageSize;

		//有数据并且在数据范围之内才查（limit数据范围第一个数据为0），否则就不需要再查了
		if (total > 0 && limit < total) {
			map.put("pageSize", pageSize);
			map.put("limit", limit);

			syList = insbOrderManageDao.queryOrderPagingList(map);
			resultList = syList;
		}

		//list里面有数据才进入数据权限过滤
		if (null != syList && syList.size() > 0) {
			StringBuilder privilegeStr = new StringBuilder();
			List<Map<String, String>> privileges = insbGroupmembersDao.selectGroupIdinfosByUserCodes4Tasklist(queryModel.getUserCode());

			for (Map<String, String> privilege : privileges) {//解析所有业管组及权限信息
				if (null != privilege.get("tasktype") && privilege.get("tasktype").contains(",")) {
					String[] tasks = privilege.get("tasktype").split(",");
					for (String task : tasks) {
						if (StringUtil.isNotEmpty(task)) {
							privilegeStr.append(task).append("@").append(privilege.get("deptid")).append("@").append(privilege.get("provideid"));
						}
					}
				} else {
					privilegeStr.append(privilege.get("tasktype")).append("@").append(privilege.get("deptid")).append("@").append(privilege.get("provideid"));
				}
			}

			String privilegeString = privilegeStr.toString();
			//有业管权限人员根据其具体权限控制可查看任务
			if (privilegeString.length() > 0) {
				map.put("pageSize", total);
				map.put("limit", 0);
				syList = insbOrderManageDao.queryOrderPagingList(map);
				LogUtil.debug(queryModel.getUserCode() + "权限privilege=" + privilegeString);

				tempcarTaskList = new ArrayList<>();
				String cartaskinfo = null;

				try {
					for (Map<Object, Object> cartask : syList) {
						cartaskinfo = "@" + String.valueOf(cartask.get("deptcode")) + "@" + (String.valueOf(cartask.get("inscomcode")).substring(0, 4));
						//不在权限组内，删除不现实
						if (!privilegeString.contains(cartaskinfo)) {
							LogUtil.info(cartask.get("taskid") + "," + cartask.get("inscomcode") + "=" + queryModel.getUserCode() + "权限无privilege=" + cartaskinfo);
							total--;
						} else {
							tempcarTaskList.add(cartask);
						}
					}
				} catch (Exception e) {
					LogUtil.info(queryModel.getUserCode() + "权限判断异常");
					e.printStackTrace();
				}
			}
		}
		
		//所有页数
		int totalPage = getTotalPage(total,pageSize);
		resultMap.put("totalPage", totalPage);
		resultMap.put("currentPage", currentPage);
		resultMap.put("total", total);

		if(null!=tempcarTaskList){
			resultList = tempcarTaskList.subList((int)limit, (int)(limit+pageSize>total?total:limit+pageSize));
		}
		
		// 取最新的支付信息数据
		if (resultList != null && resultList.size() > 0) {
			INSBOrderpayment temp = null;
			for (Map<Object, Object> taskTemp : resultList) {
				String taskId = String.valueOf(taskTemp.get("taskid"));
				temp = new INSBOrderpayment();
				temp.setTaskid(taskId);
				// 取数据
				INSBOrderpayment orderpayment = orderpaymentDao.selectNewestPayInfo(temp);
				if (orderpayment != null) {
					taskTemp.put("orderPayTime", DateUtil.toDateTimeString(orderpayment.getModifytime()));
					taskTemp.put("payresult", orderpayment.getPayresult());
					taskTemp.put("payflowno", orderpayment.getPayflowno());
					taskTemp.put("payid", orderpayment.getId());
				}
				String inscomcode = String.valueOf(taskTemp.get("inscomcode"));
				List<Map<String, Object>> usercomment = insbUsercommentService.getNearestUserComment3(
						taskId, inscomcode, null,0);
				if (usercomment != null && !usercomment.isEmpty()) {
					Map<String, Object> commentMap = usercomment.get(0);//最新的用户备注
					if (commentMap != null) {
						taskTemp.put("noti", commentMap.get("commentcontent"));
					}
				}
			}
		}

		resultMap.put("rowList", resultList);
		return resultMap;
	}

}
