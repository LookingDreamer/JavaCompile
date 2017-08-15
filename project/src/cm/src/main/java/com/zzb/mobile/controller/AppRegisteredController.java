package com.zzb.mobile.controller;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.service.INSCDeptService;
import com.common.ModelUtil;
import com.zzb.cm.entity.INSBFilelibraryUploadCosFail;
import com.zzb.cm.service.INSBFilelibraryUploadCosFailService;
import com.zzb.mobile.model.*;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.mobile.service.AppLoginService;
import com.zzb.mobile.service.AppRegisteredService;

@Controller
@RequestMapping("/mobile/registered/*")
public class AppRegisteredController extends BaseController {

    @Resource
    private AppRegisteredService registeredService;

    @Resource
    private AppLoginService appLoginService;
    @Resource
    private HttpServletRequest request;
    @Resource
    private INSBFilelibraryUploadCosFailService filelibraryUploadCosFailService;


    @Resource
    private INSBAgentService agentService;
    @Resource
    private INSCDeptService deptService;

    public static volatile Boolean running = false;
    public static Boolean interrupt = false;

    public static Thread reuploadThread = null;


    /**
     * 接口描述：注册前，先验证图片验证码，再发送手机验证码，验证手机号是否存在，存在提示手机号存在！
     * 请求地址	/mobile/registered/sendValidateCode
     *
     * @param phoneNo 手机号
     */

    @RequestMapping(value = "/sendValidateCode", method = RequestMethod.POST)
    @ResponseBody
    public CommonModel sendValidateCode(@RequestBody ValidateCodeParam validate)
            throws ControllerException {
        return registeredService.sendValidateCode(request, validate.getPhone(), validate.getUuid(), validate.getVerifyCode());
    }

    /**
     * 接口描述：提交注册信息
     * 请求地址	/mobile/registered/submitRegInfo
     *
     * @param regInfoJSON 注册信息
     */
    @RequestMapping(value = "/submitRegInfo", method = RequestMethod.POST)
    @ResponseBody
    public CommonModel submitRegInfo(@RequestBody String regInfoJSON)
            throws ControllerException {
        return registeredService.submitRegInfo(regInfoJSON);
    }

    @RequestMapping(value = "/bindPhone", method = RequestMethod.POST)
    @ResponseBody
    public CommonModel bindPhone(@RequestBody String bindPhoneJSON)
            throws ControllerException {
        return registeredService.bindPhone(bindPhoneJSON);
    }

    /**
     * 接口描述：提交注册信息
     * 请求地址	/mobile/registered/bindWorkCode
     *
     * @param regInfoJSON 注册信息
     */
    @RequestMapping(value = "/bindWorkCode", method = RequestMethod.POST)
    @ResponseBody
    public CommonModel bindWorkCode(@RequestBody BindJobNumParam bindInfo)
            throws ControllerException {
        return registeredService.bindWorkCode(bindInfo.getTempJobNum(), bindInfo.getJobNumOrIdCard(), bindInfo.getJobNumPassword());
    }

    /**
     * 接口描述：验证验证码是否正确
     * 请求地址	/mobile/registered/validateCode
     *
     * @param regInfoJSON 注册信息
     */
    @RequestMapping(value = "/validateCode", method = RequestMethod.POST)
    @ResponseBody
    public CommonModel validateCode(@RequestBody ValidateCodeParam codeParam)
            throws ControllerException {
        return appLoginService.validationCode(request, codeParam.getPhone(), codeParam.getCode(), codeParam.getUuid());
    }

    /**
     * 接口描述：文件上传
     * 请求地址	/mobile/registered/fileUpLoad
     *
     * @param
     */
    @RequestMapping(value = "/filesUpLoad", method = RequestMethod.POST)
    @ResponseBody
    public CommonModel filesUpLoad(@RequestParam("files") MultipartFile[] files, @RequestParam("fileTypes") String[] fileTypes, @RequestParam("fileDescribes") String[] fileDescribes, @RequestParam("jobNum") String jobNum)
            throws ControllerException {
        return registeredService.filesUpLoad(request, files, fileTypes, fileDescribes, jobNum);
    }

