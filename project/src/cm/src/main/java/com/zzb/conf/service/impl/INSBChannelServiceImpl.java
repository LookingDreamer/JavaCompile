package com.zzb.conf.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.exception.ServiceException;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCDept;
import com.common.HttpClientUtil;
import com.zzb.chn.util.JsonUtils;
import com.zzb.conf.controller.vo.BaseInfoVo;
import com.zzb.conf.controller.vo.ChannelRespVo;
import com.zzb.conf.controller.vo.TreeVo;
import com.zzb.conf.dao.INSBAgentDao;
import com.zzb.conf.dao.INSBAgreementareaDao;
import com.zzb.conf.dao.INSBAgreementproviderDao;
import com.zzb.conf.dao.INSBChannelDao;
import com.zzb.conf.dao.INSBChannelagreementDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.INSBAgreementarea;
import com.zzb.conf.entity.INSBAgreementdept;
import com.zzb.conf.entity.INSBAgreementpaymethod;
import com.zzb.conf.entity.INSBAgreementprovider;
import com.zzb.conf.entity.INSBChannel;
import com.zzb.conf.entity.INSBChannelagreement;
import com.zzb.conf.service.INSBAgreementdeptService;
import com.zzb.conf.service.INSBAgreementpaymethodService;
import com.zzb.conf.service.INSBChannelService;
import com.zzb.conf.service.INSBChannelagreementService;
import com.zzb.extra.model.SelectProviderBeanForMinizzb;

