package com.pack;

import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.lang.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.rmi.CORBA.Util;
import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
import java.util.regex.*;
import  java.nio.file.StandardCopyOption.*;
import org.apache.commons.codec.digest.*;
import org.apache.commons.io.IOUtils;
import java.io.File;
import java.io.FileInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;


/**
 * author: cjianquan
 * date: 2016/11/11
 */
@Service("packService")
public class PackService {

    @Value("${svn.url}")
    public String svnUrl;
    @Value("${svn.username}")
    public String svnUsername;
    @Value("${svn.password}")
    public String svnPassword;
    @Value("${svn.project_suffix}")
    public String svnProjectSuffix;
    @Value("${svn.interval_days}")
    public  String svnIntervalDays;
    @Value("${src.java}")
    public String srcJava;
    @Value("${src.resources}")
    public String srcResources;
    @Value("${src.webapp}")
    public String srcWebapp;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public boolean packFiles(PackBean packBean) throws Exception {
        String propath = packBean.getPropath();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String clazzPath = propath + "\\" + sdf.format(new Date());
        File clazzFile = new File(clazzPath);
        // cjianquan 2016/9/6
        //删除目录
        try {
            org.apache.commons.io.FileUtils.deleteDirectory(clazzFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        clazzFile.mkdirs();
        if (packBean.getDestPackPath() == null || "".equals(packBean.getDestPackPath())) {
            packBean.setDestPackPath(clazzPath);
        }
        try {
            for (String i_pack : packBean.getPackFiles()) {
                if (i_pack.endsWith(".java")) {
                    String javaName = PackUtils.getFilenameWithoutExt(i_pack);
                    String packagePath = i_pack.substring(i_pack.indexOf(packBean.getSrcPath()) + packBean.getSrcPath().length(), i_pack.lastIndexOf("\\"));
                    String javaPath = packBean.getPropath() + packBean.getCompilePath() + packagePath;
                    File javaFile = new File(javaPath);
                    System.out.println(" javaFile:" + javaFile);
                    for (File tmpFile : javaFile.listFiles()) {
                        if (tmpFile.getName().contains(javaName + ".class") || tmpFile.getName().matches(javaName + "\\$.*\\.class")) {
                            org.apache.commons.io.FileUtils.copyFileToDirectory(tmpFile, new File(packBean.getDestPackPath() + PackUtils.webinfocls + packagePath));
                        }
                    }
                    System.out.println(" end javaPath:" + javaPath);
                } else if (i_pack.contains(PackUtils.resources)) {
                    //将该文件复制到WebRoot/WEB-INF/classes/中
                    String packagePath = i_pack.substring(i_pack.indexOf(packBean.getResourcesPath()) + packBean.getResourcesPath().length(), i_pack.lastIndexOf("\\"));
                    org.apache.commons.io.FileUtils.copyFileToDirectory(new File(i_pack), new File(packBean.getDestPackPath() + PackUtils.webinfocls + packagePath));
                } else if (i_pack.contains(packBean.getWrPath())) {
                    //将该文件复制到WebRoot中
                    String packagePath = i_pack.substring(i_pack.indexOf(packBean.getWrPath()) + packBean.getWrPath().length(), i_pack.lastIndexOf("\\"));
                    org.apache.commons.io.FileUtils.copyFileToDirectory(new File(i_pack), new File(packBean.getDestPackPath() + packagePath));
                } else {
                    String packagePath = i_pack.substring(i_pack.indexOf(packBean.getSrcPath()) + packBean.getSrcPath().length(), i_pack.lastIndexOf("\\"));
                    org.apache.commons.io.FileUtils.copyFileToDirectory(new File(i_pack), new File(packBean.getDestPackPath() + PackUtils.webinfocls + packagePath));
                }
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
        return true;
    }

    public List<String> getUpdatefiles(PackBean packBean) throws Exception {
        List<String> fileList = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            System.out.println("开始获取更新文件....");
            Date packTime = sdf.parse(packBean.getPacktime());
            System.out.println("更新日期:" + packBean.getPacktime() + "   getPropath:" + packBean.getPropath() + "    getSrcPath:" + packBean.getSrcPath() + "");
            //获取src文件
            PackUtils.getUpdateFiles(packBean.getPropath() + packBean.getSrcPath(), packTime, fileList);
            //获取resource文件
            PackUtils.getUpdateFiles(packBean.getPropath() + packBean.getResourcesPath(), packTime, fileList);

//            PackUtils.getUpdateFiles(packBean.getPropath() + packBean.getWrPath(),packBean.getCompilePath(),packTime,fileList);
        } catch (ParseException e) {
            System.out.println("发生了异常");
            throw new Exception(e);
        } catch (Exception e) {
            throw new Exception(e);
        }

        return fileList;
    }

    public HashMap<String, String> javaComplier(PackBean packBean,List<Map<String, String>> stackList) throws Exception {
        HashMap<String, String> result = new HashMap<String, String>();
        List<String> fileList = new ArrayList<String>();
        logger.info("开始参数判断....");
        String tomcatLib = packBean.getTomcatLib();
        String otherLib = packBean.getOtherLib();
        String sourcePath = packBean.getSourcePath();
        String outPath = packBean.getOutPath();
        String javaFiles = packBean.getJavaFiles();
        if (tomcatLib == null || tomcatLib.equals("")) {
            logger.error("tomcat lib路径不能为空");
            result.put("status", "6");
            result.put("msg", "tomcatLib路径不能为空");
            return result;
        }
        if (!PackUtils.getDirectoryTrue(tomcatLib)) {
            logger.error("tomcat lib:" + tomcatLib + " 不存在");
            result.put("status", "16");
            result.put("msg", "tomcat lib:" + tomcatLib + " 不存在");
            return result;
        }
        if (otherLib == null || otherLib.equals("")) {
            logger.error("第三方lib路径不能为空");
            result.put("status", "7");
            result.put("msg", "otherLib路径不能为空");
            return result;
        }
        if (!PackUtils.getDirectoryTrue(otherLib)) {
            logger.error("otherLib:" + otherLib + " 不存在");
            result.put("status", "17");
            result.put("msg", "otherLib:" + otherLib + " 不存在");
            return result;
        }
        if (sourcePath == null || sourcePath.equals("")) {
            logger.error("sourcePath路径不能为空");
            result.put("status", "8");
            result.put("msg", "sourcePath路径不能为空");
            return result;
        }
        if (!PackUtils.getDirectoryTrue(sourcePath)) {
            logger.error("sourcePath:" + sourcePath + " 不存在");
            result.put("status", "18");
            result.put("msg", "sourcePath:" + sourcePath + " 不存在");
            return result;
        }
        if (outPath == null || outPath.equals("")) {
            logger.error("outPath路径不能为空");
            result.put("status", "9");
            result.put("msg", "outPath路径不能为空");
            return result;
        }
        File dir = new File(outPath);
        if (!dir .exists()  && !dir .isDirectory() ) {
            logger.info("outPath:" + outPath + " 不存在,开始创建.");
            if (dir.mkdirs()) {
                logger.info("创建目录" + outPath + "成功！");
            } else {
                logger.error("创建目录" + outPath + "失败！");
                result.put("status", "19");
                result.put("msg", "创建目录" + outPath + "失败！");
                return result;
            }

        }
        if (javaFiles == null || javaFiles.equals("")) {
            logger.error("javaFile路径不能为空");
            result.put("status", "10");
            result.put("msg", "javaFiles路径不能为空");
            return result;
        }
        if (!PackUtils.getFileTrue(javaFiles)) {
            logger.error("javaFiles:" + javaFiles + " 不存在");
            result.put("status", "19");
            result.put("msg", "javaFiles:" + javaFiles + " 不存在");
            return result;
        }
        logger.info("结束参数判断....");
        logger.info("打印参数 tomcatLib:" + tomcatLib + " otherLib:" + otherLib + " sourcePath:" + sourcePath + " outPath:" + outPath + " javaFiles:" + javaFiles);
        result.put("tomcatLib", tomcatLib);
        result.put("otherLib", otherLib);
        result.put("sourcePath", sourcePath);
        result.put("outPath", outPath);
        result.put("javaFiles", javaFiles);
        logger.info("结束参数判断....");

        logger.info("开始获取lib文件....");
        //获取tomcat lib文件
        List<String> tomcatFileList = new ArrayList<String>();
        PackUtils.getLibFiles(tomcatLib, tomcatFileList);
        String tomcatLibString;
        if (PackUtils.isWindowsTrue()){
            tomcatLibString = StringUtils.join(tomcatFileList, ";");
        }else{
            tomcatLibString = StringUtils.join(tomcatFileList, ":");
        }

//        logger.info("tomcatLibString: "+tomcatLibString);

        //获取第三方lib文件
        List<String> otherFileList = new ArrayList<String>();
        PackUtils.getLibFiles(otherLib, otherFileList);
        String otherLibString;
        if (PackUtils.isWindowsTrue()){
            otherLibString = StringUtils.join(otherFileList, ";");
        }else {
            otherLibString = StringUtils.join(otherFileList, ":");
        }

//        logger.info("otherLibString: "+otherLibString);

        //合并lib文件
        String libPath;
        if (PackUtils.isWindowsTrue()) {
            libPath = tomcatLibString + ";" + otherLibString;
        }else{
            libPath = tomcatLibString + ":" + otherLibString;
        }
        logger.info("结束获取lib文件....");

        //转换javaFile为list
        String[] javaFileList = new String[]{};
        javaFileList = javaFiles.split(",");
        Integer javaFileCount = javaFileList.length;
        result.put("javaFileCount",""+javaFileCount);

        //清空outPath目录下文件

        File ofile = new File(outPath);
        if (ofile.isDirectory()) {
            String[] files = ofile.list();

            if (files.length > 0) {
                logger.info(ofile.getPath()+"目录不为空,开始清空文件,包含文件个数："+files.length);
                boolean success = PackUtils.deleteDir(ofile);
                if (success) {
                    logger.info("成功清空目录下文件: " + outPath);
                    ofile.mkdirs();
                } else {
                    logger.error("失败清空目录下文件: " + outPath);
                    result.put("status", "49");
                    result.put("msg", "失败删除目录: " + outPath);
                    return result;
                }
            }
        }
        Boolean suc;
        try {

            String[] getCompilePath = javaFileList;
            logger.info("获取编译文件:" + javaFiles);

            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler(); // 返回java 编译器
            if (compiler == null) {
                logger.error("JDK required (running inside of JRE)");
                result.put("status", "50");
                result.put("msg", "JDK required (running inside of JRE)");
                return result;
            }
            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
            StandardJavaFileManager manager = compiler.getStandardFileManager(diagnostics, Locale.CHINA, Charset.forName("UTF-8"));
            Iterable<? extends JavaFileObject> compilationUnits = manager.getJavaFileObjects(getCompilePath);
            List<String> optionList = new ArrayList<String>();
            String newClassPath = libPath;
            optionList.addAll(Arrays.asList("-sourcepath", sourcePath));
            optionList.addAll(Arrays.asList("-d", outPath));
            optionList.add("-cp");
            optionList.add(newClassPath);

            JavaCompiler.CompilationTask task = compiler.getTask(null, manager, diagnostics, optionList, null, compilationUnits);
            // 如果没有编译警告和错误,这个call() 方法会编译所有的 compilationUnits 变量指定的文件,以及有依赖关系的可编译的文件.
            suc = task.call();

            if (!suc) {
                for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
                    HashMap<String,String> stack = new HashMap<String,String>();
                    stack.put("Code",""+diagnostic.getCode());
                    stack.put("Kind",""+diagnostic.getKind());
                    stack.put("Position",""+diagnostic.getPosition());
                    stack.put("Start",""+diagnostic.getStartPosition());
                    stack.put("End",""+diagnostic.getEndPosition());
                    stack.put("Source",""+diagnostic.getSource());
                    stack.put("Message",""+diagnostic.getMessage(null));
                    stackList.add(stack);

                }
            }
            manager.close();

        } catch (Exception e) {
            suc = false;
            result.put("status", "66");
            result.put("msg", "执行编译异常");
            logger.error("执行编译异常");
            logger.error(e.getMessage(), e);
            String errorString = PackUtils.getStackTrace(e);
            result.put("stack",errorString);
            return  result;
        }

        if (suc){
            result.put("status", "1");
            result.put("msg", "编译成功");
            logger.info("编译成功");
            //获取编译成功后JAVA class文件
            List<String> javaClassFileList = new ArrayList<String>();
            PackUtils.getLibFiles(outPath, javaClassFileList);
            Integer javaClassFileCount = javaClassFileList.size();
            result.put("javaClassFileCount",""+javaClassFileCount);
            String javaClassFile = StringUtils.join(javaClassFileList, ",");
            result.put("javaClassFile",javaClassFile);
            String javaClassFileChangeJavaFile = javaClassFile.replace(".class", ".java");
            String[] javaToClassFileList = new String[]{};
            javaToClassFileList = javaClassFileChangeJavaFile.split(",");
            logger.info("javaFileList:"+javaFileList);
            logger.info("javaToClassFileList:"+javaToClassFileList);
            String srcJavaToClassFile = javaFiles.replace(".java", ".class");
            String[] srcJavaToClassFileList = new String[]{};
            srcJavaToClassFileList = srcJavaToClassFile.split(",");
            List<String> javaClassFileList1 = new ArrayList<String>();
            javaClassFileList1  = javaClassFileList;
            for ( String v :srcJavaToClassFileList){
                File tempFile =new File( v.trim());
                String fileName = tempFile.getName();
                logger.info(fileName);
                Iterator<String> sListIterator = javaClassFileList1.iterator();
                while(sListIterator.hasNext()){
                    String e = sListIterator.next();
                    if(e.endsWith(fileName)){
                        logger.info("删除相同的元素:"+e);
                        sListIterator.remove();
                    }
                }

            }
            String dependClassCount = ""+javaClassFileList1.size();
            String dependClass = StringUtils.join(javaClassFileList1, ",");
            result.put("dependClassCount",dependClassCount);
            result.put("dependClass",dependClass);
            logger.info("依赖class数量:"+dependClassCount+"依赖class文件:"+dependClass);

        }else{
            result.put("status", "90");
            result.put("msg", "编译失败");
            logger.error("编译失败");
        }

        return result;
    }


    public HashMap<String, String> getCommitInfo(PackBean packBean,List<Map<String, String>> commitList) throws Exception {
        HashMap<String, String> result = new HashMap<String, String>();
        List<String> fileList = new ArrayList<String>();
        //如果没有传参则从配置文件获取缺省配置
        logger.info("开始参数判断....");
        String resvnUrl = packBean.getSvnUrl();
        String resvnUsername = packBean.getSvnUsername();
        String resvnPassword = packBean.getSvnPassword();
        String resvnProjectSuffix = packBean.getSvnProjectSuffix();
        if (resvnUrl == null || resvnUrl.equals("")) {
            resvnUrl = svnUrl + svnProjectSuffix;
        }
        if (resvnUsername == null || resvnUsername.equals("")) {
            resvnUsername = svnUsername;
        }

        if (resvnPassword == null || resvnPassword.equals("")) {
            resvnPassword = svnPassword;
        }

        if (resvnProjectSuffix == null || resvnProjectSuffix.equals("")) {
            resvnProjectSuffix = svnProjectSuffix;
        }
        logger.info("结束参数判断....");
        logger.info("打印参数 resvnUrl:" + resvnUrl + " resvnUsername:" + resvnUsername + " resvnPassword:" + resvnPassword + " resvnProjectSuffix:" + resvnProjectSuffix );

        SVNClientManager clientManager = SVNUtil.authSvn(resvnUrl, resvnUsername, resvnPassword);
        if (clientManager == null || clientManager.equals("")) {
            logger.error("登陆svn失败");
            result.put("status", "6");
            result.put("msg", "登陆svn失败");
            return result;
        }
        logger.info("登陆svn成功");

        try
        {
            logger.info("开始获取svn历史记录");
            List<String> res = new ArrayList<String>();
//            List<Map<String, String>> commitList = new ArrayList();
            res = SVNUtil.filterCommitHistory(packBean,commitList,svnIntervalDays);
            if (res.get(0) == "2"){
                result.put("status","6");
                result.put("msg","svn开始日期格式不正确");
                return  result;
            }
            if (res.get(0) == "3"){
                result.put("status","7");
                result.put("msg","svn结束日期格式不正确");
                return  result;
            }
            logger.info("结束获取svn历史记录");
            result.put("status","1");
            result.put("msg","获取成功");
        }
        catch(Exception e)
        {
            String errorString = PackUtils.getStackTrace(e);
            result.put("stack",errorString);
            logger.error("获取svn提交历史异常");
            result.put("status", "8");
            result.put("msg", "获取svn提交历史异常");
            return result;
        }
        return result;

    }



    public HashMap<String, String> updateSvnDirectory(PackBean packBean,List<Map<String, String>> commitList) throws Exception {
        HashMap<String, String> result = new HashMap<String, String>();
        List<String> fileList = new ArrayList<String>();
        //如果没有传参则从配置文件获取缺省配置
        logger.info("开始参数判断....");
        String resvnUrl = packBean.getSvnUrl();
        String resvnUsername = packBean.getSvnUsername();
        String resvnPassword = packBean.getSvnPassword();
        String resvnProjectSuffix = packBean.getSvnProjectSuffix();
        String propath = packBean.getPropath();
        String revisions = packBean.getRevisions();
        String wrPath = packBean.getWrPath();
        String srcPath = packBean.getSrcPath();
        String resourcesPath = packBean.getResourcesPath();
        String addFilesPath = packBean.getAddFilesPath();
        if (propath == null || propath.equals("")) {
            logger.error("propath路径不能为空");
            result.put("status", "10");
            result.put("msg", "propath路径不能为空");
            return result;
        }
        if (revisions == null || revisions.equals("")) {
            logger.error("revisions不能为空");
            result.put("status", "11");
            result.put("msg", "revisions不能为空");
            return result;
        }
        if (addFilesPath == null || addFilesPath.equals("")) {
            logger.error("addFilesPath不能为空");
            result.put("status", "12");
            result.put("msg", "addFilesPath不能为空");
            return result;
        }

        File rfile = new File(propath);
        if (!rfile.exists()  && !rfile.isDirectory() ) {
            logger.info("propath:" + propath + " 不存在,开始创建.");
            if (rfile.mkdirs()) {
                logger.info("创建目录" + propath + "成功！");
            } else {
                logger.error("创建目录" + propath + "失败！");
                result.put("status", "29");
                result.put("msg", "创建目录" + propath + "失败！");
                return result;
            }

        }else{
            logger.info("本地项目路径:"+propath+"已经存在");
        }
        File afile = new File(addFilesPath);
        if (!afile.exists()  && !afile.isDirectory() ) {
            logger.info("addFilesPath:" + addFilesPath + " 不存在,开始创建.");
            if (afile.mkdirs()) {
                logger.info("创建目录" + addFilesPath + "成功！");
            } else {
                logger.error("创建目录" + addFilesPath + "失败！");
                result.put("status", "39");
                result.put("msg", "创建目录" + addFilesPath + "失败！");
                return result;
            }

        }else{
            logger.info("增量打包路径:"+addFilesPath+"已经存在");
            String[] afiles = afile.list();
            if (afiles.length > 0) {
                logger.info(afile.getPath()+"目录不为空,开始清空文件,包含文件个数："+afiles.length);
                boolean success = PackUtils.deleteDir(afile);
                if (success) {
                    logger.info("成功清空目录下文件: " + addFilesPath);
                    afile.mkdirs();
                } else {
                    logger.error("失败清空目录下文件: " + addFilesPath);
                    result.put("status", "59");
                    result.put("msg", "失败删除目录: " + addFilesPath);
                    return result;
                }
            }
        }
        if (resvnUrl == null || resvnUrl.equals("")) {
            resvnUrl = svnUrl ;
        }
        if (resvnUsername == null || resvnUsername.equals("")) {
            resvnUsername = svnUsername;
        }

        if (resvnPassword == null || resvnPassword.equals("")) {
            resvnPassword = svnPassword;
        }

        if (resvnProjectSuffix == null || resvnProjectSuffix.equals("")) {
            resvnProjectSuffix = svnProjectSuffix;
        }
        logger.info("结束参数判断....");
        logger.info("打印参数 resvnUrl:" + resvnUrl + " resvnUsername:" + resvnUsername + " resvnPassword:" + resvnPassword + " resvnProjectSuffix:" + resvnProjectSuffix );

        SVNClientManager clientManager = SVNUtil.authSvn(resvnUrl, resvnUsername, resvnPassword);
        if (clientManager == null || clientManager.equals("")) {
            logger.error("登陆svn失败");
            result.put("status", "16");
            result.put("msg", "登陆svn失败");
            return result;
        }
        logger.info("登陆svn成功");

        try
        {
            logger.info("开始更新svn");
            List<String> res = new ArrayList<String>();
            SVNURL repositoryURL = null;
            try {
                repositoryURL = SVNURL.parseURIEncoded(resvnUrl).appendPath(svnProjectSuffix, false);
            } catch (SVNException e) {
                System.out.println(""+e);
                logger.error("设置svn项目路径异常");
                result.put("status", "7");
                result.put("msg", "设置svn项目路径异常");
                return result;
            }

            File ws = new File(propath);
            if(!SVNWCUtil.isVersionedDirectory(ws)){
                logger.info("svn版本不存在,开始checkout");
                SVNUtil.checkout(clientManager, repositoryURL, SVNRevision.HEAD, new File(propath), SVNDepth.INFINITY);
            }else{
                logger.info("svn版本存在,开始update");
                SVNUtil.update(clientManager, ws, SVNRevision.HEAD, SVNDepth.INFINITY);
            }

            logger.info("结束更新svn");
            result.put("status","1");
            result.put("msg","更新成功");
        }
        catch(Exception e)
        {
            String errorString = PackUtils.getStackTrace(e);
            result.put("stack",errorString);
            logger.error("更新svn异常");
            result.put("status", "8");
            result.put("msg", "更新svn异常");
            return result;
        }
        return result;

    }


    public HashMap<String, String> runComplier(PackBean packBean,List<Map<String, String>> commitList,List<Map<String, String>> newstackList ) throws Exception {
        HashMap<String, String> result = new HashMap<String, String>();
        List<String> fileList = new ArrayList<String>();
        List<Map<String, String>> stackList = new ArrayList();
        String propath = packBean.getPropath();
        String revisions = packBean.getRevisions();
        String wrPath = packBean.getWrPath();
        String srcPath = packBean.getSrcPath();
        String resourcesPath = packBean.getResourcesPath();
        String resvnProjectSuffix = packBean.getSvnProjectSuffix();
        String addFilesPath = packBean.getAddFilesPath();
        if (wrPath == null || wrPath.equals("")) {
            wrPath = srcWebapp;
        }
        if (srcPath == null || srcPath.equals("")) {
            srcPath = srcJava ;
        }
        if (resourcesPath == null || resourcesPath.equals("")) {
            resourcesPath = srcResources;
        }
        if (resvnProjectSuffix == null || resvnProjectSuffix.equals("")) {
            resvnProjectSuffix = svnProjectSuffix;
        }
        //更新svn
        result = this.updateSvnDirectory(packBean,commitList);
        if (result.get("status") != "1" ){
            logger.info("更新svn失败!");
            return  result;
        }
        //根据版本号获取更新文件
        try {
            result = this.getCommitInfo(packBean,commitList);
            logger.info("返回数据:"+result.toString());
            logger.info("返回stack:"+commitList);
            result.put("Count",commitList.size()+"");
        } catch (Exception e) {
            result.put("status","99");
            result.put("msg","获取提交历史异常");
            result.put("error",e.getMessage());
            logger.error(e.getMessage(), e);
            PackUtils.printCallStatck(e,stackList);
            String errorString = PackUtils.getStackTrace(e);
            result.put("stack",errorString);
            return  result;
        }
        //创建打包后class路径
        String addClassPath = addFilesPath+"/WEB-INF/classes/";
        File addfile = new File(addClassPath);
        if (!addfile.exists()  && !addfile.isDirectory() ) {
            logger.info("addFiles classPath:" + addClassPath + " 不存在,开始创建.");
            if (addfile.mkdirs()) {
                logger.info("创建目录" + addClassPath + "成功！");
            } else {
                logger.error("创建目录" + addClassPath + "失败！");
                result.put("status", "49");
                result.put("msg", "创建目录addFiles classPath" + addClassPath + "失败！");
                return result;
            }

        }

        //拆分文件类型
        List<String> javaFileLists = new ArrayList<String>();
        List<String> resourcesFileLists = new ArrayList<String>();
        List<String> webappFileLists = new ArrayList<String>();
        List<String> otherFileLists = new ArrayList<String>();
        Integer totalFileCount = 0;
        for( Map<String, String> commit : commitList){
            String Paths = commit.get("Paths");
            String FileCount = commit.get("FileCount");
            String PathsList[] = Paths.split(",");
            for ( String path : PathsList){
                //处理/src/main/java/
                String pattern = ".*"+srcPath+".*";
                if (Pattern.matches(pattern, path)){
                    String arrayPath[] = path.split(svnProjectSuffix);
                    String realPath = propath + arrayPath[1];
                    File sfile = new File(realPath);
                    logger.info("java组合路径:" + realPath);
                    if (sfile.isFile() == false){
                        logger.info("java组合路径:" + realPath + "文件不存在.");
                        result.put("status", "88");
                        result.put("msg", "文件路径:"+realPath+"不存在");
                        return result;

                    }
                    javaFileLists.add(realPath);
                }
                //处理/src/main/resources/
                String resourcesPattern = ".*"+resourcesPath+".*";
                if (Pattern.matches(resourcesPattern, path)){
                    String arrayPath[] = path.split(svnProjectSuffix);
                    String realPath = propath + arrayPath[1];
                    File sfile = new File(realPath);
                    logger.info("resources组合路径:" + realPath);
                    if (sfile.isFile() == false){
                        logger.info("resources组合路径:" + realPath + "文件不存在.");
                        result.put("status", "87");
                        result.put("msg", "文件路径:"+realPath+"不存在");
                        return result;

                    }
                    //copy resources文件到增量文件夹
                    File directory = new File(realPath);
                    String courseFile = directory.getParent();
                    String courseFile1 = courseFile.replace(File.separator,"/");
                    String courseFileList[] = courseFile1.split(resourcesPath);
                    if (courseFileList.length != 2 ){
                        logger.info("获取resources子路径:"+realPath+"失败");
                        result.put("status", "77");
                        result.put("msg", "获取resources子路径:"+realPath+"失败");
                        return result;
                    }
                    String lastFileName = courseFileList[1];

                    String addResourcesPath = addClassPath +"/"+ lastFileName ;
                    File addResourcesfile = new File(addResourcesPath);
                    if (!addResourcesfile.exists()  && !addResourcesfile.isDirectory() ) {
                        logger.info("addFiles resourcesPath:" + addResourcesPath + " 不存在,开始创建.");
                        if (addResourcesfile.mkdirs()) {
                            logger.info("创建目录resources" + addResourcesPath + "成功！");
                        } else {
                            logger.error("创建目录resources" + addResourcesPath + "失败！");
                            result.put("status", "49");
                            result.put("msg", "创建目录addResourcesPath" + addResourcesPath + "失败！");
                            return result;
                        }

                    }
                    logger.info("resources复制路径: "+addResourcesPath);
                    org.apache.commons.io.FileUtils.copyFileToDirectory(sfile, new File(addResourcesPath));
                    resourcesFileLists.add(realPath);
                }
                //处理/src/main/webapp/
                String webappPattern = ".*"+wrPath+".*";
                if (Pattern.matches(webappPattern, path)){
                    String arrayPath[] = path.split(svnProjectSuffix);
                    String realPath = propath + arrayPath[1];
                    File sfile = new File(realPath);
                    logger.info("webapp组合路径:" + realPath);
                    if (sfile.isFile() == false){
                        logger.info("webapp组合路径:" + realPath + "文件不存在.");
                        result.put("status", "86");
                        result.put("msg", "文件路径:"+realPath+"不存在");
                        return result;

                    }
                    //copy webapp文件到增量文件夹
                    File directory = new File(realPath);
                    String courseFile = directory.getParent();
                    String courseFile1 = courseFile.replace(File.separator,"/");
                    String courseFileList[] = courseFile1.split(wrPath);
                    if (courseFileList.length != 2 ){
                        logger.info("获取webapp子路径:"+realPath+"失败");
                        result.put("status", "77");
                        result.put("msg", "获取webapp子路径:"+realPath+"失败");
                        return result;
                    }
                    String lastFileName = courseFileList[1];

                    String addWebappPath = addFilesPath +"/"+ lastFileName ;
                    File addResourcesfile = new File(addWebappPath);
                    if (!addResourcesfile.exists()  && !addResourcesfile.isDirectory() ) {
                        logger.info("addFiles webappPath:" + addWebappPath + " 不存在,开始创建.");
                        if (addResourcesfile.mkdirs()) {
                            logger.info("创建目录webapp" + addWebappPath + "成功！");
                        } else {
                            logger.error("创建目录webapp" + addWebappPath + "失败！");
                            result.put("status", "49");
                            result.put("msg", "创建目录webappPath" + addWebappPath + "失败！");
                            return result;
                        }

                    }
                    logger.info("webapp复制路径: "+addWebappPath);
                    org.apache.commons.io.FileUtils.copyFileToDirectory(sfile, new File(addWebappPath));
                    webappFileLists.add(realPath);
                }
                //处理非标准目录
                if (Pattern.matches(webappPattern, path) == false &&  Pattern.matches(resourcesPattern, path)  == false && Pattern.matches(pattern, path) == false ){
                    String arrayPath[] = path.split(svnProjectSuffix);
                    String realPath = propath + arrayPath[1];
                    File sfile = new File(realPath);
                    logger.info("other组合路径:" + realPath);
                    if (sfile.isFile() == false){
                        logger.info("other组合路径:" + realPath + "文件不存在.");
                        result.put("status", "85");
                        result.put("msg", "文件路径:"+realPath+"不存在");
                        return result;

                    }
                    otherFileLists.add(realPath);
                }
            }
            int nextCount=Integer.parseInt(FileCount);
            totalFileCount = totalFileCount + nextCount ;


        }

        int totaljavaFilesCount = javaFileLists.size() ;
        int totalresourcesFilesCount = resourcesFileLists.size();
        int totalwebappFilesCount = webappFileLists.size() ;
        int totalotherFilesCount = otherFileLists.size() ;
        result.put("FileCount",totalFileCount+"");
        result.put("javaFilesCount",totaljavaFilesCount+"");
        result.put("resourcesFilesCount",totalresourcesFilesCount+"");
        result.put("webappFilesCount",totalwebappFilesCount+"");
        result.put("otherFilesCount",totalotherFilesCount+"");
        String revisionsList[] = revisions.split(",");
        result.put("requestRevisionsCount",revisionsList.length+"");

        String javaFileListsString = StringUtils.join(javaFileLists, ",");
        String resourcesFileListsString = StringUtils.join(resourcesFileLists, ",");
        String webappFileListsString = StringUtils.join(webappFileLists, ",");
        String otherFileListsString = StringUtils.join(otherFileLists, ",");
        result.put("javaFiles",javaFileListsString);
        result.put("resourcesFiles",resourcesFileListsString);
        result.put("webappFiles",webappFileListsString);
        result.put("otherFiles",otherFileListsString);

        logger.info("totalFileCount: "+totalFileCount +" javaFilesCount:"+totaljavaFilesCount+" resourcesFilesCount:" +totalresourcesFilesCount+" webappFilesCount:" + totalwebappFilesCount +" otherFilesCount:"+totalotherFilesCount);
        //开始编译java文件
       String sourcePath = propath + "/"+srcPath;
//        List<Map<String, String>> newstackList = new ArrayList();
        HashMap<String,String> javaResult = new HashMap<String,String>();
//        PackBean packBean = new PackBean();
        packBean.setJavaFiles(javaFileListsString);
        packBean.setOutPath(addFilesPath+"/classes");
        packBean.setSourcePath(sourcePath);
        try {
            javaResult = this.javaComplier(packBean,newstackList);
            logger.info("返回数据:"+javaResult.toString());
            logger.info("返回编译异常stack:"+newstackList);
            HashMap<String,String> map3 = new HashMap<String,String>();
            map3.putAll(result);
            map3.putAll(javaResult);
            if (javaResult.get("status") != "1" ){
                return  map3;
            }
            result.put("status","1");
            result.put("msg","编译成功");
            org.apache.commons.io.FileUtils.copyDirectory(new File(addFilesPath+"/classes"), new File(addClassPath));
            org.apache.commons.io.FileUtils.deleteDirectory(new File(addFilesPath+"/classes"));
        } catch (Exception e) {
            result.put("status","2");
            result.put("msg","编译异常");
            result.put("error",e.getMessage());
            logger.error(e.getMessage(), e);
            List<Map<String, String>> stackList1 = new ArrayList();
            PackUtils.printCallStatck(e,stackList1);
            String errorString = PackUtils.getStackTrace(e);
            result.put("stack",errorString);
            return  result;
        }
        result.put("addFilesPath",addFilesPath);

        return result;
    }


    public HashMap<String, String> getMD5(PackBean packBean ) throws Exception {
        HashMap<String,String> result = new HashMap<String,String>();
        String propath = packBean.getPropath();
        String pomFile = packBean.getPomFile();
        if ( propath == null || propath.equals("")){
            result.put("status","6");
            result.put("msg","propath路径不能为空.");
            return  result;
        }
        if ( pomFile == null || pomFile.equals("")){
            result.put("status","7");
            result.put("msg","pomFile路径不能为空.");
            return  result;
        }
        String newFile = propath +"/" +pomFile ;
        File file = new File(newFile);
        if (!file.exists() || !file.isFile()){
            result.put("status","8");
            result.put("msg","pomFile文件不存在:"+newFile);
            return  result;
        }

        String md5 = this.getMd5ByFile(new File(newFile));
//        System.out.println("MD5:"+v.toUpperCase());
        logger.info("file"+newFile + " MD5:"+md5);
        result.put("file",newFile);
        result.put("md5",md5);
        result.put("status","1");
        result.put("msg","执行成功");
        return  result;
    }


    public static String getMd5ByFile(File file) throws FileNotFoundException {
        String value = null;
        FileInputStream in = new FileInputStream(file);
        try {
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

}