    /**
     * 接口描述：文件上传
     * 请求地址	/mobile/registered/fileUpLoad
     *
     * @param
     */
    @RequestMapping(value = "/fileUpLoad", method = RequestMethod.POST)
    @ResponseBody
    public CommonModel fileUpLoad(@RequestParam("file") MultipartFile file, @RequestParam("fileType") String fileType, @RequestParam("fileDescribes") String fileDescribes, @RequestParam("jobNum") String jobNum)
            throws ControllerException {
        return registeredService.fileUpLoad(request, file, fileType, fileDescribes, jobNum);
    }

    /**
     * 接口描述：文件上传
     * 请求地址	/mobile/registered/fileUpLoadBase64
     * ios上传影像接口与pc不一致，废弃掉ios的上传接口，统一用pc上传接口：/mobile/registered/fileUpLoadBase64ByCos
     * @param
     */
    @Deprecated
    @RequestMapping(value = "/fileUpLoadBase64", method = RequestMethod.POST)
    @ResponseBody
    public CommonModel fileUpLoadBase64(@RequestBody FileUploadBase64Param param)
            throws ControllerException {
        return registeredService.fileUpLoadBase64(request, param.getFile(), param.getFileName(), param.getFileType(), param.getFileDescribes(), param.getJobNum(), param.getTaskId());
    }

    /**
     * 接口描述：文件上传 腾讯cos
     * 请求地址	/mobile/registered/fileUpLoadBase64ByCos
     *
     * @param
     */
    @RequestMapping(value = "/fileUpLoadBase64ByCos", method = RequestMethod.POST)
    @ResponseBody
    public CommonModel fileUpLoadBase64ByCos(@RequestBody FileUploadBase64Param param)
            throws ControllerException {
        return registeredService.fileUpLoadBase64ByCos(request, param.getFile(), param.getFileName(), param.getFileType(), param.getFileDescribes(), param.getJobNum(), param.getTaskId());
    }

    @RequestMapping(value = "/filesPackDownload", method = RequestMethod.POST)
    @ResponseBody
//    public void filesPackDownload(@RequestBody FileUploadBase64Param param, HttpServletResponse response) throws ControllerException {
    public void filesPackDownload(HttpServletResponse response) throws ControllerException {//get post form
        String taskId = request.getParameter("taskId");
        String files = request.getParameter("files");
        String zipFileName = taskId + "img" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + ".zip";
        try {
            LogUtil.info("任务 " + taskId + " 下载文件时, 提交数据是：taskId = " + taskId + " files = "+files);
            if (StringUtil.isEmpty(files)) {
                LogUtil.info("任务 " + taskId + " 下载文件时, 提交数据异常");
            } else {
//                response.setContentType("application/x-msdownload"); // 通知客户文件的MIME类型：
//                /*attachment是以附件下载的形式，inline是以线上浏览的形式。当点击“保存”的时候都可以下载，当点击“打开”的时候attachment是在本地机里打开，inline是在浏览器里打开。*/
//                response.setHeader("Content-disposition", "attachment; filename=" + zipFileName);

                response.setHeader("Content-Type", "application/octet-stream");
                response.setHeader("Content-Disposition", "attachment;filename="+zipFileName);

                String strArray = files;
                String[] filePathArray = strArray.split(",");
                URL urlfile = null;
                InputStream ips = null;
                OutputStream responseOps = response.getOutputStream();
                //要下载的文件目录
                ZipOutputStream zos = new ZipOutputStream(responseOps);
                try {
                    for (int i = 0; i < filePathArray.length; i++) {
                        try {
                            urlfile = new URL(filePathArray[i]);
                            String[] array = filePathArray[i].split("\\.");
                            String imgType = "."+array[(array.length - 1)];
                            String fileName = taskId + "_" + String.valueOf(i) + imgType;
                            ips = urlfile.openStream();
                            doZip(ips, zos, fileName);
                            LogUtil.info("任务 " + taskId + " 服务器下载文件 文件成功 ， " + filePathArray[i]);
                        } catch (Exception ex) {
                            LogUtil.info("任务 " + taskId + " 下载文件 " + filePathArray[i] + " 出错 " + ex.getStackTrace());
                        } finally {
                            if (ips != null) {
                                ips.close();
                                ips = null;
                            }
                        }
                    }
                    responseOps.flush();
                } catch (Exception ex) {
                    LogUtil.info("任务 " + taskId + " 下载文件 到服务器时 出错 " + ex.getStackTrace());
                } finally {
                    zos.close();
                    zos = null;
                    responseOps.close();
                }
            }
            LogUtil.info("任务 " + taskId + " 服务端下载完成 ---> 客户端开始下载 " + zipFileName);
        } catch (Exception ex) {
            LogUtil.info("任务 " + taskId + " 下载文件 时 出错 " + ex.getStackTrace());
        }
    }

