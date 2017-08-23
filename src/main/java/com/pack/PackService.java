package com.pack;

import org.apache.commons.io.filefilter.FalseFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

/**
 * author: cjianquan
 * date: 2016/11/11
 */
@Service("packService")
public class PackService {
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
        if (!PackUtils.getDirectoryTrue(outPath)) {
            logger.info("outPath:" + outPath + " 不存在,开始创建.");
            File dir = new File(outPath);
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
                    logger.info("成功删除目录: " + outPath);
                    ofile.mkdirs();
                } else {
                    logger.error("失败删除目录: " + outPath);
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
            logger.error("执行编译异常");
            logger.error(e.getMessage(), e);
            String errorString = PackUtils.getStackTrace(e);
            result.put("stack",errorString);
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
        }else{
            result.put("status", "90");
            result.put("msg", "编译失败");
            logger.error("编译失败");
        }

        return result;
    }


}
