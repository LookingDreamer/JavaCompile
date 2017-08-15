package com.zzb.extra.service.impl;

import javax.annotation.Resource;

import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.zzb.chn.util.JsonUtils;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.extra.controller.vo.AccountWithdrawVo;
import com.zzb.extra.dao.INSBAccountDetailsDao;
import com.zzb.extra.dao.INSBAccountWithdrawDao;
import com.zzb.extra.entity.INSBAccount;
import com.zzb.extra.entity.INSBAccountDetails;
import com.zzb.extra.entity.INSBAccountWithdraw;
import com.zzb.extra.model.WxMsgTemplateModel;
import com.zzb.extra.service.INSBAccountDetailsService;
import com.zzb.extra.service.INSBAccountService;
import com.zzb.extra.service.INSBAccountWithdrawService;
import com.zzb.extra.service.WxMsgTemplateService;
import com.zzb.extra.util.ParamUtils;
import com.zzb.extra.util.wxmsgtemplate.TemplateData;
import com.zzb.extra.util.wxmsgtemplate.WxTemplate;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class INSBAccountWithdrawServiceImpl extends BaseServiceImpl<INSBAccountWithdraw> implements
        INSBAccountWithdrawService {
    @Resource
    private INSBAccountService insbAccountService;

    @Resource
    private INSBAccountWithdrawDao insbAccountWithdrawDao;

    @Resource
    private INSBAccountDetailsService insbAccountDetailsService;

    @Resource
    private INSBAgentService insbAgentService;

    @Resource
    private WxMsgTemplateService wxMsgTemplateService;

    @Resource
    private INSBAccountDetailsDao insbAccountDetailsDao;

    @Override
    protected BaseDao<INSBAccountWithdraw> getBaseDao() {
        return insbAccountWithdrawDao;
    }

    @Override
    public String queryPagingList(Map<String, Object> map) {
        long total = insbAccountWithdrawDao.queryPagingListCount(map);
        List<Map<Object, Object>> infoList = insbAccountWithdrawDao.queryPagingList(map);
        return ParamUtils.resultMap(total, infoList);
    }

    /**
     * 创建提现任务
     *
     * @param withdrawVo
     * @return
     */
    @Override
    public String putAccountWithdraw(AccountWithdrawVo withdrawVo) {
        if (withdrawVo.getAgentid() == null || withdrawVo.getAgentid().equals("") ||
                withdrawVo.getAmount() == null || withdrawVo.getAmount().toString().equals("") || withdrawVo.getAmount() == 0 ||
                withdrawVo.getBankname() == null || withdrawVo.getBankname().equals("") ||
                withdrawVo.getCardnumber() == null || withdrawVo.getCardnumber().equals(""))
            return ParamUtils.resultMap(false, "参数不正确");

        INSBAccount insbAccount = insbAccountService.initAccount(withdrawVo.getAgentid());
        if (insbAccount == null) return ParamUtils.resultMap(false, "用户不存在或未验证");

        Double balance = insbAccountDetailsDao.queryBalance(insbAccount.getId());
        Double blockedFund = insbAccountWithdrawDao.queryBlocked(insbAccount.getId());

        //当次提现金额不可大于余额减提现中（冻结）金额
        if (withdrawVo.getAmount() > balance - blockedFund)
            return ParamUtils.resultMap(false, "金额超出可提现额度");

        INSBAccountWithdraw accountWithdraw = new INSBAccountWithdraw();
        accountWithdraw.setId(UUIDUtils.random());
        accountWithdraw.setAccountid(insbAccount.getId());
        accountWithdraw.setOperator("admin");
        accountWithdraw.setBankname(withdrawVo.getBankname());
        accountWithdraw.setCardnumber(withdrawVo.getCardnumber());
        accountWithdraw.setBranch(withdrawVo.getBranch());
        accountWithdraw.setAmount(withdrawVo.getAmount());
        accountWithdraw.setStatus("0");
        accountWithdraw.setNoti("");
        accountWithdraw.setCreatetime(new Date());
        if (insbAccountWithdrawDao.putAccountWithdraw(accountWithdraw) > 0)
            return ParamUtils.resultMap(true, "操作成功");
        else
            return ParamUtils.resultMap(false, "操作失败");
    }

    @Override
    public void updateWithdrawStatus(INSBAccountWithdraw accountWithdraw) {
        if (accountWithdraw.getId() != null && !accountWithdraw.getId().equals("")) {
            INSBAccountWithdraw insbAccountWithdraw = insbAccountWithdrawDao.queryById(accountWithdraw.getId());
            if (insbAccountWithdraw.getStatus().equals("0")) {
                accountWithdraw.setModifytime(new Date());
                int result = insbAccountWithdrawDao.updateStatusAndNoti(accountWithdraw);
                if (result > 0 && accountWithdraw.getStatus().equals("1")) {

                    Double balance = insbAccountDetailsDao.queryBalance(insbAccountWithdraw.getAccountid());
                    balance = balance - insbAccountWithdraw.getAmount();
                    Map<String,Object> notiMap = new HashMap<String,Object>();
                    notiMap.put("银行", insbAccountWithdraw.getBankname());
                    notiMap.put("银行卡号",insbAccountWithdraw.getCardnumber());
                    notiMap.put("个人财富余额",balance);
                    notiMap.put("用户姓名",insbAccountWithdrawDao.queryAgentNameByAccountid(insbAccountWithdraw.getAccountid()));
                    String noti = JsonUtils.serialize(notiMap);
                    insbAccountDetailsService.intoAccountDetails(insbAccountWithdraw.getAccountid(), "2", "201", insbAccountWithdraw.getAmount(), noti, accountWithdraw.getOperator(),"");
                    //发送提现成功消息
                    wxMsgTemplateService.sendCashMsg(insbAccountWithdraw.getAccountid(), (insbAccountWithdraw.getAmount()).toString(), "13");
                    //
                }
            }
            //发送提现失败消息
            if("2".equals(accountWithdraw.getStatus())){
                wxMsgTemplateService.sendCashMsg(insbAccountWithdraw.getAccountid(),(insbAccountWithdraw.getAmount()).toString(),"14");
            }
            //
        }
    }
}
