package com.pack;

import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * author: cjianquan
 * date: 2016/11/11
 */
//@ConfigurationProperties(prefix = "svn")
public class PackBean implements java.io.Serializable{
    //项目类型
    private String protype;
    //项目路径
    private String propath;
    //WebRoot路径(相对于项目路径)
    private String wrPath;
    //class编译路径(相对于项目路径)
    private String compilePath;
    //时间:yyyy-MM-dd HH:mm:ss
    private String packtime;
    //源码路径(相对于项目路径)
    private String srcPath;
    //配置资料文件路径(相对于项目路径)
    private String resourcesPath;
    //打包后目的目录
    private String destPackPath ;

    private List<String> packFiles;
    //tomcat lib路径
    private String tomcatLib;
    //第三方 lib路径
    private String otherLib;
    //source Path路径
    private String sourcePath;
    //outpath路径
    private String outPath;
    //pomFile路径
    private String pomFile;
    //javaFiles路径
    private String javaFiles;

    public String getJavaFiles() {
        return javaFiles;
    }

    public void setJavaFiles(String javaFiles) {
        this.javaFiles = javaFiles;
    }

    public String getTomcatLib() {
        return tomcatLib;
    }

    public void setTomcatLib(String tomcatLib) {
        this.tomcatLib = tomcatLib;
    }

    public String getOtherLib() {
        return otherLib;
    }

    public void setOtherLib(String otherLib) {
        this.otherLib = otherLib;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getOutPath() {
        return outPath;
    }

    public void setOutPath(String outPath) {
        this.outPath = outPath;
    }

    public List<String> getPackFiles() {
        return packFiles;
    }

    public void setPackFiles(List<String> packFiles) {
        this.packFiles = packFiles;
    }

    public String getDestPackPath() {
        return destPackPath;
    }

    public void setDestPackPath(String destPackPath) {
        this.destPackPath = destPackPath;
    }

    public String getSrcPath() {
        return srcPath;
    }

    public void setSrcPath(String srcPath) {
        this.srcPath = srcPath;
    }

    public String getResourcesPath() {
        return resourcesPath;
    }

    public void setResourcesPath(String resourcesPath) {
        this.resourcesPath = resourcesPath;
    }

    public String getProtype() {
        return protype;
    }

    public void setProtype(String protype) {
        this.protype = protype;
    }

    public String getPropath() {
        return propath;
    }

    public void setPropath(String propath) {
        this.propath = propath;
    }

    public String getWrPath() {
        return wrPath;
    }

    public void setWrPath(String wrPath) {
        this.wrPath = wrPath;
    }

    public String getCompilePath() {
        return compilePath;
    }

    public void setCompilePath(String compilePath) {
        this.compilePath = compilePath;
    }

    public String getPacktime() {
        return packtime;
    }

    public void setPacktime(String packtime) {
        this.packtime = packtime;
    }

}
