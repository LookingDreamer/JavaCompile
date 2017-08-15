package com.zzb.warranty.model;

import com.cninsure.core.dao.domain.Identifiable;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by Administrator on 2017/2/14.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CMProduct implements Identifiable {
    private String id;
    private String code;
    private String name;
    private String description;


    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String s) {
        this.id = s;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
