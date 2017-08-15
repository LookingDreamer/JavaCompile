package com.cninsure.system.controller;

import com.common.redis.IRedisClient;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.tools.util.ValidateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.cninsure.system.dao.INSCCodeDao;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCCodeService;
import com.common.ConstUtil;
import com.common.ModelUtil;
import com.zzb.cm.entity.INSBFilelibrary;
import com.zzb.cm.service.INSBFilelibraryService;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.service.INSBProviderService;

@Controller
@RequestMapping("/file/*")
public class FileUpLoadController extends BaseController {
	public static final String TYPEID = "typeid";
	public static final String CM_FILE_UPLOAD = "cm:file_upload";
	public static final String ID = "id";
	@Resource
	private INSBFilelibraryService inscFilelibraryService;
	@Resource
	private INSCCodeService inscCodeService;
	@Resource
	private INSCCodeDao insccodeDao;
	@Resource
	private HttpServletRequest request;
	@Resource
	private INSBProviderService insbProviderService;

	@Resource
	private IRedisClient redisClient;

	@RequestMapping(value = "index", method = RequestMethod.GET)
	public ModelAndView index() {
		ModelAndView mav = new ModelAndView("system/fileupload");
		return mav;
	}

	@RequestMapping(value = "addclassify", method = RequestMethod.GET)
	public ModelAndView addClassify(@RequestParam("classify") String classify) {
		LogUtil.info(classify);
		ModelAndView mav = new ModelAndView("system/addclassify");
		mav.addObject("classify", classify);
		return mav;
	}

	@RequestMapping(value = "saveclassify", method = RequestMethod.POST)
	@ResponseBody
	public String saveClassify(@RequestParam("classify") String classify,
			@RequestParam("classifyname") String classifyname) {
		LogUtil.info(classify);
		LogUtil.info(classifyname);
		return "";
	}

	@RequestMapping(value = "uploadfile", method = RequestMethod.POST)
	@ResponseBody
	public String uploadFile(HttpSession session, @RequestParam("file") MultipartFile file,
			@RequestParam("filetype") String filetype,
			@RequestParam(value = "filedescribe", required = false) String filedescribe) throws ControllerException {
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		Map<String, Object> resultMap = inscFilelibraryService.upload(request, file, filetype, filedescribe,
				loginUser.getUsercode());
		JSONObject jsonObject = JSONObject.fromObject(resultMap);
		return jsonObject.toString();
	}

	@RequestMapping(value = "mediaedit", method = RequestMethod.GET)
	public ModelAndView mediaEdit(@RequestParam(value = "previewpicturelid") String previewpicturelid,
			@RequestParam(value = "fileclassifytype", required = false) String fileclassifytype)
			throws ControllerException {
		INSBFilelibrary temInscFilelibrary = new INSBFilelibrary();
		temInscFilelibrary = inscFilelibraryService.queryById(previewpicturelid);
		ModelAndView mav = new ModelAndView("system/mediaedit");
		mav.addObject("entity", temInscFilelibrary);
		mav.addObject("imgclassifytype", fileclassifytype);
		return mav;
	}

	@RequestMapping(value = "uploadpage", method = RequestMethod.GET)
	public ModelAndView upLoad(@RequestParam(value = "offset", required = false) Integer offset,
			@RequestParam(value = "limit", required = false) Integer limit) throws ControllerException {
		ModelAndView mav = new ModelAndView("system/fileupload");
		List<INSBFilelibrary> imgslist = new ArrayList<INSBFilelibrary>();
		if (ModelUtil.isVoluation(offset, limit)) {
			offset = ConstUtil.OFFSET;
			limit = ConstUtil.LIMIT;
		}
		imgslist = inscFilelibraryService.queryAll(offset, limit);
		mav.addObject("imgslist", imgslist);
		return mav;
	}

