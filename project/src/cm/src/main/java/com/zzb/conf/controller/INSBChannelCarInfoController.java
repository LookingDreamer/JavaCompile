package com.zzb.conf.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.DateUtil;
import com.common.ExcelUtil;
import com.common.PagingParams;
import com.zzb.conf.entity.INSBBatch;
import com.zzb.conf.entity.INSBChannelcarinfo;
import com.zzb.conf.service.INSBBatchService;
import com.zzb.conf.service.INSBChannelcarinfoService;

@Controller
@RequestMapping("/channel/*") 
public class INSBChannelCarInfoController extends BaseController {
	
	private static final HttpServletResponse HttpServletResponse = null;
	@Resource
	private INSBChannelcarinfoService insbChannelcarinfoService;
	@Resource
	private INSBBatchService insbBatchService;
	// 文件上传
	@RequestMapping(value = "upload", method = RequestMethod.POST)
	@ResponseBody
	public String uploadFile(@RequestParam("uploadfile") MultipartFile uploadfile,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (!isMultipart) {
			return "文件错误";
		}
		System.out.println("--------------------------------------");
		String channelid = request.getParameter("channelid");
		System.out.println(channelid);
		// 获取系统时间 yyyy-MM-d hh:mm:ss
		String datestring = DateUtil.getCurrentDate() + " "
				+ DateUtil.getCurrentTime();
		String datestr = datestring.replace(":", "-");
		String path = request.getSession().getServletContext().getRealPath("/") + "/static/upload/";
		// 文件上传路径
//		String uploadPath = path + "/uploadFile/" + datestr + "/";
		String uploadPath = path + "/channelcar/" + datestr + "/";
		// 获取当前用户;
		// 上传目录是否存在，不存在重新创建
		File pathFile = new File(uploadPath);
		if (!pathFile.exists()) {
			// 文件夹不存 创建文件
			pathFile.mkdirs();
		}
		 System.out.println("文件类型：" + uploadfile.getContentType());
		 System.out.println("文件名称：" + uploadfile.getOriginalFilename());
		 System.out.println("文件大小:" + uploadfile.getSize());
		 System.out.println(".................................................");

		// 将文件copy上传到服务器指定位置
		uploadfile.transferTo(new File(uploadPath + uploadfile.getOriginalFilename()));
		// 批次号
		SimpleDateFormat sdfDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String batchcode = sdfDateFormat.format(new Date());
		// 获取文件路径
		String newfile = uploadPath +  uploadfile.getOriginalFilename();
		List<Row> rows = testRead(newfile);
		Row firstRow = rows.get(0);
		List<INSBChannelcarinfo> channelcarinfoList = new ArrayList<INSBChannelcarinfo>();
		JSONObject jsonObject = new JSONObject();
		if(firstRow != null && !"".equals(firstRow)){
			if ("地区".equals(firstRow.getCell(0).toString()) && "车牌号码".equals(firstRow.getCell(1).toString())
					&&"客户姓名".equals(firstRow.getCell(2).toString()) &&"地址".equals(firstRow.getCell(3).toString())
					&&"手机号码".equals(firstRow.getCell(4).toString()) &&"车辆品牌".equals(firstRow.getCell(5).toString())
					&&"车辆型号".equals(firstRow.getCell(6).toString()) &&"初次登记年月".equals(firstRow.getCell(7).toString())
					&&"发动机号".equals(firstRow.getCell(8).toString().trim())&&"车架号".equals(firstRow.getCell(9).toString().trim())) {
				 for (Row row : rows) {
					 if (!"地区".equals(row.getCell(0).toString())) {
						 INSBChannelcarinfo channelcarinfo = row2DBChannelCarInfo(row, channelid ,batchcode);
						 channelcarinfoList.add(channelcarinfo);
					  }
				 }
				 //如果是个空表如何操作？
				 insbChannelcarinfoService.insertInBatch(channelcarinfoList);
				 INSBBatch insbBatch=new INSBBatch();
				 insbBatch.setId(UUID.randomUUID().toString());
				 insbBatch.setChannelcode(channelid);
				 insbBatch.setBatchacode(batchcode);
				 insbBatch.setCreatetime(new Date());
				 insbBatch.setModifytime(new Date());
				 insbBatchService.insert(insbBatch);
			}
			else {
				jsonObject.accumulate("msg", "FileError");
				return jsonObject.toString();
			}
		}else {
			jsonObject.accumulate("msg", "FileError");
			return jsonObject.toString();
		}
		jsonObject.accumulate("msg", "success");
		return jsonObject.toString();
	}

