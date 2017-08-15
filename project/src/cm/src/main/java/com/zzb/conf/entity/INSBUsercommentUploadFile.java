package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

/**
 * Created by liwucai on 2016/11/14 15:08.
 */
public class INSBUsercommentUploadFile extends BaseEntity implements Identifiable {
    private static final long serialVersionUID = 1L;
    private String usercommentid; //用户备注id
    private String codetype;//上传文件类型

    public String getUsercommentid() {
        return usercommentid;
    }

    public void setUsercommentid(String usercommentid) {
        this.usercommentid = usercommentid;
    }

    public String getCodetype() {
        return codetype;
    }

    public void setCodetype(String codetype) {
        this.codetype = codetype;
    }
}