    /**
     *  打包为 zip 文件
     * @param fileName 待打包的文件
     * @param zos zip zip输出流
     * @throws IOException
     */
    public static void doZip(InputStream in , ZipOutputStream zos,String fileName)
            throws IOException {
        zos.putNextEntry(new ZipEntry(fileName));
        byte[] buffer = new byte[1024];
        int r = 0;
        while ((r = in.read(buffer)) != -1) {
            zos.write(buffer, 0, r);
        }
        zos.flush();
    }


    /**
     * 接口描述：cos文件上传失败，调用接口把上传失败重新上传 腾讯cos
     * 请求地址	/mobile/registered/fileUpLoadBase64ByCosRedo
     *
     * @param
     */
    @RequestMapping(value = "/fileUpLoadBase64ByCosRedo", method = RequestMethod.GET)
    @ResponseBody
    public CommonModel fileUpLoadBase64ByCosRedo(@RequestParam boolean isInterrupt)
            throws ControllerException {

        CommonModel model = new CommonModel();
        try {
            if (isInterrupt) {
                if (reuploadThread != null) {
                    AppRegisteredController.interrupt = true;
                    reuploadThread = null;
                    model.setStatus("success");
                    model.setMessage("调用成功，线程已执行中断...");
                } else {
                    model.setStatus("success");
                    model.setMessage("调用成功，线程已中断...");
                }
            } else {
                if (AppRegisteredController.running) {
                    model.setStatus("success");
                    model.setMessage("调用成功，线程正在上传...");
                } else {
                    AppRegisteredController.interrupt = false;
                    AppRegisteredController.running = true;
                    reuploadThread = new Thread(() -> {
                        LogUtil.info(Thread.currentThread().getName() + " 开始运行...");
                        List<INSBFilelibraryUploadCosFail> list = filelibraryUploadCosFailService.selectOneTimeReUploadCos();
                        while (list.size() > 0 && !AppRegisteredController.interrupt) {
                            filelibraryUploadCosFailService.uploadCosTask();
                            list = filelibraryUploadCosFailService.selectOneTimeReUploadCos();
                        }
                        AppRegisteredController.interrupt = false;
                        AppRegisteredController.running = false;
                        LogUtil.info(Thread.currentThread().getName() + " 结束运行...");
                    }, "重新上传cos线程");
                    reuploadThread.start();
                    model.setStatus("success");
                    model.setMessage("调用成功，线程开始上传");
                }
            }
        } catch (Exception ex) {
            model.setStatus("fail");
            model.setMessage("调用发生异常" + ex.getStackTrace());
        }
        return model;
    }


    /**
     * 接口描述：文件上传
     * 请求地址	/mobile/registered/fileUpLoadWeChat
     *
     * @param
     */
    @RequestMapping(value = "/fileUpLoadWeChat", method = RequestMethod.POST)
    @ResponseBody
    public CommonModel fileUpLoadWeChat(@RequestBody FileUploadBase64Param param)
            throws ControllerException {
        return registeredService.fileUpLoadWeChat(request, param.getMediaid(), param.getFileName(), param.getFileType(), param.getFileDescribes(), param.getJobNum(), param.getTaskId());
    }

    /**
     * 接口描述：认证
     * 请求地址	/mobile/registered/certification
     *
     * @param
     */
    @RequestMapping(value = "/certification", method = RequestMethod.POST)
    @ResponseBody
    public CommonModel certification(@RequestBody CertificationParam param)
            throws ControllerException {
        return registeredService.certification(param.getJobNumOrIdCard(), param.getMainBiz(), param.getIdCardPhotoA(),
                param.getIdCardPhotoB(), param.getBankCardA(), param.getQualificationA(),
                param.getQualificationPage(), param.getNoti(), param.getAgree(), param.getIdno(), param.getName(), param.getBank());
    }

