package com.zzb.mobile.service.impl;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.UUIDUtils;
import com.common.redis.CMRedisClient;
import com.zzb.conf.service.INSBOrderpaymentService;
import com.zzb.mobile.dao.InsbpaymenttransactionDao;
import com.zzb.mobile.entity.Insbpaymenttransaction;
import com.zzb.mobile.service.InsbpaymenttransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Transactional
public class InsbpaymenttransactionServiceImpl extends BaseServiceImpl<Insbpaymenttransaction>
        implements InsbpaymenttransactionService {

    @Resource
    private INSBOrderpaymentService iNSBOrderpaymentService;
    @Resource
    private InsbpaymenttransactionDao insbpaymenttransactionDao;
    @Resource
    private CMRedisClient redisClient; //Redis服务

    private String redisModel = "paymenttransaction" ;
    private Integer redisTimeout =  87840 ;//24*60*60 ;

    @Override
    protected BaseDao<Insbpaymenttransaction> getBaseDao() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 获取支付流水号
     * @param date 日期，流水号根据日期生成
     * @return
     */
    @Override
    public String getPaymenttransaction(Date date) {
        //支付流水号
        StringBuilder paymentransaction = new StringBuilder();

        //获取流水号
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String key = sdf.format(date) ;

        //根据日期查找支付流水号表
        int tst = 1 ;
        Object transactionObj = redisClient.get(redisModel, key) ;

        if(transactionObj != null) { //Redis有
            tst = redisClient.atomicIncr(redisModel, key) ; //如果不存在则设置值为1且返回1
            redisClient.expire(redisModel, key, redisTimeout) ;
        } else { //Redis没有
            Insbpaymenttransaction transation = insbpaymenttransactionDao.selectOne(key);
            if(transation != null) {
                tst = transation.getTransaction() + 1 ;
                redisClient.set(redisModel, key, tst, redisTimeout) ;
            } else {
                redisClient.set(redisModel, key, tst, redisTimeout);
            }
        }

        String str = String.format("%09d", tst);      //补够9位流水号
        String[] st = sdf.format(date).split("-");
        paymentransaction.append("zzb").append(st[0])
                .append(st[1]).append(st[2]).append(str);    //返回流水号

        return paymentransaction.toString();
    }

}
