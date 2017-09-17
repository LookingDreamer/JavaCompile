package com.pack;

import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * author: huanggaoming
 * date: 2017/09/17
 */
public class PackBean implements java.io.Serializable{
    //项目类型
    private String protype;
    //项目路径
    private String propath;
    //WebRoot路径(相对于项目路径) webapp eg: /src/main/webapp
    private String wrPath;
    //class编译路径(相对于项目路径) class路径
    private String compilePath;
    //时间:yyyy-MM-dd HH:mm:ss
    private String packtime;
    //源码路径(相对于项目路径) java路径 eg: /src/main/java
    private String srcPath;
    //配置资料文件路径(相对于项目路径) resource路径 eg: /src/main/resources
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
    //svn url
    private String svnUrl;
    //svn 用户名
    private String svnUsername;
    //svn 密码
    private String svnPassword;
    //svn 项目相对路径
    private String svnProjectSuffix;
    //svn开始日期
    private String startDate;
     //svn结束日期
    private String endDate;
     //svn开始版本号
    private String startRevision;
     //svn结束版本号
    private String endRevision;
    //版本号列表,以,作为分隔符号
    private String Revisions;
    //增量后打包路径
    private String addFilesPath;
    //全量后打包路径
    private String targetPath;
    //mvn命令路径
    private String mvnPath;
    //mvn setting文件
    private String mvnSettingFile;
    //cmd命令
    private String cmd;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getMvnPath() {
        return mvnPath;
    }

    public void setMvnPath(String mvnPath) {
        this.mvnPath = mvnPath;
    }

    public String getMvnSettingFile() {
        return mvnSettingFile;
    }

    public void setMvnSettingFile(String mvnSettingFile) {
        this.mvnSettingFile = mvnSettingFile;
    }

    public String getAddFilesPath() {
        return addFilesPath;
    }

    public void setAddFilesPath(String addFilesPath) {
        this.addFilesPath = addFilesPath;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public String getRevisions() {
        return Revisions;
    }

    public void setRevisions(String revisions) {
        Revisions = revisions;
    }

    public String getPomFile() {
        return pomFile;
    }

    public void setPomFile(String pomFile) {
        this.pomFile = pomFile;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartRevision() {
        return startRevision;
    }

    public void setStartRevision(String startRevision) {
        this.startRevision = startRevision;
    }

    public String getEndRevision() {
        return endRevision;
    }

    public void setEndRevision(String endRevision) {
        this.endRevision = endRevision;
    }

    public String getSvnProjectSuffix() {
        return svnProjectSuffix;
    }

    public void setSvnProjectSuffix(String svnProjectSuffix) {
        this.svnProjectSuffix = svnProjectSuffix;
    }

    public String getSvnPassword() {
        return svnPassword;
    }

    public void setSvnPassword(String svnPassword) {
        this.svnPassword = svnPassword;
    }

    public String getSvnUsername() {
        return svnUsername;
    }

    public void setSvnUsername(String svnUsername) {
        this.svnUsername = svnUsername;
    }

    public String getSvnUrl() {
        return svnUrl;
    }

    public void setSvnUrl(String svnUrl) {
        this.svnUrl = svnUrl;
    }

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