	private INSBChannelcarinfo row2DBChannelCarInfo(Row row, String channelid, String batchcode) {
		INSBChannelcarinfo channelcarinfo = new INSBChannelcarinfo();
		channelcarinfo.setId(UUID.randomUUID().toString());
		channelcarinfo.setDrivingarea(row.getCell(0).toString());
		channelcarinfo.setCarlicenseno(row.getCell(1).toString());
		channelcarinfo.setCustomername(row.getCell(2).toString());
		channelcarinfo.setAddress(row.getCell(3).toString());
		channelcarinfo.setPhonenumber(row.getCell(4).toString());
		channelcarinfo.setVehiclebrand(row.getCell(5).toString());
		channelcarinfo.setVehicletype(row.getCell(6).toString());
		channelcarinfo.setRegistdate(row.getCell(7).getDateCellValue());
		channelcarinfo.setEngineno(row.getCell(8).toString());
		channelcarinfo.setChassiso(row.getCell(9).toString());
		channelcarinfo.setCreatetime(new Date());
		channelcarinfo.setModifytime(new Date());
		channelcarinfo.setBatchcode(batchcode);
		channelcarinfo.setChannelcode(channelid);
		return channelcarinfo;
	}

	// 车辆管理 加载数据
	@RequestMapping(value = "initchannelcarinfolist", method = RequestMethod.GET)
	@ResponseBody
	public String showCartaskmanageList(@ModelAttribute PagingParams para,INSBChannelcarinfo channelcar) throws ControllerException {
		Map<String, Object> map = BeanUtils.toMap(para,channelcar);
		String channelcarinfoList = insbChannelcarinfoService.initChannelCarInfo(map);
		return channelcarinfoList;
	}
	
	public List<Row> testRead(String filePath){
		try {
			 ExcelUtil eu = new ExcelUtil();  
		        eu.setExcelPath(filePath);  
		        return eu.readExcel();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] args) {
//		INSBChannelCarInfoController insb = new INSBChannelCarInfoController();
//		insb.testRead("");
//		insb.download(null);
		System.out.println(System.getProperty("user.dir"));
	}
	
	
	/**
	 * @param arrayid
	 * @throws ControllerException
	 * @throws IOException
	 */
	@RequestMapping(value = "export", method = RequestMethod.GET)
	public HttpServletResponse exportTXT(@RequestParam(value = "arrayid") List<String> arrayid,HttpServletResponse response) throws ControllerException, IOException {
		String result = insbChannelcarinfoService.exportChannelCarInfo(arrayid);
		String fileName = URLEncoder.encode("渠道车辆批次信息","UTF-8");
		response.setContentType("text/plain");
		response.addHeader("Content-Disposition", "attachment;filename=" + fileName + ".txt");
		OutputStream os = new BufferedOutputStream(response.getOutputStream());
		os.write(result.getBytes("UTF-8"));
		os.flush();
		os.close();
		return response;
	} 
	
	/**
	 * 罗
	 * 批次号查询显示
	 * @param channelcode
	 * @return
	 */
	@RequestMapping(value = "batchlist", method = RequestMethod.GET)
	@ResponseBody
	public String batchList(INSBBatch insbBatch) throws ControllerException{
		String insbBatchList = insbBatchService.initInsbBatch(insbBatch);
		return insbBatchList;
	}

	/**
	 * 模板下载
	 * @param channelcode
	 * @return
	 */
	@SuppressWarnings("resource")
	@RequestMapping(value = "download", method = RequestMethod.GET)
	@ResponseBody
	public HttpServletResponse download(HttpServletResponse response,HttpServletRequest request){
		try {
			//下载CM项目中download中的渠道车辆信息导入模板文件
			String path = request.getSession().getServletContext().getRealPath("/") + "/static/download/渠道车辆信息导入模板.xlsx";
			File file = new File(path);
			String fileName = file.getName(); 
			InputStream inputStream = new BufferedInputStream(new FileInputStream(path));
			byte[] buffer = new byte[inputStream.available()];
			inputStream.read(buffer);
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.replaceAll(" ", "").getBytes("utf-8"),"iso8859-1"));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream os = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			os.write(buffer);
			inputStream.close();
			os.close();
			os.flush();
			return HttpServletResponse;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
