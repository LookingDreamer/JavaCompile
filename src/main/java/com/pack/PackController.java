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
/**
 * author: cjianquan
 * date: 2016/9/2
 */
@Controller
@RequestMapping("/packController")
public class PackController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PackService packService;


    @RequestMapping(value = "/goPack")
    public String goPack(HttpServletRequest request){
        return "pack";
    }

    @RequestMapping(value = "/getUpdatefiles")
    @ResponseBody
    public List<String> getUpdatefiles(PackBean packBean, HttpServletRequest request){
        List<String> fileList = new ArrayList<String>();

        try{
            fileList = this.packService.getUpdatefiles(packBean);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        logger.info(fileList.toString());
        return fileList;
    }

    @RequestMapping(value = "/packFiles")
    @ResponseBody
    public String packFiles(HttpServletRequest request,PackBean packBean){
        String msg = "";
        //1.源码备份，目前这一步先省略
        //2.编译文件拷贝

        try {
            this.packService.packFiles(packBean);
            msg = "success";
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            msg = "fail";
        }

        return msg;
    }

    @RequestMapping(value = "/Compiler")
    @ResponseBody
    public String Compiler (HttpServletRequest request,PackBean packBean) throws IOException {

        String msg = "this s test msg";
        List desList = new ArrayList();
        String getCompilePath = packBean.getCompilePath();
        logger.info("获取编译文件:" + getCompilePath);

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler(); // 返回java 编译器
        if (compiler == null) {
            throw new IOException("JDK required (running inside of JRE)");
        }
        // DiagnosticCollector 是监听器的一种实现
        DiagnosticCollector<JavaFileObject>  diagnostics = new DiagnosticCollector<>();
        // java 文件管理器
        StandardJavaFileManager manager = compiler.getStandardFileManager(diagnostics, Locale.CHINA, Charset.forName("UTF-8"));
  /* Iterable<? extends JavaFileObject> getJavaFileObjectsFromFiles(Iterable<? extends File> files)
     Iterable<? extends JavaFileObject> getJavaFileObjectsFromStrings(Iterable<String> names) */

        // 所要编译的源文件
//        Iterable<? extends JavaFileObject> compilationUnits = manager.getJavaFileObjects("C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/src/main/java/com/zzb/model/AppCodeModel.java",
//        "C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/src/main/java/com/zzb/mobile/entity/AgentUpdatePwdVo.java"
//        );
        Iterable<? extends JavaFileObject> compilationUnits = manager.getJavaFileObjects(getCompilePath);

        List<String> optionList = new ArrayList<String>();
// set compiler's classpath to be same as the runtime'
        String cm_lib = "/Users/huanggaoming/myProject/pack/project/lib/aopalliance-1.0.jar;/Users/huanggaoming/myProject/pack/project/lib/aspectjrt-1.8.5.jar;/Users/huanggaoming/myProject/pack/project/lib/aspectjweaver-1.8.5.jar;/Users/huanggaoming/myProject/pack/project/lib/c3p0-0.9.1.1.jar;/Users/huanggaoming/myProject/pack/project/lib/classmate-1.1.0.jar;/Users/huanggaoming/myProject/pack/project/lib/commons-beanutils-1.7.0.jar;/Users/huanggaoming/myProject/pack/project/lib/commons-codec-1.10.jar;/Users/huanggaoming/myProject/pack/project/lib/commons-collections-3.2.2.jar;/Users/huanggaoming/myProject/pack/project/lib/commons-dbcp2-2.1.jar;/Users/huanggaoming/myProject/pack/project/lib/commons-fileupload-1.3.1.jar;/Users/huanggaoming/myProject/pack/project/lib/commons-io-2.2.jar;/Users/huanggaoming/myProject/pack/project/lib/commons-lang-2.6.jar;/Users/huanggaoming/myProject/pack/project/lib/commons-lang3-3.4.jar;/Users/huanggaoming/myProject/pack/project/lib/commons-logging-1.0.3.jar;/Users/huanggaoming/myProject/pack/project/lib/commons-pool2-2.2.jar;/Users/huanggaoming/myProject/pack/project/lib/core-3.2.0.jar;/Users/huanggaoming/myProject/pack/project/lib/cos_api-3.1.jar;/Users/huanggaoming/myProject/pack/project/lib/dom4j-1.6.1.jar;/Users/huanggaoming/myProject/pack/project/lib/ehcache-2.10.0.jar;/Users/huanggaoming/myProject/pack/project/lib/ehcache-core-2.4.5.jar;/Users/huanggaoming/myProject/pack/project/lib/ehcache-spring-annotations-1.2.0.jar;/Users/huanggaoming/myProject/pack/project/lib/ezmorph-1.0.6.jar;/Users/huanggaoming/myProject/pack/project/lib/fastjson-1.2.29.jar;/Users/huanggaoming/myProject/pack/project/lib/freemarker-2.3.22.jar;/Users/huanggaoming/myProject/pack/project/lib/gson-2.3.1.jar;/Users/huanggaoming/myProject/pack/project/lib/guava-r09.jar;/Users/huanggaoming/myProject/pack/project/lib/hibernate-validator-5.2.4.Final.jar;/Users/huanggaoming/myProject/pack/project/lib/hibernate-validator-annotation-processor-5.2.4.Final.jar;/Users/huanggaoming/myProject/pack/project/lib/httpasyncclient-4.0.1.jar;/Users/huanggaoming/myProject/pack/project/lib/httpclient-4.3.6.jar;/Users/huanggaoming/myProject/pack/project/lib/httpcore-4.4.3.jar;/Users/huanggaoming/myProject/pack/project/lib/httpcore-nio-4.3.2.jar;/Users/huanggaoming/myProject/pack/project/lib/httpmime-4.5.1.jar;/Users/huanggaoming/myProject/pack/project/lib/jackson-annotations-2.5.0.jar;/Users/huanggaoming/myProject/pack/project/lib/jackson-core-2.5.3.jar;/Users/huanggaoming/myProject/pack/project/lib/jackson-core-asl-1.9.13.jar;/Users/huanggaoming/myProject/pack/project/lib/jackson-databind-2.5.3.jar;/Users/huanggaoming/myProject/pack/project/lib/jackson-mapper-asl-1.9.13.jar;/Users/huanggaoming/myProject/pack/project/lib/jaxen-1.1.6.jar;/Users/huanggaoming/myProject/pack/project/lib/jboss-logging-3.2.1.Final.jar;/Users/huanggaoming/myProject/pack/project/lib/jcl-over-slf4j-1.7.10.jar;/Users/huanggaoming/myProject/pack/project/lib/jedis-2.6.1.jar;/Users/huanggaoming/myProject/pack/project/lib/jldap-4.3.jar;/Users/huanggaoming/myProject/pack/project/lib/json-20140107.jar;/Users/huanggaoming/myProject/pack/project/lib/json-lib-2.4-jdk15.jar;/Users/huanggaoming/myProject/pack/project/lib/jxmpp-core-0.4.2-beta1.jar;/Users/huanggaoming/myProject/pack/project/lib/jxmpp-util-cache-0.4.2-beta1.jar;/Users/huanggaoming/myProject/pack/project/lib/lib.zip;/Users/huanggaoming/myProject/pack/project/lib/log4j-1.2.17.jar;/Users/huanggaoming/myProject/pack/project/lib/log4j-api-2.3.jar;/Users/huanggaoming/myProject/pack/project/lib/log4j-core-2.3.jar;/Users/huanggaoming/myProject/pack/project/lib/mybatis-3.2.8.jar;/Users/huanggaoming/myProject/pack/project/lib/mybatis-spring-1.2.2.jar;/Users/huanggaoming/myProject/pack/project/lib/mysql-connector-java-5.1.35.jar;/Users/huanggaoming/myProject/pack/project/lib/ojdbc14-10.2.0.4.0.jar;/Users/huanggaoming/myProject/pack/project/lib/poi-3.10.1.jar;/Users/huanggaoming/myProject/pack/project/lib/poi-ooxml-3.10.1.jar;/Users/huanggaoming/myProject/pack/project/lib/poi-ooxml-schemas-3.10.1.jar;/Users/huanggaoming/myProject/pack/project/lib/poi-report-1.0.0.jar;/Users/huanggaoming/myProject/pack/project/lib/quartz-2.2.1.jar;/Users/huanggaoming/myProject/pack/project/lib/quartz-jobs-2.2.1.jar;/Users/huanggaoming/myProject/pack/project/lib/report4-1.0.1.jar;/Users/huanggaoming/myProject/pack/project/lib/slf4j-api-1.7.10.jar;/Users/huanggaoming/myProject/pack/project/lib/slf4j-log4j12-1.7.12.jar;/Users/huanggaoming/myProject/pack/project/lib/smack-core-4.1.3.jar;/Users/huanggaoming/myProject/pack/project/lib/smack-extensions-4.1.3.jar;/Users/huanggaoming/myProject/pack/project/lib/smack-im-4.1.3.jar;/Users/huanggaoming/myProject/pack/project/lib/smack-java7-4.1.3.jar;/Users/huanggaoming/myProject/pack/project/lib/smack-resolver-javax-4.1.3.jar;/Users/huanggaoming/myProject/pack/project/lib/smack-sasl-javax-4.1.3.jar;/Users/huanggaoming/myProject/pack/project/lib/smack-tcp-4.1.3.jar;/Users/huanggaoming/myProject/pack/project/lib/spring-aop-4.1.6.RELEASE.jar;/Users/huanggaoming/myProject/pack/project/lib/spring-beans-4.1.6.RELEASE.jar;/Users/huanggaoming/myProject/pack/project/lib/spring-context-4.1.6.RELEASE.jar;/Users/huanggaoming/myProject/pack/project/lib/spring-context-support-4.1.6.RELEASE.jar;/Users/huanggaoming/myProject/pack/project/lib/spring-core-4.1.6.RELEASE.jar;/Users/huanggaoming/myProject/pack/project/lib/spring-data-commons-1.10.0.RELEASE.jar;/Users/huanggaoming/myProject/pack/project/lib/spring-data-jpa-1.8.0.RELEASE.jar;/Users/huanggaoming/myProject/pack/project/lib/spring-data-redis-1.4.2.RELEASE.jar;/Users/huanggaoming/myProject/pack/project/lib/spring-expression-4.1.6.RELEASE.jar;/Users/huanggaoming/myProject/pack/project/lib/spring-jdbc-4.0.9.RELEASE.jar;/Users/huanggaoming/myProject/pack/project/lib/spring-orm-4.0.9.RELEASE.jar;/Users/huanggaoming/myProject/pack/project/lib/spring-security-config-4.0.1.RELEASE.jar;/Users/huanggaoming/myProject/pack/project/lib/spring-security-core-4.0.1.RELEASE.jar;/Users/huanggaoming/myProject/pack/project/lib/spring-security-web-4.0.1.RELEASE.jar;/Users/huanggaoming/myProject/pack/project/lib/spring-session-1.0.2.RELEASE.jar;/Users/huanggaoming/myProject/pack/project/lib/spring-session-data-redis-1.0.2.RELEASE.jar;/Users/huanggaoming/myProject/pack/project/lib/spring-tx-4.0.9.RELEASE.jar;/Users/huanggaoming/myProject/pack/project/lib/spring-web-4.1.6.RELEASE.jar;/Users/huanggaoming/myProject/pack/project/lib/spring-webmvc-4.1.6.RELEASE.jar;/Users/huanggaoming/myProject/pack/project/lib/stax-api-1.0.1.jar;/Users/huanggaoming/myProject/pack/project/lib/validation-api-1.1.0.Final.jar;/Users/huanggaoming/myProject/pack/project/lib/web-core-1.0.1.jar;/Users/huanggaoming/myProject/pack/project/lib/xml-apis-1.0.b2.jar;/Users/huanggaoming/myProject/pack/project/lib/xmlbeans-2.6.0.jar;/Users/huanggaoming/myProject/pack/project/lib/xmlpull-1.1.3.1.jar;/Users/huanggaoming/myProject/pack/project/lib/xpp3-1.1.4c.jar;/Users/huanggaoming/myProject/pack/project/lib/xpp3_min-1.1.4c.jar;/Users/huanggaoming/myProject/pack/project/lib/xstream-1.4.8.jar";

        String tomcat_lib="/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/annotations-api.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/catalina-ant.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/catalina-ha.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/catalina-storeconfig.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/catalina-tribes.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/catalina.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/ecj-4.5.1.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/el-api.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/jasper-el.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/jasper.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/jaspic-api.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/jsp-api.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/servlet-api.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-api.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-coyote.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-dbcp.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-i18n-es.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-i18n-fr.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-i18n-ja.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-jdbc.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-jni.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-util-scan.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-util.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/tomcat-websocket.jar;/Users/huanggaoming/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib/websocket-api.jar";

        String newClassPath = cm_lib + ";"+ tomcat_lib;
//        optionList.addAll(Arrays.asList("-classpath",System.getProperty("java.class.path")));
        optionList.addAll(Arrays.asList("-cp",newClassPath));
        

        logger.info("设置java.class.path: "+newClassPath);
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
            logger.info("\r\n获取错误描述"+desList.toString());


//            return  "compile faild! \r\n错误描述"+desList;
        }

  /* 只有当所有的编译单元都执行成功了,这个 call() 方法才返回 Boolean.TRUE  . 一旦有任何错误,这个方法就会返回 Boolean.FALSE .
   * 在展示运行这个例子之前,让我们添加最后一个东西,DiagnosticListener , 或者更确切的说,  DiagnosticCollector .的实现类.
   * 把这个监听器当作getTask()的第三个参数传递进去,你就可以在编译之后进行一些调式信息的查询了. */
        for(Diagnostic diagnostic : diagnostics.getDiagnostics())
        {
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

        return  "compile finish! \r\nstatus: "+suc+" \n\rerror desc:\r\n"+ desList;
    }


}

