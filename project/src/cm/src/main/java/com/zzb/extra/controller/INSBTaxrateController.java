package com.zzb.extra.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCCodeService;
import com.common.PagingParams;
import com.zzb.conf.dao.INSBChannelDao;
import com.zzb.conf.service.INSBChannelService;
import com.zzb.extra.controller.vo.TaxrateVo;
import com.zzb.extra.entity.INSBChnTaxrate;
import com.zzb.extra.service.INSBTaxrateService;
import com.zzb.extra.util.ParamUtils;

/**
 * 
 * @author shiguiwu
 * @date 2017年4月18日
 */
@Controller
@RequestMapping("/taxrate/**")
public class INSBTaxrateController extends BaseController {

	@Resource
	private INSBTaxrateService insbTaxrateService;
	
	@Resource
	private INSBChannelService insbChannelService;
	
	@Resource
	private INSBChannelDao insbChannelDao;
	
	@Resource
	private INSCCodeService inscCodeServicce;
	
	@Resource
	private INSBChannelDao channelDao;
	/**
	 * 返回视图
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "taxrateVeiw", method = RequestMethod.GET)
	public ModelAndView taxrateVeiw(HttpSession session) {
		ModelAndView mav = new ModelAndView("chn/taxrate");
//		查询所以渠道
		List<Map<String, String>> channels = insbChannelDao.selectChannelParent();
		mav.addObject("channels", channels);
//		税率的状态
		List<INSCCode> taxrateList = inscCodeServicce.queryINSCCodeByCode("status", "taxratestatus");
		mav.addObject("taxrateList", taxrateList);
		return mav;
	}
	/**
	 * 查询税率列表
	 * @param params
	 * @param taxrate
	 * @return
	 */
	@RequestMapping(value = "getTarrateList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getTarrateList(@ModelAttribute PagingParams pagParam,TaxrateVo taxrate) throws ControllerException {
		try {
			Map<String, Object> param = BeanUtils.toMap(taxrate,pagParam);
			return insbTaxrateService.queryTaxrateList(param);
		} catch (ControllerException e) {
			// TODO: handle exception
			LogUtil.info("税率查询出错" + e.getMessage());
			return null;
		}
	}
	/**
	 * 增加或者更新税率
	 * @param taxrate
	 * @param session
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "saveOrUpdateTaxrate", method = RequestMethod.GET)
	@ResponseBody
	public String saveOrUpdateTaxrate (@ModelAttribute INSBChnTaxrate taxrate, HttpSession session, String channelids) throws ControllerException {
		try {
			String operator = ((INSCUser)session.getAttribute("insc_user")).getUsercode();
			return insbTaxrateService.saveOrUpdateTaxrate(taxrate, operator, channelids);
		} catch (ControllerException e) {
			// TODO: handle exception
			LogUtil.info("更新或者增加失败" + e.getMessage());
			return ParamUtils.resultMap(false, "操作失败");
		}
	}
	/**
	 * 删除税率，支持批量删除
	 * @param ids
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "deleteTaxrate", method = RequestMethod.GET)
	@ResponseBody
	public String deleteTaxrate(String ids) throws ControllerException {
		try {
			return insbTaxrateService.batchDeleteOrDeleteTaxrate(ids);
		} catch (ControllerException e) {
			// TODO: handle exception
			LogUtil.info("删除税率出错" + e.getMessage());
			return ParamUtils.resultMap(false, "操作失败");
		}
	}
	/**
	 * 批量复制税率
	 * @param session
	 * @param taxIds
	 * @param channelids
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "batchCopyTaxrate", method = RequestMethod.POST)
	@ResponseBody
	public String batchCopyTaxrate(HttpSession session, @RequestParam String ratioIds, @RequestParam String channelIds,
			@RequestParam Date createtime)
	throws ControllerException {
		try {
			String operator = ((INSCUser)session.getAttribute("insc_user")).getUsercode();
			return insbTaxrateService.batchCopyTaxrate(operator, ratioIds, channelIds, createtime);
		} catch (ControllerException e) {
			// TODO: handle exception
			LogUtil.info("批量复制失败" + e.getMessage());
			return ParamUtils.resultMap(false, "操作失败");
		}
		
	}
	/**
	 * 批量修改
	 * @param session
	 * @param status
	 * @param taxrateIds
	 * @return
	 */
	@RequestMapping(value = "batchUpdateTaxrateStatus", method = RequestMethod.GET)
	@ResponseBody
	public String batchUpdateTaxrateStatus(HttpSession session,@ModelAttribute INSBChnTaxrate taxrate) {
		try {
			String operator = ((INSCUser)session.getAttribute("insc_user")).getUsercode();
			return insbTaxrateService.updateStatusByIds(operator,taxrate);
		} catch (Exception e) {
			// TODO: handle exception
			LogUtil.info("批量修改状态失败" + e.getMessage());
			return ParamUtils.resultMap(false, "操作失败");
		}
	}
	
	/**
	 * 初始化渠道树
	 * @return
	 */
	@RequestMapping(value = "initChanneTree", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> initChanneTree() {
		try {
			List<Map<String, String>> result = new ArrayList<>();
			List<Map<String, String>> chList = channelDao.selectChannelParent();
			for(Map<String, String> mapTemp : chList) {
				mapTemp.remove("isParent");
				mapTemp.put("isParent", "false");
				result.add(mapTemp);
			}
			
			return result;
		} catch (Exception e) {
			LogUtil.info("查询渠道树失败" + e.getMessage());
			return null;
		}
	}
	
	

}
