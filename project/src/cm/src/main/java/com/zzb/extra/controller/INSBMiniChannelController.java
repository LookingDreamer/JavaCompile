package com.zzb.extra.controller;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.cninsure.system.entity.INSCUser;
import com.common.PagingParams;
import com.runqian.report4.remote.LogInfo;
import com.zzb.chn.util.JsonUtils;
import com.zzb.extra.entity.INSBMinichannel;
import com.zzb.extra.entity.INSBMinichannelway;
import com.zzb.extra.model.MiniChannelModel;
import com.zzb.extra.service.INSBMinichannelService;
import com.zzb.extra.service.INSBMinichannelwayService;
import com.zzb.extra.util.ParamUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import sun.rmi.runtime.Log;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/14.
 */
@Controller
@RequestMapping("/minichannel/*")
public class INSBMiniChannelController extends BaseController {
    /*
     *
     * 查询页面跳转和页面初始化
     */
    @Resource
    private INSBMinichannelService insbMinichannelService;

    @Resource
    private INSBMinichannelwayService insbMinichannelwayService;


    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ModelAndView getList(Map map) {
        ModelAndView mav = new ModelAndView("extra/market/channel/list");
        return mav;
    }

    /**
     *
     *
     * @return
     */
    @RequestMapping(value = "queryChannelList", method = RequestMethod.GET)
    @ResponseBody
    public String queryChannelList(@ModelAttribute PagingParams para,@ModelAttribute MiniChannelModel miniChannelModel){
        Map<String, Object> map = BeanUtils.toMap(para, miniChannelModel);
        String result = insbMinichannelService.queryChannelList(map);
        return result;
    }

    @RequestMapping(value = "deleteChannel", method = RequestMethod.POST)
    @ResponseBody
    public String deleteChannel(INSBMinichannel insbMinichannel){
        String result;
        try{
            Map<String, Object> map = BeanUtils.toMap(insbMinichannel);
            INSBMinichannelway way = new INSBMinichannelway();
            way.setChannelcode(insbMinichannel.getChannelcode());
            long total = insbMinichannelwayService.queryCount(way);
            if(total>0){
                result =  ParamUtils.resultMap(false, "hasdetail");
                return result;
            }
            insbMinichannelService.deleteById(insbMinichannel.getId());
            result =  ParamUtils.resultMap(true, "success");
        }catch (Exception e){
            LogUtil.info("deleteChannel fail channelcode="+insbMinichannel.getChannelcode()+ " "+e.getMessage());
            result =  ParamUtils.resultMap(false, "fail");
        }

        return result;
    }

    @RequestMapping(value = "deleteChannelWay", method = RequestMethod.POST)
    @ResponseBody
    public String deleteChannelWay(String id,String channelcode){
        String result;
        try{
            insbMinichannelwayService.deleteById(id);
            INSBMinichannelway insbMinichannelway = new INSBMinichannelway();
            insbMinichannelway.setChannelcode(channelcode);
            long total = insbMinichannelwayService.queryCount(insbMinichannelway);
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("waynum",total);
            map.put("channelcode",channelcode);
            insbMinichannelService.updateWayNum(map);
            result =  ParamUtils.resultMap(true, "success");
        }catch (Exception e){
            LogUtil.info("deleteChannelWay fail id="+id+ " "+e.getMessage());
            result =  ParamUtils.resultMap(false, "fail");
        }

        return result;
    }

    @RequestMapping(value = "saveChannel", method = RequestMethod.POST)
    @ResponseBody
    public String saveChannel(HttpSession session,INSBMinichannel insbMinichannel){
        String result="";
        try {
            Calendar ca = Calendar.getInstance();
            INSCUser user = (INSCUser) session.getAttribute("insc_user");
            Long tempcode = insbMinichannelService.selectMaxTempcode();
            if (tempcode == null) {
                tempcode = 1L;
            } else {
                tempcode++;
            }
            if (StringUtil.isEmpty(insbMinichannel.getId())) {
                insbMinichannel.setId(UUIDUtils.random());
                insbMinichannel.setTempcode(tempcode.intValue());
                insbMinichannel.setChannelcode("qd" + StringUtil.leftPad(String.valueOf(tempcode.intValue()), '0', 5));
                insbMinichannel.setWaynum(0);
                insbMinichannel.setCreatetime(ca.getTime());
                insbMinichannel.setOperator(user.getUsercode());
                insbMinichannelService.insert(insbMinichannel);
            } else {
                insbMinichannel.setModifytime(ca.getTime());
                insbMinichannel.setOperator(user.getUsercode());
                insbMinichannelService.updateById(insbMinichannel);
            }
            result =  ParamUtils.resultMap(true, "success");
        }catch (Exception e){
            LogUtil.info("saveChannel error "+e.getMessage());
            result = ParamUtils.resultMap(false, "fail");
        }
        return result;
    }

    @RequestMapping(value = "queryChannelWaylistByCode", method = RequestMethod.GET)
    @ResponseBody
    public String queryChannelWaylistByCode(@ModelAttribute PagingParams para,@ModelAttribute MiniChannelModel miniChannelModel){
        String result="";
        Map<String, Object> map = BeanUtils.toMap(para, miniChannelModel);
        try {
            result =  insbMinichannelwayService.queryChannelWayList(map);
        }catch (Exception e){
            LogUtil.info("queryChannelWaylistByCode error "+e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "saveChannelWay", method = RequestMethod.POST)
    @ResponseBody
    public String saveChannelWay(HttpSession session,INSBMinichannelway insbMinichannelway){
        String result="";
        try {
            Map<String, Object> map = BeanUtils.toMap(insbMinichannelway);
            Calendar ca = Calendar.getInstance();
            INSCUser user = (INSCUser) session.getAttribute("insc_user");
            Long tempcode = insbMinichannelwayService.selectMaxWayode(map);
            if (tempcode == null) {
                tempcode = 1L;
            } else {
                tempcode++;
            }
            if (StringUtil.isEmpty(insbMinichannelway.getId())) {
                insbMinichannelway.setId(UUIDUtils.random());
                insbMinichannelway.setWaycode(tempcode.intValue());
                insbMinichannelway.setCreatetime(ca.getTime());
                insbMinichannelway.setOperator(user.getUsercode());
                insbMinichannelwayService.insert(insbMinichannelway);
                INSBMinichannelway tempway = new INSBMinichannelway();
                tempway.setChannelcode(insbMinichannelway.getChannelcode());
                long total = insbMinichannelwayService.queryCount(tempway);
                Map<String,Object> tempmap = new HashMap<String,Object>();
                tempmap.put("waynum",total);
                tempmap.put("channelcode",insbMinichannelway.getChannelcode());
                insbMinichannelService.updateWayNum(tempmap);
            } else {
                insbMinichannelway.setModifytime(ca.getTime());
                insbMinichannelway.setOperator(user.getUsercode());
                insbMinichannelwayService.updateById(insbMinichannelway);
            }
            result =  ParamUtils.resultMap(true, "success");
        }catch (Exception e){
            LogUtil.info("saveChannelWay error "+e.getMessage());
            result = ParamUtils.resultMap(false, "fail");
        }
        return result;
    }

}
