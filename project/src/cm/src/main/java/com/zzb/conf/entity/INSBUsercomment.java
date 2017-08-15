package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

import java.util.List;


public class INSBUsercomment extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	private String instanceId;

	/**
	 * 轨迹id
	 */
	private String trackid;

	/**
	 * 轨迹类型
	 */
	private Integer tracktype;

	/**
	 * 备注类型
	 */
	private Integer commenttype;

	/**
	 * 备注内容类型
	 */
	private Integer commentcontenttype;

	/**
	 * 备注内容
	 */
	private String commentcontent;

	/**
	 * 上传的影像codetype
	 */
	private List<String> codetypes;

	private Integer commentsource;

	public Integer getCommentsource() {
		return commentsource;
	}

	public void setCommentsource(Integer commentsource) {
		this.commentsource = commentsource;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public List<String> getCodetypes() {
		return codetypes;
	}

	public void setCodetypes(List<String> codetypes) {
		this.codetypes = codetypes;
	}

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

	public Integer getCommenttype() {
		return commenttype;
	}

	public void setCommenttype(Integer commenttype) {
		this.commenttype = commenttype;
	}

	public Integer getCommentcontenttype() {
		return commentcontenttype;
	}

	public void setCommentcontenttype(Integer commentcontenttype) {
		this.commentcontenttype = commentcontenttype;
	}

	public String getCommentcontent() {
		return commentcontent;
	}

	public void setCommentcontent(String commentcontent) {
		this.commentcontent = commentcontent;
	}

	@Override
	public String toString() {
		return "INSBUsercomment [trackid=" + trackid + ", tracktype="
				+ tracktype + ", commenttype=" + commenttype
				+ ", commentcontenttype=" + commentcontenttype
				+ ", commentcontent=" + commentcontent + "]";
	}

	
	
}