	@RequestMapping(value = "mediavideoshow", method = RequestMethod.GET)
	public ModelAndView mediavideoshow(@RequestParam(value = "previewvideoid") String previewvideoid)
			throws ControllerException, ServletException, IOException {
		ModelAndView mav = new ModelAndView("system/mediavideoshow");
		INSBFilelibrary videoentity = inscFilelibraryService.queryById(previewvideoid);
		String filepath = videoentity.getFilepath();
		String suffix = filepath.substring(filepath.lastIndexOf(".") + 1, filepath.length());
		String pa = "http://" + ValidateUtil.getConfigValue("localhost.ip") + ":"
				+ ValidateUtil.getConfigValue("localhost.port") + "/"
				+ ValidateUtil.getConfigValue("localhost.projectName") + filepath
						.substring(
								filepath.indexOf(ValidateUtil.getConfigValue("localhost.projectName"))
										+ ValidateUtil.getConfigValue("localhost.projectName").length(),
								filepath.length());
		Map<String, Object> map = new HashMap<String, Object>();
		mav.addObject("videopath", pa);
		mav.addObject("videotype", suffix);

		return mav;

	}

	@RequestMapping(value = "mediaaudioshow", method = RequestMethod.GET)
	public ModelAndView mediaaudioshow(@RequestParam(value = "previewaudioid") String previewaudioid)
			throws ControllerException {
		ModelAndView mav = new ModelAndView("system/mediaaudioshow");
		INSBFilelibrary videoentity = inscFilelibraryService.queryById(previewaudioid);
		String filepath = videoentity.getFilepath();
		String suffix = filepath.substring(filepath.lastIndexOf(".") + 1, filepath.length());
		// String
		// pa="http://"+ValidateUtil.getConfigValue("localhost.ip")+":"+ValidateUtil.getConfigValue("localhost.port")+"/"+
		// ValidateUtil.getConfigValue("localhost.projectName")+filepath.substring(filepath.indexOf(ValidateUtil.getConfigValue("localhost.projectName"))+
		// ValidateUtil.getConfigValue("localhost.projectName").length(),
		// filepath.length());
		Map<String, Object> map = new HashMap<String, Object>();
		mav.addObject("audiopath", filepath);
		mav.addObject("audiotype", suffix);

		return mav;
	}

	@RequestMapping(value = "previewpicturel", method = RequestMethod.GET)
	public ModelAndView previewPicturel(@RequestParam(value = "previewpicturelid") String previewpicturelid,
			@RequestParam(value = "fileclassifytype", required = false) String fileclassifytype)
			throws ControllerException {
		ModelAndView mav = new ModelAndView("system/previewpicturel");
		List<INSBFilelibrary> imgsList = new ArrayList<INSBFilelibrary>();
		imgsList = inscFilelibraryService.initFileLibraryData("img", "", "", fileclassifytype);
		for (int i = 0; i < imgsList.size(); i++) {
			INSBFilelibrary temInscFilelibrary = new INSBFilelibrary();
			temInscFilelibrary = imgsList.get(i);
			if (previewpicturelid.equals(temInscFilelibrary.getId())) {
				mav.addObject("index", i);
				break;
			}
		}
		mav.addObject("imgslist", imgsList);
		return mav;
	}

	@RequestMapping(value = "medialibrary", method = RequestMethod.GET)
	public ModelAndView mediaLibrary(@RequestParam(value = "filetype") String filetype,
			@RequestParam(value = "classifytype") String classifytype) throws ControllerException {
		ModelAndView mav = new ModelAndView("system/medialibrary");
		mav.addObject("filetype", filetype);
		mav.addObject(filetype + "classifytype", classifytype);
		return mav;
	}

