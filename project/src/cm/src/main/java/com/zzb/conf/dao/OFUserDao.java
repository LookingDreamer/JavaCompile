package com.zzb.conf.dao;

import java.util.List;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.OFUser;

public interface OFUserDao  {

	public void insert(OFUser user);

	public List<OFUser> queryOFUser(OFUser user);
	
	public OFUser queryOneOFUser(OFUser user);
	
	public OFUser queryByUserName(String username);
	
	public void deleteByUserName(String username);
	
	public int updateByUserName(OFUser user);

}