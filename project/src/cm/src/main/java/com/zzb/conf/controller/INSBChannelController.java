package com.zzb.conf.controller;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCDeptService;
import com.zzb.conf.controller.vo.BaseInfoVo;
import com.zzb.conf.controller.vo.ChannelRespVo;
import com.zzb.conf.controller.vo.TreeVo;
import com.zzb.conf.entity.INSBChannel;
import com.zzb.conf.service.INSBChannelService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/channel")
public class INSBChannelController extends BaseController {
	
	@Resource
	private INSBChannelService channelService;
	@Resource
	private INSCDeptService deptservice;

	/**
	 * 渠道信息管理首页
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/list" , method =RequestMethod.GET)
	public ModelAndView index() throws ControllerException {
		ModelAndView model = new ModelAndView("zzbconf/insbchannellist");
		return model;
	}

	/**
	 * 查询渠道树
	 * @param parentCode  父类ID
	 * @return
	 * @throws ControllerException
	 */
	@ResponseBody
	@RequestMapping(value = "/queryTreeList", method= RequestMethod.POST)
	public List<TreeVo> queryTreeList(@RequestParam(value="parentCode", required=false) String parentCode)
			throws ControllerException {
		List<TreeVo> treeList = channelService.queryTreeListByPid(parentCode);
		return treeList;
	}
	/**
	 * 渠道树模糊查询
	 * @param parentCode
	 * @param channelname
	 * @return
	 * @throws ControllerException
	 */
	@ResponseBody
	@RequestMapping(value = "/queryTreeListDim", method= RequestMethod.POST)
	public List<Map<String, String>> queryTreeListDim()
			throws ControllerException {
		List<Map<String, String>> treeList = channelService.queryTreeListByPidDim();
		return treeList;
	}

	@ResponseBody
	@RequestMapping(value = "/queryTreeTopList", method= RequestMethod.POST)
	public List<TreeVo> queryTreeTopList(@RequestParam(value="parentCode", required=false) String parentCode)
			throws ControllerException {
		List<TreeVo> treeList = channelService.queryTreeListByPid(parentCode,true);
		return treeList;
	}

	/**
	 * 查询机构树
	 * @param parentcode
	 * @return
	 * @throws ControllerException
	 */
	@ResponseBody
	@RequestMapping(value = "/queryDeptTreeList",method = RequestMethod.POST)
	public List<Map<String, String>> queryDeptTreeList(@RequestParam(value="id",required=false) String parentcode)
			throws ControllerException{
		return deptservice.queryTreeList(parentcode);
	}

	/**
	 * 查询渠道详情
	 * @param id 渠道ID
	 * @return
	 * @throws ControllerException
	 */
	@ResponseBody
	@RequestMapping(value = "/channelInfo", method = RequestMethod.POST)
	public ModelMap channelInfo(@RequestParam("id") String id)
			throws ControllerException {
		ModelMap mm = new ModelMap();

		//查询渠道信息
		ChannelRespVo channelInfo = channelService.queryDetailById(id);

		if(channelInfo != null) { //成功
			mm.put("errCode", "0");
			mm.put("errMsg" , "") ;
			mm.put("channelInfo", channelInfo);
		} else { //失败
			mm.put("errCode", "01001001") ;
			mm.put("errMsg" , "不存在该渠道id="+id) ;
		}

		return mm;
	}

	/**
	 * 添加渠道
	 * @param session session
	 * @param channel 管道
	 * @param baseInfoVo 基本信息
	 * @return
	 * @throws ControllerException
	 */
	@ResponseBody
	@RequestMapping(value = "/saveAndUpdate", method = RequestMethod.POST)
	public ModelMap saveAndUpdate(HttpSession session ,INSBChannel channel, BaseInfoVo baseInfoVo) {
		ModelMap mm = new ModelMap();

		String operator = ((INSCUser)session.getAttribute("insc_user")).getUsername() ;

		channel.setId(baseInfoVo.getChannelid()) ;
		channel.setOperator(operator) ;
		String errMsg = null ;
		try {
			if (StringUtil.isEmpty(channel.getId())) {
				//添加节点
				channelService.addChannel(channel, baseInfoVo);
			} else {
				//更新节点
				channelService.updateChannel(channel, baseInfoVo);
			}
			channelService.updateAgentForChannel(channel, baseInfoVo);
		} catch(Exception e) {
			errMsg = e.getMessage() ;
		}

		if(errMsg == null) {
			mm.put("errCode", "0");
			mm.put("errMsg" , "") ;
		} else { //失败
			mm.put("errCode", "01001002") ;
			mm.put("errMsg" , errMsg) ;
		}

		return mm;
	}


	/**
	 * 删除渠道
	 * @param id 渠道ID
	 * @return
	 * @throws ControllerException
	 */
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ModelMap deleteById(@RequestParam("id") String id)
			throws ControllerException {
		ModelMap mm = new ModelMap();

		//删除渠道
		String errMsg = null ;
		try {
			channelService.deleteById(id) ;
		} catch(Exception e) {
			errMsg = e.getMessage() ;
		}

		if(errMsg == null) {
			mm.put("errCode", "0") ;
			mm.put("errMsg", "") ;
		} else {
			mm.put("errCode", "01001003") ;
			mm.put("errMsg", errMsg) ;
		}

		return mm;
	}

	/**
	 * com.zzb.mobile.controller.AppInsuredQuoteController#searchProviderForMinizzb(com.zzb.extra.model.SearchProviderModelForMinizzb)
	 * 替代 此接口方法
	 * 查询渠道供应商列表
	 * @param city 地区码（市）
	 * @param usersource 渠道编码 (如：minizzb)
	 * @return
	 * @throws ControllerException
	 */
//	@RequestMapping(value = "queryChannelProviderList",method = RequestMethod.GET)
//	@ResponseBody
//	public List<Map<String, Object>> queryChannelProviderList(@RequestParam(value="city",required=true) String city,@RequestParam(value="usersource",required=false) String usersource) throws ControllerException{
//		String channelinnercode = "minizzb";//usersource
//		return service.queryChannelProviderList(city,channelinnercode);
//	}





}
