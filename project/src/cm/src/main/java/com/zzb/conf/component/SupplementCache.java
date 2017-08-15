package com.zzb.conf.component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.common.HttpSender;
import com.common.InsCompany;
import com.common.redis.CMRedisClient;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.cm.entity.INSBSupplement;
import com.zzb.cm.service.INSBCarinfoService;
import com.zzb.cm.service.INSBQuoteinfoService;
import com.zzb.cm.service.INSBQuotetotalinfoService;
import com.zzb.cm.service.INSBSupplementService;
import com.zzb.conf.entity.INSBAgreement;
import com.zzb.conf.entity.INSBWorkflowsub;
import com.zzb.conf.service.INSBAgreementService;
import com.zzb.conf.service.INSBWorkflowsubService;

import net.sf.json.JSONObject;
/**
 * 试算规则补充项引擎缓存管理器
 * @author askqvn
 * @author Dai
 * @since 16-7-25
 * @version 1.0
 */
@Service
public class SupplementCache {
	
	@Resource
	private INSBAgreementService insbagreementservice;
	@Resource
	private INSBQuotetotalinfoService insbQuotetotalinfoService;
	@Resource
	private INSBSupplementService insbSupplementService;
	@Resource
	private INSBCarinfoService insbCarinfoService;
	@Resource
	INSBWorkflowsubService insbWorkflowsubService;
	@Resource
	INSBQuoteinfoService insbQuoteinfoService;
	@Resource
	INSBAgreementService insbAgreementService;
	
    /**
     * 以补充数据项命名空间为key缓存所有的不确定的补充数据对应信息
     */
    public Map<String, SupplementModel> allSupplementMap = new HashMap<String, SupplementModel>();
    private Map<String, SupplementModel> allSupplementMapHotSwap;

