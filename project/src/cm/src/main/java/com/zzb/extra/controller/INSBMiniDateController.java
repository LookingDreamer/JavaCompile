package com.zzb.extra.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCCodeService;
import com.common.PagingParams;
import com.zzb.extra.entity.INSBMiniDate;
/**
 * 
 * @author Shigw
 *
 */
import com.zzb.extra.service.INSBMiniDateService;
import com.zzb.extra.util.ParamUtils;

@Controller
@RequestMapping("/minidate/*")
public class INSBMiniDateController extends BaseController {

	@Resource
	private INSBMiniDateService insbMiniDateService;
	
	@Resource
	private INSCCodeService codeService;
	
	
	@RequestMapping(value = "/miniDateList", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView miniDateCof() {
		
		ModelAndView mav = new ModelAndView("extra/miniDateConf");

		SimpleDateFormat sf = new SimpleDateFormat("yyyy");
		
		Calendar c = Calendar.getInstance();
		 //营销活动类型
        List<INSCCode> datetypeList = codeService.queryINSCCodeByCode("minidate", "minidate");
        mav.addObject("datetypeList", datetypeList);
        mav.addObject("year", sf.format(c.getTime()));
		
		
		
		return mav;
	}

	@RequestMapping(value = "/initMiniDate", method = RequestMethod.GET)
	@ResponseBody
	public String initMiniDate(@ModelAttribute INSBMiniDate dateTmp) {
		try {
			Map<String, Object> map = BeanUtils.toMap(dateTmp);
			
			List<INSBMiniDate> resultList = insbMiniDateService.selectDate(map);
			
			if(resultList.size() != 0 && resultList != null) {
				
				insbMiniDateService.deleteByYear(dateTmp.getYear());
				
				Integer year = Integer.valueOf(dateTmp.getYear());
				
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat sf1 = new SimpleDateFormat("yyyy");
				SimpleDateFormat sf2 = new SimpleDateFormat("MM");
				SimpleDateFormat sf3 = new SimpleDateFormat("dd");
				SimpleDateFormat sf4 = new SimpleDateFormat("E");
				
				Calendar c = Calendar.getInstance();
				c.set(year, 00, 01);
				
				
				// c.add(Calendar.DAY_OF_MONTH, 23);
				INSBMiniDate date = null;
				for (int i = 0; i <= 390; i++) {
					date = new INSBMiniDate();
					date.setId(UUIDUtils.random());
					date.setCreatetime(new Date());
					date.setYear(sf1.format(c.getTime()));
					date.setMonth(sf2.format(c.getTime()));
					
					date.setDay(sf3.format(c.getTime()));
					date.setDatestr(sf.format(c.getTime()));
					date.setDate(c.getTime());
					
					date.setWeekday(getWeekOfDate(c.getTime()));
					if(getWeekOfDate(c.getTime()).equals("星期日") || getWeekOfDate(c.getTime()).equals("星期六") ) {
						date.setDatetype("02");
					}else {
						date.setDatetype("01");
					}
					
					insbMiniDateService.insertDate(date);
					c.add(Calendar.DAY_OF_MONTH, 1);
					if(!sf1.format(c.getTime()).equals( dateTmp.getYear())) break;
				}
				return "再次初始化成功";
			}else {
				
				
				Integer year = Integer.valueOf(dateTmp.getYear());
				
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat sf1 = new SimpleDateFormat("yyyy");
				SimpleDateFormat sf2 = new SimpleDateFormat("MM");
				SimpleDateFormat sf3 = new SimpleDateFormat("dd");
				
				
				Calendar c = Calendar.getInstance();
				c.set(year, 00, 01);
				
				
				// c.add(Calendar.DAY_OF_MONTH, 23);
				INSBMiniDate date = null;
				for (int i = 0; i <= 390; i++) {
					date = new INSBMiniDate();
					date.setId(UUIDUtils.random());
					date.setCreatetime(new Date());
					date.setYear(sf1.format(c.getTime()));
					date.setMonth(sf2.format(c.getTime()));
					
					date.setDay(sf3.format(c.getTime()));
					date.setDatestr(sf.format(c.getTime()));
					date.setDate(c.getTime());
					
					date.setWeekday(getWeekOfDate(c.getTime()));
					if(getWeekOfDate(c.getTime()).equals("星期日") || getWeekOfDate(c.getTime()).equals("星期六") ) {
						date.setDatetype("02");
					}else {
						date.setDatetype("01");
					}
					
					insbMiniDateService.insertDate(date);
					c.add(Calendar.DAY_OF_MONTH, 1);
					if(!sf1.format(c.getTime()).equals( dateTmp.getYear())) break;
					
					
				}
			}
			return "success";
		} catch (Exception e) {
			
			LogUtil.info("初始化有误"+e);
			return "fail";
		}

	}
	@RequestMapping(value = "/queryMiniDate", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> queryMiniDate(@ModelAttribute PagingParams para,@ModelAttribute INSBMiniDate date) {
		
		Map<String, Object> map = BeanUtils.toMap(para,date);
		
		return insbMiniDateService.queryMiniDateList(map);
		
		
	}
	@RequestMapping(value = "/updateMiniDatetype", method = RequestMethod.POST)
	@ResponseBody
	public String updateMiniDatetype(HttpSession session, INSBMiniDate date) {
		
		try {
			 Calendar ca = Calendar.getInstance();
	         INSCUser user = (INSCUser) session.getAttribute("insc_user");
	         date.setModifytime(ca.getTime());
	         date.setOperator(user.getUsercode());
	         
	         insbMiniDateService.updateById(date);
	         
		} catch (Exception e) {
			return "fail";
		}
		return "success";
		
	}
	
	
	@RequestMapping(value = "/initDatetype", method = RequestMethod.GET)
	@ResponseBody
	public String initDatetype() {

		try {

			List<INSBMiniDate> dates = insbMiniDateService.selectDate(new HashMap<>());

			for (INSBMiniDate date : dates) {
				if (!date.getWeekday().equals("星期日") && !date.getWeekday().equals("星期六")) {
					if (!isHoliday(date.getDatestr())) {

						date.setDatetype("01");
						insbMiniDateService.updateById(date);
					} else {
						date.setDatetype("02");
						insbMiniDateService.updateById(date);
					}

				} else {
					if (isDiaoxiu(date.getDatestr())) {

						date.setDatetype("01");
						insbMiniDateService.updateById(date);
					} else {

						date.setDatetype("02");
						insbMiniDateService.updateById(date);
					}
				}
			}
			
			return ParamUtils.resultMap(true, "修改完毕");
		} catch (Exception e) {

			return ParamUtils.resultMap(false, "系统错误");
		}
	}

	public static boolean isHoliday(String year) {
		List<String> ls = new ArrayList<String>();

		// 元旦isNewYear
		ls.add("2016-12-31");
		ls.add("2017-01-01");
		ls.add("2017-01-02");// 周一

		// 春节isSpring
		ls.add("2017-01-27");
		ls.add("2017-01-28");
		ls.add("2017-01-29");
		ls.add("2017-01-30");
		ls.add("2017-01-31");
		ls.add("2017-02-01");
		ls.add("2017-02-02");

		// 清明节isTomb
		ls.add("2017-04-02");
		ls.add("2017-04-03");
		ls.add("2017-04-04");

		// 劳动节isLabour
		ls.add("2017-04-29");
		ls.add("2017-04-30");
		ls.add("2017-05-01");

		// 端午节isBoat
		ls.add("2017-05-28");
		ls.add("2017-05-29");
		ls.add("2017-05-30");

		// 国庆节isNational

		ls.add("2017-10-01");
		ls.add("2017-10-02");
		ls.add("2017-10-03");
		ls.add("2017-10-04");
		ls.add("2017-10-05");
		ls.add("2017-10-06");
		ls.add("2017-10-07");
		ls.add("2017-10-08");

		//

		if (ls.contains(year))
			return true;
		return false;
	}

	public static boolean isDiaoxiu(String findDate) {
		List<String> ls1 = new ArrayList<String>();

		ls1.add("2017-01-22");
		ls1.add("2017-02-04");
		ls1.add("2017-04-01");
		ls1.add("2017-05-27");
		ls1.add("2017-09-30");

		if (ls1.contains(findDate))
			return true;
		return false;
	}
	/**

     * 获取当前日期是星期几<br>
     * 
     * @param dt
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;

        return weekDays[w];
    }

}
