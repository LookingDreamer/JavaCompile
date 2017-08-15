package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;
import com.common.ModelUtil;


public class INSBOperatorcomment extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 轨迹id
	 */
	private String trackid;

	/**
	 * 轨迹类型
	 */
	private Integer tracktype;

	/**
	 * 备注内容
	 */
	private String commentcontent;

	public String getTrackid() {
		return trackid;
	}

	public void setTrackid(String trackid) {
		this.trackid = trackid;
	}

	public Integer getTracktype() {
		return tracktype;
	}

	public void setTracktype(Integer tracktype) {
		this.tracktype = tracktype;
	}

	public String getCommentcontent() {
		return (commentcontent!=null && commentcontent.trim().length()>0) ? ModelUtil.replaceHtml(commentcontent) : null;
	}

	public void setCommentcontent(String commentcontent) {
		this.commentcontent = commentcontent;
	}

	@Override
	public String toString() {
		return "INSBOperatorcomment [trackid=" + trackid + ", tracktype="
				+ tracktype + ", commentcontent=" + commentcontent + "]";
	}
	
	

}