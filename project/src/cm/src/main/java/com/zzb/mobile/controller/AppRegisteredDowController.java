//package com.zzb.mobile.controller;
//
//import com.cninsure.core.controller.BaseController;
//import com.cninsure.core.exception.ControllerException;
//import com.cninsure.core.utils.LogUtil;
//import com.cninsure.core.utils.StringUtil;
//import com.cninsure.system.service.INSCDeptService;
//import com.zzb.cm.entity.INSBFilelibraryUploadCosFail;
//import com.zzb.cm.service.INSBFilelibraryUploadCosFailService;
//import com.zzb.conf.service.INSBAgentService;
//import com.zzb.mobile.model.*;
//import com.zzb.mobile.service.AppLoginService;
//import com.zzb.mobile.service.AppRegisteredService;
//import com.zzb.warranty.util.DateUtils;
//import net.sf.json.JSONObject;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.URL;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipOutputStream;
//
//@Controller
//@RequestMapping("/mobile/registered/*")
//public class AppRegisteredDowController extends BaseController {
//
//    @Resource
//    private AppRegisteredService registeredService;
//
//    @Resource
//    private AppLoginService appLoginService;
//    @Resource
//    private HttpServletRequest request;
//    @Resource
//    private INSBFilelibraryUploadCosFailService filelibraryUploadCosFailService;
//
//
//    @Resource
//    private INSBAgentService agentService;
//    @Resource
//    private INSCDeptService deptService;
//
//    public static volatile Boolean running = false;
//    public static Boolean interrupt = false;
//
//    public static Thread reuploadThread = null;
//
//    @RequestMapping(value = "/filesPackDownload", method = RequestMethod.POST)
//    @ResponseBody
//    public void filesPackDownload(HttpServletResponse response) throws ControllerException {// post form
//        String taskId = request.getParameter("taskId");
//        String files = request.getParameter("files");
//        String zipFileName = taskId + "img" + DateUtils.format(new Date(),"yyyyMMddHHmmssSSS") + ".zip";
//        try {
//            LogUtil.info("任务 " + taskId + " 下载文件时, 提交数据是：taskId = " + taskId + " files = "+files);
//            if (StringUtil.isEmpty(files)) {
//                LogUtil.info("任务 " + taskId + " 下载文件时, 提交数据异常");
//            } else {
//                response.setHeader("Content-Type", "application/octet-stream");
//                response.setHeader("Content-Disposition", "attachment;filename="+zipFileName);
//
//                String strArray = files;
//                String[] filePathArray = strArray.split(",");
//                URL urlfile = null;
//                InputStream ips = null;
//                OutputStream responseOps = response.getOutputStream();
//                //要下载的文件目录
//                ZipOutputStream zos = new ZipOutputStream(responseOps);
//                try {
//                    for (int i = 0; i < filePathArray.length; i++) {
//                        try {
//                            urlfile = new URL(filePathArray[i]);
//                            String[] array = filePathArray[i].split("\\.");
//                            String imgType = "."+array[(array.length - 1)];
//                            String fileName = taskId + "_" + String.valueOf(i) + imgType;
//                            ips = urlfile.openStream();
//                            doZip(ips, zos, fileName);
//                            LogUtil.info("任务 " + taskId + " 服务器下载文件 文件成功 ， " + filePathArray[i]);
//                        } catch (Exception ex) {
//                            LogUtil.info("任务 " + taskId + " 下载文件 " + filePathArray[i] + " 出错 " + ex.getStackTrace());
//                        } finally {
//                            if (ips != null) {
//                                ips.close();
//                                ips = null;
//                            }
//                        }
//                    }
//                    responseOps.flush();
//                } catch (Exception ex) {
//                    LogUtil.info("任务 " + taskId + " 下载文件 到服务器时 出错 " + ex.getStackTrace());
//                } finally {
//                    zos.close();
//                    zos = null;
//                    responseOps.close();
//                }
//            }
//            LogUtil.info("任务 " + taskId + " 服务端下载完成 ---> 客户端开始下载 " + zipFileName);
//        } catch (Exception ex) {
//            LogUtil.info("任务 " + taskId + " 下载文件 时 出错 " + ex.getStackTrace());
//        }
//    }
//
//    /**
//     *  打包为 zip 文件
//     * @param fileName 待打包的文件
//     * @param zos zip zip输出流
//     * @throws IOException
//     */
//    public static void doZip(InputStream in , ZipOutputStream zos,String fileName)
//            throws IOException {
//        zos.putNextEntry(new ZipEntry(fileName));
//        byte[] buffer = new byte[1024];
//        int r = 0;
//        while ((r = in.read(buffer)) != -1) {
//            zos.write(buffer, 0, r);
//        }
//        zos.flush();
//    }
//
//
//}
