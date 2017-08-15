package com.zzb.conf.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.cninsure.core.utils.LogUtil;
import com.cninsure.system.entity.INSCUser;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.system.service.INSCDeptService;
import com.zzb.conf.service.INSBHolidayAreaService;
import com.zzb.conf.service.INSBWorkTimeAreaService;
import com.zzb.conf.service.INSBWorkTimeService;
import com.zzb.model.WorkTimeModel;


@Controller
@RequestMapping("/worktime/*")
public class INSBWorkTimeController extends BaseController{
	@Resource
	private INSBWorkTimeService insbWorkTimeService;
	@Resource
	private INSBWorkTimeAreaService insbWorkTimeAreaService;
	@Resource
	private INSBHolidayAreaService insbHolidayAreaService;
	@Resource
	private INSCDeptService deptService;
	
	@RequestMapping(value="list",method=RequestMethod.GET)
	public ModelAndView queryList() throws ControllerException{
		ModelAndView result = new ModelAndView("zzbconf/worktimelist");
		Map<Object, Object> rowList1 = insbWorkTimeService.queryworktimelistByPage(1,null);
		result.addObject("allData", rowList1);
		return result;
	}
	
	//分页展示
	@RequestMapping(value="/queryworktimelistByPage",method=RequestMethod.POST)
	@ResponseBody
	public ModelAndView queryworktimelistByPage(@RequestParam(value="currentPage",required=false) int currentPage,
			@RequestParam(value="onlyfuture2",required=false) String onlyfuture2) throws ControllerException{
		ModelAndView mav = new ModelAndView("zzbconf/worktimelist");
		int truePage=1;
		if(currentPage != 0){
			truePage=currentPage;
		}
		Map<Object, Object> rowList = insbWorkTimeService.queryworktimelistByPage(truePage,onlyfuture2);
		mav.addObject("allData", rowList);
		return mav;
	}
	
	/*//通过机构id查询（机构树访问）
	@RequestMapping(value="/queryOneBydeptid",method=RequestMethod.POST)
	@ResponseBody
	public ModelAndView queryOneBydeptid(@RequestParam(value="deptid",required=false) String deptid) throws ControllerException{
		ModelAndView mav = new ModelAndView("zzbconf/worktimelist");
		if(!deptid.equals("")&&deptid != ""){
			Map<Object, Object> rowList = insbWorkTimeService.queryOneBydeptid(deptid);
			mav.addObject("allData", rowList);
		}else{
			mav.addObject("allData","");
		}
		return mav;
	}*/
	
	//通过机构id查询（机构树访问）
	@RequestMapping(value="/queryOneFuture",method=RequestMethod.POST)
	@ResponseBody
	public ModelAndView queryOneFuture(@RequestParam(value="deptid",required=false) String deptid,
			@RequestParam(value="onlyfuture",required=false) String onlyfuture) throws ControllerException{
		ModelAndView mav = new ModelAndView("zzbconf/worktimelist");
		
		Map<Object, Object> map = new HashMap<Object,Object>();
		map.put("inscdeptid", deptid);
		map.put("future", onlyfuture);
		System.out.println(onlyfuture);
		
		if(!deptid.equals("") && null != deptid){
			Map<Object, Object> rowList = insbWorkTimeService.queryOneFuture(map);
			mav.addObject("allData", rowList);
		}else{
			mav.addObject("allData","");
		}
		return mav;
	}
	
	//通过机构id查询（点击编辑，ajax访问）
	@RequestMapping(value="/queryEditOneBydeptid",method=RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> queryEditOneBydeptid(@RequestParam(value="deptid",required=false) String deptid) throws ControllerException{
		/*Map<Object, Object> rowList = insbWorkTimeService.queryOneBydeptid(deptid);*/
		Map<Object, Object> rowList = insbWorkTimeService.queryOneBydeptid(deptid);
		return rowList;
	}
	
	
	//编辑保存
	@RequestMapping(value="/editworktimesave",method=RequestMethod.POST)
	@ResponseBody
	public ModelAndView editworktimesave(@ModelAttribute WorkTimeModel model, HttpSession session) throws ControllerException{
		ModelAndView mav = new ModelAndView("zzbconf/worktimelist");
		INSCUser inscUser = (INSCUser) session.getAttribute("insc_user");

		LogUtil.info("worktime editworktimesave getUsercode" + inscUser.getUsercode() +" model" + JSONObject.fromObject(model).toString());
		if(!model.getInscdeptid().equals("")||model.getInscdeptid()!=""){
			insbWorkTimeService.updateOrAdd(model);
		}
		
		String deptid=model.getInscdeptid();
		LogUtil.info("worktime deptid:"+deptid);
		Map<Object, Object> rowList = insbWorkTimeService.queryOneBydeptid(deptid);
		mav.addObject("allData", rowList);
		return mav;
	}
	
	//弹窗中 删除某一时间对  ----工作时间
	@RequestMapping(value="/deleteOneWorktimeByid",method=RequestMethod.GET)
	@ResponseBody
	public void deleteWorktime(HttpSession session, @RequestParam(value="areaid") String areaid) throws ControllerException{
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		LogUtil.info("工作时间删除id为%s,操作人:%s", areaid, operator.getUsercode());
		int i=insbWorkTimeAreaService.deleteById(areaid);
		System.out.println("是否删除了："+i);
		
	}
	
	//弹窗中 删除某一时间对  ----节假日时间
	@RequestMapping(value="/deleteOneHolidayByid",method=RequestMethod.GET)
	@ResponseBody
	public void deleteholiday(HttpSession session, @RequestParam(value="holidayareaid") String holidayareaid) throws ControllerException{
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		LogUtil.info("节假日时间删除id为%s,操作人:%s", holidayareaid, operator.getUsercode());
		int i=insbHolidayAreaService.deleteById(holidayareaid);
		System.out.println("是否删除了："+i);
		
	}
	
	//机构树
	@RequestMapping(value="/querydepttree",method=RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> queryList(@RequestParam(value="id",required=false) String parentcode) throws ControllerException{
		return deptService.queryTreeList(parentcode);
	}
	
	@RequestMapping(value = "menulist",method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView menulist()throws ControllerException{
		ModelAndView model = new ModelAndView("system/menulist");
		return model;
	}
}
