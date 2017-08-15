package com.zzb.extra.controller;

import com.alibaba.fastjson.JSONObject;
import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCCodeService;
import com.common.PagingParams;
import com.zzb.extra.entity.INSBAccount;
import com.zzb.extra.entity.INSBAccountWithdraw;
import com.zzb.extra.model.AccountDetailsQueryModel;
import com.zzb.extra.model.AccountWithdrawQueryModel;
import com.zzb.extra.service.INSBAccountDetailsService;
import com.zzb.extra.service.INSBAccountService;
import com.zzb.extra.service.INSBAccountWithdrawService;
import com.zzb.extra.util.ExcelUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/account/*")
public class AccountController extends BaseController {

    @Resource
    private INSBAccountService insbAccountService;

    @Resource
    private INSBAccountDetailsService insbAccountDetailsService;

    @Resource
    private INSBAccountWithdrawService insbAccountWithdrawService;

    @Resource
    private INSCCodeService codeService;

    @RequestMapping(value = "/queryDetails", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView queryDetails() throws ControllerException {
        ModelAndView mav = new ModelAndView("extra/accountDetailsList");
        //资金来源
        List<INSCCode> fundSourceList = codeService.queryINSCCodeByCode("accountDetails", "fundSource");
        mav.addObject("fundSourceList", fundSourceList);

        //收支类型
        List<INSCCode> fundTypeList = codeService.queryINSCCodeByCode("accountDetails", "fundType");
        mav.addObject("fundTypeList", fundTypeList);

        return mav;
    }

    @RequestMapping(value = "/queryWithdraw", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView queryWithdraw() throws ControllerException {
        ModelAndView mav = new ModelAndView("extra/accountWithdrawList");
        //提现状态
        List<INSCCode> withdrawStatusList = codeService.queryINSCCodeByCode("accountDetails", "withdrawStatus");
        mav.addObject("withdrawStatusList", withdrawStatusList);

        return mav;
    }

    /**
     * 查询账户
     *
     * @param accountid
     * @return
     */
    @RequestMapping(value = "/getAccount", method = RequestMethod.GET)
    @ResponseBody
    public INSBAccount getUserAccount(String accountid) {
        try {
            INSBAccount userAccount = new INSBAccount();
            userAccount = this.insbAccountService.queryById(accountid);
            return userAccount;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 查询出入账列表
     *
     * @param para
     * @param queryModel
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "/queryDetailsList", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String queryDetailsList(HttpSession session, @ModelAttribute PagingParams para, @ModelAttribute AccountDetailsQueryModel queryModel) throws ControllerException {
        Map<String, Object> map = BeanUtils.toMap(queryModel, para);
        System.out.println(map);
        return insbAccountDetailsService.queryPagingList(map);

    }

    /**
     * 查询提现任务列表
     *
     * @param para
     * @param queryModel
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "/queryWithdrawList", method = RequestMethod.GET)
    @ResponseBody
    public String queryWithdrawList(HttpSession session, @ModelAttribute PagingParams para, @ModelAttribute AccountWithdrawQueryModel queryModel) throws ControllerException {
        Map<String, Object> map = BeanUtils.toMap(queryModel, para);
        System.out.println(map);
        return insbAccountWithdrawService.queryPagingList(map);
    }

    /**
     * 更新提现状态及备注
     *
     * @param entity
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "updateWithdrawStatus", method = RequestMethod.POST)
    @ResponseBody
    public String updateWithdrawStatus(HttpSession session, @ModelAttribute INSBAccountWithdraw entity) throws ControllerException {
        try {
            INSCUser user = (INSCUser) session.getAttribute("insc_user");
            entity.setOperator(user.getUsercode());
            insbAccountWithdrawService.updateWithdrawStatus(entity);
        } catch (Exception e) {
            return "fail";
        }
        return "success";
    }

    @RequestMapping(value = "/exportExcelList", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView exportExcelList() throws ControllerException {
        ModelAndView mav = new ModelAndView("extra/exportExcelList");
        return mav;
    }

    @RequestMapping(value = "/exportBusinessList", method = RequestMethod.POST)
    @ResponseBody
    public String exportBusinessList(HttpServletRequest request,HttpServletResponse response,String startdate,String enddate,String listType,String withdrawStatus) throws ControllerException {
        String result = "";
        if("0".equals(listType)){ //业务清单
            try {
                String fileName="业务清单";
                String sheetName="业务清单";
                List<Map<String,Object>> list=insbAccountDetailsService.queryBusinessList(startdate,enddate);
                //System.out.println(JSONObject.toJSONString(list));
                String columnNames[] = {"工号","任务号","人员性质","客户名称","机构","车牌号码","被保险人","出单日期","保险公司","总保费","商业险保单号","商业险金额","商业险酬金率","商业险酬金","交强险保单号","交强险金额","交强险酬金率","交强险酬金","酬金","奖券兑现","奖金","赏金率","赏金（推荐奖）","应付佣金"};//列名
                String keys[] = {"jobnum","taskid","persontype","name","shortname","carlicenseno","insuredname","insuredate","agreementname","totalinsuredamount","commercial_policyno","commercial_num","commercial_rate","commercial_counts","compulsory_policyno","compulsory_num","compulsory_rate","compulsory_counts","commission_total","redpacket","cashprize","reward_rate","reward_counts","total_amount"};//map中的key
                Integer amountColumns[] = {9,11,13,15,17,18,19,20,22,23} ;//金额栏位设置为数字格式
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                //组装EXCEL数据
                ExcelUtils.createWorkBook(sheetName, list, keys, columnNames,amountColumns).write(os);
                result = exportExcel(request,response,fileName,os);
            }catch (Exception e){
                LogUtil.info("exportBusinessList=="+e.getMessage());
            }
        } else if("1".equals(listType)){ //提现清单
            try {
                String fileName="提现清单";
                String sheetName="提现清单";
                List<Map<String,Object>> list=insbAccountDetailsService.queryWithdrawListByDate(startdate, enddate,withdrawStatus);
                //System.out.println(JSONObject.toJSONString(list));
                String columnNames[] = {"工号","手机号码","提现日期","提现申请人","提现金额","提现次数","账户余额","收款账户","收款银行","账号","提现状态"};//列名
                String keys[] = {"jobnum","phone","withdraw_time","name","amount","cnt","balance","cardowner","bankname","cardnumber","withdrawstatus"};//map中的key
                Integer amountColumns[] = {4,6} ;
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                //组装EXCEL数据
                ExcelUtils.createWorkBook(sheetName, list, keys, columnNames,amountColumns).write(os);
                result = exportExcel(request,response,fileName,os);
            }catch (Exception e){
                LogUtil.info("exportBusinessList=="+e.getMessage());
            }
        }else if("2".equals(listType)){ //奖金清单
            try {
                String fileName="奖金清单";
                String sheetName="奖金清单";
                List<Map<String,Object>> list=insbAccountDetailsService.queryCashPrizeListByDate(startdate, enddate);
                //System.out.println(JSONObject.toJSONString(list));
                String columnNames[] = {"工号","手机号码","任务号","客户名称","奖金获得时间","活动名称","奖品名称","奖品类型","金额","兑现标准"};//列名
                String keys[] = {"jobnum","phone","taskid","name","createtime","activityname","prizename","codename","counts","noti"};//map中的key
                Integer amountColumns[] = {8} ;
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                //组装EXCEL数据
                ExcelUtils.createWorkBook(sheetName, list, keys, columnNames,amountColumns).write(os);
                result = exportExcel(request,response,fileName,os);
            }catch (Exception e){
                LogUtil.info("exportBusinessList=="+e.getMessage());
            }
        }else if("3".equals(listType)){ //个人财富汇总清单
            try {
                String fileName="个人财富汇总清单";
                String sheetName="个人财富汇总清单";
                List<Map<String,Object>> list=insbAccountDetailsService.queryPersonalWealthByDate(startdate, enddate);
                //System.out.println(JSONObject.toJSONString(list));
                String columnNames[] = {"时间","工号","姓名","手机号码","总收入","总支出","余额"};//列名
                String keys[] = {"yyyymmdd","jobnum","name","phone","income","expenses","balance"};//map中的key
                Integer amountColumns[] = {4,5,6} ;
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                //组装EXCEL数据
                ExcelUtils.createWorkBook(sheetName, list, keys, columnNames,amountColumns).write(os);
                result = exportExcel(request,response,fileName,os);
            }catch (Exception e){
                LogUtil.info("exportBusinessList=="+e.getMessage());
            }
        }

            return null;

    }

    @RequestMapping(value = "/exportSumBusinessList", method = RequestMethod.POST)
    @ResponseBody
    public String exportSumBusinessList(HttpServletRequest request,HttpServletResponse response,String startdate,String listType) throws ControllerException {
        String result = "";
        if("0".equals(listType)){ //个人财富按月汇总清单
            try {
                String fileName="个人财富按月汇总清单";
                String sheetName="个人财富按月汇总清单";
                List<Map<String,Object>> list=insbAccountDetailsService.queryPersonalWealthByMonth(startdate);
                //System.out.println(JSONObject.toJSONString(list));
                String columnNames[] = {"月份","工号","姓名","手机号码","总收入","总支出","余额"};//列名
                String keys[] = {"yyyymm","jobnum","name","phone","income","expenses","balance"};//map中的key
                Integer amountColumns[] = {4,5,6} ;
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                //组装EXCEL数据
                ExcelUtils.createWorkBook(sheetName, list, keys, columnNames,amountColumns).write(os);
                result = exportExcel(request,response,fileName,os);
            }catch (Exception e){
                LogUtil.info("exportBusinessList=="+e.getMessage());
            }
        } else if("1".equals(listType)){ //机构交易汇总表
            try {
                String fileName="机构交易汇总表";
                String sheetName="机构交易汇总表";
                List<Map<String,Object>> list=insbAccountDetailsService.querySumBusinessByMonth(startdate);
                String columnNames[] = {"月份","机构","本月总保费\r\n金额","本月总商业险\r\n金额","本月总交强险\r\n金额","本月酬金总额","本月奖券总额","本月奖金总额","本月赏金总额","本月总佣金","本年累计总保\r\n费金额","本年累计商业\r\n险金额","本年累计交强\r\n险金额","本年酬金累计","本年奖券累计","本年奖金累计","本年赏金累计","本年累计总佣\r\n金金额"};//列名
                String keys[] = {"yyyymm","shortname","totalinsuredamount","commercial_num","compulsory_num","commission_total","redpacket","cashprize","reward_counts","total_amount","totalinsuredamount_yyyy","commercial_num_yyyy","compulsory_num_yyyy","commission_total_yyyy","redpacket_yyyy","cashprize_yyyy","reward_counts_yyyy","total_amount_yyyy"};//map中的key
                Integer amountColumns[] = {2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17} ;
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                //组装EXCEL数据
                ExcelUtils.createWorkBook(sheetName, list, keys, columnNames,amountColumns).write(os);
                result = exportExcel(request,response,fileName,os);
            }catch (Exception e){
                LogUtil.info("exportBusinessList=="+e.getMessage());
            }
        }
        return null;
    }

    private String exportExcel(HttpServletRequest request,HttpServletResponse response,String fileName,ByteArrayOutputStream os){
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try{
        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);
        // 设置response参数，可以打开下载页面
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename="+ new String((fileName +
                ".xls").getBytes(), "iso-8859-1"));
        ServletOutputStream out = response.getOutputStream();
        bis = new BufferedInputStream(is);
        bos = new BufferedOutputStream(out);
        byte[] buff = new byte[2048];
        int bytesRead;
        // Simple read/write loop.
        while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
            bos.write(buff, 0, bytesRead);
        }
    } catch (Exception e) {
        LogUtil.info("exportBusinessList=="+e.getMessage());
        return "error";
    } finally {
        if (bis != null)
            try {
                bis.close();
            }catch (Exception e){
                LogUtil.info("exportBusinessList=="+e.getMessage());
            }
        if (bos != null)
            try {
                bos.close();
            }catch (Exception e){
                LogUtil.info("exportBusinessList=="+e.getMessage());
            }
    }
    return "success";
    }
}