    /**
     * 以各个供应商id为key保存对应的补充数据项列表
     */
    public Map<String, List<SupplementModel>> providerComplementMap = new HashMap<String, List<SupplementModel>>();
    private Map<String, List<SupplementModel>> providerComplementMapHotSwap;
    /**
     * 以各个供应商id为key保存对应的供应商协议规则信息
     */
    public Map<String, INSBAgreement> providerInfoMap = new HashMap<String, INSBAgreement>();
    private Map<String, INSBAgreement> providerInfoMapHotSwap;
    public static final Map<String, String> RULE_VALUE_MAP = new HashMap<String, String>();
    static {
    	RULE_VALUE_MAP.put("carBrandName","carbrandname");//    OK		品牌名称： carBrandName
    	RULE_VALUE_MAP.put("price","price");//    OK		新车购置价： price
	    RULE_VALUE_MAP.put("taxPrice","taxPrice");//    OK		新车购置价(含税)： taxPrice
	    RULE_VALUE_MAP.put("analogyPrice","analogyprice");//    OK		类比价： analogyPrice
	    RULE_VALUE_MAP.put("analogyTaxPrice","analogytaxprice");//    OK		类比价(含税)： analogyTaxPrice
	    RULE_VALUE_MAP.put("carModelDate","carmodeldate");//    OK		上市年份： carModelDate
	    RULE_VALUE_MAP.put("seatCnt","seatcnt");//    OK		核定载客： seatCnt
	    RULE_VALUE_MAP.put("modelLoad","modelload");//    OK		核定载质量： modelLoad
	    RULE_VALUE_MAP.put("fullload","fullload");//    OK		整备质量： fullLoad
	    
	    RULE_VALUE_MAP.put("ruleItem.fullLoadCalculationType","fullload1");//    OK	自重计算标准
	    
	    RULE_VALUE_MAP.put("displacement","displacement");//    OK		排气量： displacement
	    RULE_VALUE_MAP.put("tradeModelCode","trademodelcode");//    OK		行业车型编码：vehicleInfo.tradeModelCode
	    
	    RULE_VALUE_MAP.put("car.specific.selfInsureRate","selfinsurerate");//    OK		自主核保系数：car.specific.selfInsureRate
	    RULE_VALUE_MAP.put("car.specific.selfChannelRate","selfchannelrate");//    OK		自主渠道系数：car.specific.selfChannelRate
	    RULE_VALUE_MAP.put("car.specific.NcdRate","ncdrate");//    OK		NCD系数： car.specific.NcdRate
	    RULE_VALUE_MAP.put("car.specific.specialVehicleType","vehicletype");//    		车辆种类：vehicleInfo.vehicleType（待定）
	    RULE_VALUE_MAP.put("ruleItem.basicRiskPremium","basicriskpremium");//    OK		机动车车损纯风险基准保费：quoteItems_ruleItem.basicRiskPremium
	    
	    RULE_VALUE_MAP.put("plateform.InsureCo","insureco");//    OK		查询公司名称：plateform.InsureCo
	    RULE_VALUE_MAP.put("application.saleChannelType","pureesale");// 是否纯电销
	    
	    RULE_VALUE_MAP.put("noClaimDiscountCoefficient","noclaimdiscountcoefficient");//    OK	无赔款优待系数： noClaimDiscountCoefficient
	    RULE_VALUE_MAP.put("noClaimDiscountCoefficientReasons","noclaimdiscountcoefficientreasons");//    OK	无赔款折扣浮动原因:noClaimDiscountCoefficientReasons
	    RULE_VALUE_MAP.put("loyaltyReasons","loyaltyreasons");//    OK	客户忠诚度原因： loyaltyReasons
	    
	    RULE_VALUE_MAP.put("application.trafficOffenceDiscount","trafficoffencediscount");//    OK	交通违法系数:trafficOffenceDiscount
	    
	    RULE_VALUE_MAP.put("compulsoryClaimRate","compulsoryclaimrate");//    OK	交强险理赔系数:compulsoryClaimRate
	    RULE_VALUE_MAP.put("compulsoryClaimRateReasons","compulsoryclaimratereasons");//    OK	交强险浮动原因:compulsoryClaimRateReasons
	    
	    RULE_VALUE_MAP.put("application.firstInsureType","firstinsuretype");//    OK	投保类型:firstInsureType
	    RULE_VALUE_MAP.put("application.commercialClaimTimes","bwcommercialclaimtimes");//    OK	上年商业险理赔次数:bwCommercialClaimTimes
	    RULE_VALUE_MAP.put("application.lastCommercialClaimSum","bwlastclaimsum");//    OK	上年商业险理赔金额:bwLastClaimSum
	    RULE_VALUE_MAP.put("application.compulsoryClaimTimes","bwcompulsoryclaimtimes");//    OK	上年交强险理赔次数:bwCompulsoryClaimTimes
	    RULE_VALUE_MAP.put("application.lastCompulsoryClaimSum","bwlastcompulsoryclaimsum");//    OK	上年交强险理赔金额:bwLastCompulsoryClaimSum
	    
	    RULE_VALUE_MAP.put("claimTimes","claimtimes");//    OK	平台商业险理赔次数： claimTimes
	    RULE_VALUE_MAP.put("compulsoryClaimTimes","compulsoryclaimtimes");//    OK	平台交强险理赔次数： compulsoryClaimTimes
	    RULE_VALUE_MAP.put("lastClaimSum","lastclaimsum");//    OK	平台商业险理赔金额： lastClaimSum
	    RULE_VALUE_MAP.put("bizPolicies","bizpolicies");//    OK	商业险种重复投保记录:bizPolicies(节点) jsonArray
	    RULE_VALUE_MAP.put("insCorpId","inscorpId");//    		保险公司ID：insCorpId
	    RULE_VALUE_MAP.put("insCorpCode","inscorpCode");//    		保险公司代码：insCorpCode 
	    RULE_VALUE_MAP.put("insCorpName","inscorpname");//    OK		保险公司名称：insCorpName
	    RULE_VALUE_MAP.put("policyStartTime","policystarttime");//    OK		起保时间：policyStartTime
	    RULE_VALUE_MAP.put("policyEndTime","policyendtime");//    OK		终保时间：policyEndTime
	    RULE_VALUE_MAP.put("policyId","policyid");//    OK		保单号：policyId
	    RULE_VALUE_MAP.put("bizClaims","bizclaims");//    OK	商业险理赔列表:bizClaims(节点)jsonArray
	    RULE_VALUE_MAP.put("application.lastInsureCo","inscorpid0");//    		保险公司ID：insCorpId
	    RULE_VALUE_MAP.put("insCorpCode","inscorpCode");//    		保险公司代码：insCorpCode 
	    RULE_VALUE_MAP.put("insCorpName","inscorpName");//    OK		保险公司名称：insCorpName
	    RULE_VALUE_MAP.put("caseStartTime","casectarttime");//    OK		出险时间：caseStartTime
	    RULE_VALUE_MAP.put("caseEndTime","caseendtime");//    OK		结案时间：caseEndTime
	    RULE_VALUE_MAP.put("claimAmount","claimamount");//    		理赔金额：claimAmount
	    RULE_VALUE_MAP.put("policyId","policyid");//    		保单号：policyId
	    RULE_VALUE_MAP.put("efcPolicies","efcpolicies");//    OK	交强险种重复投保记录:efcPolicies(节点)jsonArray
	    RULE_VALUE_MAP.put("insCorpId","inscorpId");//    		保险公司ID：insCorpId
	    RULE_VALUE_MAP.put("insCorpCode","inscorpCode");//    		保险公司代码：insCorpCode 
	    RULE_VALUE_MAP.put("insCorpName","inscorpName");//    OK		保险公司名称：insCorpName
	    RULE_VALUE_MAP.put("policyStartTime","policystarttime");//    OK		起保时间：policyStartTime
	    RULE_VALUE_MAP.put("policyEndTime","policyendtime");//    OK		终保时间：policyEndTime
	    RULE_VALUE_MAP.put("policyId","policyid");//    OK		保单号：policyId
	    RULE_VALUE_MAP.put("efcClaims","efcclaims");//    OK	交强险理赔列表:efcClaims(节点)jsonArray
	    RULE_VALUE_MAP.put("insCorpId","inscorpid");//    		保险公司ID：insCorpId
	    RULE_VALUE_MAP.put("insCorpCode","inscorpcode");//    		保险公司代码：insCorpCode 
	    RULE_VALUE_MAP.put("insCorpName","inscorpname");//    OK		保险公司名称：insCorpName
	    RULE_VALUE_MAP.put("caseStartTime","casestarttime");//    OK		出险时间：caseStartTime
	    RULE_VALUE_MAP.put("caseEndTime","caseendtime");//    OK		结案时间：caseEndTime
	    RULE_VALUE_MAP.put("claimAmount","claimamount");//    		理赔金额：claimAmount
	    RULE_VALUE_MAP.put("policyId","policyid");//    		保单号：policyId
	    RULE_VALUE_MAP.put("SYendMark","syendmark");//    	商业险起保标识： SYendMark
	    RULE_VALUE_MAP.put("errorMsgSY","errornsgsy");//    	商业险起保提示语： errorMsgSY
	    RULE_VALUE_MAP.put("JQendMark","jqendmark");//    	交强险起保标识： JQendMark
	    RULE_VALUE_MAP.put("errorMsgJQ","errormsgjq");//    	交强险起保提示语： errorMsgJQ
	    RULE_VALUE_MAP.put("licenseNo","licenseno");//    	车牌　	： 　 licenseNo
	    RULE_VALUE_MAP.put("engineNo","engineno");//    	发动机	：	engineNo
	    RULE_VALUE_MAP.put("vinNo","vinno");//    	vin   	:		 vinNo
	    RULE_VALUE_MAP.put("enrollDate","enrolldate");//    	初登日期 ： enrollDate
	    RULE_VALUE_MAP.put("succed","succed");//    	成功标识succed: 	success
	    
	    RULE_VALUE_MAP.put("京","北京");
	    RULE_VALUE_MAP.put("津","天津");
	    RULE_VALUE_MAP.put("冀","河北");
	    RULE_VALUE_MAP.put("晋","山西");
	    RULE_VALUE_MAP.put("蒙","内蒙古");
	    RULE_VALUE_MAP.put("辽","辽宁");
	    RULE_VALUE_MAP.put("吉","吉林");
	    RULE_VALUE_MAP.put("黑","黑龙江");
	    RULE_VALUE_MAP.put("沪","上海");
	    RULE_VALUE_MAP.put("申","上海");
	    RULE_VALUE_MAP.put("苏","江苏");
	    RULE_VALUE_MAP.put("浙","浙江");
	    RULE_VALUE_MAP.put("皖","安徽");
	    RULE_VALUE_MAP.put("闽","福建");
	    RULE_VALUE_MAP.put("赣","江西");
	    RULE_VALUE_MAP.put("鲁","山东");
	    RULE_VALUE_MAP.put("豫","河南");
	    RULE_VALUE_MAP.put("鄂","湖北");
	    RULE_VALUE_MAP.put("湘","湖南");
	    RULE_VALUE_MAP.put("粤","广东");
	    RULE_VALUE_MAP.put("桂","广西");
	    RULE_VALUE_MAP.put("琼","海南");
	    RULE_VALUE_MAP.put("渝","重庆");
	    RULE_VALUE_MAP.put("川","四川");
	    RULE_VALUE_MAP.put("蜀","四川");
	    RULE_VALUE_MAP.put("黔","贵州");
	    RULE_VALUE_MAP.put("贵","贵州");
	    RULE_VALUE_MAP.put("云","云南");
	    RULE_VALUE_MAP.put("滇","云南");
	    RULE_VALUE_MAP.put("藏","西藏");
	    RULE_VALUE_MAP.put("陕","陕西");
	    RULE_VALUE_MAP.put("秦","陕西");
	    RULE_VALUE_MAP.put("甘","甘肃");
	    RULE_VALUE_MAP.put("陇","甘肃");
	    RULE_VALUE_MAP.put("青","青海");
	    RULE_VALUE_MAP.put("宁","宁夏");
	    RULE_VALUE_MAP.put("新","新疆");
	    RULE_VALUE_MAP.put("港","香港");
	    RULE_VALUE_MAP.put("澳","澳门");
	    RULE_VALUE_MAP.put("台","台湾");
	    
	    RULE_VALUE_MAP.put("京A" , "北京市");
	    RULE_VALUE_MAP.put("京B" , "北京市");
	    RULE_VALUE_MAP.put("京C" , "北京市");
	    RULE_VALUE_MAP.put("京D" , "北京市");
	    RULE_VALUE_MAP.put("京E" , "北京市");
	    RULE_VALUE_MAP.put("京F" , "北京市");
	    RULE_VALUE_MAP.put("京G" , "北京市");
	    RULE_VALUE_MAP.put("京H" , "北京市");
	    RULE_VALUE_MAP.put("京J" , "北京市");
	    RULE_VALUE_MAP.put("京K" , "北京市");
	    RULE_VALUE_MAP.put("京L" , "北京市");
	    RULE_VALUE_MAP.put("京M" , "北京市");
	    RULE_VALUE_MAP.put("京Y" , "北京市");
	    RULE_VALUE_MAP.put("津A" , "天津市");
	    RULE_VALUE_MAP.put("津B" , "天津市");
	    RULE_VALUE_MAP.put("津C" , "天津市");
	    RULE_VALUE_MAP.put("津D" , "天津市");
	    RULE_VALUE_MAP.put("津E" , "天津市");
	    RULE_VALUE_MAP.put("津F" , "天津市");
	    RULE_VALUE_MAP.put("津G" , "天津市");
	    RULE_VALUE_MAP.put("津H" , "天津市");
	    RULE_VALUE_MAP.put("沪A" , "上海市");
	    RULE_VALUE_MAP.put("沪B" , "上海市");
	    RULE_VALUE_MAP.put("沪C" , "上海市");
	    RULE_VALUE_MAP.put("沪D" , "上海市");
	    RULE_VALUE_MAP.put("沪R" , "上海市");
	    RULE_VALUE_MAP.put("宁A" , "银川市");
	    RULE_VALUE_MAP.put("宁B" , "石嘴山市");
	    RULE_VALUE_MAP.put("宁C" , "银南市");
	    RULE_VALUE_MAP.put("宁D" , "固原市");
	    RULE_VALUE_MAP.put("宁E" , "中卫市");
	    RULE_VALUE_MAP.put("渝A" , "重庆市");
	    RULE_VALUE_MAP.put("渝B" , "重庆市");
	    RULE_VALUE_MAP.put("渝C" , "永川区");
	    RULE_VALUE_MAP.put("渝F" , "万州区");
	    RULE_VALUE_MAP.put("渝G" , "涪陵区");
	    RULE_VALUE_MAP.put("渝H" , "黔江区");
	    RULE_VALUE_MAP.put("琼A" , "海口市");
	    RULE_VALUE_MAP.put("琼B" , "三亚市");
	    RULE_VALUE_MAP.put("琼C" , "琼海市");
	    RULE_VALUE_MAP.put("琼D" , "五指山市");
	    RULE_VALUE_MAP.put("琼E" , "洋浦开发区");
	    RULE_VALUE_MAP.put("藏A" , "拉萨市");
	    RULE_VALUE_MAP.put("藏B" , "昌都地区");
	    RULE_VALUE_MAP.put("藏C" , "山南地区");
	    RULE_VALUE_MAP.put("藏D" , "日喀则地区");
	    RULE_VALUE_MAP.put("藏E" , "那曲地区");
	    RULE_VALUE_MAP.put("藏F" , "阿里地区");
	    RULE_VALUE_MAP.put("藏G" , "林芝地区");
	    RULE_VALUE_MAP.put("藏H" , "驻四川省天全县车辆管理所");
	    RULE_VALUE_MAP.put("藏J" , "驻青海省格尔木市车辆管理所");
	    RULE_VALUE_MAP.put("青A" , "西宁市");
	    RULE_VALUE_MAP.put("青B" , "海东市");
	    RULE_VALUE_MAP.put("青C" , "海北藏族自治州");
	    RULE_VALUE_MAP.put("青D" , "黄南藏族自治州");
	    RULE_VALUE_MAP.put("青E" , "海南藏族自治州");
	    RULE_VALUE_MAP.put("青F" , "果洛藏族自治州");
	    RULE_VALUE_MAP.put("青G" , "玉树藏族自治州");
	    RULE_VALUE_MAP.put("青H" , "海西蒙古族藏族自治州");
	    RULE_VALUE_MAP.put("川A" , "成都市");
	    RULE_VALUE_MAP.put("川B" , "绵阳市");
	    RULE_VALUE_MAP.put("川C" , "自贡市");
	    RULE_VALUE_MAP.put("川D" , "攀枝花市");
	    RULE_VALUE_MAP.put("川E" , "泸州市");
	    RULE_VALUE_MAP.put("川F" , "德阳市");
	    RULE_VALUE_MAP.put("川H" , "广元市");
	    RULE_VALUE_MAP.put("川J" , "遂宁市");
	    RULE_VALUE_MAP.put("川K" , "内江市");
	    RULE_VALUE_MAP.put("川L" , "乐山市");
	    RULE_VALUE_MAP.put("川M" , "资阳市");
	    RULE_VALUE_MAP.put("川N" , "");
	    RULE_VALUE_MAP.put("川P" , "");
	    RULE_VALUE_MAP.put("川Q" , "宜宾市");
	    RULE_VALUE_MAP.put("川R" , "南充市");
	    RULE_VALUE_MAP.put("川S" , "达州市");
	    RULE_VALUE_MAP.put("川T" , "雅安市");
	    RULE_VALUE_MAP.put("川U" , "阿坝藏族羌族自治州");
	    RULE_VALUE_MAP.put("川V" , "甘孜藏族自治州");
	    RULE_VALUE_MAP.put("川W" , "凉山彝族自治州");
	    RULE_VALUE_MAP.put("川X" , "广安市");
	    RULE_VALUE_MAP.put("川Y" , "巴中市");
	    RULE_VALUE_MAP.put("川Z" , "眉山市");
	    RULE_VALUE_MAP.put("粤A" , "广州市");
	    RULE_VALUE_MAP.put("粤B" , "深圳市");
	    RULE_VALUE_MAP.put("粤C" , "珠海市");
	    RULE_VALUE_MAP.put("粤D" , "汕头市");
	    RULE_VALUE_MAP.put("粤E" , "佛山市");
	    RULE_VALUE_MAP.put("粤F" , "韶关市");
	    RULE_VALUE_MAP.put("粤G" , "湛江市");
	    RULE_VALUE_MAP.put("粤H" , "肇庆市");
	    RULE_VALUE_MAP.put("粤J" , "江门市");
	    RULE_VALUE_MAP.put("粤K" , "茂名市");
	    RULE_VALUE_MAP.put("粤L" , "惠州市");
	    RULE_VALUE_MAP.put("粤M" , "梅州市");
	    RULE_VALUE_MAP.put("粤N" , "汕尾市");
	    RULE_VALUE_MAP.put("粤P" , "河源市");
	    RULE_VALUE_MAP.put("粤Q" , "阳江市");
	    RULE_VALUE_MAP.put("粤R" , "清远市");
	    RULE_VALUE_MAP.put("粤S" , "东莞市");
	    RULE_VALUE_MAP.put("粤T" , "中山市");
	    RULE_VALUE_MAP.put("粤U" , "潮州市");
	    RULE_VALUE_MAP.put("粤V" , "揭阳市");
	    RULE_VALUE_MAP.put("粤W" , "云浮市");
	    RULE_VALUE_MAP.put("粤X" , "顺德区");
	    RULE_VALUE_MAP.put("粤Y" , "南海区");
	    RULE_VALUE_MAP.put("粤Z" , "港澳进入内地车辆");
	    RULE_VALUE_MAP.put("贵A" , "贵阳市");
	    RULE_VALUE_MAP.put("贵B" , "六盘水市");
	    RULE_VALUE_MAP.put("贵C" , "遵义市");
	    RULE_VALUE_MAP.put("贵D" , "铜仁市");
	    RULE_VALUE_MAP.put("贵E" , "黔西南布依族苗族自治州");
	    RULE_VALUE_MAP.put("贵F" , "毕节市");
	    RULE_VALUE_MAP.put("贵G" , "安顺市");
	    RULE_VALUE_MAP.put("贵H" , "黔东南苗族侗族自治州");
	    RULE_VALUE_MAP.put("贵J" , "黔南布依族苗族自治州");
	    RULE_VALUE_MAP.put("闽A" , "福州市");
	    RULE_VALUE_MAP.put("闽B" , "莆田市");
	    RULE_VALUE_MAP.put("闽C" , "泉州市");
	    RULE_VALUE_MAP.put("闽D" , "厦门市");
	    RULE_VALUE_MAP.put("闽E" , "漳州市");
	    RULE_VALUE_MAP.put("闽F" , "龙岩市");
	    RULE_VALUE_MAP.put("闽G" , "三明市");
	    RULE_VALUE_MAP.put("闽H" , "南平市");
	    RULE_VALUE_MAP.put("闽J" , "宁德市");
	    RULE_VALUE_MAP.put("闽K" , "省直系统");
	    RULE_VALUE_MAP.put("吉A" , "长春市");
	    RULE_VALUE_MAP.put("吉B" , "吉林市");
	    RULE_VALUE_MAP.put("吉C" , "四平市");
	    RULE_VALUE_MAP.put("吉D" , "辽源市");
	    RULE_VALUE_MAP.put("吉E" , "通化市");
	    RULE_VALUE_MAP.put("吉F" , "白山市");
	    RULE_VALUE_MAP.put("吉G" , "白城市");
	    RULE_VALUE_MAP.put("吉H" , "延边朝鲜族自治州");
	    RULE_VALUE_MAP.put("吉J" , "松原市");
	    RULE_VALUE_MAP.put("陕A" , "西安市");
	    RULE_VALUE_MAP.put("陕B" , "铜川市");
	    RULE_VALUE_MAP.put("陕C" , "宝鸡市");
	    RULE_VALUE_MAP.put("陕D" , "咸阳市");
	    RULE_VALUE_MAP.put("陕E" , "渭南市");
	    RULE_VALUE_MAP.put("陕F" , "汉中市");
	    RULE_VALUE_MAP.put("陕G" , "安康市");
	    RULE_VALUE_MAP.put("陕H" , "商洛市");
	    RULE_VALUE_MAP.put("陕J" , "延安市");
	    RULE_VALUE_MAP.put("陕K" , "榆林市");
	    RULE_VALUE_MAP.put("陕V" , "杨凌高新农业示范区");
	    RULE_VALUE_MAP.put("蒙A" , "呼和浩特市");
	    RULE_VALUE_MAP.put("蒙B" , "包头市");
	    RULE_VALUE_MAP.put("蒙C" , "乌海市");
	    RULE_VALUE_MAP.put("蒙D" , "赤峰市");
	    RULE_VALUE_MAP.put("蒙E" , "呼伦贝尔市");
	    RULE_VALUE_MAP.put("蒙F" , "兴安盟");
	    RULE_VALUE_MAP.put("蒙G" , "通辽市");
	    RULE_VALUE_MAP.put("蒙H" , "锡林郭勒盟");
	    RULE_VALUE_MAP.put("蒙J" , "乌兰察布盟");
	    RULE_VALUE_MAP.put("蒙K" , "鄂尔多斯市");
	    RULE_VALUE_MAP.put("蒙L" , "巴彦淖尔盟");
	    RULE_VALUE_MAP.put("蒙M" , "阿拉善盟");
	    RULE_VALUE_MAP.put("晋A" , "太原市");
	    RULE_VALUE_MAP.put("晋B" , "大同市");
	    RULE_VALUE_MAP.put("晋C" , "阳泉市");
	    RULE_VALUE_MAP.put("晋D" , "长治市");
	    RULE_VALUE_MAP.put("晋E" , "晋城市");
	    RULE_VALUE_MAP.put("晋F" , "朔州市");
	    RULE_VALUE_MAP.put("晋H" , "忻州市");
	    RULE_VALUE_MAP.put("晋J" , "吕梁地区");
	    RULE_VALUE_MAP.put("晋K" , "晋中市");
	    RULE_VALUE_MAP.put("晋L" , "临汾市");
	    RULE_VALUE_MAP.put("晋M" , "运城市");
	    RULE_VALUE_MAP.put("甘A" , "兰州市");
	    RULE_VALUE_MAP.put("甘B" , "嘉峪关市");
	    RULE_VALUE_MAP.put("甘C" , "金昌市");
	    RULE_VALUE_MAP.put("甘D" , "白银市");
	    RULE_VALUE_MAP.put("甘E" , "天水市");
	    RULE_VALUE_MAP.put("甘F" , "洒泉市");
	    RULE_VALUE_MAP.put("甘G" , "张掖市");
	    RULE_VALUE_MAP.put("甘H" , "武威市");
	    RULE_VALUE_MAP.put("甘J" , "定西市");
	    RULE_VALUE_MAP.put("甘K" , "陇南市");
	    RULE_VALUE_MAP.put("甘L" , "平凉市");
	    RULE_VALUE_MAP.put("甘M" , "庆阳市");
	    RULE_VALUE_MAP.put("甘N" , "临夏回族自治州");
	    RULE_VALUE_MAP.put("甘P" , "甘南藏族自治州");
	    RULE_VALUE_MAP.put("桂A" , "南宁市");
	    RULE_VALUE_MAP.put("桂B" , "柳州市");
	    RULE_VALUE_MAP.put("桂C" , "桂林市");
	    RULE_VALUE_MAP.put("桂D" , "梧州市");
	    RULE_VALUE_MAP.put("桂E" , "北海市");
	    RULE_VALUE_MAP.put("桂F" , "崇左市");
	    RULE_VALUE_MAP.put("桂G" , "来宾市");
	    RULE_VALUE_MAP.put("桂H" , "桂林地区");
	    RULE_VALUE_MAP.put("桂J" , "贺州市");
	    RULE_VALUE_MAP.put("桂K" , "玉林市");
	    RULE_VALUE_MAP.put("桂L" , "百色市");
	    RULE_VALUE_MAP.put("桂M" , "河池市");
	    RULE_VALUE_MAP.put("桂N" , "钦州市");
	    RULE_VALUE_MAP.put("桂P" , "防城港市");
	    RULE_VALUE_MAP.put("桂R" , "贵港市");
	    RULE_VALUE_MAP.put("鄂A" , "武汉市");
	    RULE_VALUE_MAP.put("鄂B" , "黄石市");
	    RULE_VALUE_MAP.put("鄂C" , "十堰市");
	    RULE_VALUE_MAP.put("鄂D" , "荆州市");
	    RULE_VALUE_MAP.put("鄂E" , "宜昌市");
	    RULE_VALUE_MAP.put("鄂F" , "襄樊市");
	    RULE_VALUE_MAP.put("鄂G" , "鄂州市");
	    RULE_VALUE_MAP.put("鄂H" , "荆门市");
	    RULE_VALUE_MAP.put("鄂J" , "黄冈市");
	    RULE_VALUE_MAP.put("鄂K" , "孝感市");
	    RULE_VALUE_MAP.put("鄂L" , "咸宁市");
	    RULE_VALUE_MAP.put("鄂M" , "仙桃市");
	    RULE_VALUE_MAP.put("鄂N" , "潜江市");
	    RULE_VALUE_MAP.put("鄂P" , "神农架林区");
	    RULE_VALUE_MAP.put("鄂Q" , "恩施土家族苗族自治州");
	    RULE_VALUE_MAP.put("鄂R" , "天门市");
	    RULE_VALUE_MAP.put("鄂S" , "随州市");
	    RULE_VALUE_MAP.put("赣A" , "南昌市");
	    RULE_VALUE_MAP.put("赣B" , "赣州市");
	    RULE_VALUE_MAP.put("赣C" , "宜春市");
	    RULE_VALUE_MAP.put("赣D" , "吉安市");
	    RULE_VALUE_MAP.put("赣E" , "上饶市");
	    RULE_VALUE_MAP.put("赣F" , "抚州市");
	    RULE_VALUE_MAP.put("赣G" , "九江市");
	    RULE_VALUE_MAP.put("赣H" , "景德镇市");
	    RULE_VALUE_MAP.put("赣J" , "萍乡市");
	    RULE_VALUE_MAP.put("赣K" , "新余市");
	    RULE_VALUE_MAP.put("赣L" , "鹰潭市");
	    RULE_VALUE_MAP.put("赣M" , "南昌市");
	    RULE_VALUE_MAP.put("浙A" , "杭州市");
	    RULE_VALUE_MAP.put("浙B" , "宁波市");
	    RULE_VALUE_MAP.put("浙C" , "温州市");
	    RULE_VALUE_MAP.put("浙D" , "绍兴市");
	    RULE_VALUE_MAP.put("浙E" , "湖州市");
	    RULE_VALUE_MAP.put("浙F" , "嘉兴市");
	    RULE_VALUE_MAP.put("浙G" , "金华市");
	    RULE_VALUE_MAP.put("浙H" , "衢州市");
	    RULE_VALUE_MAP.put("浙J" , "台州市");
	    RULE_VALUE_MAP.put("浙K" , "丽水市");
	    RULE_VALUE_MAP.put("浙L" , "舟山市");
	    RULE_VALUE_MAP.put("苏A" , "南京市");
	    RULE_VALUE_MAP.put("苏B" , "无锡市");
	    RULE_VALUE_MAP.put("苏C" , "徐州市");
	    RULE_VALUE_MAP.put("苏D" , "常州市");
	    RULE_VALUE_MAP.put("苏E" , "苏州市");
	    RULE_VALUE_MAP.put("苏F" , "南通市");
	    RULE_VALUE_MAP.put("苏G" , "连云港市");
	    RULE_VALUE_MAP.put("苏H" , "淮安市");
	    RULE_VALUE_MAP.put("苏J" , "盐城市");
	    RULE_VALUE_MAP.put("苏K" , "扬州市");
	    RULE_VALUE_MAP.put("苏L" , "镇江市");
	    RULE_VALUE_MAP.put("苏M" , "泰州市");
	    RULE_VALUE_MAP.put("苏N" , "宿迁市");
	    RULE_VALUE_MAP.put("新A" , "乌鲁木齐市");
	    RULE_VALUE_MAP.put("新B" , "昌吉回族自治州");
	    RULE_VALUE_MAP.put("新C" , "石河子市");
	    RULE_VALUE_MAP.put("新D" , "奎屯市");
	    RULE_VALUE_MAP.put("新E" , "博尔塔拉蒙古自治州");
	    RULE_VALUE_MAP.put("新F" , "伊犁哈萨克自治州直辖县");
	    RULE_VALUE_MAP.put("新G" , "塔城市");
	    RULE_VALUE_MAP.put("新H" , "阿勒泰市");
	    RULE_VALUE_MAP.put("新J" , "克拉玛依市");
	    RULE_VALUE_MAP.put("新K" , "吐鲁番市");
	    RULE_VALUE_MAP.put("新L" , "哈密市");
	    RULE_VALUE_MAP.put("新M" , "巴音郭愣蒙古自治州");
	    RULE_VALUE_MAP.put("新N" , "阿克苏市");
	    RULE_VALUE_MAP.put("新P" , "克孜勒苏柯尔克孜自治州");
	    RULE_VALUE_MAP.put("新Q" , "喀什市");
	    RULE_VALUE_MAP.put("新R" , "和田市");
	    RULE_VALUE_MAP.put("鲁A" , "济南市");
	    RULE_VALUE_MAP.put("鲁B" , "青岛市");
	    RULE_VALUE_MAP.put("鲁C" , "淄博市");
	    RULE_VALUE_MAP.put("鲁D" , "枣庄市");
	    RULE_VALUE_MAP.put("鲁E" , "东营市");
	    RULE_VALUE_MAP.put("鲁F" , "烟台市");
	    RULE_VALUE_MAP.put("鲁G" , "潍坊市");
	    RULE_VALUE_MAP.put("鲁H" , "济宁市");
	    RULE_VALUE_MAP.put("鲁J" , "泰安市");
	    RULE_VALUE_MAP.put("鲁K" , "威海市");
	    RULE_VALUE_MAP.put("鲁L" , "日照市");
	    RULE_VALUE_MAP.put("鲁M" , "滨州市");
	    RULE_VALUE_MAP.put("鲁N" , "德州市");
	    RULE_VALUE_MAP.put("鲁P" , "聊城市");
	    RULE_VALUE_MAP.put("鲁Q" , "临沂市");
	    RULE_VALUE_MAP.put("鲁R" , "荷泽市");
	    RULE_VALUE_MAP.put("鲁S" , "莱芜市");
	    RULE_VALUE_MAP.put("鲁U" , "青岛市增补");
	    RULE_VALUE_MAP.put("鲁V" , "潍坊市增补");
	    RULE_VALUE_MAP.put("鲁Y" , "烟台市");
	    RULE_VALUE_MAP.put("皖A" , "合肥市");
	    RULE_VALUE_MAP.put("皖B" , "芜湖市");
	    RULE_VALUE_MAP.put("皖C" , "蚌埠市");
	    RULE_VALUE_MAP.put("皖D" , "淮南市");
	    RULE_VALUE_MAP.put("皖E" , "马鞍山市");
	    RULE_VALUE_MAP.put("皖F" , "淮北市");
	    RULE_VALUE_MAP.put("皖G" , "铜陵市");
	    RULE_VALUE_MAP.put("皖H" , "安庆市");
	    RULE_VALUE_MAP.put("皖J" , "黄山市");
	    RULE_VALUE_MAP.put("皖K" , "阜阳市");
	    RULE_VALUE_MAP.put("皖L" , "宿州市");
	    RULE_VALUE_MAP.put("皖M" , "滁州市");
	    RULE_VALUE_MAP.put("皖N" , "六安市");
	    RULE_VALUE_MAP.put("皖P" , "宣城市");
	    RULE_VALUE_MAP.put("皖Q" , "巢湖市");
	    RULE_VALUE_MAP.put("皖R" , "池州市");
	    RULE_VALUE_MAP.put("皖S" , "亳州市");
	    RULE_VALUE_MAP.put("湘A" , "长沙市");
	    RULE_VALUE_MAP.put("湘B" , "株洲市");
	    RULE_VALUE_MAP.put("湘C" , "湘潭市");
	    RULE_VALUE_MAP.put("湘D" , "衡阳市");
	    RULE_VALUE_MAP.put("湘E" , "邵阳市");
	    RULE_VALUE_MAP.put("湘F" , "岳阳市");
	    RULE_VALUE_MAP.put("湘G" , "张家界市");
	    RULE_VALUE_MAP.put("湘H" , "益阳市");
	    RULE_VALUE_MAP.put("湘J" , "常德市");
	    RULE_VALUE_MAP.put("湘K" , "娄底市");
	    RULE_VALUE_MAP.put("湘L" , "郴州市");
	    RULE_VALUE_MAP.put("湘M" , "永州市");
	    RULE_VALUE_MAP.put("湘N" , "怀化市");
	    RULE_VALUE_MAP.put("湘U" , "湘西土家族苗族自治州");
	    RULE_VALUE_MAP.put("黑A" , "哈尔滨市");
	    RULE_VALUE_MAP.put("黑B" , "齐齐哈尔市");
	    RULE_VALUE_MAP.put("黑C" , "牡丹江市");
	    RULE_VALUE_MAP.put("黑D" , "佳木斯市");
	    RULE_VALUE_MAP.put("黑E" , "大庆市");
	    RULE_VALUE_MAP.put("黑F" , "伊春市");
	    RULE_VALUE_MAP.put("黑G" , "鸡西市");
	    RULE_VALUE_MAP.put("黑H" , "鹤岗市");
	    RULE_VALUE_MAP.put("黑J" , "双鸭山市");
	    RULE_VALUE_MAP.put("黑K" , "七台河市");
	    RULE_VALUE_MAP.put("黑L" , "哈尔滨市");
	    RULE_VALUE_MAP.put("黑M" , "绥化市");
	    RULE_VALUE_MAP.put("黑N" , "黑河市");
	    RULE_VALUE_MAP.put("黑P" , "大兴安岭地区");
	    RULE_VALUE_MAP.put("黑R" , "农恳系统");
	    RULE_VALUE_MAP.put("辽A" , "沈阳市");
	    RULE_VALUE_MAP.put("辽B" , "大连市");
	    RULE_VALUE_MAP.put("辽C" , "鞍山市");
	    RULE_VALUE_MAP.put("辽D" , "抚顺市");
	    RULE_VALUE_MAP.put("辽E" , "本溪市");
	    RULE_VALUE_MAP.put("辽F" , "丹东市");
	    RULE_VALUE_MAP.put("辽G" , "锦州市");
	    RULE_VALUE_MAP.put("辽H" , "营口市");
	    RULE_VALUE_MAP.put("辽J" , "阜新市");
	    RULE_VALUE_MAP.put("辽K" , "辽阳市");
	    RULE_VALUE_MAP.put("辽L" , "盘锦市");
	    RULE_VALUE_MAP.put("辽M" , "铁岭市");
	    RULE_VALUE_MAP.put("辽N" , "朝阳市");
	    RULE_VALUE_MAP.put("辽P" , "葫芦岛市");
	    RULE_VALUE_MAP.put("云A" , "昆明市");
	    RULE_VALUE_MAP.put("云V" , "东川市");
	    RULE_VALUE_MAP.put("云C" , "昭通市");
	    RULE_VALUE_MAP.put("云D" , "曲靖市");
	    RULE_VALUE_MAP.put("云E" , "楚雄彝族自治州");
	    RULE_VALUE_MAP.put("云F" , "玉溪市");
	    RULE_VALUE_MAP.put("云G" , "红河哈尼族彝族自治州");
	    RULE_VALUE_MAP.put("云H" , "文山壮族苗族自治州");
	    RULE_VALUE_MAP.put("云J" , "思茅市");
	    RULE_VALUE_MAP.put("云K" , "西双版纳傣族自治州");
	    RULE_VALUE_MAP.put("云L" , "大理白族自治州");
	    RULE_VALUE_MAP.put("云M" , "保山市");
	    RULE_VALUE_MAP.put("云N" , "德宏傣族景颇族自治州");
	    RULE_VALUE_MAP.put("云P" , "丽江市");
	    RULE_VALUE_MAP.put("云Q" , "怒江傈僳族自治州");
	    RULE_VALUE_MAP.put("云R" , "迪庆藏族自治州");
	    RULE_VALUE_MAP.put("云S" , "临沧地区");
	    RULE_VALUE_MAP.put("豫A" , "郑州市");
	    RULE_VALUE_MAP.put("豫B" , "开封市");
	    RULE_VALUE_MAP.put("豫C" , "洛阳市");
	    RULE_VALUE_MAP.put("豫D" , "平顶山市");
	    RULE_VALUE_MAP.put("豫E" , "安阳市");
	    RULE_VALUE_MAP.put("豫F" , "鹤壁市");
	    RULE_VALUE_MAP.put("豫G" , "新乡市");
	    RULE_VALUE_MAP.put("豫H" , "焦作市");
	    RULE_VALUE_MAP.put("豫J" , "濮阳市");
	    RULE_VALUE_MAP.put("豫K" , "许昌市");
	    RULE_VALUE_MAP.put("豫L" , "漯河市");
	    RULE_VALUE_MAP.put("豫M" , "三门峡市");
	    RULE_VALUE_MAP.put("豫N" , "商丘市");
	    RULE_VALUE_MAP.put("豫P" , "周口市");
	    RULE_VALUE_MAP.put("豫Q" , "驻马店市");
	    RULE_VALUE_MAP.put("豫R" , "南阳市");
	    RULE_VALUE_MAP.put("豫S" , "信阳市");
	    RULE_VALUE_MAP.put("豫U" , "济源市");
	    RULE_VALUE_MAP.put("冀A" , "石家庄市");
	    RULE_VALUE_MAP.put("冀B" , "唐山市");
	    RULE_VALUE_MAP.put("冀C" , "秦皇岛市");
	    RULE_VALUE_MAP.put("冀D" , "邯郸市");
	    RULE_VALUE_MAP.put("冀E" , "邢台市");
	    RULE_VALUE_MAP.put("冀F" , "保定市");
	    RULE_VALUE_MAP.put("冀G" , "张家口市");
	    RULE_VALUE_MAP.put("冀H" , "承德市");
	    RULE_VALUE_MAP.put("冀J" , "沧州市");
	    RULE_VALUE_MAP.put("冀R" , "廊坊市");
	    RULE_VALUE_MAP.put("冀S" , "沧州市");
	    RULE_VALUE_MAP.put("冀T" , "衡水市");
    }
    
