package com.zzb.report.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCDeptService;
import com.runqian.report4.usermodel.Context;

/**
 * CM系统 车型投保  订单列表 
 */
@Controller
@RequestMapping("/report/*")
public class INSBReportController extends BaseController {
	@Resource
	private INSCDeptService inscDeptService;
	
//    @RequestMapping(value = "/{reportname:.*}", method = RequestMethod.GET)
//    @ResponseBody
//    public ModelAndView report(HttpSession session, HttpServletRequest request, @PathVariable String reportname) throws ControllerException {
//        if("showReport".equals(reportname))
//            return showReport(session, request);
//        ModelAndView mav = new ModelAndView("report/" + reportname);
//        return mav;
//    }
	@RequestMapping(value = "/costDiff", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView costDiff(HttpSession session, HttpServletRequest request) throws ControllerException {
		ModelAndView mav = new ModelAndView("report/costDiff");
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();
		if("1200000000".equals(loginUser.getUserorganization())){
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("id", "1200000000");
			map.put("name", "泛华财险营业集团");
			list.add(map);
        	list.addAll(inscDeptService.queryDeptList("1200000000"));
        }else{
	        INSCDept dept = inscDeptService.queryById(loginUser.getUserorganization());
	        Map<Object, Object> map = new HashMap<Object, Object>();
	        if(!"02".equals(dept.getComtype())){
	        	dept = inscDeptService.queryById(dept.getParentcodes().split("\\+")[3]);
	        }
	        map.put("id", dept.getId());
	        map.put("name", dept.getComname());
	        list.add(map);
        }     
    	mav.addObject("deptList", list);
        return mav;
    }
	@RequestMapping(value = "/processEfficiencyMain", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView processEfficiencyMain(HttpSession session, HttpServletRequest request) throws ControllerException {
        ModelAndView mav = new ModelAndView("report/processEfficiencyMain");
        INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
        List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();
        if("1200000000".equals(loginUser.getUserorganization())){
        	Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("id", "1200000000");
			map.put("name", "泛华财险营业集团");
			list.add(map);
        	list.addAll(inscDeptService.queryDeptList("1200000000"));
        }else{
	        INSCDept dept = inscDeptService.queryById(loginUser.getUserorganization());
	        Map<Object, Object> map = new HashMap<Object, Object>();
	        if(!"02".equals(dept.getComtype())){
	        	dept = inscDeptService.queryById(dept.getParentcodes().split("\\+")[3]);
	        }
	        map.put("id", dept.getId());
	        map.put("name", dept.getComname());
	        list.add(map);
        }
    	mav.addObject("deptList", list);
        return mav;
    }
	
    @RequestMapping(value = "/transactionList", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView transactionList(HttpSession session, HttpServletRequest request) throws ControllerException {
		ModelAndView mav = new ModelAndView("report/transactionList");
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();
		if ("1200000000".equals(loginUser.getUserorganization())) {
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("id", "1200000000");
			map.put("name", "泛华财险营业集团");
			list.add(map);
			list.addAll(inscDeptService.queryDeptList("1200000000"));
		} else {
			INSCDept dept = inscDeptService.queryById(loginUser.getUserorganization());
			Map<Object, Object> map = new HashMap<Object, Object>();
			if (!"02".equals(dept.getComtype())) {
				dept = inscDeptService.queryById(dept.getParentcodes().split("\\+")[3]);
			}
			map.put("id", dept.getId());
			map.put("name", dept.getComname());
			list.add(map);
		}
		mav.addObject("deptList", list);
		return mav;
    }
    
    @RequestMapping(value = "/activeUser", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView activeUser(HttpSession session, HttpServletRequest request) throws ControllerException {
        ModelAndView mav = new ModelAndView("report/activeUser");
        INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
        LogUtil.info("Userorganization:" + loginUser.getUserorganization());
        List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();
        if("1200000000".equals(loginUser.getUserorganization())){
        	Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("id", "1200000000");
			map.put("name", "泛华财险营业集团");
			list.add(map);
        	list.addAll(inscDeptService.queryDeptList("1200000000"));
        }else{
	        INSCDept dept = inscDeptService.queryById(loginUser.getUserorganization());
	        Map<Object, Object> map = new HashMap<Object, Object>();
	        if(!"02".equals(dept.getComtype())){
	        	dept = inscDeptService.queryById(dept.getParentcodes().split("\\+")[3]);
	        }
	        map.put("id", dept.getId());
	        map.put("name", dept.getComname());
	        list.add(map);
        }
        mav.addObject("deptList", list);
        return mav;
    }
    @RequestMapping(value = "/agentInfo", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView agentInfo(HttpSession session, HttpServletRequest request) throws ControllerException {
        ModelAndView mav = new ModelAndView("report/agentInfo");
        INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
        List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();
        if("1200000000".equals(loginUser.getUserorganization())){
        	Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("id", "1200000000");
			map.put("name", "泛华财险营业集团");
			list.add(map);
        	list.addAll(inscDeptService.queryDeptList("1200000000"));
        }else{
	        INSCDept dept = inscDeptService.queryById(loginUser.getUserorganization());
	        Map<Object, Object> map = new HashMap<Object, Object>();
	        if(!"02".equals(dept.getComtype())){
	        	dept = inscDeptService.queryById(dept.getParentcodes().split("\\+")[3]);
	        }
	        map.put("id", dept.getId());
	        map.put("name", dept.getComname());
	        list.add(map);
        }
        mav.addObject("deptList", list);
        return mav;
    }
    
    @RequestMapping(value = "/agentBusiness", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView agentBusiness(HttpSession session, HttpServletRequest request) throws ControllerException {
        ModelAndView mav = new ModelAndView("report/agentBusiness");
        INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
        List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();
        if("1200000000".equals(loginUser.getUserorganization())){
        	Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("id", "1200000000");
			map.put("name", "泛华财险营业集团");
			list.add(map);
        	list.addAll(inscDeptService.queryDeptList("1200000000"));
        }else{
	        INSCDept dept = inscDeptService.queryById(loginUser.getUserorganization());
	        Map<Object, Object> map = new HashMap<Object, Object>();
	        if(!"02".equals(dept.getComtype())){
	        	dept = inscDeptService.queryById(dept.getParentcodes().split("\\+")[3]);
	        }
	        map.put("id", dept.getId());
	        map.put("name", dept.getComname());
	        list.add(map);
        }
        mav.addObject("deptList", list);
        return mav;
    }
    
    @RequestMapping(value = "/secondPayList", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView secondPayList(HttpSession session, HttpServletRequest request) throws ControllerException {
        ModelAndView mav = new ModelAndView("report/secondPayList");
        INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
        List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();
        if("1200000000".equals(loginUser.getUserorganization())){
        	Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("id", "1200000000");
			map.put("name", "泛华财险营业集团");
			list.add(map);
        	list.addAll(inscDeptService.queryDeptList("1200000000"));
        }else{
	        INSCDept dept = inscDeptService.queryById(loginUser.getUserorganization());
	        Map<Object, Object> map = new HashMap<Object, Object>();
	        if(!"02".equals(dept.getComtype())){
	        	dept = inscDeptService.queryById(dept.getParentcodes().split("\\+")[3]);
	        }
	        map.put("id", dept.getId());
	        map.put("name", dept.getComname());
	        list.add(map);
        }
        mav.addObject("deptList", list);
        return mav;
    }
    
    @RequestMapping(value = "/processEfficiency", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView processEfficiency(HttpSession session, HttpServletRequest request) throws ControllerException {
        ModelAndView mav = new ModelAndView("report/processEfficiency");
        INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
        List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();
        if("1200000000".equals(loginUser.getUserorganization())){
        	Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("id", "1200000000");
			map.put("name", "泛华财险营业集团");
			list.add(map);
        	list.addAll(inscDeptService.queryDeptList("1200000000"));
        }else{
	        INSCDept dept = inscDeptService.queryById(loginUser.getUserorganization());
	        Map<Object, Object> map = new HashMap<Object, Object>();
	        if(!"02".equals(dept.getComtype())){
	        	dept = inscDeptService.queryById(dept.getParentcodes().split("\\+")[3]);
	        }
	        map.put("id", dept.getId());
	        map.put("name", dept.getComname());
	        list.add(map);
        }
        mav.addObject("deptList", list);
        return mav;
    }

    @RequestMapping(value = "/efficiencyAgentManager", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView efficiencyAgentManager(HttpSession session, HttpServletRequest request) throws ControllerException {
        ModelAndView mav = new ModelAndView("report/efficiencyAgentManager");
        INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
        List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();
        if("1200000000".equals(loginUser.getUserorganization())){
        	Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("id", "1200000000");
			map.put("name", "泛华财险营业集团");
			list.add(map);
        	list.addAll(inscDeptService.queryDeptList("1200000000"));
        }else{
	        INSCDept dept = inscDeptService.queryById(loginUser.getUserorganization());
	        Map<Object, Object> map = new HashMap<Object, Object>();
	        if(!"02".equals(dept.getComtype())){
	        	dept = inscDeptService.queryById(dept.getParentcodes().split("\\+")[3]);
	        }
	        map.put("id", dept.getId());
	        map.put("name", dept.getComname());
	        list.add(map);
        }
        mav.addObject("deptList", list);
        return mav;
    }
    @RequestMapping(value = "/efficiencyOrgWebsite", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView efficiencyOrgWebsite(HttpSession session, HttpServletRequest request) throws ControllerException {
        ModelAndView mav = new ModelAndView("report/efficiencyOrgWebsite");
        INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
        List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();
        if("1200000000".equals(loginUser.getUserorganization())){
        	Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("id", "1200000000");
			map.put("name", "泛华财险营业集团");
			list.add(map);
        	list.addAll(inscDeptService.queryDeptList("1200000000"));
        }else{
	        INSCDept dept = inscDeptService.queryById(loginUser.getUserorganization());
	        Map<Object, Object> map = new HashMap<Object, Object>();
	        if(!"02".equals(dept.getComtype())){
	        	dept = inscDeptService.queryById(dept.getParentcodes().split("\\+")[3]);
	        }
	        map.put("id", dept.getId());
	        map.put("name", dept.getComname());
	        list.add(map);
        }
        mav.addObject("deptList", list);
        return mav;
    }
    @RequestMapping(value = "/efficiencyPrivname", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView efficiencyPrivname(HttpSession session, HttpServletRequest request) throws ControllerException {
        ModelAndView mav = new ModelAndView("report/efficiencyPrivname");
        INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
        List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();
        if("1200000000".equals(loginUser.getUserorganization())){
        	Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("id", "1200000000");
			map.put("name", "泛华财险营业集团");
			list.add(map);
        	list.addAll(inscDeptService.queryDeptList("1200000000"));
        }else{
	        INSCDept dept = inscDeptService.queryById(loginUser.getUserorganization());
	        Map<Object, Object> map = new HashMap<Object, Object>();
	        if(!"02".equals(dept.getComtype())){
	        	dept = inscDeptService.queryById(dept.getParentcodes().split("\\+")[3]);
	        }
	        map.put("id", dept.getId());
	        map.put("name", dept.getComname());
	        list.add(map);
        }
        mav.addObject("deptList", list);
        return mav;
    }
    @RequestMapping(value = "/efficiencyTimeframe", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView efficiencyTimeframe(HttpSession session, HttpServletRequest request) throws ControllerException {
        ModelAndView mav = new ModelAndView("report/efficiencyTimeframe");
        INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
        List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();
        if("1200000000".equals(loginUser.getUserorganization())){
        	Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("id", "1200000000");
			map.put("name", "泛华财险营业集团");
			list.add(map);
        	list.addAll(inscDeptService.queryDeptList("1200000000"));
        }else{
	        INSCDept dept = inscDeptService.queryById(loginUser.getUserorganization());
	        Map<Object, Object> map = new HashMap<Object, Object>();
	        if(!"02".equals(dept.getComtype())){
	        	dept = inscDeptService.queryById(dept.getParentcodes().split("\\+")[3]);
	        }
	        map.put("id", dept.getId());
	        map.put("name", dept.getComname());
	        list.add(map);
        }
        mav.addObject("deptList", list);
        return mav;
    }
    @RequestMapping(value = "/efficiencyOrgOverview", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView efficiencyOrgOverview(HttpSession session, HttpServletRequest request) throws ControllerException {
        ModelAndView mav = new ModelAndView("report/efficiencyOrgOverview");
        INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
        List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();
        if("1200000000".equals(loginUser.getUserorganization())){
        	Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("id", "1200000000");
			map.put("name", "泛华财险营业集团");
			list.add(map);
        	list.addAll(inscDeptService.queryDeptList("1200000000"));
        }else{
	        INSCDept dept = inscDeptService.queryById(loginUser.getUserorganization());
	        Map<Object, Object> map = new HashMap<Object, Object>();
	        if(!"02".equals(dept.getComtype())){
	        	dept = inscDeptService.queryById(dept.getParentcodes().split("\\+")[3]);
	        }
	        map.put("id", dept.getId());
	        map.put("name", dept.getComname());
	        list.add(map);
        }
        mav.addObject("deptList", list);
        return mav;
    }
    
    @RequestMapping(value = "/expressList", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView expressList(HttpSession session, HttpServletRequest request) throws ControllerException {
        ModelAndView mav = new ModelAndView("report/expressList");
        INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
        List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();
        if("1200000000".equals(loginUser.getUserorganization())){
        	Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("id", "1200000000");
			map.put("name", "泛华财险营业集团");
			list.add(map);
        	list.addAll(inscDeptService.queryDeptList("1200000000"));
        }else{
	        INSCDept dept = inscDeptService.queryById(loginUser.getUserorganization());
	        Map<Object, Object> map = new HashMap<Object, Object>();
	        if(!"02".equals(dept.getComtype())){
	        	dept = inscDeptService.queryById(dept.getParentcodes().split("\\+")[3]);
	        }
	        map.put("id", dept.getId());
	        map.put("name", dept.getComname());
	        list.add(map);
        }
        mav.addObject("deptList", list);
        return mav;
    }

	@RequestMapping(value = "/efficiencyOrgOverviewDesc", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView efficiencyOrgOverviewDesc(HttpSession session, HttpServletRequest request) throws ControllerException {
		ModelAndView mav = new ModelAndView("report/efficiencyOrgOverviewDesc");
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();
		if("1200000000".equals(loginUser.getUserorganization())){
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("id", "1200000000");
			map.put("name", "泛华财险营业集团");
			list.add(map);
			list.addAll(inscDeptService.queryDeptList("1200000000"));
		}else{
			INSCDept dept = inscDeptService.queryById(loginUser.getUserorganization());
			Map<Object, Object> map = new HashMap<Object, Object>();
			if(!"02".equals(dept.getComtype())){
				dept = inscDeptService.queryById(dept.getParentcodes().split("\\+")[3]);
			}
			map.put("id", dept.getId());
			map.put("name", dept.getComname());
			list.add(map);
		}
		mav.addObject("deptList", list);
		return mav;
	}

	@RequestMapping(value = "/chPayback", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView chPayback(HttpSession session, HttpServletRequest request) throws ControllerException {

		ModelAndView mav = new ModelAndView("report/chPayback");
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();
		if("1200000000".equals(loginUser.getUserorganization())){
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("id", "1200000000");
			map.put("name", "泛华财险营业集团");
			list.add(map);
			list.addAll(inscDeptService.queryDeptList("1200000000"));
		}else{
			INSCDept dept = inscDeptService.queryById(loginUser.getUserorganization());
			Map<Object, Object> map = new HashMap<Object, Object>();
			if(!"02".equals(dept.getComtype())){
				dept = inscDeptService.queryById(dept.getParentcodes().split("\\+")[3]);
			}
			map.put("id", dept.getId());
			map.put("name", dept.getComname());
			list.add(map);
		}
		mav.addObject("deptList", list);
		return mav;
	}

	@RequestMapping(value = "/chPrePay", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView chPrePay(HttpSession session, HttpServletRequest request) throws ControllerException {

		ModelAndView mav = new ModelAndView("report/chPrePay");
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();
		if("1200000000".equals(loginUser.getUserorganization())){
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("id", "1200000000");
			map.put("name", "泛华财险营业集团");
			list.add(map);
			list.addAll(inscDeptService.queryDeptList("1200000000"));
		}else{
			INSCDept dept = inscDeptService.queryById(loginUser.getUserorganization());
			Map<Object, Object> map = new HashMap<Object, Object>();
			if(!"02".equals(dept.getComtype())){
				dept = inscDeptService.queryById(dept.getParentcodes().split("\\+")[3]);
			}
			map.put("id", dept.getId());
			map.put("name", dept.getComname());
			list.add(map);
		}
		mav.addObject("deptList", list);
		return mav;
	}


	@RequestMapping(value = "/agentOptCRate", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView agentOptCRate(HttpSession session, HttpServletRequest request) throws ControllerException {
		// String reportName=request.getParameter("reportName");
		// System.out.println("==========reportName==============="+reportName);
		ModelAndView mav = new ModelAndView("report/agentOptCRate");
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();
		if("1200000000".equals(loginUser.getUserorganization())){
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("id", "1200000000");
			map.put("name", "泛华财险营业集团");
			list.add(map);
			list.addAll(inscDeptService.queryDeptList("1200000000"));
		}else{
			INSCDept dept = inscDeptService.queryById(loginUser.getUserorganization());
			Map<Object, Object> map = new HashMap<Object, Object>();
			if(!"02".equals(dept.getComtype())){
				dept = inscDeptService.queryById(dept.getParentcodes().split("\\+")[3]);
			}
			map.put("id", dept.getId());
			map.put("name", dept.getComname());
			list.add(map);
		}
		mav.addObject("deptList", list);
		return mav;
	}



	@RequestMapping(value = "/generalReportByDept", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView generalReportByDept(HttpSession session, HttpServletRequest request,String reportName) throws ControllerException {
		// String reportName=request.getParameter("reportName");
		 System.out.println("==========reportName==============="+reportName);
		ModelAndView mav = new ModelAndView("report/"+reportName);
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();
		if("1200000000".equals(loginUser.getUserorganization())){
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("id", "1200000000");
			map.put("name", "泛华财险营业集团");
			list.add(map);
			list.addAll(inscDeptService.queryDeptList("1200000000"));
		}else{
			INSCDept dept = inscDeptService.queryById(loginUser.getUserorganization());
			Map<Object, Object> map = new HashMap<Object, Object>();
			if(!"02".equals(dept.getComtype())){
				dept = inscDeptService.queryById(dept.getParentcodes().split("\\+")[3]);
			}
			map.put("id", dept.getId());
			map.put("name", dept.getComname());
			list.add(map);
		}
		mav.addObject("deptList", list);
		return mav;
	}
    
    @RequestMapping(value = "/showReport", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView showReport(HttpSession session, HttpServletRequest request) throws ControllerException {
//		request.setCharacterEncoding("utf-8");
        String report = request.getParameter("raq");

        System.out.println("report=" + report);
        String reportFileHome = Context.getInitCtx().getMainDir();
        StringBuffer param = new StringBuffer();

        //保证报表名称的完整性
        int iTmp = 0;
        if ((iTmp = report.lastIndexOf(".raq")) <= 0) {
            report = report + ".raq";
            iTmp = 0;
        }

        Enumeration paramNames = request.getParameterNames();
        if (paramNames != null) {
            while (paramNames.hasMoreElements()) {
                String paramName = (String) paramNames.nextElement();

                String paramValue = request.getParameter(paramName);
                if (paramValue != null) {
                    //把参数拼成name=value;name2=value2;.....的形式
                    param.append(paramName).append("=").append(paramValue).append(";");
                }
            }
        }

        //以下代码是检测这个报表是否有相应的参数模板
        String paramFile = report.substring(0, iTmp) + "_arg.raq";
        System.out.println("paramfile=" + reportFileHome + File.separator + paramFile);
        System.out.println("param=" + param.toString());
        File f = new File(request.getServletContext().getRealPath(reportFileHome + File.separator + paramFile));
        ModelAndView mav = new ModelAndView("report/reportFiles/showReport");
        System.out.println("exists=" + f.exists());
        mav.addObject("exists", String.valueOf(f.exists()));
        mav.addObject("param", param.toString());
        mav.addObject("reportstr", report);
        return mav;
    }


}