	@RequestMapping(value = "upload", method = RequestMethod.POST)
	@ResponseBody
	public String saveUser(HttpSession session, @ModelAttribute INSCUser user) throws ControllerException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.accumulate("msg", true);
		return jsonObject.toString();
	}

	// 供应商logo上传
	@RequestMapping(value = "uploadprologo", method = RequestMethod.POST)
	@ResponseBody
	public String uploadProLogo(HttpSession session, @RequestParam(value = "file", required = false) MultipartFile file,
			@RequestParam(value = "proid", required = false) String proId) throws ControllerException {
		boolean flag = false;
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		try {
			String uploadImagePath = ValidateUtil.getConfigValue("uploadimage.path");
			if (StringUtil.isEmpty(uploadImagePath)) {
				flag = inscFilelibraryService.uploadProLogo(request, session.getServletContext().getRealPath("/"), file,
						loginUser.getUsercode(), proId, "logo");
			} else {
				flag = inscFilelibraryService.uploadProLogo(request, uploadImagePath, file, loginUser.getUsercode(), proId, "logo");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		INSBProvider provider = insbProviderService.queryById(proId);
		JSONObject jsonObject = new JSONObject();
		jsonObject.accumulate("provider", provider);
		if (flag) {
			jsonObject.accumulate("msg", "成功");
			return jsonObject.toString();
		} else {
			jsonObject.accumulate("msg", "失败");
			return jsonObject.toString();
		}
	}

	@RequestMapping(value = "uploadonefile", method = RequestMethod.POST)
	@ResponseBody
	public String uploadOneFile(HttpSession session, @RequestParam(value = "file", required = false) MultipartFile file,
			@RequestParam(value = "filename", required = false) String filename,
			@RequestParam(value = "filedescribe", required = false) String filedescribe, @RequestParam(ID) String id,
			@RequestParam(value = "fileclassifytype", required = false) String fileclassifytype)
			throws ControllerException {
		boolean flag = false;
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		try {
			flag = inscFilelibraryService.uploadOneFile(request, file, loginUser.getUsercode(), filename, filedescribe,
					id, fileclassifytype);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (flag) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.accumulate("result", "filebatchuploadcomplete");
			jsonObject.accumulate("newimgsrc", inscFilelibraryService.queryById(id).getFilepath());
			jsonObject.accumulate("entity", inscFilelibraryService.queryById(id));
			return jsonObject.toString();
		} else {
			JSONObject jsonObject = new JSONObject();
			jsonObject.accumulate("result", "error");
			jsonObject.accumulate("entity", inscFilelibraryService.queryById(id));
			return jsonObject.toString();
		}
	}

	@RequestMapping(value = "uploadfiles", method = RequestMethod.POST)
	@ResponseBody
	public String uploadFiles(HttpSession session, @RequestParam("file") MultipartFile[] files,
			@RequestParam(value = "filetype") String filetype, @RequestParam("filename") String[] filenames,
			@RequestParam("filedescribe") String[] filedescribes,
			@RequestParam(value = "fileclassifytype", required = false) String fileclassifytype)
			throws ControllerException {
		if (StringUtil.isEmpty(filetype)) {
			filetype = "other";
		}
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		try {
			if (StringUtil.isEmpty(ValidateUtil.getConfigValue("uploadimage.path"))) {
				inscFilelibraryService.uploadFileByCos(files, session.getServletContext().getRealPath("/"), filetype,
						loginUser.getUsercode(), filenames, filedescribes, fileclassifytype);
			} else {
				inscFilelibraryService.uploadFileByCos(files, ValidateUtil.getConfigValue("uploadimage.path"), filetype,
						loginUser.getUsercode(), filenames, filedescribes, fileclassifytype);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.accumulate("result", "filebatchuploadcomplete");
		jsonObject.accumulate("count", files.length);
		return jsonObject.toString();
	}

	@RequestMapping(value = "queryList", method = RequestMethod.POST)
	@ResponseBody
	public String queryList(HttpSession session, @RequestParam("filetype") String filetype,
			@RequestParam("fileclassifytype") String fileclassifytype, @RequestParam("filename") String filename,
			@RequestParam("filedescribe") String filedescribe, @RequestParam("taskid") String taskid,
			@RequestParam("platenum") String platenum, @RequestParam("insured") String insured)
			throws ControllerException {

		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		JSONObject jsonObject = new JSONObject();
		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(filename)) {
			if (!filename.equals("undefined")) {
				map.put("filename", filename);
			}
		}
		if (!StringUtil.isEmpty(filedescribe)) {
			if (!filedescribe.equals("undefined")) {
				map.put("filedescribe", filedescribe);
			}
		}
		// map.put("operator", loginUser.getUsercode());
		map.put("filetype", filetype);
		map.put("filecodevalue", fileclassifytype);
		if (!StringUtil.isEmpty(platenum)) {
			if (!platenum.equals("undefined")) {
				map.put("platenum", platenum);
			}
		}
		if (!StringUtil.isEmpty(insured)) {
			if (!insured.equals("undefined")) {
				map.put("insured", insured);
			}
		}
		if (!StringUtil.isEmpty(taskid)) {
			if (!taskid.equals("undefined")) {
				map.put("taskid", taskid);
			}
		}

		List<INSBFilelibrary> list = inscFilelibraryService.selectListByMap(map);
		// jsonObject.accumulate("datas",
		// inscFilelibraryService.initFileLibraryData(filetype,filename,filedescribe,fileclassifytype));
		jsonObject.put("datas", list);
		jsonObject.put(filetype + "classify", fileclassifytype);
		jsonObject.put("type", filetype);
		jsonObject.put("classifytype", fileclassifytype);

		return jsonObject.toString();
	}

	@RequestMapping(value = "initdata", method = RequestMethod.POST)
	@ResponseBody
	public String initdata(@RequestParam(value = "filetype", required = false) String filetype,
			@RequestParam(value = "fileclassifytype", required = false) String fileclassifytype)
			throws ControllerException {
		JSONObject jsonObject = new JSONObject();
		INSCCode inscCode = new INSCCode();
		if (StringUtil.isEmpty(filetype) && StringUtil.isEmpty(fileclassifytype)) {
			jsonObject.accumulate("imgdatas", inscFilelibraryService.initFileLibraryData("img", "", "",
					(inscCodeService.queryINSCCodeByCode("img", "").get(0)).getCodevalue()));
			jsonObject.accumulate("videodatas", inscFilelibraryService.initFileLibraryData("video", "", "", ""));
			jsonObject.accumulate("audiodatas", inscFilelibraryService.initFileLibraryData("audio", "", "", ""));
			jsonObject.accumulate("otherdatas", inscFilelibraryService.initFileLibraryData("other", "", "", ""));
			inscCode.setParentcode("img");
			jsonObject.accumulate("imgclassify", inscCodeService.queryINSCCodeByCode("img", ""));
			inscCode.setParentcode("video");
			jsonObject.accumulate("videoclassify", inscCodeService.queryINSCCodeByCode("video", ""));
			inscCode.setParentcode("audio");
			jsonObject.accumulate("audioclassify", inscCodeService.queryINSCCodeByCode("audio", ""));
			inscCode.setParentcode("other");
			jsonObject.accumulate("otherclassify", inscCodeService.queryINSCCodeByCode("other", ""));
		} else if ("img".equals(filetype)) {
			jsonObject.accumulate("imgdatas",
					inscFilelibraryService.initFileLibraryData("img", "", "", fileclassifytype));
			inscCode.setParentcode("img");
			jsonObject.accumulate("imgclassify", inscCodeService.queryINSCCodeByCode("img", ""));
		} else if ("video".equals(filetype)) {
			jsonObject.accumulate("videodatas",
					inscFilelibraryService.initFileLibraryData("video", "", "", fileclassifytype));
			inscCode.setParentcode("video");
			jsonObject.accumulate("videoclassify", inscCodeService.queryINSCCodeByCode("video", ""));
		} else if ("audio".equals(filetype)) {
			jsonObject.accumulate("audiodatas",
					inscFilelibraryService.initFileLibraryData("audio", "", "", fileclassifytype));
			inscCode.setParentcode("audio");
			jsonObject.accumulate("audioclassify", inscCodeService.queryINSCCodeByCode("audio", ""));
		} else if ("other".equals(filetype)) {
			jsonObject.accumulate("otherdatas",
					inscFilelibraryService.initFileLibraryData("other", "", "", fileclassifytype));
			inscCode.setParentcode("other");
			jsonObject.accumulate("otherclassify", inscCodeService.queryINSCCodeByCode("other", ""));
		} else {

		}
		jsonObject.accumulate("type", filetype);
		jsonObject.accumulate("classifytype", fileclassifytype);
		return jsonObject.toString();
	}

	@RequestMapping(value = "initclassifytypedata", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<Object, Object>> initClassifyTypedata(@RequestParam(value = "parentcode") String parentcode)
			throws ControllerException {
		return inscFilelibraryService.initMenusList(parentcode);
	}

	@RequestMapping(value = "updatebyid", method = RequestMethod.POST)
	@ResponseBody
	public String updateMenuById(@RequestParam(value = ID) String id, @RequestParam(value = "codename") String codename)
			throws ControllerException {
		INSCCode inscCode = inscCodeService.queryById(id);
		inscCode.setCodename(codename);
		inscCodeService.updateById(inscCode);
		JSONObject jsonObject = new JSONObject();
		jsonObject.accumulate("result", "ok");
		jsonObject.accumulate(ID, id);
		jsonObject.accumulate("codename", codename);
		jsonObject.accumulate("parentcode", inscCode.getParentcode());
		return jsonObject.toString();
	}

	@RequestMapping(value = "deletebyid", method = RequestMethod.POST)
	@ResponseBody
	public String deleteMenuById(@RequestParam(value = ID) String id) throws ControllerException {
		INSCCode insccode = inscCodeService.queryById(id);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("parentcode", insccode.getParentcode());
		INSBFilelibrary insbFilelibrary = new INSBFilelibrary();
		insbFilelibrary.setFiletype(insccode.getParentcode());
		insbFilelibrary.setFilecodevalue(insccode.getCodevalue());
		List<INSBFilelibrary> list = inscFilelibraryService.queryList(insbFilelibrary);
		List<String> idlist = new ArrayList<String>();
		for (INSBFilelibrary file : list) {
			idlist.add(file.getId());
		}
		inscFilelibraryService.deleteByIdInBatch(idlist);
		inscCodeService.deleteById(id);

		return jsonObject.toString();
	}

	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public String addMenu(@RequestParam(value = "codename") String codename) throws ControllerException {

		INSCCode insccode = null;
		List<String> list = new ArrayList<String>();
		list.add("img");
		list.add("video");
		list.add("audio");
		list.add("other");
		String result = "添加失败";
		Map<String, String> map = new HashMap<String, String>();
		try {
			String id = redisClient.get(CM_FILE_UPLOAD, TYPEID).toString();
			if (list.contains(id)) {
				insccode = new INSCCode();
				insccode.setParentcode(id);
				List<INSCCode> querylist = inscCodeService.queryList(insccode);
				List<Integer> intlist = new ArrayList<Integer>();
				int max = 0;
				if (querylist.size() > 0) {
					for (INSCCode code : querylist) {
						intlist.add(Integer.parseInt(code.getCodevalue()));
					}
					for (int i = 0; i < intlist.size(); i++) {
						if (intlist.get(i) > max) {
							max = intlist.get(i);
						}
					}
					insccode.setId(UUIDUtils.random());
					insccode.setCreatetime(new Date());
					insccode.setCodevalue(String.valueOf(max + 1));
					insccode.setParentcode(id);
					insccode.setOperator("1");
					insccode.setCodetype("imgclassify");
					insccode.setNoti("图片分类");
					insccode.setCodeorder(1);
					insccode.setCodename(codename);
					insccodeDao.insert(insccode);
					map.put("parentcode", id);
					result = "true";

				} else {
					insccode.setId(UUIDUtils.random());
					insccode.setCreatetime(new Date());
					insccode.setCodevalue("1"); // codevalue从1开始
					insccode.setParentcode(id);
					insccode.setOperator("1");
					insccode.setCodetype("imgclassify");
					insccode.setNoti("图片分类");
					insccode.setCodeorder(1);
					insccode.setCodename(codename);
					insccodeDao.insert(insccode);
					map.put("parentcode", id);
					result = "true";
				}

			} else {
				INSCCode inscCode = null;
				INSCCode insc = inscCodeService.queryById(redisClient.get(CM_FILE_UPLOAD, ID).toString());
				if (insc != null) {
					insccode = new INSCCode();
					insccode.setParentcode(insc.getParentcode());
					List<INSCCode> querylist = inscCodeService.queryList(insccode);
					List<Integer> intlist = new ArrayList<Integer>();
					int max = 0;
					if (querylist.size() > 0) {
						for (INSCCode code : querylist) {
							intlist.add(Integer.parseInt(code.getCodevalue()));
						}
						for (int i = 0; i < intlist.size(); i++) {
							if (intlist.get(i) > max) {
								max = intlist.get(i);
							}
						}
						inscCode = new INSCCode();
						insccode.setId(UUIDUtils.random());
						insccode.setCreatetime(new Date());
						insccode.setParentcode(insc.getParentcode());
						insccode.setCodevalue(String.valueOf(max + 1));
						insccode.setOperator("1");
						insccode.setCodetype("imgclassify");
						insccode.setNoti("图片分类");
						insccode.setCodeorder(1);
						insccode.setCodename(codename);
						insccodeDao.insert(insccode);
						map.put("parentcode", insc.getParentcode());
						result = "添加成功";
					}
				} else {
					return null;
				}

			}
		} catch (Exception e) {
			return null;
		}
		redisClient.del(CM_FILE_UPLOAD, TYPEID);
		map.put("result", result);

		return JSONObject.fromObject(map).toString();
	}

	@RequestMapping(value = "querybyid", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView queryById(@RequestParam(value = ID) String id) throws ControllerException {
		ModelAndView model = new ModelAndView();
		INSCCode inscCode = inscCodeService.queryById(id);
		model.addObject("menuobject", inscCode);
		try {
			redisClient.set(CM_FILE_UPLOAD, TYPEID, id, 20 * 24 * 60 * 60);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}

	@RequestMapping(value = "getFilePathbyid", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView getFilePathbyid(@RequestParam(value = ID) String id) throws ControllerException {
		ModelAndView model = new ModelAndView();
		INSBFilelibrary file = inscFilelibraryService.queryById(id);
		String filepath = file.getFilepath();
		String pa = filepath.substring(filepath.indexOf(ValidateUtil.getConfigValue("localhost.projectName")),
				filepath.length());
		String path = "http://" + ValidateUtil.getConfigValue("localhost.ip") + ":"
				+ ValidateUtil.getConfigValue("localhost.port") + "/" + pa;
		model.addObject("filepath", path);
		model.addObject("filetype", file.getFiletype());
		return model;
	}

	@RequestMapping(value = "deletedata", method = RequestMethod.POST)
	@ResponseBody
	public String deletedata(@RequestParam("ids") String[] ids) throws ControllerException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.accumulate("result", inscFilelibraryService.deleteFileLibraryData(ids));
		return jsonObject.toString();
	}

	public static void main(String[] args) {
		String a = "{\"a\":false}";
		JSONObject b = JSONObject.fromObject(a);
		Object c = b.get("a");
		System.out.println(String.valueOf(c));
	}

	@RequestMapping(value = "download", method = RequestMethod.GET)
	public void download(@RequestParam("fileid") String fileid, HttpServletResponse response) throws IOException {
		INSBFilelibrary insbFilelibrary = inscFilelibraryService.queryById(fileid);
		if (insbFilelibrary == null || StringUtil.isEmpty(insbFilelibrary.getFilephysicalpath()))
			return;
		String fileType = insbFilelibrary.getFilepath().substring(insbFilelibrary.getFilepath().lastIndexOf("."),
				insbFilelibrary.getFilepath().length());
		File tempFile = File.createTempFile(insbFilelibrary.getFilename() + ModelUtil.conbertToString(new Date()),
				fileType);
		FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
		URL url = new URL(insbFilelibrary.getFilepath());
		InputStream inputStream = url.openStream();
		int index = 0;
		while (index != -1) {
			index = inputStream.read();
			fileOutputStream.write(index);
			fileOutputStream.flush();
		}
		String filename = ModelUtil.conbertToString(new Date()) + fileType;
		response.setHeader("Content-Type", "application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename=" + filename);
		OutputStream os = response.getOutputStream();
		try {
			os.write(FileCopyUtils.copyToByteArray(tempFile));
			os.flush();
		} finally {
			if (os != null)
				os.close();
			if (fileOutputStream != null)
				fileOutputStream.close();
			if (inputStream != null)
				inputStream.close();
		}
	}
}
