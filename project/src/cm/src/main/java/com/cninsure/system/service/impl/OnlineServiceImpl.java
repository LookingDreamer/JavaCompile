package com.cninsure.system.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.jobpool.dispatch.DispatchTaskService;
import com.cninsure.system.dao.INSCUserDao;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.OnlineService;
@Service(value="onlineService")
@Transactional
public class OnlineServiceImpl implements OnlineService{
	@Resource
	private INSCUserDao inscUserDao ;
	@Resource
	private DispatchTaskService dispatchTaskService;
	@Resource
	private ThreadPoolTaskExecutor taskthreadPool4workflow;
	/**
	 * 上下线标记
	 * @param usercode
	 * @return 
	 */
	@Override
	public boolean changeOnlinestatus(String usercode, int onlinestatus) {
		try {
			INSCUser user = inscUserDao.selectByUserCode(usercode);
			if(null!=user){ 
				user.setOnlinestatus(onlinestatus);
				user.setModifytime(new Date());
				inscUserDao.updateById(user);
				if(onlinestatus==1){
					dispatchTaskService.userLoginForTask(user.getUsercode());
				}else if(onlinestatus==0){
					dispatchTaskService.userLogoutForTask(user.getUsercode());
				}else if(onlinestatus==2){
					dispatchTaskService.userBusyForTask(user.getUsercode());
				}
			}
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}
	@Override
	public void changeAllOnlinestatus(String[] userCodes, int onlinestatus) {
		// TODO Auto-generated method stub
		INSCUser user = null;
		List<INSCUser> users = new ArrayList<INSCUser>();
		for(int i=0;i<userCodes.length;i++){
			user = new INSCUser();
			user.setUsercode(userCodes[i]);
			user.setOnlinestatus(onlinestatus);
			user.setModifytime(new Date());
			users.add(user);
		}
		inscUserDao.updateInBatch(users);
		dispatchTaskService.usersLoginForTask(userCodes);
	}
}
