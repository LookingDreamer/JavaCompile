package com.cninsure.system.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

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
import com.cninsure.system.entity.INSCMenu;
import com.cninsure.system.service.INSCMenuService;

@Controller
@RequestMapping("/menu/*")
public class MenuController extends BaseController {
	
	@Resource
	private INSCMenuService inscMenuService;

	@RequestMapping(value = "initmenus", method = RequestMethod.GET)
	public ModelAndView initMenus(HttpSession session) throws ControllerException{
		ModelAndView mav = new ModelAndView("application");
		mav.addObject("menu", inscMenuService.queryMenusFtl("", "0"));
		return mav;
	}
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list(HttpSession session) throws ControllerException{
		ModelAndView mav = new ModelAndView("system/menusmanager");
		return mav;
	}
	
	@RequestMapping(value = "menusmanagerlist", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<Object, Object>> meunsManageTreeList(@RequestParam(value="id", required=false) String id) throws ControllerException{
		List<Map<Object,Object>> resultMenusList = new ArrayList<Map<Object,Object>>();
		resultMenusList = inscMenuService.queryMenusList(id,"parentnodecode");
		return resultMenusList;
	}
	
	/**
	 * 跳转新建菜单页面
	 * @param session
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "addftl", method = RequestMethod.GET)
	public ModelAndView addftl(HttpSession session) throws ControllerException{
		ModelAndView mav = new ModelAndView("system/menusave");
		return mav;
	}
	
	/**
	 * 新建菜单
	 * @param session
	 * @param menu
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "addmenu", method = RequestMethod.POST)
	public ModelAndView addMenu(HttpSession session,@ModelAttribute INSCMenu menu) throws ControllerException{
		ModelAndView mav = new ModelAndView("system/menusmanager"); 
		
		INSCMenu newmenu = new INSCMenu();
	    newmenu.setOperator("1");
		newmenu.setCreatetime(new Date());
		newmenu.setModifytime(null);
		newmenu.setNodename(menu.getNodename());
		newmenu.setActiveurl(menu.getActiveurl());
		newmenu.setNodecode(menu.getNodecode());
		newmenu.setParentnodecode(menu.getParentnodecode());
		newmenu.setNodelevel(menu.getNodelevel());
		newmenu.setChildflag("0");
		newmenu.setOrderflag(menu.getOrderflag());
		newmenu.setStatus(menu.getStatus());
		newmenu.setIconurl(menu.getIconurl());
		newmenu.setNoti(menu.getNoti());
		
		inscMenuService.insert(newmenu);
		
		mav.addObject("flag", "success");
		return mav;
	}
	
	/**
	 * 添加菜单页面--选择父级菜单--菜单名字集合
	 * @param session
	 * @param menu
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "addmenuparentnodename", method = RequestMethod.POST)
	public ModelAndView addMenuParentNodeName(HttpSession session,@ModelAttribute INSCMenu menu) throws ControllerException{
		ModelAndView mav = new ModelAndView("system/menusmanager");
		
		return mav; 
	}
	
	
	@RequestMapping(value = "deletebyid", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView deleteById(HttpSession session, @RequestParam(value="id") String id) throws ControllerException{
		ModelAndView mav = new ModelAndView("system/menusmanager");
		
		int count = inscMenuService.deleteById(id);
		JSONObject jsonObject = new JSONObject();
		jsonObject.accumulate("count", count);
		
		mav.addObject("flag", "success");
		return mav;
	}
	
	@RequestMapping(value = "querybyid",method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView queryById(@RequestParam(value="id") String id)throws ControllerException{
		ModelAndView model = new ModelAndView("system/menusmanager");
		INSCMenu menuobject = inscMenuService.queryById(id);
		model.addObject("menuobject", menuobject);
		return model;
	}
	
	
	@RequestMapping(value = "updatemenubyid",method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView updateMenuById(@ModelAttribute INSCMenu menu)throws ControllerException{
		ModelAndView mav = new ModelAndView("system/menusmanager"); 
		//chax 
		INSCMenu lmenu=inscMenuService.queryById(menu.getId());
		lmenu.setNodename(menu.getNodename());
		lmenu.setActiveurl(menu.getActiveurl());
		lmenu.setNodecode(menu.getNodecode());
		lmenu.setParentnodecode(menu.getParentnodecode());
		lmenu.setNodelevel(menu.getNodelevel());
		lmenu.setOrderflag(menu.getOrderflag());
		lmenu.setIconurl(menu.getIconurl());
		lmenu.setChildflag(menu.getChildflag());
		lmenu.setModifytime(new Date());
		
		inscMenuService.updateById(lmenu);
		mav.addObject("flag", "success");
		return mav;
	}
	
	/*@RequestMapping(value = "menulist",method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView menulist()throws ControllerException{
		ModelAndView model = new ModelAndView("system/menulist");
		return model;
	}*/
	
}
