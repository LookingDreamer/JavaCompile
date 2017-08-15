package com.zzb.extra.service.impl;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.service.INSCCodeService;
import com.common.HttpSender;
import com.zzb.chn.util.JsonUtils;
import com.zzb.extra.dao.INSBMiniOrderManageDao;
import com.zzb.extra.model.MiniOrderListMappingModel;
import com.zzb.extra.model.MiniOrderQueryModel;
import com.zzb.extra.service.INSBMiniOrderManageService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCDept;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.conf.dao.INSBGroupmembersDao;
import com.zzb.conf.dao.INSBPolicyManageDao;

@Service
@Transactional
public class INSBMiniOrderManageServiceImpl extends BaseServiceImpl<INSBOrder>
        implements INSBMiniOrderManageService {
    @Resource
    private INSBGroupmembersDao insbGroupmembersDao;
    @Resource
    private INSBMiniOrderManageDao insbMiniOrderManageDao;
    @Resource
    private INSBPolicyManageDao insbPolicyManageDao;
    @Resource
    private INSCDeptDao inscDeptDao;
    @Override
    protected BaseDao<INSBOrder> getBaseDao() {
        return insbMiniOrderManageDao;
    }

    @Resource
    private INSCCodeService inscCodeService;

    private static Map<String, String> httpHead = new HashMap<>();

    static {
        httpHead.put("innerPipe", "zheshiyigemimi!");
        httpHead.put("Content-Type", "application/json;charset=utf-8");
    }

    // 总页数
    private int getTotalPage(long total,int pageSize) {
        // 总条数/每页显示的条数=总页数
        int size = (int) (total / pageSize);
        // 最后一页的条数
        int mod = (int) (total % pageSize);
        if (mod != 0)
            size++;
        return total == 0 ? 1 : size;
    }

    @Override
    public Map<String, Object> queryOrderList(MiniOrderQueryModel queryModel,String deptid) {
        int pageSize = 10;
        int currentPage = queryModel.getCurrentPage() == 0?1:queryModel.getCurrentPage();
        INSCDept dept = inscDeptDao.selectById(deptid);
       // System.out.println(JsonUtils.serialize(queryModel));
        //
        /*if(dept!=null&&dept.getParentcodes()!=null){

            //
//			queryModel.setParentcodes(dept.getParentcodes()+"+"+dept.getComcode());
            queryModel.setParentcodes(dept.getDeptinnercode());
            queryModel.setComcode(dept.getComcode());
        }*/
        Map<String, Object> map = BeanUtils.toMap(queryModel);
        if  (StringUtils.isNotEmpty(queryModel.getStartdate())) {
            map.put("createTimeStart", queryModel.getStartdate() + " 00:00:00");
        }
        if  (StringUtils.isNotEmpty(queryModel.getEnddate())) {
            map.put("createTimeEnd", queryModel.getEnddate() + " 23:59:59");
        }
        //LogUtil.info(map);
        List<Map<Object,Object>> syListTotal = this.queryOrderPagingList(map);
        long total = syListTotal!=null?syListTotal.size():0;

        Map<String, Object> resultMap = new HashMap<String, Object>();

        //开始位置
        int limit = currentPage;
        map.put("pageSize", String.valueOf(pageSize));
        map.put("pageNum", String.valueOf(limit));
        //一个订单最多对应两个保单信息（商业险，交强险），只包含商业险的保单信息
        List<Map<Object,Object>> syList = this.queryOrderPagingList(map);
       // System.out.println("result==="+JsonUtils.serialize(syList));
        //所有页数
        int totalPage = getTotalPage(total,pageSize);
        resultMap.put("totalPage", totalPage);
        resultMap.put("currentPage", currentPage);
        resultMap.put("total", total);
        /*if(null!=tempcarTaskList){
            resultMap.put("rowList", tempcarTaskList.subList((int)limit, (int)(limit+pageSize>total?total:limit+pageSize)));
        }else{
            resultMap.put("rowList", syList);
        }*/
        resultMap.put("rowList", syList);
        return resultMap;
    }

    public List<Map<Object,Object>> queryOrderPagingList(Map map){
        try {
            INSCCode inscCode = new INSCCode();
            inscCode.setParentcode("ChannelForMini");
            inscCode.setCodetype("channelurl");
            inscCode.setCodevalue("01");
            inscCode = inscCodeService.queryOne(inscCode);
            String url = "";
            String signUrl = "";
            if (null != inscCode) {
                url = inscCode.getProp2() + "/channel/miniOrderQuery";
                signUrl = inscCode.getProp2() + "/channel/getSignForInner";
            }
            //Map<String, Object> map = new HashMap<String, Object>();
            String json = JsonUtils.serialize(map);
            //System.out.println("json====="+map);
            httpHead.put("channelId", "nqd_minizzb2016");
            String sign = HttpSender.doPost(signUrl, json, httpHead, "UTF-8");
            httpHead.put("sign",sign);
            String res  = HttpSender.doPost(url, json, httpHead, "UTF-8");
            MiniOrderListMappingModel orderListMappingModel = JsonUtils.deserialize(res, MiniOrderListMappingModel.class);
            return orderListMappingModel.getBody().getRows();
        }catch (Exception e){
              LogUtil.info(JsonUtils.serialize(map)+""+e.getMessage());
        }
        return  null;
    }

}

