package com.zzb.cm.service.impl;

import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.common.ModelUtil;
import com.zzb.cm.controller.vo.RenewalItemVO;
import com.zzb.cm.dao.*;
import com.zzb.cm.entity.*;
import com.zzb.cm.service.INSBCommonQuoteinfoService;
import com.zzb.cm.service.INSBRenewalService;
import com.zzb.cm.service.INSBRenewalquoteitemService;
import com.zzb.conf.dao.INSBPolicyitemDao;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.conf.entity.INSBRenewalconfigitem;
import com.zzb.conf.entity.INSBRenewalitem;
import com.zzb.conf.service.INSBRenewalconfigitemService;
import com.zzb.conf.service.INSBRenewalitemService;
import com.zzb.mobile.model.RenewaltemSaveModel;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Dai on 2016/7/2.
 */
@Service
public class INSBRenewalServiceImpl implements INSBRenewalService {

    @Resource
    private INSBCommonQuoteinfoService commonQuoteinfoService;
    @Resource
    private INSBRenewalquoteitemService renewalquoteitemService;
    @Resource
    private INSBRenewalconfigitemService renewalconfigitemService;
    @Resource
    private INSBRenewalitemService renewalitemService;
    @Resource
    private INSBCarinfoDao carinfoDao;
    @Resource
    private INSBCarinfohisDao carinfohisDao;
    @Resource
    private INSBRulequerycarinfoDao insbRulequerycarinfoDao;
    @Resource
    private INSBInsuredDao insuredDao;
    @Resource
    private INSBInsuredhisDao insuredhisDao;
    @Resource
    private INSBPersonDao personDao;
    @Resource
    private INSBCarowneinfoDao carowneinfoDao;
    @Resource
    private INSBQuoteinfoDao quoteinfoDao;
    @Resource
    private INSBPolicyitemDao policyitemDao;
    @Resource
    private INSBRulequeryrepeatinsuredDao insbRulequeryrepeatinsuredDao;

    public List<INSBRenewalquoteitem> getRenewalQuoteItems(String taskid, String inscomcode) {
        INSBRenewalquoteitem renewalquoteitem = new INSBRenewalquoteitem();
        renewalquoteitem.setTaskid(taskid);
        renewalquoteitem.setInscomcode(inscomcode);
        return renewalquoteitemService.queryList(renewalquoteitem);
    }

    public boolean saveRenewalQuoteitems(RenewaltemSaveModel model) {
        if (model == null) return false;
        if (StringUtil.isEmpty(model.getProcessinstanceid())) {
            LogUtil.error("任务号为空");
            return false;
        }
        if (StringUtil.isEmpty(model.getInscomcode())) {
            LogUtil.error(model.getProcessinstanceid() + "供应商编码为空");
            return false;
        }
        if (model.getRenewalitems() == null || model.getRenewalitems().isEmpty()) {
            LogUtil.error(model.getProcessinstanceid() + "," + model.getInscomcode() + "提交数据项为空");
            return false;
        }

        List<INSBRenewalquoteitem> quoteitemList = new ArrayList<>();

        for (String renewalQuoteItem : model.getRenewalitems()) {
            if (StringUtil.isEmpty(renewalQuoteItem)) continue;

            String[] itemData = renewalQuoteItem.split("\\|\\|\\|");
            if (itemData == null || itemData.length < 3) {
                LogUtil.error(model.getProcessinstanceid() + "," + model.getInscomcode() + "提交数据项(" + renewalQuoteItem + ")格式不正确");
                continue;
            }

            INSBRenewalquoteitem quoteitem = new INSBRenewalquoteitem();
            quoteitem.setOperator("admin");
            quoteitem.setCreatetime(new Date());
            quoteitem.setNoti(itemData[1]);
            quoteitem.setTaskid(model.getProcessinstanceid());
            quoteitem.setInscomcode(model.getInscomcode());
            quoteitem.setItemcode(itemData[0]);
            quoteitem.setItemname(itemData[1]);

            String value = itemData[2];

            try {
                if ("carVin".equals(quoteitem.getItemcode())) {
                    if (value != null && value.contains("*")) {
                        INSBCarinfohis carinfohis = commonQuoteinfoService.getCarInfo(model.getProcessinstanceid(), model.getInscomcode());
                        if (carinfohis != null) {
                            value = carinfohis.getVincode();
                        }
                    }
                } else if ("carEngineNum".equals(quoteitem.getItemcode())) {
                    if (value != null && value.contains("*")) {
                        INSBCarinfohis carinfohis = commonQuoteinfoService.getCarInfo(model.getProcessinstanceid(), model.getInscomcode());
                        if (carinfohis != null) {
                            value = carinfohis.getEngineno();
                        }
                    }
                }
            } catch (Exception e) {
                LogUtil.error("查询"+model.getProcessinstanceid()+","+model.getInscomcode()+"续保数据项"+
                        quoteitem.getItemcode()+"的值时出错："+e.getMessage());
                e.printStackTrace();
            }

            quoteitem.setItemvalue(value);
            quoteitemList.add(quoteitem);
        }

        if (!quoteitemList.isEmpty()) {
            renewalquoteitemService.deleteByTask(model.getProcessinstanceid(), model.getInscomcode());
            int result = renewalquoteitemService.save(quoteitemList);

            saveItemsToInstance(quoteitemList, model.getProcessinstanceid(), model.getInscomcode());

            if (result > 0) return true;
        }

        return false;
    }

