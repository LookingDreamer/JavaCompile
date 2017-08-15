package com.zzb.extra.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBConditions extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 来源id
	 */
	private String sourceid;

	/**
	 * 来源类型
	 */
	private String conditionsource;

    /**
     * 参数数据类型
     */
    private String paramdatatype;

	/**
	 * 参数名称
	 */
	private String paramname;

	/**
	 * 判断式
	 */
	private String expression;

	/**
	 * 参数值
	 */
	private String paramvalue;

	public String getSourceid() {
		return sourceid;
	}

	public void setSourceid(String sourceid) {
		this.sourceid = sourceid;
	}

	public String getConditionsource() {
		return conditionsource;
	}

	public void setConditionsource(String conditionsource) {
		this.conditionsource = conditionsource;
	}

    public String getParamdatatype() {
        return paramdatatype;
    }

    public void setParamdatatype(String paramdatatype) {
        this.paramdatatype = paramdatatype;
    }

	public String getParamname() {
		return paramname;
	}

	public void setParamname(String paramname) {
		this.paramname = paramname;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getParamvalue() {
		return paramvalue;
	}

	public void setParamvalue(String paramvalue) {
		this.paramvalue = paramvalue;
	}
}