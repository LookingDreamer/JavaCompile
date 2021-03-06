package com.pack;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * author: huanggaoming
 * date: 2017/09/17
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

    /**
     * 获取两个List的不同元素
     * @param list1
     * @param list2
     * @return
     */
    public static List<String> getDiffrent(List<String> list1, List<String> list2) {
        long st = System.nanoTime();
        Map<String,Integer> map = new HashMap<String,Integer>(list1.size()+list2.size());
        List<String> diff = new ArrayList<String>();
        for (String string : list1) {
            map.put(string, 1);
        }
        for (String string : list2) {
            Integer cc = map.get(string);
            if(cc!=null)
            {
                map.put(string, ++cc);
                continue;
            }
            map.put(string, 1);
        }
        for(Map.Entry<String, Integer> entry:map.entrySet())
        {
            if(entry.getValue()==1)
            {
                diff.add(entry.getKey());
            }
        }
        return list1;
    }


    /**
     * 时间戳转换成日期格式字符串
     * @param seconds 精确到秒的字符串
     * @param formatStr
     * @return
     */
    public static String timeStamp2Date(String seconds,String format) {
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
            return "";
        }
        if(format == null || format.isEmpty()){
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds+"000")));
    }
    /**
     * 日期格式字符串转换成时间戳
     * @param date 字符串日期
     * @param format 如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String date2TimeStamp(String date_str,String format){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(date_str).getTime()/1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 取得当前时间戳（精确到秒）
     * @return
     */
    public static String timeStamp(){
        long time = System.currentTimeMillis();
        String t = String.valueOf(time/1000);
        return t;
    }

    /**
     * 判断日期格式:yyyy-mm-dd
     *
     * @param sDate
     * @return
     */
    public static boolean isValidDate(String sDate) {
        String datePattern1 = "\\d{4}-\\d{2}-\\d{2}";
        String datePattern2 = "^((\\d{2}(([02468][048])|([13579][26]))"
                + "[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|"
                + "(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?"
                + "((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?("
                + "(((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?"
                + "((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
        if ((sDate != null)) {
            Pattern pattern = Pattern.compile(datePattern1);
            Matcher match = pattern.matcher(sDate);
            if (match.matches()) {
                pattern = Pattern.compile(datePattern2);
                match = pattern.matcher(sDate);
                return match.matches();
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * 功能：判断字符串是否为日期格式
     *
     * @param strDate
     * @return
     */
    public static boolean isDate(String strDate) {
        Pattern pattern = Pattern
                .compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
        Matcher m = pattern.matcher(strDate);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }


    public static HashMap<String, String> runcmd(PackBean packBean)
    {
        String cmd = packBean.getCmd();
        HashMap<String, String> result = new HashMap<String, String>();
        String propath = packBean.getPropath();
        if (PackUtils.isWindowsTrue()){
            cmd ="cmd.exe /c "+cmd;
        }
        logger.info("执行cmd:"+cmd);
        File dir = null;
        if ( propath == null || propath.equals("")){
            dir = null;
        }else{
            dir = new File(propath);
        }

        try
        {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(cmd, null, dir);
            StringBuffer inputStr = new StringBuffer();
            InputStream input = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(input);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null)
                inputStr.append(line + "\n");
                System.out.println(line);
                System.out.flush();

            StringBuffer errorStr = new StringBuffer();
            InputStream errorinput = proc.getErrorStream();
            InputStreamReader errorisr = new InputStreamReader(errorinput);
            BufferedReader errorbr = new BufferedReader(errorisr);
            String errotline = null;
            while ((errotline = errorbr.readLine()) != null)
                errorStr.append(errotline + "\n");
                System.out.println(errotline);
                System.out.flush();

            int exitVal = proc.waitFor();
            result.put("exitcode",""+exitVal);
            result.put("inputStr",inputStr.toString());
            result.put("errorStr",errorStr.toString());
            logger.info("执行返回值exitVal:"+exitVal);
            logger.info("执行返回信息inputStr:\n\r"+inputStr.toString());
            logger.info("执行返回信息errorStr:\n\r"+errorStr.toString());
        } catch (Throwable  e)
        {
            result.put("exitcode","999");
            result.put("msg","执行异常");
            String errorString = PackUtils.getStackTrace(e);
            result.put("stack",errorString);
            e.printStackTrace();
        }
        return  result;
    }
}
