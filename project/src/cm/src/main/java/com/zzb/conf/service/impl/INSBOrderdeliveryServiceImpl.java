package com.zzb.conf.service.impl;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.dao.INSCCodeDao;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.entity.INSCDept;
import com.zzb.conf.dao.INSBOrderdeliveryDao;
import com.zzb.conf.dao.INSBRegionDao;
import com.zzb.conf.entity.INSBOrderdelivery;
import com.zzb.conf.entity.INSBRegion;
import com.zzb.conf.service.INSBOrderdeliveryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@Transactional
public class INSBOrderdeliveryServiceImpl extends BaseServiceImpl<INSBOrderdelivery> implements
        INSBOrderdeliveryService {

    private static final String NUMBER_REGEX = "^[0-9]+$";

    @Resource
    private INSBOrderdeliveryDao insbOrderdeliveryDao;

    @Resource
    private INSCDeptDao inscDeptDao;

    @Resource
    private INSCCodeDao inscCodeDao;

    @Resource
    private INSBRegionDao insbRegionDao;

    @Override
    protected BaseDao<INSBOrderdelivery> getBaseDao() {
        return insbOrderdeliveryDao;
    }

    @Override
    public Map<String, Object> getOrderDeliveryDetail(String taskId, String insComCode) {
        Map<String, Object> re = new HashMap<>();
        INSBOrderdelivery insbOrderdelivery = new INSBOrderdelivery();
        insbOrderdelivery.setTaskid(taskId);
        insbOrderdelivery.setProviderid(insComCode);
        insbOrderdelivery = insbOrderdeliveryDao.selectOne(insbOrderdelivery);

        if (insbOrderdelivery == null) {
            return new HashMap<>();
        }

        Map<String, Object> orderDelivery = BeanUtils.toMap(insbOrderdelivery);
        getAddressName(orderDelivery, insbOrderdelivery.getRecipientprovince(), "recipientProvinceName");
        getAddressName(orderDelivery, insbOrderdelivery.getRecipientcity(), "recipientCityName");
        getAddressName(orderDelivery, insbOrderdelivery.getRecipientarea(), "recipientAreaName");

        Object deliveryType = orderDelivery.get("delivetype");
        if (deliveryType != null) {
            Map<String, String> codeToName = inscCodeDao.getCodeNamesMap("deliveType", "deliveType");
//            orderDelivery.put("deliveryType", DeliveryType.getDeliveryTypeByCode(deliveryType.toString()).name);
            orderDelivery.put("deliveryType", codeToName == null ? "" : codeToName.get(deliveryType.toString()));
            re.put("orderDelivery", orderDelivery);
        }

        getDepartment(re, insbOrderdelivery);
        getLogisticsCompany(re, insbOrderdelivery);
        return re;
    }

    private void getAddressName(Map<String, Object> orderDelivery, String address, String key) {
        if (!StringUtils.isBlank(address) && Pattern.matches(NUMBER_REGEX, address)) {
            INSBRegion insbRegion = insbRegionDao.selectById(address);
            orderDelivery.put(key, insbRegion == null ? null : insbRegion.getComcodename());
        } else {
            orderDelivery.put(key, address);
        }
    }

    private void getLogisticsCompany(Map<String, Object> re, INSBOrderdelivery insbOrderdelivery) {
        if (!StringUtils.isBlank(insbOrderdelivery.getLogisticscompany())) {
            INSCCode inscCode = new INSCCode();
            inscCode.setCodetype("logisticscompany");
            inscCode.setCodevalue(insbOrderdelivery.getLogisticscompany());
            inscCode = inscCodeDao.selectOne(inscCode);
            re.put("logisticsCompany", inscCode);
        }
    }

    private void getDepartment(Map<String, Object> re, INSBOrderdelivery insbOrderdelivery) {
        String deptcode = insbOrderdelivery.getDeptcode();
        if (!StringUtils.isBlank(deptcode)) {
            INSCDept inscDept = new INSCDept();
            inscDept.setComcode(deptcode);
            inscDept = inscDeptDao.selectOne(inscDept);
            re.put("department", inscDept);
        }
    }
}