    private void saveItemsToInstance(List<INSBRenewalquoteitem> quoteitemList, String taskid, String inscomcode) {
        for (INSBRenewalquoteitem item : quoteitemList) {
            try {
                switch (item.getItemcode()) {
                    case "carLicense":
                        INSBCarinfo carinfo = new INSBCarinfo();
                        carinfo.setTaskid(taskid);
                        carinfo = carinfoDao.selectOne(carinfo);
                        if (carinfo != null) {
                            carinfo.setCarlicenseno(item.getItemvalue());
                            carinfoDao.updateById(carinfo);
                        }
                        INSBCarinfohis carinfohis = new INSBCarinfohis();
                        carinfohis.setTaskid(taskid);
                        carinfohis.setInscomcode(inscomcode);
                        carinfohis = carinfohisDao.selectOne(carinfohis);
                        if (carinfohis != null) {
                            carinfohis.setCarlicenseno(item.getItemvalue());
                            carinfohisDao.updateById(carinfohis);
                        }
                        Map<String, String> params = new HashMap<>(2);
                        params.put("taskid", taskid);
                        params.put("companyid", inscomcode);
                        INSBQuoteinfo quoteinfo = quoteinfoDao.getByTaskidAndCompanyid(params);
                        if (quoteinfo != null) {
                            quoteinfo.setPlatenumber(item.getItemvalue());
                            quoteinfoDao.updateById(quoteinfo);
                        }
                        break;

                    case "carVin":
                        carinfo = new INSBCarinfo();
                        carinfo.setTaskid(taskid);
                        carinfo = carinfoDao.selectOne(carinfo);
                        if (carinfo != null) {
                            carinfo.setVincode(item.getItemvalue());
                            carinfoDao.updateById(carinfo);
                        }
                        carinfohis = new INSBCarinfohis();
                        carinfohis.setTaskid(taskid);
                        carinfohis.setInscomcode(inscomcode);
                        carinfohis = carinfohisDao.selectOne(carinfohis);
                        if (carinfohis != null) {
                            carinfohis.setVincode(item.getItemvalue());
                            carinfohisDao.updateById(carinfohis);
                        }
                        break;

                    case "carEngineNum":
                        carinfo = new INSBCarinfo();
                        carinfo.setTaskid(taskid);
                        carinfo = carinfoDao.selectOne(carinfo);
                        if (carinfo != null) {
                            carinfo.setEngineno(item.getItemvalue());
                            carinfoDao.updateById(carinfo);
                        }
                        carinfohis = new INSBCarinfohis();
                        carinfohis.setTaskid(taskid);
                        carinfohis.setInscomcode(inscomcode);
                        carinfohis = carinfohisDao.selectOne(carinfohis);
                        if (carinfohis != null) {
                            carinfohis.setEngineno(item.getItemvalue());
                            carinfohisDao.updateById(carinfohis);
                        }
                        break;

                    case "carLicenseType":
                        carinfo = new INSBCarinfo();
                        carinfo.setTaskid(taskid);
                        carinfo = carinfoDao.selectOne(carinfo);
                        if (carinfo != null) {
                            carinfo.setPlateType(Integer.parseInt(item.getItemvalue()));
                            carinfoDao.updateById(carinfo);
                        }
                        carinfohis = new INSBCarinfohis();
                        carinfohis.setTaskid(taskid);
                        carinfohis.setInscomcode(inscomcode);
                        carinfohis = carinfohisDao.selectOne(carinfohis);
                        if (carinfohis != null) {
                            carinfohis.setPlateType(Integer.parseInt(item.getItemvalue()));
                            carinfohisDao.updateById(carinfohis);
                        }
                        break;

                    case "carFirstRegDate":
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        carinfo = new INSBCarinfo();
                        carinfo.setTaskid(taskid);
                        carinfo = carinfoDao.selectOne(carinfo);
                        if (carinfo != null) {
                            carinfo.setRegistdate(dateFormat.parse(item.getItemvalue()));
                            carinfoDao.updateById(carinfo);
                        }
                        carinfohis = new INSBCarinfohis();
                        carinfohis.setTaskid(taskid);
                        carinfohis.setInscomcode(inscomcode);
                        carinfohis = carinfohisDao.selectOne(carinfohis);
                        if (carinfohis != null) {
                            carinfohis.setRegistdate(dateFormat.parse(item.getItemvalue()));
                            carinfohisDao.updateById(carinfohis);
                        }
                        break;

                    case "carOwner":
                        INSBCarowneinfo carowneinfo = new INSBCarowneinfo();
                        carowneinfo.setTaskid(taskid);
                        carowneinfo = carowneinfoDao.selectOne(carowneinfo);
                        if (carowneinfo != null) {
                            INSBPerson carperson = personDao.selectById(carowneinfo.getPersonid());
                            if (carperson != null) {
                                carperson.setName(item.getItemvalue());
                                personDao.updateById(carperson);
                            }
                        }
                        params = new HashMap<>(2);
                        params.put("taskid", taskid);
                        params.put("companyid", inscomcode);
                        quoteinfo = quoteinfoDao.getByTaskidAndCompanyid(params);
                        if (quoteinfo != null) {
                            quoteinfo.setOwnername(item.getItemvalue());
                            quoteinfoDao.updateById(quoteinfo);
                        }
                        break;

                    case "carOwnerIdType":
                        carowneinfo = new INSBCarowneinfo();
                        carowneinfo.setTaskid(taskid);
                        carowneinfo = carowneinfoDao.selectOne(carowneinfo);
                        if (carowneinfo != null) {
                            INSBPerson carperson = personDao.selectById(carowneinfo.getPersonid());
                            if (carperson != null) {
                                carperson.setIdcardtype(Integer.parseInt(item.getItemvalue()));
                                personDao.updateById(carperson);
                            }
                        }
                        break;

                    case "carOwnerIdNum":
                        carowneinfo = new INSBCarowneinfo();
                        carowneinfo.setTaskid(taskid);
                        carowneinfo = carowneinfoDao.selectOne(carowneinfo);
                        if (carowneinfo != null) {
                            INSBPerson carperson = personDao.selectById(carowneinfo.getPersonid());
                            if (carperson != null) {
                                carperson.setIdcardno(item.getItemvalue());
                                personDao.updateById(carperson);
                            }
                        }
                        break;

                    case "insuredName":
                        INSBInsured insured = new INSBInsured();
                        insured.setTaskid(taskid);
                        insured = insuredDao.selectOne(insured);
                        if (insured != null) {
                            INSBPerson person = personDao.selectById(insured.getPersonid());
                            if (person != null) {
                                person.setName(item.getItemvalue());
                                personDao.updateById(person);
                            }
                        } else {
                            saveInsuredInfo(taskid, inscomcode, item.getItemvalue(), null);
                        }
                        INSBInsuredhis insuredhis = new INSBInsuredhis();
                        insuredhis.setTaskid(taskid);
                        insuredhis.setInscomcode(inscomcode);
                        insuredhis = insuredhisDao.selectOne(insuredhis);
                        if (insuredhis != null) {
                            INSBPerson person = personDao.selectById(insuredhis.getPersonid());
                            if (person != null) {
                                person.setName(item.getItemvalue());
                                personDao.updateById(person);
                            }
                        }
                        List<INSBPolicyitem> policyitemList = policyitemDao.selectPolicyitemList(taskid);
                        if (policyitemList != null && !policyitemList.isEmpty()) {
                            for (INSBPolicyitem policyitem : policyitemList) {
                                policyitem.setInsuredname(item.getItemvalue());
                                policyitemDao.updateById(policyitem);
                            }
                        }
                        break;

                    case "insuredIdNum":
                        insured = new INSBInsured();
                        insured.setTaskid(taskid);
                        insured = insuredDao.selectOne(insured);
                        if (insured != null) {
                            INSBPerson person = personDao.selectById(insured.getPersonid());
                            if (person != null) {
                                person.setIdcardno(item.getItemvalue());
                                personDao.updateById(person);
                            }
                        } else {
                            saveInsuredInfo(taskid, inscomcode, null, item.getItemvalue());
                        }
                        insuredhis = new INSBInsuredhis();
                        insuredhis.setTaskid(taskid);
                        insuredhis.setInscomcode(inscomcode);
                        insuredhis = insuredhisDao.selectOne(insuredhis);
                        if (insuredhis != null) {
                            INSBPerson person = personDao.selectById(insuredhis.getPersonid());
                            if (person != null) {
                                person.setIdcardno(item.getItemvalue());
                                personDao.updateById(person);
                            }
                        }
                        break;

                    case "lastCommercialPoliceyNum":
                        INSBRulequeryrepeatinsured insbRulequeryrepeatinsured = new INSBRulequeryrepeatinsured();
                        insbRulequeryrepeatinsured.setTaskid(taskid);
                        insbRulequeryrepeatinsured.setRisktype("0");//商业险
                        INSBRulequeryrepeatinsured lastyearinsureinfo = insbRulequeryrepeatinsuredDao.selectOne(insbRulequeryrepeatinsured);
                        if (lastyearinsureinfo == null) {
                            insbRulequeryrepeatinsured.setCreatetime(new Date());
                            insbRulequeryrepeatinsured.setOperator("front");
                            insbRulequeryrepeatinsured.setPolicyid(item.getItemvalue());
                            insbRulequeryrepeatinsuredDao.insert(insbRulequeryrepeatinsured);
                        } else {
                            lastyearinsureinfo.setModifytime(new Date());
                            lastyearinsureinfo.setPolicyid(item.getItemvalue());
                            insbRulequeryrepeatinsuredDao.updateById(lastyearinsureinfo);
                        }
                        break;
                }
            } catch (Exception e) {
                LogUtil.error("保存"+taskid+","+inscomcode+"续保数据项"+item.getItemcode()+"（"+item.getItemvalue()+"）"+"到实体时出错："+e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void saveInsuredInfo(String taskid, String inscomcode, String name, String idCardno) {
        INSBPerson bbperson = new INSBPerson();
        bbperson.setCreatetime(new Date());
        bbperson.setOperator("admin");
        bbperson.setTaskid(taskid);
        if(StringUtil.isNotEmpty(idCardno)){
            bbperson.setGender(ModelUtil.getGenderByIdCard(idCardno));
            bbperson.setIdcardtype(0);
            bbperson.setIdcardno(idCardno);
        }else{
            bbperson.setGender(1); // 1-女,0-男
        }
        bbperson.setName(name);
        personDao.insert(bbperson);

        INSBInsured insbInsured = new INSBInsured();
        insbInsured.setCreatetime(new Date());
        insbInsured.setOperator("admin");
        insbInsured.setTaskid(taskid);
        insbInsured.setPersonid(bbperson.getId());

        insuredDao.insert(insbInsured);

        //his表
        INSBInsuredhis insuredhis = new INSBInsuredhis();
        insuredhis.setTaskid(taskid);
        insuredhis.setInscomcode(inscomcode);
        insuredhis = insuredhisDao.selectOne(insuredhis);

        if (insuredhis == null) {
            INSBPerson bbpersonhis = new INSBPerson();
            try {
                PropertyUtils.copyProperties(bbpersonhis, bbperson);
                bbpersonhis.setId(null);
                personDao.insert(bbpersonhis);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                LogUtil.error("taskid=" + taskid + ",inscomcode=" + inscomcode + ",保存被保人his人信息出错: " + e.getMessage());
                e.printStackTrace();
            }

            try {
                insuredhis = new INSBInsuredhis();
                PropertyUtils.copyProperties(insuredhis, insbInsured);
                insuredhis.setId(null);
                insuredhis.setInscomcode(inscomcode);
                if (bbpersonhis.getId() != null) {
                    insuredhis.setPersonid(bbpersonhis.getId());
                }
                insuredhisDao.insert(insuredhis);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                LogUtil.error("taskid=" + taskid + ",inscomcode=" + inscomcode + ",保存被保人his信息出错: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public List<RenewalItemVO> getRenewalConfigItems(String agreementid, String taskid, String inscomcode) {
        INSBRenewalconfigitem renewalconfigitem = new INSBRenewalconfigitem();
        renewalconfigitem.setAgreementid(agreementid);
        List<INSBRenewalconfigitem> renewalconfigitemList = renewalconfigitemService.queryList(renewalconfigitem);

        List<RenewalItemVO> result = mergeItem(renewalconfigitemList);

        for (RenewalItemVO itemVO : result) {
            itemVO.setAgreementid(agreementid);
            itemVO.setTaskid(taskid);
            itemVO.setInscomcode(inscomcode);

            try {
                switch (itemVO.getItemcode()) {
                    case "carLicense":
                        INSBCarinfohis carinfo = commonQuoteinfoService.getCarInfo(taskid, inscomcode);
                        if (carinfo != null) {
                            itemVO.setItemvalue(carinfo.getCarlicenseno());
                        }
                        break;

                    case "carVin":
                        carinfo = commonQuoteinfoService.getCarInfo(taskid, inscomcode);
                        if (carinfo != null && carinfo.getVincode() != null) {
                            itemVO.setItemvalue(ModelUtil.hiddenVin(carinfo.getVincode()));
                        }
                        break;

                    case "carEngineNum":
                        carinfo = commonQuoteinfoService.getCarInfo(taskid, inscomcode);
                        if (carinfo != null && carinfo.getEngineno() != null) {
                            itemVO.setItemvalue(ModelUtil.hiddenEngineNo(carinfo.getEngineno()));
                        }
                        break;

                    case "carLicenseType":
                        carinfo = commonQuoteinfoService.getCarInfo(taskid, inscomcode);
                        if (carinfo != null && carinfo.getPlateType() != null) {
                            itemVO.setItemvalue(carinfo.getPlateType().toString());
                        }
                        break;

                    case "carFirstRegDate":
                        carinfo = commonQuoteinfoService.getCarInfo(taskid, inscomcode);
                        if (carinfo != null && carinfo.getRegistdate() != null) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            itemVO.setItemvalue(dateFormat.format(carinfo.getRegistdate()));
                        }
                        break;

                    case "carOwner":
                        INSBCarowneinfo carowneinfo = new INSBCarowneinfo();
                        carowneinfo.setTaskid(taskid);
                        carowneinfo = carowneinfoDao.selectOne(carowneinfo);
                        if (carowneinfo != null) {
                            INSBPerson carperson = personDao.selectById(carowneinfo.getPersonid());
                            if (carperson != null) {
                                itemVO.setItemvalue(carperson.getName());
                            }
                        }
                        break;

                    case "carOwnerIdType":
                        carowneinfo = new INSBCarowneinfo();
                        carowneinfo.setTaskid(taskid);
                        carowneinfo = carowneinfoDao.selectOne(carowneinfo);
                        if (carowneinfo != null) {
                            INSBPerson carperson = personDao.selectById(carowneinfo.getPersonid());
                            if (carperson != null && carperson.getIdcardtype() != null) {
                                itemVO.setItemvalue(carperson.getIdcardtype().toString());
                            }
                        }
                        break;

                    case "carOwnerIdNum":
                        carowneinfo = new INSBCarowneinfo();
                        carowneinfo.setTaskid(taskid);
                        carowneinfo = carowneinfoDao.selectOne(carowneinfo);
                        if (carowneinfo != null) {
                            INSBPerson carperson = personDao.selectById(carowneinfo.getPersonid());
                            if (carperson != null) {
                                itemVO.setItemvalue(carperson.getIdcardno());
                            }
                        }
                        break;

                    case "insuredName":
                        INSBInsuredhis insured = commonQuoteinfoService.getInsuredinfo(taskid, inscomcode);
                        if (insured != null) {
                            INSBPerson person = personDao.selectById(insured.getPersonid());
                            if (person != null) {
                                itemVO.setItemvalue(person.getName());
                            }
                        }
                        break;

                    case "insuredIdNum":
                        insured = commonQuoteinfoService.getInsuredinfo(taskid, inscomcode);
                        if (insured != null) {
                            INSBPerson person = personDao.selectById(insured.getPersonid());
                            if (person != null) {
                                itemVO.setItemvalue(person.getIdcardno());
                            }
                        }
                        break;

                    case "lastCommercialPoliceyNum":
                        INSBLastyearinsureinfo lastyearinsureinfo = insbRulequerycarinfoDao.queryLastYearClainInfo(taskid);
                        if (lastyearinsureinfo != null) {
                            itemVO.setItemvalue(lastyearinsureinfo.getSypolicyno());
                        }
                        break;
                }
            } catch (Exception e) {
                LogUtil.error("获取"+agreementid+","+taskid+","+inscomcode+"续保数据项"+itemVO.getItemcode()+"的值时出错："+e.getMessage());
                e.printStackTrace();
            }
        }

        return result;
    }

    private List<RenewalItemVO> mergeItem(List<INSBRenewalconfigitem> renewalconfigitemList) {
        List<RenewalItemVO> itemVOs = new ArrayList<>();

        if (renewalconfigitemList == null || renewalconfigitemList.isEmpty()) return itemVOs;

        Set<String> codes = new HashSet<>();

        for (INSBRenewalconfigitem configItem : renewalconfigitemList) {
            if (StringUtil.isNotEmpty(configItem.getItemcode())) {
                codes.add(configItem.getItemcode());

                RenewalItemVO itemVO = new RenewalItemVO();
                itemVO.setItemcode(configItem.getItemcode());
                itemVO.setItemname(configItem.getItemname());
                itemVO.setItemorder(configItem.getItemorder());
                itemVO.setIsrequired(configItem.getIsrequired());
                itemVOs.add(itemVO);
            }
        }

        List<String> configCodes = new ArrayList<>(codes.size());
        configCodes.addAll(codes);
        List<INSBRenewalitem> selectedItems = renewalitemService.selectAllByCodes(configCodes);

        for (RenewalItemVO itemVO : itemVOs) {
            for (INSBRenewalitem renewalitem : selectedItems) {
                if (itemVO.getItemcode().equals(renewalitem.getItemcode())) {
                    itemVO.setItemname(renewalitem.getItemname());
                    itemVO.setIteminputtype(renewalitem.getIteminputtype());
                    itemVO.setItemvaluelist(renewalitem.getItemvaluelist());
                }
            }
        }

        return itemVOs;
    }

}