@Service
public class INSBChannelServiceImpl extends BaseServiceImpl<INSBChannel> implements
		INSBChannelService {

	@Resource
	private INSBChannelDao insbChannelDao;
	@Resource
	private INSCDeptDao inscDeptDao;
	@Resource
	private INSBChannelagreementDao insbChannelagreementDao;
	@Resource
	private INSBAgreementareaDao insbAgreementareaDao;
	@Resource
	private INSBChannelagreementService agreementService; //渠道协议服务
	@Resource
	private INSBAgreementproviderDao insbAgreementproviderDao;
	@Resource
	private INSBAgentDao insbAgentDao;
	@Resource
	private INSBAgreementdeptService agreementdeptService;
	@Resource
	private INSBAgreementpaymethodService agreementpaymethodService;

	@Override
	protected BaseDao<INSBChannel> getBaseDao() {
		return insbChannelDao;
	}

	@Override
	public List<INSBChannel> queryListByPid(String parentCode) {
		if(StringUtil.isEmpty(parentCode) || "source".equalsIgnoreCase(parentCode)){
			parentCode = "";
		}
		return insbChannelDao.selectByParentChannelCode(parentCode);
	}

	@Override
	public ChannelRespVo queryDetailById(String id) {
		//查询渠道信息
		ChannelRespVo channelInfo = null ;
		INSBChannel insbChannel = queryById(id) ;
		if(insbChannel == null) {
			return null ;
		}
		channelInfo = new ChannelRespVo(insbChannel) ;

		if(channelInfo != null && channelInfo.getDeptid() != null && !"".equals(channelInfo.getDeptid())) {
			//查询机构信息
			INSCDept dept = inscDeptDao.selectById(channelInfo.getDeptid());
			channelInfo.setDept(dept);

			//查询协议信息
			HashMap<String, Object> tempMap = new HashMap<>();
			tempMap.put("channelid", channelInfo.getId());
			tempMap.put("deptid", channelInfo.getDeptid());
			INSBChannelagreement channelagreement = insbChannelagreementDao.select4ChannelAgreement(tempMap);
			channelInfo.setAgreement(channelagreement);

			//由于历史原因省份压根没用，所以这里需要从协议地区里面获取默认省份
			if(channelagreement != null) {
				INSBAgreementarea areaConfition = new INSBAgreementarea();
				areaConfition.setAgreementid(channelagreement.getId());
				List<INSBAgreementarea> areaList = insbAgreementareaDao.selectList(areaConfition);
				if(areaList != null && !areaList.isEmpty()) {
					channelInfo.setProvince(areaList.get(0).getProvince()) ;
				}
			}
		}

		return channelInfo ;
	}

	@Override
	public List<TreeVo> queryTreeListByPid(String parentCode, boolean... hideChildren) {
		List<TreeVo> treelist = new ArrayList<>();
		boolean hide = false;
		if (hideChildren.length > 0) {
			hide = hideChildren[0];
		}
		List<INSBChannel> insbListChannel = queryListByPid(parentCode);
		if(insbListChannel != null && !insbListChannel.isEmpty()) {
			TreeVo tree = null ;
			for(INSBChannel channel : insbListChannel){
				tree = new TreeVo() ;
				tree.setId(channel.getId());
				tree.setpId(channel.getUpchannelcode());
				tree.setName(channel.getChannelname());
				tree.setIsParent(hide ? false : "1".equals(channel.getChildflag()));
				tree.setInnercode(channel.getChannelinnercode());
				treelist.add(tree);
			}
		}

		return treelist;
	}
	/**
	 * 模糊查询渠道列表树
	 * 
	 */
	@Override
	public List<Map<String, String>> queryTreeListByPidDim() {
		
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		
		List<Map<String, String>> insbChannels = insbChannelDao.selectChannelParent();
		for(Map<String, String> map : insbChannels) {
			String pId = map.get("id");
			result.add(map);
			List<Map<String, String>> childList = insbChannelDao.selectChildchannelByPid(pId);
			if(!childList.isEmpty() && childList != null) {
				for(Map<String, String> childMap : childList) {
					
					result.add(childMap);
//					String childPid = childMap.get("id");
//					List<Map<String, String>> grandsonList = insbChannelDao.selectChildchannelByPid(childPid);
//					if(!grandsonList.isEmpty() && grandsonList != null) {
//						for(Map<String, String> gransonMap : grandsonList) {
//							result.add(gransonMap);
//						}
//					}
				}
			}
		}
		return result;
	}

	@Transactional
	@Override
	public int deleteById(String id) throws ServiceException {
		if(id == null || "".equals(id)) {
			return 0 ;
		}

		//删除渠道
		INSBChannel channel = queryById(id) ;

		String upcode = channel.getUpchannelcode(); //查询父节点
		//渠道为父节点则不允许删除
		if(StringUtil.isEmpty(upcode) && queryListByPid(id).size() >= 1) {
			throw new RuntimeException("删除失败，渠道下面有子渠道") ;
		}

		//int count = insbChannelDao.updateChannelDeleteflagById(id); //更新删除标志
		int count = insbChannelDao.deleteById(id);

		//如果父节点下面没有子节点，则将父节点变为子节点
		if (!StringUtil.isEmpty(upcode) && queryListByPid(upcode).size()<1){
			insbChannelDao.updateChannelByiddel(upcode);
		}

		List<INSBChannelagreement> insbChannelagreements = insbChannelagreementDao.getChannelagreement(id);
		if ( !insbChannelagreements.isEmpty() ) {
			String agreementid = insbChannelagreements.get(0).getId();
			
			if ( StringUtil.isNotEmpty(agreementid) ) {
				INSBAgreementprovider prvDelCon = new INSBAgreementprovider();
				prvDelCon.setAgreementid(agreementid);
				insbAgreementproviderDao.delete(prvDelCon);
	
				INSBAgreementdept deptDelCon = new INSBAgreementdept();
				deptDelCon.setAgreementid(agreementid);
				agreementdeptService.delete(deptDelCon);
	
				INSBAgreementpaymethod payDelCon = new INSBAgreementpaymethod();
				payDelCon.setAgreementid(agreementid); 
				agreementpaymethodService.delete(payDelCon);
				
				INSBAgreementarea areaDelCon = new INSBAgreementarea();
				areaDelCon.setAgreementid(agreementid); 
				insbAgreementareaDao.delete(areaDelCon);
				
				insbChannelagreementDao.deleteById(agreementid);
			}
		}

		if(StringUtil.isEmpty(channel.getUpchannelcode())
				&& !StringUtil.isEmpty(channel.getAddress())) {
			//保存channel-service新接口
			Map<String, String> confMap = new HashMap<>() ;
			confMap.put("operator", "delete") ;
			confMap.put("channelId", channel.getChannelinnercode()) ;
			try {
				HttpClientUtil.doPostJson(channel.getAddress().replaceAll("channel/callBack", "config/cudConfig"), confMap, "UTF-8");
			} catch (IOException e) {
				throw new RuntimeException("删除渠道失败同步到channel-service失败") ;
			}
		}

		return count ;
	}

	@Transactional
	@Override
	public int addChannel(INSBChannel channel, BaseInfoVo baseInfoVo) throws ServiceException {

		//组装渠道参数
		if(StringUtil.isEmpty(channel.getId())) {
			String uuid = UUIDUtils.random();
			channel.setId(uuid);
			channel.setChannelcode(uuid);
		}
		Date now = new Date();
		channel.setCreatetime(now);
		channel.setModifytime(now);
		channel.setChildflag("0");
		if(StringUtil.isNotEmpty(channel.getUpchannelcode())) {
			INSBChannel upChannel = insbChannelDao.selectById(channel.getUpchannelcode());
			if(StringUtil.isNotEmpty(upChannel.getUpchannelcode())){
				throw new RuntimeException("不能建三级渠道");
			}
		}
		INSBChannel insbChannel = new INSBChannel();
		insbChannel.setChannelinnercode(channel.getChannelinnercode()); 
		insbChannel.setChildflag("1"); 
		List<INSBChannel> insbChannels = insbChannelDao.selectList(insbChannel);
		for (INSBChannel chnIt : insbChannels) {
			if ( !"0".equals(chnIt.getDeleteflag()) ) {
				throw new RuntimeException("该渠道ID与渠道【" + chnIt.getChannelname() + "】重复，请重新输入！");
			}
		}

		//新增渠道
		int count = insbChannelDao.addChannelDatas(channel);
		if(count > 0) {
			if(!StringUtil.isEmpty(channel.getUpchannelcode())) {
				baseInfoVo.setChannelid(channel.getChannelcode()); //设置渠道ID
				agreementService.saveBaseInfo(baseInfoVo); //新增渠道协议
			}

			if(StringUtil.isEmpty(channel.getUpchannelcode())
					&& !StringUtil.isEmpty(channel.getAddress())) {
				//保存channel-service新接口
				Map<String, String> confMap = new HashMap<>() ;
				confMap.put("operator", "add") ;
				confMap.put("channelId", channel.getChannelinnercode()) ;
				confMap.put("channelSecret", channel.getChannelsecret()) ;
				confMap.put("callbackUrl", channel.getWebaddress()) ;
				try {
					HttpClientUtil.doPostJson(channel.getAddress().replaceAll("channel/callBack", "config/cudConfig"), confMap, "UTF-8");
				} catch (IOException e) {
					throw new RuntimeException("新增渠道同步到channel-service失败 ") ;
				}
			}

			//更新父节点为父节点
			if(channel.getUpchannelcode()!= null
					&& !"".equals(channel.getUpchannelcode())) {
				insbChannelDao.updateChannelByid(channel.getUpchannelcode());
			}
		}

		return count ;
	}

	@Transactional
	@Override
	public int updateChannel(INSBChannel channel, BaseInfoVo baseInfoVo) throws ServiceException {
		channel.setModifytime(new Date());
		Map<String, Object> map =new  HashMap<String, Object>();
		
		INSBChannel channelData = queryById(channel.getId());
		if ( !channelData.getChannelinnercode().equals(channel.getChannelinnercode()) ) {
			INSBChannel insbChannel = new INSBChannel();
			insbChannel.setChannelinnercode(channel.getChannelinnercode()); 
			insbChannel.setChildflag("1"); 
			List<INSBChannel> insbChannels = insbChannelDao.selectList(insbChannel);
			for (INSBChannel chnIt : insbChannels) {
				if ( !"0".equals(chnIt.getDeleteflag()) ) {
					throw new RuntimeException("该渠道ID与渠道【" + chnIt.getChannelname() + "】重复，请重新输入！");
				}
			}
		}
		
		//查询父类渠道是否已删除
		if(channel.getUpchannelcode() == null || channel.getUpchannelcode().equals("") ) {
			map.put("channelinnercode", channel.getChannelinnercode());
			
			List<INSBChannel> channels = insbChannelDao.queryChildChannel(map);
//			INSBChannel tmpchn = new  
			if(channels.size() > 0 && channel != null){
				
				for(INSBChannel tmpchn : channels) {
					
					tmpchn.setChanneltype(channel.getChanneltype());
					updateById(tmpchn);
				}
			}
			
		}
		//更新渠道
		int updateNum = updateById(channel);

		//更新协议
		if(updateNum > 0 && !StringUtil.isEmpty(channel.getUpchannelcode())
				&& !StringUtil.isEmpty(baseInfoVo.getProvince())) {
			//更新渠道协议
			baseInfoVo.setChannelid(channel.getChannelcode());
			agreementService.saveBaseInfo(baseInfoVo);
		}

		//保存channel-service新接口
		if(StringUtil.isEmpty(channel.getUpchannelcode())
				&& !StringUtil.isEmpty(channel.getAddress())) {
			Map<String, String> confMap = new HashMap<>() ;
			confMap.put("operator", "update") ;
			confMap.put("channelId", channel.getChannelinnercode()) ;
			confMap.put("channelSecret", channel.getChannelsecret()) ;
			confMap.put("callbackUrl", channel.getWebaddress()) ;
			try {
				HttpClientUtil.doPostJson(channel.getAddress().replaceAll("channel/callBack", "config/cudConfig"), confMap, "UTF-8");
			} catch (IOException e) {
				throw new RuntimeException("更新渠道同步到channel-service失败"+e.getMessage()) ;
			}
		}

		return updateNum;
	}

	@Override
	public Map<String, Object> getChannelAgreementInfo(String id) {
		Map<String,Object> map = new HashMap<String,Object>();
		//查询渠道信息
		INSBChannel channel = this.queryById(id);
		//查询上级渠道信息
		INSBChannel temp = new INSBChannel();
		temp.setChannelcode(channel.getUpchannelcode());
		INSBChannel upchannel = queryOne(temp);
		//查询所属机构信息
		INSCDept dept = inscDeptDao.selectById(channel.getDeptid());
		//查询协议信息
		HashMap<String, Object> tempMap = new HashMap<String,Object>();
		tempMap.put("channelid",channel.getId());
		tempMap.put("deptid", channel.getDeptid());
		INSBChannelagreement channelagreement = insbChannelagreementDao.select4ChannelAgreement(tempMap);
		//查询省份信息
		if(channelagreement!=null){
			INSBAgreementarea temparea = new INSBAgreementarea();
			temparea.setAgreementid(channelagreement.getId());
			List<INSBAgreementarea> list = insbAgreementareaDao.selectList(temparea);
			if(list!=null && list.size()>0)
				temparea = list.get(0);
			map.put("province", temparea.getProvince());
		}
		//组织数据传回前台
		map.put("channel", channel);
		map.put("upchannel", upchannel==null?" ":upchannel);
		map.put("dept", dept);
		map.put("channelagreement", channelagreement);
		return map;
	}

	@Override
	public List<SelectProviderBeanForMinizzb> queryChannelProviderList(String city, String channelinnercode) {
		Map<String,String> data = new HashMap<String,String>();
		data.put("city",city);
		data.put("channelinnercode",channelinnercode);
		return insbChannelDao.queryChannelProviderList(data);
	}

	@Override
	public String queryChannelAgentCode(String city, String channelinnercode){
		Map<String,String> data = new HashMap<String,String>();
		data.put("city",city);
		data.put("channelinnercode",channelinnercode);
		return insbChannelDao.queryChannelAgentCode(data);
	}
	@Override
	public String updateAgentForChannel(INSBChannel channel, BaseInfoVo baseInfoVo){
		Map<String, Object> resultMap = new HashMap<>();
		if(StringUtil.isEmpty(channel.getJobnum())){
			resultMap.put("code", "00");
			resultMap.put("msg","更新成功！");
			return JsonUtils.serialize(resultMap);
		}
		try {
			String channelId = "";
			boolean flag01 = false;
			boolean flag02 = false;
			boolean flag03 = false;
			String jobnum = channel.getJobnum();
			String channelInnerCode = channel.getChannelinnercode()==null?baseInfoVo.getChannelid():channel.getChannelinnercode();

			INSBChannel insbChannelParam = new INSBChannel();
			insbChannelParam.setJobnum(jobnum);
			List<INSBChannel> insbChannelList = insbChannelDao.selectList(insbChannelParam);
			INSBAgent insbAgent = new INSBAgent();

			if (insbChannelList != null && insbChannelList.size() == 1) {
				insbAgent.setJobnum(jobnum);
				insbAgent = insbAgentDao.selectOne(insbAgent);
				insbAgent.setChannelid(channel.getId());
				insbAgent.setAgentkind(3);
				insbAgentDao.updateById(insbAgent);
			} else {
				if (insbChannelList != null && insbChannelList.size() > 1) {
					for (INSBChannel channelRow : insbChannelList) {
						if(!StringUtil.isEmpty(channelRow.getUpchannelcode())){
							INSBChannel parentChannel = new INSBChannel();
							parentChannel = insbChannelDao.selectById(channelRow.getUpchannelcode());
							if(parentChannel !=null) {
								if ("02".equals(parentChannel.getChanneltype()) && channelInnerCode.equals(channelRow.getChannelinnercode())) {
									channelId = channelRow.getId();
									flag02 = true;
								}
								if ("01".equals(parentChannel.getChanneltype())) {
									flag01 = true;
								}
								if ("03".equals(parentChannel.getChanneltype())) {
									flag03 = true;
								}
							}
						}
					}
					if (flag02) {
						insbAgent.setJobnum(jobnum);
						insbAgent = insbAgentDao.selectOne(insbAgent);
						insbAgent.setChannelid(channelId);
						insbAgent.setAgentkind(3);
						insbAgentDao.updateById(insbAgent);
					} else if (flag01 && flag03) {
						insbAgent.setJobnum(jobnum);
						insbAgent = insbAgentDao.selectOne(insbAgent);
						insbAgent.setAgentkind(3);
						insbAgentDao.updateById(insbAgent);
					}

				}

			}
			resultMap.put("code", "00");
			resultMap.put("msg","更新成功！");
		}catch (Exception e){
			LogUtil.info(JsonUtils.serialize(channel)+" errMsg="+e.getMessage());
			resultMap.put("code", "01");
			resultMap.put("msg","updateAgentForChannel更新异常！"+e.getMessage());
		}
		return JsonUtils.serialize(resultMap);
	}

	

	/*@Override
	public int updateChannelById(String id) {
		return insbChannelDao.updateChannelByid(id);
	}*/

	/*@Override
	public int updateChannelByIddel(String id) {
		return insbChannelDao.updateChannelByiddel(id);
	}*/

	/*@Override
	public int queryChannelByUpchannelcode(String id) {
		List<INSBChannel> list = queryListByPid(id);
		if( list != null && list.size() > 0 ) {
			return 1;
		}
		return 0;
	}*/

	/*@Override
	public int updateChannelDeleteflagById(String id) {
		return insbChannelDao.updateChannelDeleteflagById(id);
	}*/
}