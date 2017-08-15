package com.zzb.cm.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.LogUtil;
import com.common.JsonUtil;
import com.zzb.cm.controller.vo.CertificationQueryVO;
import com.zzb.cm.controller.vo.OrderManageVO01;
import com.zzb.cm.service.INSBOrderService;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCCodeService;
import com.common.ModelUtil;
import com.zzb.cm.controller.vo.CertificationVo;
import com.zzb.conf.entity.INSBUsercomment;
import com.zzb.conf.service.INSBCertificationService;
import com.zzb.conf.service.INSBUsercommentService;

import net.sf.json.JSONObject;

/**
 * CM系统 认证任务
 */
@Controller
@RequestMapping("/business/certificationtask/*")
public class INSBCertificationTaskController extends BaseController {
	@Resource
	private INSBCertificationService certificationService;
	@Resource
	private INSCCodeService codeService;
	@Resource
	private INSBUsercommentService insbUsercommentService;
	@Resource
	private INSBOrderService insbOrderService;

	/**
	 * 加载页面  初始化数据
	 * 
	 * @param taskid
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "showcertificationtask", method = RequestMethod.GET)
	public ModelAndView showCertificationTask(String taskid,HttpSession session, String from) throws ControllerException {
		ModelAndView mav = new ModelAndView("cm/certificationtask/certificationTask");
		CertificationVo certificationVo = new CertificationVo();
		certificationVo.setId(taskid);
		certificationVo = certificationService.getCertificationInfo(certificationVo);
		INSCCode busTypeCode = new INSCCode();
		busTypeCode.setCodetype("mainbusiness");
		busTypeCode.setParentcode("mainbusiness");
		List<INSCCode> busTypeList = codeService.queryList(busTypeCode);
		// LINING bug-2913-认证任务选择退回时没有地方填写给前端用户的备注 20160707 START
		certificationVo.setCommentcontent("");
		if( certificationVo != null && StringUtil.isNotEmpty(certificationVo.getAgentid()) ) {
			INSBUsercomment insbUsercomment = insbUsercommentService.selectUserCommentByTrackid(certificationVo.getAgentid(), 9);
			if( insbUsercomment != null && StringUtil.isNotEmpty(insbUsercomment.getCommentcontent()) ) {
				certificationVo.setCommentcontent(insbUsercomment.getCommentcontent());
			}
		}
		// LINING bug-2913-认证任务选择退回时没有地方填写给前端用户的备注 20160707 END
		mav.addObject("busTypeList", busTypeList);
		mav.addObject("certificationVo", certificationVo);
		mav.addObject("bankCardList", codeService.queryMyTaskCode("corebankcard", "corebankcard"));
		mav.addObject("rankList", codeService.queryMyTaskCode("salesmanrank", "salesmanrank"));
		String operator = ((INSCUser)session.getAttribute("insc_user")).getUsercode();
		mav.addObject("edit", operator.equals(certificationVo.getDesignatedoperator()));
		mav.addObject("from", "certificationMng".equals(from));
		if(certificationVo.getStatus()!=0)
			mav.addObject("edit", "false");
		return mav;
	}

	/**
	 * 更新并保存代理人信息
	 * 
	 * @param certificationVo
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "certificate", method = RequestMethod.POST)
	@ResponseBody
	public int certificate(HttpSession session, CertificationVo certificationVo) throws ControllerException {
		String operator = ((INSCUser)session.getAttribute("insc_user")).getUsercode();
        LogUtil.info("认证任务更新并保存代理人信息：" + JsonUtil.serialize(certificationVo));
		return certificationService.updateCertificationInfo(certificationVo, operator);
	}
	/**
	 * 获取正式工号
	 * 
	 */
	@RequestMapping(value = "getFormalNum", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String getFormalNum(HttpSession session, CertificationVo certificationVo) throws ControllerException {
		String operator = ((INSCUser)session.getAttribute("insc_user")).getUsercode();
		return JSONObject.fromObject(certificationService.getFormalNum(certificationVo,operator)).toString();
	}

	/**
	 * 图片下载
	 * 
	 * @param
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "downloadpic", method = RequestMethod.POST)
	public void downloadpic(String pic1,String pic2,String pic3,String pic4,String pic5,String agentname,HttpServletResponse res,HttpServletRequest req) throws IOException {  
		List<String> pics = new ArrayList<String>();
		pics.add(pic1);//身份证正面照路径
		pics.add(pic2);//身份证反面照路径 
		pics.add(pic3);//银行卡正面照路径
		pics.add(pic4);//资格证照片页路径
		pics.add(pic5);//资格证信息页路径
		String filename = ModelUtil.conbertToString(new Date())+agentname+".zip";
		res.setHeader("Content-Type", "application/octet-stream");
		res.setHeader("Content-Disposition", "attachment;filename="+filename);
		OutputStream os = res.getOutputStream();
		File zip = File.createTempFile("temp", ".zip");
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zip));
		for (String temp : pics) {
//			temp = temp.substring(0, temp.indexOf(","));
			if(temp.indexOf("http") == 0){
	//			temp = req.getSession().getServletContext().getRealPath("/")+temp.substring(temp.indexOf("upload/img/"));
				URL url = new URL(temp);
				String[] split = temp.split("/");
				InputStream inputStream = url.openStream();
		        ZipEntry entry = new ZipEntry(split[split.length-1]);
		        zos.putNextEntry(entry);
				int index = 0;  
				while((index = inputStream.read()) != -1){  
		        	zos.write(index);  
		        	zos.flush();
		        }
			}
		}
		zos.close();
		try{
			os.write(FileUtils.readFileToByteArray(zip));
        	os.flush();
		}finally{
			os.close();
		}
	}

	@RequestMapping(value="certificationMng", method=RequestMethod.GET)
	public ModelAndView toCarTaskManageListPage(HttpSession session, OrderManageVO01 myTaskVo01){
		ModelAndView mav = new ModelAndView("cm/certificationtask/certificationMng");
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		return mav;
	}

	@RequestMapping(value="certificationQuery", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> certificationQuery(HttpSession session, CertificationQueryVO certificationQueryVO){
		Map map = BeanUtils.toMap(certificationQueryVO);
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		map.put("loginUserId", loginUser.getId());
		map.put("userorganization", loginUser.getUserorganization());
		return certificationService.getCertificationPage(map);
	}

	/**
	 * 认证任务认领方法liuchao
	 * @param session
	 */
	@RequestMapping(value = "getCertificationTask", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getCertificationTask(HttpSession session, String cfTaskId, String usercode) throws ControllerException {
		return insbOrderService.getCertificationTask2(usercode, cfTaskId);
	}
}
