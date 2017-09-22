package com.pack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper; //Jsckson JSON Processer

import java.util.*;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import java.io.File;
import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.*;

/**
 * author: huanggaoming
 * date: 2017/09/17
 */
@Controller
@RequestMapping("/packController")
public class PackController {

    @Value("${svn.url}")
    public String svnUrl;
    @Value("${svn.username}")
    public String svnUsername;
    @Value("${svn.password}")
    public String svnPassword;
    @Value("${svn.project_suffix}")
    public String svnProjectSuffix;


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PackService packService;


    @RequestMapping(value = "/goPack")
    public String goPack(HttpServletRequest request) {
        return "pack";
    }

    @RequestMapping(value = "/getUpdatefiles")
    @ResponseBody
    public List<String> getUpdatefiles(PackBean packBean, HttpServletRequest request) {
        List<String> fileList = new ArrayList<String>();

        try {
            fileList = this.packService.getUpdatefiles(packBean);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        logger.info(fileList.toString());
        return fileList;
    }

    @RequestMapping(value = "/packFiles")
    @ResponseBody
    public String packFiles(HttpServletRequest request, PackBean packBean) {
        String msg = "";
        //1.源码备份，目前这一步先省略
        //2.编译文件拷贝

        try {
            this.packService.packFiles(packBean);
            msg = "success";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            msg = "fail";
        }

        return msg;
    }

    @RequestMapping(value="/map", method=RequestMethod.POST,produces = "application/json;text/html;charset=UTF-8")
    @ResponseBody
    public String requestList(@RequestParam Map<String, Object> param) {
        return "Request successful. Post param : Map - " + param;
    }

    @RequestMapping(value = "/JavaCompiler",method = RequestMethod.POST, produces = "application/json;text/html;charset=UTF-8")
    @ResponseBody
    public String JavaCompiler(HttpServletRequest request, PackBean packBean) {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String,String> result = new HashMap<String,String>();
        HashMap<String,List<Map<String, String>>> result2 = new HashMap<String,List<Map<String, String>>>();
        Map<String, Object> all = new HashMap<String, Object>();
        logger.info("------------compile start-------------------");
        try {
            List<Map<String, String>> newstackList = new ArrayList();
            result = this.packService.javaComplier(packBean,newstackList);
            logger.info("返回数据:"+result.toString());
            logger.info("返回stack:"+newstackList);
            result2.put("runstackList",newstackList);
        } catch (Exception e) {
            result.put("status","2");
            result.put("msg","编译异常");
            result.put("error",e.getMessage());
            logger.error(e.getMessage(), e);
            List<Map<String, String>> stackList = new ArrayList();
            PackUtils.printCallStatck(e,stackList);
            result2.put("stackList",stackList);
            String errorString = PackUtils.getStackTrace(e);
            result.put("stack",errorString);
        }
        logger.info("------------compile end-------------------");
        all.put("result",result);
        all.put("stack",result2);
        String json = "";

        try
        {
            json = mapper.writeValueAsString(all);
            logger.info("转换JSON数据: "+json);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return json;
    }


//    @RequestMapping(value = "/getString", produces = "application/json;text/html;charset=UTF-8")
//    @ResponseBody
//    public String getString(HttpServletRequest request, PackBean packBean) {
//        ObjectMapper mapper = new ObjectMapper();
//
//        HashMap<String,String> map = new HashMap<String,String>();
//        map.put("1","张三");
//        map.put("2","李四");
//        map.put("3","王五");
//        map.put("4", "Jackson");
//
//        String json = "";
//
//        try
//        {
//            json = mapper.writeValueAsString(map);
//            System.out.println(json);
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//
//        return json;
//    }



    @RequestMapping(value = "/Compiler")
    @ResponseBody
    public String Compiler(HttpServletRequest request, PackBean packBean) throws IOException {

        String msg = "this s test msg";
        List desList = new ArrayList();
        String getCompilePath = packBean.getCompilePath();
        logger.info("获取编译文件:" + getCompilePath);

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler(); // 返回java 编译器
        if (compiler == null) {
            throw new IOException("JDK required (running inside of JRE)");
        }
        logger.info(compiler.getClass().getName());
        // DiagnosticCollector 是监听器的一种实现
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        // java 文件管理器
        StandardJavaFileManager manager = compiler.getStandardFileManager(diagnostics, Locale.CHINA, Charset.forName("UTF-8"));
//        manager.setLocation(manager.CLASS_PATH, "YOUR_CLASS_PATH");
  /* Iterable<? extends JavaFileObject> getJavaFileObjectsFromFiles(Iterable<? extends File> files)
     Iterable<? extends JavaFileObject> getJavaFileObjectsFromStrings(Iterable<String> names) */

        // 所要编译的源文件
//        Iterable<? extends JavaFileObject> compilationUnits = manager.getJavaFileObjects("C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/src/main/java/com/zzb/model/AppCodeModel.java",
//        "C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/src/main/java/com/zzb/mobile/entity/AgentUpdatePwdVo.java"
//        );
        Iterable<? extends JavaFileObject> compilationUnits = manager.getJavaFileObjects(getCompilePath);

        List<String> optionList = new ArrayList<String>();
// set compiler's classpath to be same as the runtime'
//        String cm_lib = "/Users/huanggaoming/myProject/pack/project/lib/aopalliance-1.0.jar;/Users/huanggaoming/myProject/pack/project/lib/aspectjrt-1.8.5.jar;/Users/huanggaoming/myProject/pack/project/lib/aspectjweaver-1.8.5.jar;/Users/huanggaoming/myProject/pack/project/lib/c3p0-0.9.1.1.jar;/Users/huanggaoming/myProject/pack/project/lib/classmate-1.1.0.jar;/Users/huanggaoming/myProject/pack/project/lib/commons-beanutils-1.7.0.jar;/Users/huanggaoming/myProject/pack/project/lib/commons-codec-1.10.jar;/Users/huanggaoming/myProject/pack/project/lib/commons-collections-3.2.2.jar;/Users/huanggaoming/myProject/pack/project/lib/commons-dbcp2-2.1.jar;/Users/huanggaoming/myProject/pack/project/lib/commons-fileupload-1.3.1.jar;/Users/huanggaoming/myProject/pack/project/lib/commons-io-2.2.jar;/Users/huanggaoming/myProject/pack/project/lib/commons-lang-2.6.jar;/Users/huanggaoming/myProject/pack/project/lib/commons-lang3-3.4.jar;/Users/huanggaoming/myProject/pack/project/lib/commons-logging-1.0.3.jar;/Users/huanggaoming/myProject/pack/project/lib/commons-pool2-2.2.jar;/Users/huanggaoming/myProject/pack/project/lib/core-3.2.0.jar;/Users/huanggaoming/myProject/pack/project/lib/cos_api-3.1.jar;/Users/huanggaoming/myProject/pack/project/lib/dom4j-1.6.1.jar;/Users/huanggaoming/myProject/pack/project/lib/ehcache-2.10.0.jar;/Users/huanggaoming/myProject/pack/project/lib/ehcache-core-2.4.5.jar;/Users/huanggaoming/myProject/pack/project/lib/ehcache-spring-annotations-1.2.0.jar;/Users/huanggaoming/myProject/pack/project/lib/ezmorph-1.0.6.jar;/Users/huanggaoming/myProject/pack/project/lib/fastjson-1.2.29.jar;/Users/huanggaoming/myProject/pack/project/lib/freemarker-2.3.22.jar;/Users/huanggaoming/myProject/pack/project/lib/gson-2.3.1.jar;/Users/huanggaoming/myProject/pack/project/lib/guava-r09.jar;/Users/huanggaoming/myProject/pack/project/lib/hibernate-validator-5.2.4.Final.jar;/Users/huanggaoming/myProject/pack/project/lib/hibernate-validator-annotation-processor-5.2.4.Final.jar;/Users/huanggaoming/myProject/pack/project/lib/httpasyncclient-4.0.1.jar;/Users/huanggaoming/myProject/pack/project/lib/httpclient-4.3.6.jar;/Users/huanggaoming/myProject/pack/project/lib/httpcore-4.4.3.jar;/Users/huanggaoming/myProject/pack/project/lib/httpcore-nio-4.3.2.jar;/Users/huanggaoming/myProject/pack/project/lib/httpmime-4.5.1.jar;/Users/huanggaoming/myProject/pack/project/lib/jackson-annotations-2.5.0.jar;/Users/huanggaoming/myProject/pack/project/lib/jackson-core-2.5.3.jar;/Users/huanggaoming/myProject/pack/project/lib/jackson-core-asl-1.9.13.jar;/Users/huanggaoming/myProject/pack/project/lib/jackson-databind-2.5.3.jar;/Users/huanggaoming/myProject/pack/project/lib/jackson-mapper-asl-1.9.13.jar;/Users/huanggaoming/myProject/pack/project/lib/jaxen-1.1.6.jar;/Users/huanggaoming/myProject/pack/project/lib/jboss-logging-3.2.1.Final.jar;/Users/huanggaoming/myProject/pack/project/lib/jcl-over-slf4j-1.7.10.jar;/Users/huanggaoming/myProject/pack/project/lib/jedis-2.6.1.jar;/Users/huanggaoming/myProject/pack/project/lib/jldap-4.3.jar;/Users/huanggaoming/myProject/pack/project/lib/json-20140107.jar;/Users/huanggaoming/myProject/pack/project/lib/json-lib-2.4-jdk15.jar;/Users/huanggaoming/myProject/pack/project/lib/jxmpp-core-0.4.2-beta1.jar;/Users/huanggaoming/myProject/pack/project/lib/jxmpp-util-cache-0.4.2-beta1.jar;/Users/huanggaoming/myProject/pack/project/lib/lib.zip;/Users/huanggaoming/myProject/pack/project/lib/log4j-1.2.17.jar;/Users/huanggaoming/myProject/pack/project/lib/log4j-api-2.3.jar;/Users/huanggaoming/myProject/pack/project/lib/log4j-core-2.3.jar;/Users/huanggaoming/myProject/pack/project/lib/mybatis-3.2.8.jar;/Users/huanggaoming/myProject/pack/project/lib/mybatis-spring-1.2.2.jar;/Users/huanggaoming/myProject/pack/project/lib/mysql-connector-java-5.1.35.jar;/Users/huanggaoming/myProject/pack/project/lib/ojdbc14-10.2.0.4.0.jar;/Users/huanggaoming/myProject/pack/project/lib/poi-3.10.1.jar;/Users/huanggaoming/myProject/pack/project/lib/poi-ooxml-3.10.1.jar;/Users/huanggaoming/myProject/pack/project/lib/poi-ooxml-schemas-3.10.1.jar;/Users/huanggaoming/myProject/pack/project/lib/poi-report-1.0.0.jar;/Users/huanggaoming/myProject/pack/project/lib/quartz-2.2.1.jar;/Users/huanggaoming/myProject/pack/project/lib/quartz-jobs-2.2.1.jar;/Users/huanggaoming/myProject/pack/project/lib/report4-1.0.1.jar;/Users/huanggaoming/myProject/pack/project/lib/slf4j-api-1.7.10.jar;/Users/huanggaoming/myProject/pack/project/lib/slf4j-log4j12-1.7.12.jar;/Users/huanggaoming/myProject/pack/project/lib/smack-core-4.1.3.jar;/Users/huanggaoming/myProject/pack/project/lib/smack-extensions-4.1.3.jar;/Users/huanggaoming/myProject/pack/project/lib/smack-im-4.1.3.jar;/Users/huanggaoming/myProject/pack/project/lib/smack-java7-4.1.3.jar;/Users/huanggaoming/myProject/pack/project/lib/smack-resolver-javax-4.1.3.jar;/Users/huanggaoming/myProject/pack/project/lib/smack-sasl-javax-4.1.3.jar;/Users/huanggaoming/myProject/pack/project/lib/smack-tcp-4.1.3.jar;/Users/huanggaoming/myProject/pack/project/lib/spring-aop-4.1.6.RELEASE.jar;/Users/huanggaoming/myProject/pack/project/lib/spring-beans-4.1.6.RELEASE.jar;/Users/huanggaoming/myProject/pack/project/lib/spring-context-4.1.6.RELEASE.jar;/Users/huanggaoming/myProject/pack/project/lib/spring-context-support-4.1.6.RELEASE.jar;/Users/huanggaoming/myProject/pack/project/lib/spring-core-4.1.6.RELEASE.jar;/Users/huanggaoming/myProject/pack/project/lib/spring-data-commons-1.10.0.RELEASE.jar;/Users/huanggaoming/myProject/pack/project/lib/spring-data-jpa-1.8.0.RELEASE.jar;/Users/huanggaoming/myProject/pack/project/lib/spring-data-redis-1.4.2.RELEASE.jar;/Users/huanggaoming/myProject/pack/project/lib/spring-expression-4.1.6.RELEASE.jar;/Users/huanggaoming/myProject/pack/project/lib/spring-jdbc-4.0.9.RELEASE.jar;/Users/huanggaoming/myProject/pack/project/lib/spring-orm-4.0.9.RELEASE.jar;/Users/huanggaoming/myProject/pack/project/lib/spring-security-config-4.0.1.RELEASE.jar;/Users/huanggaoming/myProject/pack/project/lib/spring-security-core-4.0.1.RELEASE.jar;/Users/huanggaoming/myProject/pack/project/lib/spring-security-web-4.0.1.RELEASE.jar;/Users/huanggaoming/myProject/pack/project/lib/spring-session-1.0.2.RELEASE.jar;/Users/huanggaoming/myProject/pack/project/lib/spring-session-data-redis-1.0.2.RELEASE.jar;/Users/huanggaoming/myProject/pack/project/lib/spring-tx-4.0.9.RELEASE.jar;/Users/huanggaoming/myProject/pack/project/lib/spring-web-4.1.6.RELEASE.jar;/Users/huanggaoming/myProject/pack/project/lib/spring-webmvc-4.1.6.RELEASE.jar;/Users/huanggaoming/myProject/pack/project/lib/stax-api-1.0.1.jar;/Users/huanggaoming/myProject/pack/project/lib/validation-api-1.1.0.Final.jar;/Users/huanggaoming/myProject/pack/project/lib/web-core-1.0.1.jar;/Users/huanggaoming/myProject/pack/project/lib/xml-apis-1.0.b2.jar;/Users/huanggaoming/myProject/pack/project/lib/xmlbeans-2.6.0.jar;/Users/huanggaoming/myProject/pack/project/lib/xmlpull-1.1.3.1.jar;/Users/huanggaoming/myProject/pack/project/lib/xpp3-1.1.4c.jar;/Users/huanggaoming/myProject/pack/project/lib/xpp3_min-1.1.4c.jar;/Users/huanggaoming/myProject/pack/project/lib/xstream-1.4.8.jar";

//        String tomcat_lib = "/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/annotations-api.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/catalina-ant.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/catalina-ha.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/catalina-storeconfig.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/catalina-tribes.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/catalina.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/ecj-4.5.1.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/el-api.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/jasper-el.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/jasper.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/jaspic-api.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/jsp-api.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/servlet-api.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-api.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-coyote.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-dbcp.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-i18n-es.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-i18n-fr.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-i18n-ja.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-jdbc.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-jni.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-util-scan.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-util.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-websocket.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/websocket-api.jar";
        String cm_lib = "C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/aopalliance-1.0.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/aspectjrt-1.8.5.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/aspectjweaver-1.8.5.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/c3p0-0.9.1.1.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/classmate-1.1.0.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/commons-beanutils-1.7.0.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/commons-codec-1.10.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/commons-collections-3.2.2.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/commons-dbcp2-2.1.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/commons-fileupload-1.3.1.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/commons-io-2.2.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/commons-lang-2.6.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/commons-lang3-3.4.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/commons-logging-1.0.3.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/commons-pool2-2.2.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/core-3.2.0.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/cos_api-3.1.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/dom4j-1.6.1.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/ehcache-2.10.0.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/ehcache-core-2.4.5.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/ehcache-spring-annotations-1.2.0.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/ezmorph-1.0.6.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/fastjson-1.2.29.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/freemarker-2.3.22.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/gson-2.3.1.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/guava-r09.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/hibernate-validator-5.2.4.Final.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/hibernate-validator-annotation-processor-5.2.4.Final.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/httpasyncclient-4.0.1.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/httpclient-4.3.6.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/httpcore-4.4.3.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/httpcore-nio-4.3.2.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/httpmime-4.5.1.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/jackson-annotations-2.5.0.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/jackson-core-2.5.3.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/jackson-core-asl-1.9.13.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/jackson-databind-2.5.3.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/jackson-mapper-asl-1.9.13.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/jaxen-1.1.6.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/jboss-logging-3.2.1.Final.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/jcl-over-slf4j-1.7.10.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/jedis-2.6.1.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/jldap-4.3.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/json-20140107.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/json-lib-2.4-jdk15.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/jxmpp-core-0.4.2-beta1.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/jxmpp-util-cache-0.4.2-beta1.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/lib.zip;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/log4j-1.2.17.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/log4j-api-2.3.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/log4j-core-2.3.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/mybatis-3.2.8.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/mybatis-spring-1.2.2.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/mysql-connector-java-5.1.35.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/ojdbc14-10.2.0.4.0.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/poi-3.10.1.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/poi-ooxml-3.10.1.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/poi-ooxml-schemas-3.10.1.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/poi-report-1.0.0.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/quartz-2.2.1.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/quartz-jobs-2.2.1.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/report4-1.0.1.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/slf4j-api-1.7.10.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/slf4j-log4j12-1.7.12.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/smack-core-4.1.3.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/smack-extensions-4.1.3.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/smack-im-4.1.3.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/smack-java7-4.1.3.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/smack-resolver-javax-4.1.3.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/smack-sasl-javax-4.1.3.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/smack-tcp-4.1.3.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/spring-aop-4.1.6.RELEASE.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/spring-beans-4.1.6.RELEASE.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/spring-context-4.1.6.RELEASE.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/spring-context-support-4.1.6.RELEASE.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/spring-core-4.1.6.RELEASE.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/spring-data-commons-1.10.0.RELEASE.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/spring-data-jpa-1.8.0.RELEASE.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/spring-data-redis-1.4.2.RELEASE.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/spring-expression-4.1.6.RELEASE.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/spring-jdbc-4.0.9.RELEASE.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/spring-orm-4.0.9.RELEASE.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/spring-security-config-4.0.1.RELEASE.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/spring-security-core-4.0.1.RELEASE.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/spring-security-web-4.0.1.RELEASE.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/spring-session-1.0.2.RELEASE.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/spring-session-data-redis-1.0.2.RELEASE.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/spring-tx-4.0.9.RELEASE.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/spring-web-4.1.6.RELEASE.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/spring-webmvc-4.1.6.RELEASE.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/stax-api-1.0.1.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/validation-api-1.1.0.Final.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/web-core-1.0.1.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/xml-apis-1.0.b2.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/xmlbeans-2.6.0.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/xmlpull-1.1.3.1.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/xpp3_min-1.1.4c.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/xpp3-1.1.4c.jar;C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/lib/xstream-1.4.8.jar";
        String tomcat_lib = "C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/annotations-api.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/catalina.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/catalina-ant.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/catalina-ha.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/catalina-storeconfig.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/catalina-tribes.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/ecj-4.5.1.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/el-api.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/jasper.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/jasper-el.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/jaspic-api.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/jsp-api.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/servlet-api.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-api.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-coyote.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-dbcp.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-i18n-es.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-i18n-fr.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-i18n-ja.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-jdbc.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-jni.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-util.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-util-scan.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-websocket.jar;C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/websocket-api.jar";
        String newClassPath = cm_lib + ";" + tomcat_lib;
        optionList.addAll(Arrays.asList("-sourcepath", "C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/src/main/java"));
        optionList.addAll(Arrays.asList("-d", "C:/Users/Administrator/Desktop/myProject/pack/project/output"));
        optionList.add("-cp");
        optionList.add(newClassPath);


        logger.info("设置java.class.path: " + newClassPath);
        logger.info(optionList.toString());
//        optionList.add("-d");
//        optionList.add("/Users/huanggaoming/myProject/pack/project/lib");
//        optionList.add("-d");
//        optionList.add("/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib");
// any other options you want
//        String[] classVar = new String[]{"C:/Users/Administrator/Desktop/myProject/pack/project/lib","C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib"};
//        optionList.addAll(Arrays.asList(classVar));


//        JavaCompiler.CompilationTask task = compiler.getTask(out,jfm,diagnostics,optionList,null,jfos);

        CompilationTask task = compiler.getTask(null, manager, diagnostics, optionList, null, compilationUnits);
        // 如果没有编译警告和错误,这个call() 方法会编译所有的 compilationUnits 变量指定的文件,以及有依赖关系的可编译的文件.
        Boolean suc = task.call();
        if (!suc) {

            for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics()) {
                System.err.format("\n\r 错误行数:%d 错误描述:%s", diagnostic.getLineNumber(), diagnostic);
                desList.add(diagnostic.toString());
            }
//            return  "编译失败";
//            throw new IOException("Could not compile project");
            System.out.println("\n\r编译失败");
            logger.info("\r\n获取错误描述" + desList.toString());


            return "compile faild! \r\n错误描述" + desList;
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

        return "compile finish! \r\nstatus: " + suc + " \n\rerror desc:\r\n" + desList;
    }

    @RequestMapping(value = "/getconfig")
    @ResponseBody
    public String getconfig(HttpServletRequest request, PackBean packBean) throws IOException {
        System.out.println("svn url config: " + svnUrl);
        return  svnUrl;
    }


    @RequestMapping(value = "/svnManage")
    @ResponseBody
    public String svnManage(HttpServletRequest request, PackBean packBean) throws IOException {
        System.out.println("svn url config: "+svnUrl);
        String svnUrl = "http://10.68.3.85/svn/zhangzhongbao/branches/cm/requirement/cm";
        String  username = "huanggaoming";
        String  password = "baowang2015";
        SVNClientManager clientManager = SVNUtil.authSvn(svnUrl, username, password);
        if (null == clientManager) {
            System.out.println("SVN login error! >>> url:" + svnUrl
                    + " username:" + username + " password:" + password);
            return "faild";
        }

        System.out.println("登陆成功");

        // 注册一个更新事件处理器
//        clientManager.getCommitClient().setEventHandler(new UpdateEventHandler());

        SVNURL repositoryURL = null;
        try {
            // eg: http://svn.ambow.com/wlpt/bsp
            repositoryURL = SVNURL.parseURIEncoded(svnUrl).appendPath("src", false);
        } catch (SVNException e) {
            System.out.println(""+e);
            return "异常了";
        }

        String workspace ="C:\\Users\\Administrator\\Desktop\\svn";
//        File ws = new File(workspace);
//        if(!SVNWCUtil.isVersionedDirectory(ws)){
//            SVNUtil.checkout(clientManager, repositoryURL, SVNRevision.HEAD, new File(workspace), SVNDepth.INFINITY);
//        }else{
//            SVNUtil.update(clientManager, ws, SVNRevision.HEAD, SVNDepth.INFINITY);
//        }
        //获取log信息

        try
        {
            List<String> history = new ArrayList<String>();
            List<Map<String, String>> commitList = new ArrayList();
            history = SVNUtil.filterCommitHistory(packBean,commitList,"7");
            logger.info(history.toString());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


        return  "end";
    }


    @RequestMapping(value = "/getCommitInfo",method = RequestMethod.POST, produces = "application/json;text/html;charset=UTF-8")
    @ResponseBody
    public String getCommitInfo(HttpServletRequest request, PackBean packBean) {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String,String> result = new HashMap<String,String>();
        HashMap<String,List<Map<String, String>>> result2 = new HashMap<String,List<Map<String, String>>>();
        Map<String, Object> all = new HashMap<String, Object>();
        logger.info("------------getCommitInfo start-------------------");
        List<Map<String, String>> commitList = new ArrayList();
        long startTime=System.currentTimeMillis();   //获取开始时间

        try {
            result = this.packService.getCommitInfo(packBean,commitList);
            logger.info("返回数据:"+result.toString());
            logger.info("返回stack:"+commitList);
            result2.put("commitList",commitList);
            result.put("Count",commitList.size()+"");
        } catch (Exception e) {
            result.put("status","99");
            result.put("msg","获取提交历史异常");
            result.put("error",e.getMessage());
            logger.error(e.getMessage(), e);
            List<Map<String, String>> stackList = new ArrayList();
            PackUtils.printCallStatck(e,stackList);
            result2.put("stackList",stackList);
            String errorString = PackUtils.getStackTrace(e);
            result.put("stack",errorString);
        }
        logger.info("------------getCommitInfo end-------------------");
        long endTime=System.currentTimeMillis(); //获取结束时间
        result.put("takes",(endTime-startTime)+"ms");
        all.put("result",result);
        all.put("stack",result2);
        String json = "";

        try
        {
            json = mapper.writeValueAsString(all);
            logger.info("转换JSON数据: "+json);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return json;
    }



    @RequestMapping(value = "/runCompiler",method = RequestMethod.POST, produces = "application/json;text/html;charset=UTF-8")
    @ResponseBody
    public String runCompiler(HttpServletRequest request, PackBean packBean) {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String,String> result = new HashMap<String,String>();
        HashMap<String,List<Map<String, String>>> result2 = new HashMap<String,List<Map<String, String>>>();
        Map<String, Object> all = new HashMap<String, Object>();
        long startTime=System.currentTimeMillis();   //获取开始时间
        logger.info("------------run compile start-------------------");
        try {
            List<Map<String, String>> commitList = new ArrayList();
            List<Map<String, String>> newstackList = new ArrayList();
            result = this.packService.runComplier(packBean,commitList,newstackList);
            logger.info("返回数据:"+result.toString());
            logger.info("返回文件列表:"+commitList);
            result2.put("commitList",commitList);
            result2.put("stackList",newstackList);
        } catch (Exception e) {
            result.put("status","22");
            result.put("msg","编译异常");
            result.put("error",e.getMessage());
            logger.error(e.getMessage(), e);
            List<Map<String, String>> stackList = new ArrayList();
            PackUtils.printCallStatck(e,stackList);
            result2.put("stackList",stackList);
            String errorString = PackUtils.getStackTrace(e);
            result.put("stack",errorString);
        }
        logger.info("------------run compile end-------------------");
        long endTime=System.currentTimeMillis(); //获取结束时间
        result.put("takes",(endTime-startTime)+"ms");
        all.put("result",result);
        all.put("stack",result2);
        String json = "";

        try
        {
            json = mapper.writeValueAsString(all);
            logger.info("转换JSON数据: "+json);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return json;
    }



    @RequestMapping(value = "/getFileMd5",method = RequestMethod.POST, produces = "application/json;text/html;charset=UTF-8")
    @ResponseBody
    public String getFileMd5(HttpServletRequest request, PackBean packBean) {
        String json = "";
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String,String> result = new HashMap<String,String>();
        long startTime=System.currentTimeMillis();   //获取开始时间
        try {

            result = this.packService.getMD5(packBean);
        } catch (Exception e) {
            result.put("status","22");
            result.put("msg","执行异常");
            result.put("error",e.getMessage());
            e.printStackTrace();

        }

        long endTime=System.currentTimeMillis(); //获取结束时间
        result.put("takes",(endTime-startTime)+"ms");
        try
        {
            json = mapper.writeValueAsString(result);
            logger.info("转换JSON数据: "+json);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return json;
    }


    @RequestMapping(value = "/runCmd",method = RequestMethod.POST, produces = "application/json;text/html;charset=UTF-8")
    @ResponseBody
    public String runCmd(HttpServletRequest request, PackBean packBean) {
        String json = "";
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String,String> result = new HashMap<String,String>();
        long startTime=System.currentTimeMillis();   //获取开始时间
        try {

            result = this.packService.runCmd(packBean);
        } catch (Exception e) {
            result.put("status","22");
            result.put("msg","执行异常");
            result.put("error",e.getMessage());
        }

        long endTime=System.currentTimeMillis(); //获取结束时间
        result.put("takes",(endTime-startTime)+"ms");
        try
        {
            json = mapper.writeValueAsString(result);
            logger.info("转换JSON数据: "+json);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return json;
    }


    @RequestMapping(value = "/mvnCompile",method = RequestMethod.POST, produces = "application/json;text/html;charset=UTF-8")
    @ResponseBody
    public String mvnCompile(HttpServletRequest request, PackBean packBean) {
        String json = "";
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String,String> result = new HashMap<String,String>();
        long startTime=System.currentTimeMillis();   //获取开始时间
        try {

            result = this.packService.mvnCompile(packBean);
        } catch (Exception e) {
            result.put("status","22");
            result.put("msg","执行异常");
            result.put("error",e.getMessage());
        }

        long endTime=System.currentTimeMillis(); //获取结束时间
        result.put("takes",(endTime-startTime)+"ms");
        try
        {
            json = mapper.writeValueAsString(result);
            logger.info("转换JSON数据: "+json);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return json;
    }


}

