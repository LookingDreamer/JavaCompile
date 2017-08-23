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

    public HashMap<String, String> javaComplier(PackBean packBean) throws Exception {
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
        String tomcatLibString = StringUtils.join(tomcatFileList, ";");
//        logger.info("tomcatLibString: "+tomcatLibString);

        //获取第三方lib文件
        List<String> otherFileList = new ArrayList<String>();
        PackUtils.getLibFiles(otherLib, otherFileList);
        String otherLibString = StringUtils.join(otherFileList, ";");
//        logger.info("otherLibString: "+otherLibString);

        //合并lib文件
        String libPath = tomcatLibString + ";" + otherLibString;
        logger.info("结束获取lib文件....");

        //转换javaFile为list
        String[] javaFileList = new String[]{};
        javaFileList = javaFiles.split(",");

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

        try {

            String msg = "this s test msg";
            List desList = new ArrayList();
            String[] getCompilePath = javaFileList;
            logger.info("获取编译文件:" + getCompilePath.toString());

            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler(); // 返回java 编译器
            if (compiler == null) {
                logger.error("JDK required (running inside of JRE)");
                result.put("status", "50");
                result.put("msg", "JDK required (running inside of JRE)");
                return result;
            }
            // DiagnosticCollector 是监听器的一种实现
            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
            // java 文件管理器
            StandardJavaFileManager manager = compiler.getStandardFileManager(diagnostics, Locale.CHINA, Charset.forName("UTF-8"));

            Iterable<? extends JavaFileObject> compilationUnits = manager.getJavaFileObjects(getCompilePath);

            List<String> optionList = new ArrayList<String>();
//            String cm_lib = "C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/aopalliance-1.0.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/aspectjrt-1.8.5.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/aspectjweaver-1.8.5.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/c3p0-0.9.1.1.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/classmate-1.1.0.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/commons-beanutils-1.7.0.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/commons-codec-1.10.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/commons-collections-3.2.2.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/commons-dbcp2-2.1.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/commons-fileupload-1.3.1.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/commons-io-2.2.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/commons-lang-2.6.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/commons-lang3-3.4.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/commons-logging-1.0.3.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/commons-pool2-2.2.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/core-3.2.0.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/cos_api-3.1.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/dom4j-1.6.1.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/ehcache-2.10.0.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/ehcache-core-2.4.5.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/ehcache-spring-annotations-1.2.0.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/ezmorph-1.0.6.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/fastjson-1.2.29.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/freemarker-2.3.22.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/gson-2.3.1.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/guava-r09.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/hibernate-validator-5.2.4.Final.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/hibernate-validator-annotation-processor-5.2.4.Final.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/httpasyncclient-4.0.1.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/httpclient-4.3.6.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/httpcore-4.4.3.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/httpcore-nio-4.3.2.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/httpmime-4.5.1.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/jackson-annotations-2.5.0.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/jackson-core-2.5.3.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/jackson-core-asl-1.9.13.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/jackson-databind-2.5.3.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/jackson-mapper-asl-1.9.13.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/jaxen-1.1.6.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/jboss-logging-3.2.1.Final.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/jcl-over-slf4j-1.7.10.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/jedis-2.6.1.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/jldap-4.3.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/json-20140107.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/json-lib-2.4-jdk15.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/jxmpp-core-0.4.2-beta1.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/jxmpp-util-cache-0.4.2-beta1.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/lib.zip;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/log4j-1.2.17.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/log4j-api-2.3.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/log4j-core-2.3.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/mybatis-3.2.8.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/mybatis-spring-1.2.2.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/mysql-connector-java-5.1.35.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/ojdbc14-10.2.0.4.0.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/poi-3.10.1.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/poi-ooxml-3.10.1.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/poi-ooxml-schemas-3.10.1.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/poi-report-1.0.0.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/quartz-2.2.1.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/quartz-jobs-2.2.1.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/report4-1.0.1.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/slf4j-api-1.7.10.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/slf4j-log4j12-1.7.12.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/smack-core-4.1.3.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/smack-extensions-4.1.3.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/smack-im-4.1.3.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/smack-java7-4.1.3.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/smack-resolver-javax-4.1.3.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/smack-sasl-javax-4.1.3.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/smack-tcp-4.1.3.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/spring-aop-4.1.6.RELEASE.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/spring-beans-4.1.6.RELEASE.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/spring-context-4.1.6.RELEASE.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/spring-context-support-4.1.6.RELEASE.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/spring-core-4.1.6.RELEASE.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/spring-data-commons-1.10.0.RELEASE.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/spring-data-jpa-1.8.0.RELEASE.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/spring-data-redis-1.4.2.RELEASE.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/spring-expression-4.1.6.RELEASE.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/spring-jdbc-4.0.9.RELEASE.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/spring-orm-4.0.9.RELEASE.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/spring-security-config-4.0.1.RELEASE.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/spring-security-core-4.0.1.RELEASE.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/spring-security-web-4.0.1.RELEASE.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/spring-session-1.0.2.RELEASE.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/spring-session-data-redis-1.0.2.RELEASE.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/spring-tx-4.0.9.RELEASE.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/spring-web-4.1.6.RELEASE.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/spring-webmvc-4.1.6.RELEASE.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/stax-api-1.0.1.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/validation-api-1.1.0.Final.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/web-core-1.0.1.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/xml-apis-1.0.b2.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/xmlbeans-2.6.0.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/xmlpull-1.1.3.1.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/xpp3_min-1.1.4c.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/xpp3-1.1.4c.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/xstream-1.4.8.jar";
//            String tomcat_lib = "C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/annotations-api.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/catalina.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/catalina-ant.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/catalina-ha.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/catalina-storeconfig.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/catalina-tribes.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/ecj-4.5.1.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/el-api.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/jasper.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/jasper-el.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/jaspic-api.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/jsp-api.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/servlet-api.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-api.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-coyote.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-dbcp.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-i18n-es.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-i18n-fr.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-i18n-ja.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-jdbc.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-jni.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-util.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-util-scan.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-websocket.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/websocket-api.jar";
//            String newClassPath = cm_lib + ";" + tomcat_lib;
            String newClassPath = libPath;
//            optionList.addAll(Arrays.asList("-sourcepath", "C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/src/main/java"));
//            optionList.addAll(Arrays.asList("-d", "C:/Users/Administrator/Desktop/myProject/pack/project/output"));
            optionList.addAll(Arrays.asList("-sourcepath", sourcePath));
            optionList.addAll(Arrays.asList("-d", outPath));
            optionList.add("-cp");
            optionList.add(newClassPath);


//            logger.info("设置java.class.path: " + newClassPath);
//            logger.info(optionList.toString());

            JavaCompiler.CompilationTask task = compiler.getTask(null, manager, diagnostics, optionList, null, compilationUnits);
            // 如果没有编译警告和错误,这个call() 方法会编译所有的 compilationUnits 变量指定的文件,以及有依赖关系的可编译的文件.
            Boolean suc = task.call();
            if (!suc) {

                for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics()) {
                    System.err.format("\n\r 错误行数:%d 错误描述:%s", diagnostic.getLineNumber(), diagnostic);
                    desList.add(diagnostic.toString());
                }
                System.out.println("\n\r编译失败");
                logger.info("\r\n获取错误描述" + desList.toString());


            }

  /* 只有当所有的编译单元都执行成功了,这个 call() 方法才返回 Boolean.TRUE  . 一旦有任何错误,这个方法就会返回 Boolean.FALSE .
   * 在展示运行这个例子之前,让我们添加最后一个东西,DiagnosticListener , 或者更确切的说,  DiagnosticCollector .的实现类.
   * 把这个监听器当作getTask()的第三个参数传递进去,你就可以在编译之后进行一些调式信息的查询了. */
            for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
                System.out.printf(
                        "Code: %s%n" +
                                "Kind: %s%n" +
                                "Position: %s%n" +
                                "Start Position: %s%n" +
                                "End Position: %s%n" +
                                "Source: %s%n" +
                                "Message:  %s%n",
                        diagnostic.getCode(), diagnostic.getKind(),
                        diagnostic.getPosition(), diagnostic.getStartPosition(),
                        diagnostic.getEndPosition(), diagnostic.getSource(),
                        diagnostic.getMessage(null));
            }
            manager.close();
            System.out.println("success : " + suc);


        } catch (Exception e) {
            throw new Exception(e);
        }

        result.put("status", "1");
        result.put("msg", "编译成功");
        return result;
    }


}
