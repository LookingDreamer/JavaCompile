package com.zzb.extra.util.wxmsgtemplate;

import java.io.Serializable;

/**
 * Created by Heweicheng on 2016/6/29.
 */
public class TemplateData implements Serializable{
    private static final long serialVersionUID = 1L;
    private String value;
    private String color;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


}