    /**
     * 接口描述：获取验证码图片
     * 请求地址	/mobile/registered/getValidateCodeImg
     */
    @RequestMapping(value = "/getValidateCodeImg", method = RequestMethod.POST)
    @ResponseBody
    public CommonModel getValidateCodeImg()
            throws ControllerException {

        return registeredService.getValidateCodeImg(request);
    }

    /**
     * 接口描述：以流的方式显示验证码图片
     * 请求地址：/mobile/registered/validateCodeImg
     *
     * @param reponse
     * @throws ControllerException
     */
    @RequestMapping(value = "/validateCodeImg", method = RequestMethod.GET)
    public void validateCodeImg(HttpServletResponse response)
            throws ControllerException {
        registeredService.validateCodeImg(request, response);
    }

    /**
     * 接口描述：验证验证码图片
     * 请求地址	/mobile/registered/validateCodeImg
     */
    @RequestMapping(value = "/validateCodeImg", method = RequestMethod.POST)
    @ResponseBody
    public CommonModel validateCodeImg(@RequestBody ValidateCodeImgParam param)
            throws ControllerException {
        return registeredService.validateCodeImg(request, param.getUuid(), param.getCode());
    }

    /**
     * 接口描述：验证验证码图片
     * 请求地址	/mobile/registered/coreAgentDock
     */
    @RequestMapping(value = "/coreAgentDock", method = RequestMethod.POST)
    @ResponseBody
    public CommonModel coreAgentDock(@RequestBody String param)
            throws ControllerException {
        return registeredService.coreAgentDock(param);
    }


    /**
     * 前端修改代理人密码
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateAgentPwd", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> updateAgentPwd(@RequestHeader(value = "token") String token, @RequestBody String param) {
        LogUtil.info("前端修改代理人密码：token=" + token);
        return agentService.changeAgentPwd(token, param);
    }

    /**
     * 统计出该代理人成功推荐的人数、被推荐人中的首单人数、这些人的姓名、所属平台、注册时间、首单时间
     *
     * @return
     */
    @RequestMapping(value = "/myRecommend", method = RequestMethod.GET)
    @ResponseBody
    public CommonModel myRecommend(String agentnum, int limit, long offset) throws ControllerException {
        LogUtil.info("推荐统计代理人工号：" + agentnum + ",limit：" + limit + ",offset：" + offset);
        return registeredService.myRecommend(agentnum, limit, offset);
    }

    @RequestMapping(value = "/validatePhone", method = RequestMethod.POST)
    @ResponseBody
    public CommonModel validatePhone(@RequestBody ValidateCodeParam validate)
            throws ControllerException {
        return registeredService.validatePhone(validate.getPhone());
    }

    @RequestMapping(value = "/validateReferrer", method = RequestMethod.POST)
    @ResponseBody
    public CommonModel validateReferrer(@RequestBody JSONObject object)
            throws ControllerException {
        return registeredService.validateReferrer(object);
    }

    @RequestMapping(value = "/agentRegion", method = RequestMethod.POST)
    @ResponseBody
    public CommonModel agentRegion(@RequestBody JSONObject object)
            throws ControllerException {
        return registeredService.agentRegion(object);
    }

    @RequestMapping(value = "/bank", method = RequestMethod.POST)
    @ResponseBody
    public CommonModel bank(@RequestBody JSONObject object)
            throws ControllerException {
        return registeredService.bank(object);
    }

    /**
     * 解绑代理人openid
     * http://pm.baoxian.in/zentao/task-view-1991.html
     * @param token
     * @param jsonObject
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value="/unBindOpenid",method=RequestMethod.POST)
    @ResponseBody
    public CommonModel removeOpenid(@RequestHeader(value = "token") String token, @RequestBody JSONObject jsonObject) throws ControllerException{
        return registeredService.removeOpenid(jsonObject,token);
    }
}