	//是否正在刷新数据
    private boolean running = false;


    public SupplementCache() {}

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean isRunning) {
        running = isRunning;
    }

    public boolean resetRunning() {
        setRunning(false);
        return true;
    }

    /**
     * 1、从规则引擎加载所有补充数据项
     * 2、从供应商管理后台加载供应商基本数据，然后再计算每个供应商的补充数据项
     * @param onlySupplement 是否只从规则引擎加载补充数据项
     * @return
     */
    private synchronized boolean loadData(boolean onlySupplement) {
        setRunning(true);
        try {
            allSupplementMapHotSwap = new HashMap<String, SupplementModel>();
            providerComplementMapHotSwap = new HashMap<String, List<SupplementModel>>();
            providerInfoMapHotSwap = new HashMap<String, INSBAgreement>();
            
            initAllSupplementInfo();
            initAllProviderInfo();
            if (!allSupplementMapHotSwap.isEmpty()) {
                allSupplementMap = allSupplementMapHotSwap;
            }

            if (!providerComplementMapHotSwap.isEmpty()) {
                providerComplementMap = providerComplementMapHotSwap;
            }
            if (!providerInfoMapHotSwap.isEmpty()) {
                providerInfoMap = providerInfoMapHotSwap;
            }

            return true;

        } catch (Exception e) {
        	LogUtil.error("刷新供应商规则引擎补充数据项缓存信息出错", e);
            return false;
        } finally {
            setRunning(false);
        }
    }

    /**
     * 刷新缓存
     */
    public String flush(boolean onlySupplement) {
        if (isRunning()) {
            return "[running]刷新供应商规则引擎补充数据项缓存信息操作已在执行，本次操作忽略";
        }

        LogUtil.info("---------开始刷新供应商规则引擎补充数据项缓存信息-----------");
        boolean result = loadData(onlySupplement);
        LogUtil.info("---------结束刷新供应商规则引擎补充数据项缓存信息-----------");

        return "[${result}]供应商规则引擎补充数据项缓存刷新操作执行"+result;
    }

    /**
     * 缓存初始化
     */
    public void init(boolean onlySupplement) {
        String msg = flush(onlySupplement);
        LogUtil.info(msg);
    }

    public static SupplementModel mapToModel(String supplementKey, Map<String, Object> map) {
        SupplementModel model = new SupplementModel();
        model.setKey(supplementKey);
        Class<SupplementModel> clazz = SupplementModel.class;

        try {
        	Field[] fields = clazz.getDeclaredFields();
        	for (String key : map.keySet()) {
                for (Field field:fields) {
                	if(field.getName().equals(key)){
	                    field.setAccessible(true);
	                    field.set(model, map.get(key));
	                    break;
                	}
                }
            }
        } catch (Exception e) {
        	LogUtil.error(e.getMessage(), e);
        }

        if (model.getMetadataValue().contains(":")) {
            model.setType(SupplementModel.TYPE_LIST);
            HashMap<String,String> matedataMap = new HashMap<>();
            model.setMetadataValueMap(matedataMap);
            for(String it:model.getMetadataValue().split(",")){
                String[] eachItem = it.split(":");
                if (eachItem.length == 2) {
                	matedataMap.put(eachItem[0], eachItem[1]);
                }
            }
            CMRedisClient.getInstance().set(3, MODULE_NAME, model.getKey(), com.alibaba.fastjson.JSONObject.toJSONString(matedataMap));
        } else if (BOOLEAN_TYPE.equals(model.getMetadataType())) {
            model.setType(SupplementModel.TYPE_BOOLEAN);
        } else if (DATE_TYPE.equals(model.getMetadataType())) {
            model.setType(SupplementModel.TYPE_DATE);
        } else if (NUMBER_LIST.toString().contains(model.getMetadataType())) {
            model.setType(SupplementModel.TYPE_NUMBER);
        } else {
            model.setType(SupplementModel.TYPE_STRING);
        }
        return model;
    }
    private static final String[] NUMBER_LIST = {"Integer", "Long", "Double", "Float"};
    private static final String BOOLEAN_TYPE = "Boolean";
    private static final String DATE_TYPE = "Date";
    public static final String MODULE_NAME = "providerComplementMapHotSwap";
    private static String RULE_PARAM_HOST = "";
    private static String RULE_SERVER_IP ="";
	static {
		// 读取相关的配置  
		ResourceBundle resourceBundle = ResourceBundle.getBundle("config/rule");
		RULE_SERVER_IP = resourceBundle.getString("rule.handler.server.ip");
		RULE_PARAM_HOST = resourceBundle.getString("rule.handler.server.supplyParam");
		//如果不是HTTP开头就拼接,是就不用拼接
		if(!RULE_PARAM_HOST.startsWith("http")){
			RULE_PARAM_HOST = RULE_SERVER_IP+RULE_PARAM_HOST;
		}
	}
    private void getAllSupplementInfo() {
        try {
			LogUtil.info("RULE_PARAM_HOST=="+RULE_PARAM_HOST);
			//http请求规则接口,返回JSON
        	String result = HttpSender.doGet(RULE_PARAM_HOST, null);
        	JSONObject json=JSONObject.fromObject(result); 
//            RuleEngineApi clientApi = new RuleEngineApi();
//            clientApi.interfaceName("supplyParam");
//            clientApi.execute();
//            LogUtil.info("从规则引擎获取的补充数据项：${clientApi.resultCollection}");
//            Map<String,Object> results = clientApi.getResultCollection();
            Iterator iterator = json.entrySet().iterator();
            while(iterator.hasNext()) {
            	Map.Entry entry = (Entry) iterator.next();
            	String key = (String) entry.getKey();
            	Map value = (Map)JSONObject.fromObject(entry.getValue());
            	allSupplementMapHotSwap.put(key, mapToModel(key, value));
            }
            LogUtil.info("缓存规则引擎获取的补充数据项：" + result);
        } catch (Exception e) {
        	LogUtil.error(e.getMessage(), e);
        }
    }

    private void initAllSupplementInfo() {
        getAllSupplementInfo();
        if (allSupplementMapHotSwap.size() == 0) {
        	LogUtil.info("没有从规则引擎获取到补充数据项，重新查询");
            getAllSupplementInfo();
        }
    }
    /**
     * 初始化固定的算价因子
     * @return
     */
    private String initAllbaseElements() {
    	List<SupplementModel> baseElements = new ArrayList<SupplementModel>();
    	SupplementModel base = null;
    	for(int i=0;i<=16;i++){//初始化默认的八项基本算价因子
    		base = new SupplementModel();
    		base.setBasicItem(true);
    		base.setDefaultValue("");
    		base.setType(SupplementModel.TYPE_STRING);
    		base.setOrder(i);
    		if(0==i){
    			base.setKey("application.firstInsureType");
    			base.setMetadataName("投保类型");
    		}else if(1==i){
    			base.setKey("application.commercialClaimTimes");
    			base.setMetadataName("上年商业险理赔次数");
    		}else if(2==i){
    			base.setKey("application.compulsoryClaimTimes");
    			base.setMetadataName("上年交强险理赔次数");
    		}else if(3==i){
    			base.setKey("application.registerArea");
    			base.setMetadataName("车辆归属区域");
    		}else if(4==i){
    			base.setKey("ruleItem.customDiscount");
    			base.setMetadataName("自定义折扣");
    		}else if(5==i){
    			base.setKey("ruleItem.noRenewalDiscountReason");
    			base.setMetadataName("无续保优惠原因");
    		}else if(6==i){
    			base.setKey("car.specific.vehicularApplications");
    			base.setMetadataName("车俩用途");
    		}else if(7==i){
    			base.setKey("car.specific.specialVehicleType");
    			base.setMetadataName("特种车类型");
    		}else if(8==i){
    			base.setKey("ruleItem.fullLoadCalculationType");
    			base.setMetadataName("自重计算标准");
    		}else if(9==i){
    			base.setKey("ruleItem.basicRiskPremium");
    			base.setMetadataName("机动车车损纯风险基准保费");
    		}else if(10==i){
    			base.setKey("plateform.InsureCo");
    			base.setMetadataName("查询保险公司");
    		}else if(11==i){
    			base.setKey("ruleItem.RiskPremium");
    			base.setMetadataName("查询公司车损保费");
    		}else if(12==i){
    			base.setKey("car.specific.NcdRate");
    			base.setMetadataName("查询公司NCD系数");
    		}else if(13==i){
    			base.setKey("application.trafficOffenceDiscount");
    			base.setMetadataName("查询公司交通违法系数");
    		}else if(14==i){
    			base.setKey("car.specific.selfInsureRate");
    			base.setMetadataName("查询公司自主核保系数");
    		}else if(15==i){
    			base.setKey("car.specific.selfChannelRate");
    			base.setMetadataName("查询公司自主渠道系数");
    		}else{
    			base.setKey("ruleItem.selfRate");
    			base.setMetadataName("查询公司自主系数");
    		}
    		baseElements.add(base);
    	}
    	return com.alibaba.fastjson.JSONArray.toJSONString(baseElements);
	}
    private void initAllProviderInfo() {
        List<INSBAgreement> providerList = getAllProviderInfo();
        if (null == providerList || providerList.size() == 0) {
        	LogUtil.error("没有查询到供应商数据，重新查询");
            providerList = getAllProviderInfo();
        }

        if (providerList.size() > 0) {
            for (INSBAgreement basePropsProvider : providerList) {
                providerInfoMapHotSwap.put(basePropsProvider.getProviderid(), basePropsProvider);
            }

            Collection<SupplementModel> allSupplements = allSupplementMapHotSwap.values();

            for (INSBAgreement provider : providerList) {
                String ruleId = provider.getAgreementtrule();
                List<SupplementModel> eachList = getSupplementModelList(ruleId, allSupplements);
                if (null == eachList || eachList.isEmpty()) {
                	LogUtil.error("供应商"+provider.getProviderid()+"对应的规则"+ruleId+"没有找到补充数据项");
                } else {
                    providerComplementMapHotSwap.put(provider.getProviderid(), eachList);
                    //CMRedisClient.getInstance().set(MODULE_NAME, provider.getProviderid(), com.alibaba.fastjson.JSONArray.toJSONString(eachList));
                    if(null!=CMRedisClient.getInstance().get(MODULE_NAME, provider.getProviderid()))
                    	CMRedisClient.getInstance().del(MODULE_NAME, provider.getProviderid());
                    CMRedisClient.getInstance().set(3, MODULE_NAME, provider.getProviderid(), com.alibaba.fastjson.JSONArray.toJSONString(eachList));
                }
            }
        }

        if (providerComplementMapHotSwap.size() == 0) {
        	LogUtil.error("没有查询到供应商补充数据项");
        }
    }
    private List<INSBAgreement> getAllProviderInfo() {
        try {
        	INSBAgreement param = new INSBAgreement();
        	return insbagreementservice.queryList(param);
        } catch (Exception e) {
        	LogUtil.error(e.getMessage(), e);
            return null;
        }
    }
    private List<SupplementModel> getSupplementModelList(String ruleId, Collection<SupplementModel> allSupplements) {
        List<SupplementModel> eachList = new ArrayList<SupplementModel>();
        for (SupplementModel model : allSupplements) {
            if (model.getValidRuleIdList().toString().contains(ruleId)) {
                eachList.add(model);
            }
        }
        return eachList;
    }


    /**
     * 根据供应商id列表获取这些供应商公共的补充项信息
     * @param providerIds
     * @return
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     */
    public Map<String, SupplementModel> getAllSupModelsByProviderIds(List<String> providerIds) throws IllegalAccessException, InvocationTargetException {
        Map<String, SupplementModel> result = new HashMap<String, SupplementModel>();
        for (String providerId : providerIds) {
            List<SupplementModel> eachModelList = providerComplementMap.get(providerId);
            if (eachModelList.size() > 0) {
                List<SupplementModel> eachModelListClone = new ArrayList<>();
                for (SupplementModel eachModel: eachModelList) {
                    SupplementModel eachModelClone = new SupplementModel();
                    BeanUtils.copyProperties(eachModelClone, eachModel);
                    eachModelListClone.add(eachModelClone);
                }
                for (SupplementModel eachModelClone : eachModelListClone) {
                    if (!result.containsKey(eachModelClone.getKey())) {
                        result.put(eachModelClone.getKey(), eachModelClone);
                    }
                    SupplementModel modelInMap = result.get(eachModelClone.getKey());
                    if (null!=modelInMap && null==providerInfoMap.get(providerId)) {
                        modelInMap.setProviderAgreementList(new ArrayList<>());
                    }
                    modelInMap.getProviderAgreementList().add(providerInfoMap.get(providerId));
                }
            }
        }
        return result;
    }
    
    /**
     * 初始化保存平台查询的规则报价需要的规则项和值
     * @param taskid
     * @return boolean 返回true表示保存成功，false表示保存失败
     */
    public boolean saveTaskidSupplementDatas(String taskid){
    	//注释：根据供应商查协议表数据不准确，可能查到多条数据; 修改为查主任务下所有子任务(子任务表中有协议id)
//    	List<String> inscomcodeList = insbQuotetotalinfoService.getInscomcodeListByInstanceId(taskid);//人工规则报价
//    	Map<String,Object> rulequery = insbCarinfoService.getCarInfoByTaskId(taskid);
//		INSBSupplement insbSupplement = null;
//		String tmp = initAllbaseElements();
//		for (int i = 0; i < inscomcodeList.size(); i++) {
//			INSBAgreement insbagreement = new INSBAgreement();
//			insbagreement.setProviderid(inscomcodeList.get(i));
//			List<INSBAgreement> insbagreements = insbagreementservice.queryList(insbagreement);
//	    	if(null!=insbagreements&&insbagreements.size()>0){
//	    		insbagreement = insbagreements.get(0);
//	    	}else{
//	    		LogUtil.error("providerid="+inscomcodeList.get(i)+"taskid="+taskid+",供应商没有配置规则协议记录");
//	    		continue;
//	    	}
    	
    	INSBWorkflowsub insbwfsub = new INSBWorkflowsub();
		insbwfsub.setMaininstanceid(taskid);
		List<INSBWorkflowsub> allSubList = insbWorkflowsubService.queryList(insbwfsub);
		Map<String,Object> rulequery = insbCarinfoService.getCarInfoByTaskId(taskid);
		INSBSupplement insbSupplement = null;
		String tmp = initAllbaseElements();
		
		if(allSubList != null){
			String instanceId = null;
			INSBQuoteinfo quoteInfo = null;
			String agreementId = null;
			INSBAgreement insbAgreement = null;
			
			for(INSBWorkflowsub temp : allSubList){
				instanceId = temp.getInstanceid();
				quoteInfo = insbQuoteinfoService.getQuoteinfoByWorkflowinstanceid(instanceId);
				if(quoteInfo != null){
					agreementId = quoteInfo.getAgreementid();
					if(StringUtil.isEmpty(agreementId)){
						continue;
					}
					insbAgreement = insbAgreementService.queryById(agreementId);
					if (insbAgreement == null) {
						continue;
					}
					String providerid = insbAgreement.getProviderid();

					// 规则报价是否启动 不等于1 表示没有启用规则报价
					if (insbAgreement.getAgreementrule() == null || !insbAgreement.getAgreementrule().equals("1")) {
						LogUtil.error("providerid=" + providerid + "taskid=" + taskid + ",供应商配置规则协议未启用agreementId=" + agreementId);
						continue;
					} else {
						LogUtil.info("providerid=" + providerid + "taskid=" + taskid + ",<开始初始化默认>的规则算价因子");
						// 初始化默认的规则算价因子
						String basejson = String.valueOf(
								CMRedisClient.getInstance().get(3, SupplementCache.MODULE_NAME, "baseElements"));
						if (StringUtil.isEmpty(basejson)) {
							basejson = tmp;
							// CMRedisClient.getInstance().set(SupplementCache.MODULE_NAME,
							// "baseElements", basejson);
							CMRedisClient.getInstance().set(3, SupplementCache.MODULE_NAME, "baseElements", basejson);
							if (null != CMRedisClient.getInstance().get(SupplementCache.MODULE_NAME, "baseElements"))
								;
							CMRedisClient.getInstance().del(SupplementCache.MODULE_NAME, "baseElements");
						}
						List<SupplementModel> basemodels = com.alibaba.fastjson.JSONArray.parseArray(basejson,
								SupplementModel.class);
						saveSupplements(insbSupplement, basemodels, providerid, taskid, insbAgreement, rulequery);
						LogUtil.info("providerid=" + providerid + "taskid=" + taskid + ",<结束默认>的规则算价因子");
						
						// 初始化补充项算价因子
						String json = (String) CMRedisClient.getInstance().get(3, SupplementCache.MODULE_NAME,
								providerid);
						LogUtil.info("providerid=" + providerid + "taskid=" + taskid + ",<开始补充项>的规则算价因子");
						if (StringUtil.isEmpty(json)) {
							LogUtil.error("providerid=" + providerid + "taskid=" + taskid + ",redis中补充项算价因子json是null");
							continue;
						}

						List<SupplementModel> models = com.alibaba.fastjson.JSONArray.parseArray(json,
								SupplementModel.class);
						if (null != models) {
							saveSupplements(insbSupplement, models, providerid, taskid, insbAgreement, rulequery);
							LogUtil.info("providerid=" + providerid + "taskid=" + taskid + ",<结束补充项>规则算价因子");
						}
					}
				}
			}
		} else {
			LogUtil.error("saveTaskidSupplementDatas, taskid=" + taskid + ",没有找到子任务allSubList=" + allSubList);
		}
		return true;
    }
    /**
     * 初始化保存规则算价因子
     * @param insbSupplement
     * @param models
     * @param inscomcode
     * @param taskid
     * @param insbagreement
     * @param rulequery
     */
	private void saveSupplements(INSBSupplement insbSupplement, List<SupplementModel> models, String inscomcode, String taskid,
			INSBAgreement insbagreement, Map<String, Object> rulequery) {
		insbSupplement = new INSBSupplement();
		// insbSupplement.setKeyid(model.getKey());
		insbSupplement.setProviderid(inscomcode);
		insbSupplement.setTaskid(taskid);
		insbSupplement.setRuleid(insbagreement.getAgreementtrule());
		List<INSBSupplement> insbSupplements = insbSupplementService.queryList(insbSupplement);
//		LogUtil.info("saveSupplements,taskid=" + taskid + ", models=" + com.alibaba.fastjson.JSONArray.toJSONString(models) + ", rulequery=" + JSONObject.fromObject(rulequery).toString());
		for (SupplementModel model : models) {
			insbSupplement = new INSBSupplement();
			insbSupplement.setKeyid(model.getKey());
			insbSupplement.setProviderid(inscomcode);
			insbSupplement.setTaskid(taskid);
			insbSupplement.setRuleid(insbagreement.getAgreementtrule());
			if (null != model.getMetadataName() && model.getMetadataName().contains(".")) {
				insbSupplement.setMetadataName(model.getMetadataName().substring(model.getMetadataName().indexOf(".") + 1));
			} else {
				insbSupplement.setMetadataName(model.getMetadataName());
			}
			 
			Map<String, String> redisMap = com.alibaba.fastjson.JSONObject.parseObject(String.valueOf(CMRedisClient.getInstance().get(3, SupplementCache.MODULE_NAME, insbSupplement.getKeyid())), Map.class);//默认从redis里面获取补充项的取值Map
			Map<String, String> map = model.getMetadataValueMap();
			if (null != map) {
				String str = JSONObject.fromObject(map).toString();
				str = str.substring(1);
				str = "{\"\":\"\"," + str;
				insbSupplement.setMetadataValue(str);
			}
			if(null != redisMap && StringUtil.isEmpty(insbSupplement.getMetadataValue())){//默认从redis里面获取补充项的取值Map
				String str = JSONObject.fromObject(redisMap).toString();
				str = str.substring(1);
				str = "{\"\":\"\"," + str;
				insbSupplement.setMetadataValue(str);
			}
			if (null != rulequery.get(RULE_VALUE_MAP.get(model.getKey())) && null != map) {
				insbSupplement.setMetadataValueKey(map.get(rulequery.get(RULE_VALUE_MAP.get(model.getKey())).toString()));
			} else if (null != rulequery.get(RULE_VALUE_MAP.get(model.getKey()))) {
				insbSupplement.setMetadataValueKey(rulequery.get(RULE_VALUE_MAP.get(model.getKey())).toString());
			} else if (insbSupplement.getMetadataName().contains("自重计算标准")) {// 自重计算标准设置默认值
				insbSupplement.setMetadataValueKey("默认");
			} else if (insbSupplement.getMetadataName().contains("车辆归属区域")) {// 自重计算标准设置默认值
				insbSupplement.setMetadataValueKey(checkRegisterArea(taskid, String.valueOf(rulequery.get("licenseno"))));
			} else {
				insbSupplement.setMetadataValueKey("");
			}
			if (null != rulequery.get(RULE_VALUE_MAP.get(model.getKey())) && null != redisMap) {//默认从redis里面获取补充项的取值Map，处理值
				if(null != redisMap.get(rulequery.get(RULE_VALUE_MAP.get(model.getKey())).toString())){
					insbSupplement.setMetadataValueKey(redisMap.get(rulequery.get(RULE_VALUE_MAP.get(model.getKey())).toString()));
				}
			}
			if (model.getMetadataName().contains("上年承保公司")) {
				String inscorpcode0 = String.valueOf(rulequery.get("inscorpcode0")),
						inscorpname0 = String.valueOf(rulequery.get("inscorpname0")),
						inscorpcode1 = String.valueOf(rulequery.get("inscorpcode1")),
						inscorpname1 = String.valueOf(rulequery.get("inscorpname1")),
						inscorpcodec0 = String.valueOf(rulequery.get("inscorpcodec0")),
						inscorpcodec1 = String.valueOf(rulequery.get("inscorpcodec1"));

				if (InsCompany.containsKey(inscorpcode0)) {
					insbSupplement.setMetadataValueKey(InsCompany.getCode(inscorpcode0));
				} else if (InsCompany.containsKey(inscorpname0)) {
					insbSupplement.setMetadataValueKey(InsCompany.getCode(inscorpname0));
				} else if (InsCompany.containsKey(inscorpcode1)) {
					insbSupplement.setMetadataValueKey(InsCompany.getCode(inscorpcode1));
				} else if (InsCompany.containsKey(inscorpname1)) {
					insbSupplement.setMetadataValueKey(InsCompany.getCode(inscorpname1));
				} else if (InsCompany.containsKey(inscorpcodec0)) {
					insbSupplement.setMetadataValueKey(InsCompany.getCode(inscorpcodec0));
				} else if (InsCompany.containsKey(inscorpcodec1)) {
					insbSupplement.setMetadataValueKey(InsCompany.getCode(inscorpcodec1));
				}

				if (StringUtil.isEmpty(insbSupplement.getMetadataValueKey())) {
					if (StringUtil.isNotEmpty(inscorpcode0))
						insbSupplement.setMetadataValueKey(inscorpcode0);
					else if (StringUtil.isNotEmpty(inscorpcode1))
						insbSupplement.setMetadataValueKey(inscorpcode1);
					else if (StringUtil.isNotEmpty(inscorpcodec0))
						insbSupplement.setMetadataValueKey(inscorpcodec0);
					else if (StringUtil.isNotEmpty(inscorpcodec1))
						insbSupplement.setMetadataValueKey(inscorpcodec1);
				}

				insbSupplement
						.setMetadataValue("{\"无\":\"0\",\"国寿财险\":\"2002\",\"人保财险\":\"2005\",\"平安财险\":\"2007\",\"太保财险\":\"2011\",\"太平财险\":\"2016\",\"阳光财险\":\"2019\",\"阳光农业相互保险\":\"2020\",\"大地财险\":\"2021\",\"永诚财险\":\"2022\",\"华泰财险\":\"2023\",\"安邦财险\":\"2024\",\"天安保险\":\"2025\",\"安盛天平财险\":\"2026\",\"中华联合财险\":\"2027\",\"中银保险\":\"2028\",\"中意财险\":\"2029\",\"美亚财险\":\"2034\",\"易安财险\":\"2039\",\"英大泰和财险\":\"2040\",\"渤海财险\":\"2041\",\"都邦财险\":\"2042\",\"华安财险\":\"2043\",\"众诚保险\":\"2044\",\"天安财险\":\"2045\",\"永安财险\":\"2046\",\"安联财险\":\"2049\",\"锦泰财险\":\"2050\",\"长江财险\":\"2051\",\"北部湾财产\":\"2052\",\"亚太财险\":\"2053\",\"民安财险\":\"2054\",\"众安在线财险\":\"2056\",\"燕赵财险\":\"2057\",\"安诚财险\":\"2058\",\"合众财险\":\"2059\",\"安心财险\":\"2060\",\"中路财险\":\"2061\",\"恒邦财险\":\"2062\",\"燕赵财险\":\"2063\",\"华农财险\":\"2064\",\"诚泰财险\":\"2065\",\"亚太财险\":\"2066\",\"日本兴亚财险\":\"2067\",\"安华农业\":\"2068\",\"都帮财险\":\"2069\",\"史带财险\":\"2071\",\"北部湾财险\":\"2072\",\"泰山财险\":\"2073\",\"太阳联合\":\"2074\",\"三星财险中国\":\"2075\",\"中煤财险\":\"2076\",\"富德财险\":\"2077\",\"出口信用保险\":\"2078\",\"中航安盟财险\":\"2079\",\"鑫安汽车保险\":\"2080\",\"三井海上火灾\":\"2081\",\"安信农业\":\"2082\",\"邱博保险\":\"2083\",\"现代保险\":\"2084\",\"利宝保险\":\"2085\",\"长安责任\":\"2086\",\"国泰财险\":\"2087\",\"鼎和财险\":\"2088\",\"法国安盟\":\"2089\",\"浙商财险\":\"2090\",\"苏黎世财险\":\"2091\",\"丘博保险中国\":\"2092\",\"东京海上火灾\":\"2093\",\"日本财险中国\":\"2094\",\"紫金财险\":\"2095\",\"信达财险\":\"2096\",\"安盛保险\":\"2097\",\"乐爱金财险中国\":\"2098\",\"美亚财险\":\"2099\",\"国元农业\":\"2100\",\"富邦财险\":\"2101\",\"泰山财险\":\"2102\",\"华海财险\":\"2103\",\"瑞士再保险公司\":\"3001\",\"慕尼黑再保险公司\":\"3002\",\"中铁财产自保\":\"3003\",\"平安保险代理\":\"4001\",\"泰康在线\":\"4002\",\"中汇国际保险\":\"5005\"}");
				CMRedisClient.getInstance().set(3, SupplementCache.MODULE_NAME, insbSupplement.getKeyid(), insbSupplement.getMetadataValue());
			}
			boolean isUpdate = false;
			for(INSBSupplement supplement : insbSupplements){
				if(model.getKey() != null && model.getKey().equals(supplement.getKeyid())){
					insbSupplement.setId(supplement.getId());
					isUpdate = true;
					break;
				}
			}
			if(isUpdate)	
				insbSupplementService.updateById(insbSupplement);
			else{
				insbSupplement.setOrdernum(model.getOrder());
				insbSupplement.setCreatetime(new Date());
				insbSupplementService.insert(insbSupplement);
			}
			
//			LogUtil.info("saveSupplement_one,taskid=" + taskid + ", insbSupplement=" + JSONObject.fromObject(insbSupplement).toString());
		}
	}
	
	/**
     * 校验车牌号归属区域
     * @param taskid
     * @param plateNumber
     * @return "0":本地；"1":省内异地;"2":省外异地;"3":港澳车
     */
    public String checkRegisterArea(String taskid,String plateNumber) {
        String registerAreaVal = "0";
		if (StringUtil.isEmpty(plateNumber)) return registerAreaVal;

        try {
        	INSBQuotetotalinfo info = new INSBQuotetotalinfo();
    		info.setTaskid(taskid);
    		info = insbQuotetotalinfoService.queryOne(info);

            if (!"new".equals(plateNumber) && !plateNumber.contains("未上牌")) {
				String regexStr = "^粤Z.*(港|澳)\\$";
				boolean isHKMacCar = plateNumber.matches(regexStr);
                if (isHKMacCar){
                    registerAreaVal = "3";
                }else if (null!=info.getInsprovincename()&&!info.getInsprovincename().contains(RULE_VALUE_MAP.get(plateNumber.substring(0,1)))) {
                    registerAreaVal = "2";
                } else if (null!=info.getInscityname()&&!info.getInscityname().contains(RULE_VALUE_MAP.get(plateNumber.substring(0,2)))&& null!=info.getInsprovincename()&&info.getInsprovincename().contains(RULE_VALUE_MAP.get(plateNumber.substring(0,1)))) {
                    registerAreaVal = "1";
                }
            }
        } catch (Exception t) {
        	LogUtil.info("获取车辆归属地异常taskid"+taskid, t);
        }
        return registerAreaVal;
    }

    public static void main(String[] args) {
    	String plateNumber = "粤A88888";
		System.out.println("粤Z.*".substring(0, 2));
		INSBQuotetotalinfo info = new INSBQuotetotalinfo();
		info.setInscityname("广州市");
		info.setInsprovincename("广东省");
        String registerAreaVal = "0";
        String regexStr = "^粤Z.*(港|澳)\\$";
        boolean isHKMacCar = plateNumber.matches(regexStr);
        try {
			if (!"new".equals(plateNumber) && !plateNumber.contains("未上牌")) {
				if (isHKMacCar){
					registerAreaVal = "3";
				}else if (null!=info.getInsprovincename()&&!info.getInsprovincename().contains(RULE_VALUE_MAP.get(plateNumber.substring(0,1)))) {
					registerAreaVal = "2";
				} else if (null!=info.getInscityname()&&!info.getInscityname().contains(RULE_VALUE_MAP.get(plateNumber.substring(0,2)))&& null!=info.getInsprovincename()&&info.getInsprovincename().contains(RULE_VALUE_MAP.get(plateNumber.substring(0,1)))) {
					registerAreaVal = "1";
				} else {
					registerAreaVal = "0";
				}
			}
        } catch (Exception t) {
        	LogUtil.info("获取车辆归属地异常taskid"+0, t);
        }
        System.out.println(registerAreaVal+"=="+RULE_VALUE_MAP.get(plateNumber.substring(0,2))+"=="+RULE_VALUE_MAP.get(plateNumber.substring(0,1)));
	}
}
