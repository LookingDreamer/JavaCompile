package com.pack;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 打包工具类
 * author: cjianquan
 * date: 2016/9/2
 */
public class PackUtils {
    private static Logger logger = LoggerFactory.getLogger("PackUtils");

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
//     * @param apppath 项目根目录
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
     * 获取lib文件
     * 文件名放入resultFileNameList
     * @param rootFile lib路径
     * @author huanggaoming
     * 2017-08-23
     */
    public static void getLibFiles(String rootFile,List<String> resultFileNameList){
        File rFile = new File(rootFile);
        File[] fileArr = rFile.listFiles();
        for(File file:fileArr){
            if(file.isDirectory()){//是目录，则递归查找该目录
                getLibFiles(file.getAbsolutePath(),resultFileNameList);
            }else if(file.isFile()){//是文件
                //文件名称放进resultFileNameList
                resultFileNameList.add(file.getAbsolutePath());

            }
        }
    }

    /**
     * 判断是否存在file
     * @param rootFile lib路径,可以是目录或者文件
     * @author huanggaoming
     * 2017-08-23
     */
    public static boolean  getFileTrue(String rootFile){
        logger.info("开始处理: "+rootFile);
        //判断是否含有特殊符号',',如果有则分割成list,然后在判断
        String regEx = ",";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(rootFile);
        if(m.find()) {
            logger.info("开始,号的处理");
            String[] arrayStr =new String[]{};
            arrayStr = rootFile.split(",");
            for (String s : arrayStr) {
                logger.info("分割后文件:"+s);
                File sfile = new File(s);
                if ( sfile.isDirectory()){
                }else if(sfile.isFile()){
                }else{
                    logger.info("返回为假,文件或者目录不存在:"+s);
                    return  false;
                }
            }
            logger.info("结束,号的处理");
            return  true;
        }else {

            File file = new File(rootFile);
            if (file.isFile()) {
                return true;
            } else {
                return false;
            }
        }


    }


    /**
     * 判断是否存在directory
     * @param rootFile lib路径,可以是目录或者文件
     * @author huanggaoming
     * 2017-08-23
     */
    public static boolean  getDirectoryTrue(String rootFile){
        logger.info("开始处理: "+rootFile);
        File dfile = new File(rootFile);
        if (dfile.isDirectory()) {
            return true;
        }
        return  false;
    }

    /**
     * 打印并保存异常信息
     * @author huanggaoming
     * 2017-08-23
     */
    public static void printCallStatck(Throwable ex, List<Map<String, String>> result) {

        StackTraceElement[] stackElements = ex.getStackTrace();
        if (stackElements != null) {
            for (int i = 0; i < stackElements.length; i++) {
                //只打印和保存与本程序有关的错误信息
                if(stackElements[i].getClassName().startsWith("com.pack")) {
                    HashMap<String, String> res = new HashMap<String, String>();
                    logger.info(" ");
                    logger.info("getClassName: " + stackElements[i].getClassName() + "    ");
                    logger.info("getFileName: " + stackElements[i].getFileName() + "     ");
                    logger.info("getLineNumber: " + stackElements[i].getLineNumber() + "    ");
                    logger.info("getMethodName: " + stackElements[i].getMethodName());
                    res.put("ClassName", stackElements[i].getClassName());
                    res.put("FileName", stackElements[i].getFileName());
                    res.put("LineNumber", "" + stackElements[i].getLineNumber());
                    res.put("MethodName", stackElements[i].getMethodName());
                    result.add(res);
                }
            }
        }
    }

    /**
     * 将异常堆栈转换为字符串
     * @param aThrowable 异常
     * @return String
     */
    public static String getStackTrace(Throwable aThrowable) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        aThrowable.printStackTrace(printWriter);
        return result.toString();
    }


    /**
     * 删除空目录
     * @param dir 将要删除的目录路径
     */
    public static void doDeleteEmptyDir(String dir) {
        boolean success = (new File(dir)).delete();
        if (success) {
            System.out.println("Successfully deleted empty directory: " + dir);
        } else {
            System.out.println("Failed to delete empty directory: " + dir);
        }
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
//递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }


    /**
     * 判断是否是windows
     * @author huanggaoming
     * 2017-08-23
     */
    public static boolean  isWindowsTrue(){
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.indexOf("windows")>=0){
            return  true;
        }else{
            return  false;
        }
    }

}
