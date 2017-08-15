package com.zzb.conf.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.service.INSCCodeService;
import com.common.ConstUtil;
import com.common.ModelUtil;
import com.common.PagingParams;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.conf.entity.INSBRegion;
import com.zzb.conf.service.INSBCarinfoManagerService;
import com.zzb.conf.service.INSBPolicyManageService;
import com.zzb.model.PolicyQueryModel;

@Controller
@RequestMapping("/carmanager/*")
public class INSBCarManagerController extends BaseController {

	@Resource
	private INSBCarinfoManagerService insbCarinfoManagerService;
	@Resource
	private INSBPolicyManageService insbPolicyManageService;
	@Resource
	private INSCCodeService inscCodeService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(value="offset",required=false) Integer offset,
            @RequestParam(value="limit",required=false) Integer limit) throws ControllerException {
		ModelAndView mav = new ModelAndView("zzbconf/carlist");
        if(ModelUtil.isVoluation(offset, limit)){
            offset=ConstUtil.OFFSET;
            limit=ConstUtil.LIMIT;
        }
        List<Map<String, String>> list=insbCarinfoManagerService.querycarmodellist(offset, limit);
		mav.addObject("carmodellist", list);
		
		return mav;
	}
	
	@RequestMapping(value = "carinfolist", method = RequestMethod.GET)
	public ModelAndView carInfoList() throws ControllerException {
		ModelAndView mav = new ModelAndView("zzbconf/carinfolist");
		return mav;
	}

	// 车辆管理 加载数据
	@RequestMapping(value = "initcarlist", method = RequestMethod.GET)
	@ResponseBody
	public  Map<String, Object> showCartaskmanageList(@ModelAttribute PagingParams para,
			@ModelAttribute INSBCarinfo car) throws ControllerException {
		Map<String, Object> map = BeanUtils.toMap(car,para); 
		return insbCarinfoManagerService.initCarList(map);
	}
	
	/**
	 * 车辆明细 按钮跳转页面
	 * 
	 * @param carId
	 * @return
	 */
	@RequestMapping(value = "carinfobtn", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView carinfobtn(@RequestParam(value = "id",required = true) String carId) {
		ModelAndView mav = new ModelAndView("zzbconf/caredit");
		
		System.out.println("carid传过来没："+carId);
		INSBCarinfo car = insbCarinfoManagerService.queryById(carId);
		/*INSBCarinfo carList = insbCarinfoManagerService.queryById(carId);
		System.out.println("carList查到没::"+carList);
		List<INSBCarinfo> car1 = new ArrayList<INSBCarinfo>();*/
		mav.addObject("car", car);

		return mav;
	}
	
	/**
	 * 车辆明细 保单列表
	 * 
	 * @param carId
	 * @return
	 */
	@RequestMapping(value = "carinfopolicy", method = RequestMethod.GET)
	@ResponseBody
	public String carinfopolicy(@ModelAttribute PagingParams para,
			@ModelAttribute PolicyQueryModel queryModel) {
		Map<String, Object> map = BeanUtils.toMap(queryModel,para);
		String carPolicy = insbPolicyManageService.queryPagingList(map,"");
		
		return carPolicy;
	}
	
	//查询车型列表
	/*@RequestMapping(value="querycarmodellist",method=RequestMethod.GET)
	@ResponseBody
	public ModelAndView queryProList() throws ControllerException{
		ModelAndView mav = new ModelAndView("zzbconf/carlist");
		List<Map<String, String>> list=insbCarinfoManagerService.querycarmodellist();
		mav.addObject("carmodellist", list);
		return mav;
	}*/
	
	/**
	 * 转到编辑页面
	 * 
	 * @param carId
	 * @return
	 */
	@RequestMapping(value = "editcar", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView editcar(@RequestParam(required = true) String carId) {
		ModelAndView mav = new ModelAndView("zzbconf/caredit");
		
		System.out.println("carid传过来没："+carId);
		INSBCarinfo car = insbCarinfoManagerService.queryById(carId);
		/*INSBCarinfo carList = insbCarinfoManagerService.queryById(carId);
		System.out.println("carList查到没::"+carList);
		List<INSBCarinfo> car1 = new ArrayList<INSBCarinfo>();*/
		mav.addObject("car", car);

		return mav;
	}
	/**
	 * 属性编辑器
	 * @param binder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	/**
	 * 保存  (编辑和添加)
	 * @param carIda
	 * @return
	 */
	@RequestMapping(value = "savecar", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView savecar(@ModelAttribute INSBCarinfo car,
			@RequestParam(value="offset",required=false) Integer offset,
            @RequestParam(value="limit",required=false) Integer limit) {
		ModelAndView mav = new ModelAndView("zzbconf/carlist");
		String carid=car.getId();
		if(carid!=null && !"".equals(carid)){
//			INSBCarinfo oldcar = insbCarinfoManagerService.queryById(carid);
//			if(oldcar.getOwner()!=car.getOwner()){
//				oldcar.setOwner(car.getOwner());
//			}
//			oldcar.setOperator(car.getOperator());
//			oldcar.setCreatetime(car.getCreatetime());
//			oldcar.setTaskid(car.getTaskid());
			insbCarinfoManagerService.updateById(car);
		}else if(carid==null || "".equals(carid)){
			car.setOperator("test");
			car.setCreatetime(new Date());
			car.setTaskid("490");
			car.setRegistdate(null);
			car.setTransferdate(null);
			car.setSigndate(null);
			car.setBusinessstartdate(null);
			car.setBusinessenddate(null);
			car.setStrongstartdate(null);
			car.setStrongenddate(null);
			car.setIneffectualDate(null);
			car.setRejectDate(null);
			car.setLastCheckDate(null);
			insbCarinfoManagerService.insert(car);  //id为空插入
		}
		if(ModelUtil.isVoluation(offset, limit)){
            offset=ConstUtil.OFFSET;
            limit=ConstUtil.LIMIT;
        }
		List<Map<String, String>> list=insbCarinfoManagerService.querycarmodellist(offset, limit);
		mav.addObject("carmodellist", list);
		return mav;
	}
	
	/**
	 * 转到添加页面
	 * @param 
	 * @return
	 */
	@RequestMapping(value = "caradd", method = RequestMethod.GET)
	public ModelAndView caradd() {
		ModelAndView mav = new ModelAndView("zzbconf/caredit");
		return mav;
	}
	
	/**
	 * 车辆所属性质修改车辆信息的时候使用
	 */
	@RequestMapping(value = "getCarPropertyEx", method = RequestMethod.GET)
	@ResponseBody
	public List<INSCCode> getCarPropertyEx(String carproperty) {
		String codetype = null;
		if(carproperty.equals("0")){
			codetype = "personalCar";
		}else if(carproperty.equals("1")){
			codetype = "enterpriseCar";
		}else
			codetype = "groupCar";
		return inscCodeService.queryINSCCodeByCode("UseProps", codetype);
	}
	
}