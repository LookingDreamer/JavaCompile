package com.pack;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 打包工具类
 * author: cjianquan
 * date: 2016/9/2
 */
public class PackUtils {

    public static String srcmain = "\\src\\main\\";

    public static String src = "\\src\\";


    public static String resources = "\\resources";//资源文件目录
    static String webapp = "\\webapp";//web应用目录
    static String target = "\\target\\classes";//编译路径目录
    public static String webinfocls="\\WEB-INF\\classes";

    public static void main(String[] args) {
        String s1 = "PackController.class";
        String s2 = "PackController$TestCon.class";
        System.out.println(System.getProperty("user.dir"));
        System.out.println(s1.contains("PackController"+".class"));
        System.out.println(s2.matches("PackController"+"\\$.*\\.class"));
    }

    /**
     * 获取文件修改时间>打包时间的文件
     * cjianquan 2016/9/6
     * @param apppath 项目根目录
     * @param packtime 打包时间，即文件修改时间>打包时间的文件进行打包
     * @param fileList 文件修改时间>打包时间的文件存放在该列表
     */
    public static void getUpdatefiles(String apppath,String packtime,List<String> fileList){
        apppath+="\\src\\main\\";
//        apppath +="\\WebRoot";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            getUpdateFiles(apppath, sdf.parse(packtime), fileList);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    /**
     * 打包文件
     * cjianquan 2016/9/6
     * @param packList 需要打包的文件列表
     * @param apppath 项目根目录
     * @param webName 项目的名称
     * @param destPath 打包后文件的存放目录
     */
    public static void packFiles(List<String> packList,String propath,String webName,String destPath) throws Exception{
        try {
//            target = "\\out\\artifacts\\qzvip_Web_exploded\\WEB-INF\\classes";
            String deployPath = propath + target ;
            for(String pack:packList){
                if(pack.endsWith(".java")){
                    //将对应的class文件复制到destPath/WEB-INF/classes/
                    String javaPath = deployPath+getMiddlePath(pack,webName);
                    File javaFile = new File(javaPath);
                    String javaName = getFilenameWithoutExt(pack);
                    for(File tmpFile:javaFile.listFiles()){
                        if(tmpFile.getName().contains(javaName+".class") || tmpFile.getName().matches(javaName + "\\$.*\\.class")){
                            org.apache.commons.io.FileUtils.copyFileToDirectory(tmpFile,new File(destPath + webinfocls + getMiddlePath(pack, webName)));
                        }
                    }
                }else if(pack.contains(resources)){
                    //将该文件复制到destPath/WEB-INF/classes/中
                    org.apache.commons.io.FileUtils.copyFileToDirectory(new File(pack),new File(destPath+webinfocls+getMiddlePath(pack,webName)));
                }else if(pack.contains(webapp)){
                    //将该文件复制到destPath中
                    org.apache.commons.io.FileUtils.copyFileToDirectory(new File(pack),new File(destPath+getMiddlePath(pack,webName)));
                }else if(pack.contains("WebRoot")){
                    //将该文件复制到destPath中
                    org.apache.commons.io.FileUtils.copyFileToDirectory(new File(pack),new File(destPath+getMiddlePath(pack,webName)));
                }
            }
        } catch (Exception e) {
           e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 获取java文件的名称，不带文件扩展名,即类名
     * cjianquan 2016/9/6
     * @param file 文件全路径
     * @return
     */
    public static String getFilenameWithoutExt(String file){
        String r = file.substring(file.lastIndexOf("\\")+1,file.lastIndexOf(".java"));
        return r;
    }

    /**
     * 获取文件的中间路径
     * cjianquan 2016/9/6
     * @param file 文件的全路径
     * @param webName 项目名称
     * @return
     */
    public static String getMiddlePath(String file,String webName){
        String javaCheck=webName+"\\src\\main\\java";
        String resoucesCheck=webName+"\\src\\main"+resources;
        String webappCheck=webName+"\\src\\main"+webapp;

        String r = "";
        if(file.endsWith(".java")){
            r = file.substring(file.indexOf(javaCheck),file.lastIndexOf("\\")).replace(javaCheck, "");
        }else if(file.contains(resources)){
            r = file.substring(file.indexOf(resoucesCheck),file.lastIndexOf("\\")).replace(resoucesCheck, "");
        }else if(file.contains(webapp)){
            r = file.substring(file.indexOf(webappCheck),file.lastIndexOf("\\")).replace(webappCheck, "");
        }else if(file.contains("WebRoot")){
            r = file.substring(file.indexOf(webName+"\\WebRoot"),file.lastIndexOf("\\")).replace(webName+"\\WebRoot", "");
        }
        return r;
    }




    /**
     * 获取在指定日期updateDate后修改过的文件列表
     * 文件名放入resultFileNameList
     * @param rootFile webRoot的路径
     * @param updateDate 更新的日期
     * @param resultFileNameList 更新的文件清单
     * @author cjianquan
     * 2015-2-6
     */
    public static void getUpdateFiles(String rootFile,Date updateDate,List<String> resultFileNameList){
//        System.out.println("-----------------------");
        File rFile = new File(rootFile);
        File[] fileArr = rFile.listFiles();
        long lastModifiedTime = 0;
        Date lastModifiedDate = null;
        for(File file:fileArr){
            if(file.isDirectory()){//是目录，则递归查找该目录
                getUpdateFiles(file.getAbsolutePath(),updateDate,resultFileNameList);
            }else if(file.isFile()){//是文件
                lastModifiedTime = file.lastModified();
                lastModifiedDate = new Date(lastModifiedTime);
                if(updateDate.compareTo(lastModifiedDate)<0){//最后更新时间大于给定的时间
                    //文件名称放进resultFileNameList
                    resultFileNameList.add(file.getAbsolutePath());
                }
            }
        }
    }



    /**
     * 获取目录下的lib文件
     * 文件名放入resultFileNameList
     * @param rootFile webRoot的路径
     * @ignoreDir 忽略的目录
     * @param updateDate 更新的日期
     * @param resultFileNameList 更新的文件清单
     */
    // cjianquan 2016/11/11
    public static void getLibFiles(String rootFile,String ignoreDir,Date updateDate,List<String> resultFileNameList){
        System.out.println("=========");
        File rFile = new File(rootFile);
        File[] fileArr = rFile.listFiles();
        long lastModifiedTime = 0;
        Date lastModifiedDate = null;
        for(File file:fileArr){
            if(file.isDirectory()){//是目录，则递归查找该目录
                getUpdateFiles(file.getAbsolutePath(),updateDate,resultFileNameList);
            }else if(file.isFile()){//是文件
                lastModifiedTime = file.lastModified();
                lastModifiedDate = new Date(lastModifiedTime);
                if(updateDate.compareTo(lastModifiedDate)<0){//最后更新时间大于给定的时间
                    //文件名称放进resultFileNameList
                    resultFileNameList.add(file.getAbsolutePath());
                }
            }
        }
    }



}
