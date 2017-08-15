package com.zzb.conf.controller.vo;

import com.cninsure.system.entity.INSCDept;
import com.zzb.conf.entity.INSBChannel;
import com.zzb.conf.entity.INSBChannelagreement;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * 渠道响应Vo
 *
 * @author  wu-shangsen
 * @version 1.0, 2016/8/30
 */
public class ChannelRespVo extends INSBChannel implements Serializable {

    private INSCDept dept ; //机构信息

    private INSBChannelagreement agreement ; //合同信息

    public ChannelRespVo() {
    }

    public ChannelRespVo(INSBChannel insbChannel) {
        BeanUtils.copyProperties(insbChannel, this);
    }

    public INSCDept getDept() {
        return dept;
    }

    public void setDept(INSCDept dept) {
        this.dept = dept;
    }

    public INSBChannelagreement getAgreement() {
        return agreement;
    }

    public void setAgreement(INSBChannelagreement agreement) {
        this.agreement = agreement;
    }
}
