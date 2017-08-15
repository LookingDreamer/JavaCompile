package com.zzb.conf.controller.vo;

/**
 * 树节点
 *
 * @author  wu-shangsen
 * @version 1.0, 2016/8/30
 */
public class TreeVo {

    private String id ; //ID

    private String pId ; //父节点ID

    private String name ; //节点名称

    private boolean isParent ; //是否是父节点

    private String innercode;

    public String getInnercode() {
        return innercode;
    }

    public void setInnercode(String innercode) {
        this.innercode = innercode;
    }

    public TreeVo() {
    }

    public TreeVo(String id, String pId, String name, boolean isParent) {
        this.id = id;
        this.pId = pId;
        this.name = name;
        this.isParent = isParent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsParent() {
        return isParent;
    }

    public void setIsParent(boolean isParent) {
        this.isParent = isParent;
    }
}
