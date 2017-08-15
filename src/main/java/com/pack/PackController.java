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

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler(); // 返回java 编译器
        // DiagnosticCollector 是监听器的一种实现
        DiagnosticCollector<JavaFileObject>  diagnostics = new DiagnosticCollector<>();
        // java 文件管理器
        StandardJavaFileManager manager = compiler.getStandardFileManager(diagnostics, Locale.CHINA, Charset.forName("UTF-8"));
  /* Iterable<? extends JavaFileObject> getJavaFileObjectsFromFiles(Iterable<? extends File> files)
     Iterable<? extends JavaFileObject> getJavaFileObjectsFromStrings(Iterable<String> names) */

        // 所要编译的源文件
        Iterable<? extends JavaFileObject> compilationUnits = manager.getJavaFileObjects("C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/src/main/java/com/zzb/model/AppCodeModel.java",
        "C:/Users/Administrator/Desktop/myProject/pack/project/src/cm/src/main/java/com/zzb/mobile/entity/AgentUpdatePwdVo.java"
        );

        List<String> optionList = new ArrayList<String>();
// set compiler's classpath to be same as the runtime's
        optionList.addAll(Arrays.asList("-classpath",System.getProperty("java.class.path")));
        System.out.println("设置java.class.path: "+System.getProperty("java.class.path"));

// any other options you want
//        String[] classVar = new String[]{"C:/Users/Administrator/Desktop/myProject/pack/project/lib","C:/Users/Administrator/Desktop/Webserver/apache-tomcat-9.0.0.M4/lib"};
//        optionList.addAll(Arrays.asList(classVar));



//        JavaCompiler.CompilationTask task = compiler.getTask(out,jfm,diagnostics,optionList,null,jfos);

        CompilationTask task = compiler.getTask(null, manager, diagnostics, optionList, null, compilationUnits);
        // 如果没有编译警告和错误,这个call() 方法会编译所有的 compilationUnits 变量指定的文件,以及有依赖关系的可编译的文件.
        Boolean suc = task.call();

  /* 只有当所有的编译单元都执行成功了,这个 call() 方法才返回 Boolean.TRUE  . 一旦有任何错误,这个方法就会返回 Boolean.FALSE .
   * 在展示运行这个例子之前,让我们添加最后一个东西,DiagnosticListener , 或者更确切的说,  DiagnosticCollector .的实现类.
   * 把这个监听器当作getTask()的第三个参数传递进去,你就可以在编译之后进行一些调式信息的查询了. */
        for(Diagnostic diagnostic : diagnostics.getDiagnostics())
        {
            System.console().printf(
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

        return msg;
    }


}

