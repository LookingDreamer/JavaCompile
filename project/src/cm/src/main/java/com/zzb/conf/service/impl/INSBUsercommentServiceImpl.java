package com.zzb.conf.service.impl;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;

import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.zzb.cm.dao.INSBFilebusinessDao;
import com.zzb.cm.dao.INSBFilelibraryDao;
import com.zzb.conf.entity.INSBUsercommentUploadFile;
import com.zzb.conf.service.INSBUsercommentUploadFileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.system.dao.INSCCodeDao;
import com.cninsure.system.entity.INSCCode;
import com.zzb.conf.dao.INSBUsercommentDao;
import com.zzb.conf.entity.INSBUsercomment;
import com.zzb.conf.service.INSBUsercommentService;

@Service
@Transactional
public class INSBUsercommentServiceImpl extends BaseServiceImpl<INSBUsercomment> implements INSBUsercommentService {
	@Resource
	private INSBUsercommentDao insbUsercommentDao;
	@Resource
	private INSBUsercommentUploadFileService insbUsercommentUploadFileService;
	@Resource
	private INSCCodeDao inscCodeDao;
	@Resource
	private INSBFilelibraryDao insbFilelibraryDao;
	@Resource
	private INSBFilebusinessDao insbFilebusinessDao;

	@Override
	protected BaseDao<INSBUsercomment> getBaseDao() {
		return insbUsercommentDao;
	}

	/**
	 * 根据流程轨迹id查询某节点的用户备注信息 trackid:主或子流程轨迹id；tracktype:主流程或子流程标记 1主流程，2子流程
	 */
	@Override
	public INSBUsercomment selectUserCommentByTrackid(String trackid, Integer tracktype) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("trackid", trackid);
		params.put("tracktype", tracktype);
		return insbUsercommentDao.selectUserCommentByTrackid(params);
	}

	/**
	 * 修改用户备注
	 */
	@Override
	public String editUserComment(INSBUsercomment usercomment) {
		Date date = new Date();
		// 通过传回的id判断此任务节点之前是否有用户备注
		if (usercomment.getId() == null || "".equals(usercomment.getId())) {// 之前没有用户备注，新添加用户备注
			usercomment.setId(null);
			usercomment.setCreatetime(date);
			usercomment.setCommentsource(1);
			insbUsercommentDao.insert(usercomment);
		} else {// 之前有用户备注，查询更新处理
			INSBUsercomment temp = insbUsercommentDao.selectById(usercomment.getId());
			usercomment.setCreatetime(temp.getCreatetime());
			usercomment.setModifytime(date);
			System.out.println("修改的备注id = " + usercomment.getId());
			insbUsercommentDao.updateById(usercomment);
		}
		// 更新备注影像
		if (StringUtils.isNotBlank(usercomment.getId())) {
			System.out.println("上传的备注id = " + usercomment.getId());
			insbUsercommentUploadFileService.deleteByUsercommentId(usercomment.getId());
			if (usercomment.getCodetypes() != null) {
				for (String codeType : usercomment.getCodetypes()) {
					INSBUsercommentUploadFile userCommentUpf = new INSBUsercommentUploadFile();
					userCommentUpf.setUsercommentid(usercomment.getId());
					userCommentUpf.setCodetype(codeType);
					userCommentUpf.setCreatetime(new Date());
					insbUsercommentUploadFileService.insert(userCommentUpf);
				}
				//删除退回备注要重新上传的文件
				if(StringUtils.isNotBlank(usercomment.getInstanceId())){
					//先查询：退回备注要重新上传的文件id,文件业务关连表的id  instanceid usercomment.getCodetypes()
					List<Map<String,String>> list = insbUsercommentUploadFileService.findFileIds(usercomment.getInstanceId(),usercomment.getCodetypes());
					if(list!=null && list.size()>0){
						//再删除
						List flib_id_list = Arrays.asList(list.stream().map(item -> (String) item.get("flib_id")).toArray());
						List fbus_id_list = Arrays.asList(list.stream().map(item -> item.get("fbus_id")).toArray());
						if(flib_id_list!= null && flib_id_list.size()>0){
							insbFilelibraryDao.deleteIn(flib_id_list);
							LogUtil.info("更新核保退回备注，任务id:" + usercomment.getInstanceId() + " 表：insbFilelibrary 删除影像类型：" + usercomment.getCodetypes());
						}
						if(fbus_id_list!=null && fbus_id_list.size()>0){
							insbFilebusinessDao.deleteIn(fbus_id_list);
							LogUtil.info("更新核保退回备注，任务id:" + usercomment.getInstanceId() + " 表：insbFilebusiness 删除影像类型：" + usercomment.getCodetypes());
						}
					} else {
						LogUtil.info("更新核保退回备注，任务ID："+usercomment.getInstanceId()+" 之前无上传影像类型："+usercomment.getCodetypes());
					}
				}else{
					LogUtil.info("更新核保退回备注，无任务id,无法删除退回的影像，跟踪号:"+usercomment.getTrackid() + " id:"+usercomment.getId());
				}
			}
		}
		return "success";
	}

	/**
	 * 通过选定的备注类型查询对应的备注内容类型
	 */
	@Override
	public List<INSCCode> getCommentcontenttypeListByCommenttype(String commenttypeCodeValue) {
		// 查询备注类型
		Map<String, String> param = new HashMap<String, String>();
		param.put("codetype", "commenttype");
		param.put("parentcode", "commenttype");
		param.put("codevalue", commenttypeCodeValue);
		List<INSCCode> commenttype = inscCodeDao.selectINSCCodeByCode(param);
		List<INSCCode> commentcontenttypeList = new ArrayList<INSCCode>();
		if (commenttype != null && commenttype.size() > 0) {
			String[] commentcontenttypeValues = commenttype.get(0).getProp2().split(",");
			param.replace("codetype", "commentcontenttype");
			param.replace("parentcode", "commentcontenttype");
			for (int i = 0; i < commentcontenttypeValues.length; i++) {
				param.replace("codevalue", commentcontenttypeValues[i]);
				commentcontenttypeList.add(inscCodeDao.selectINSCCodeByCode(param).get(0));
			}
		}
		return commentcontenttypeList;
	}

	/**
	 * 查询当前节点之前的用户备注信息 入参 instanceid： 主流程实例id ; inscomcode 保险公司code ; dqtaskcode
	 * 当前节点code
	 */
	@Override
	public List<INSBUsercomment> getNearestUserComment(String instanceid, String inscomcode, String dqtaskcode) {
		// 组织参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("instanceid", instanceid);
		params.put("inscomcode", inscomcode);
		params.put("dqtaskcode", dqtaskcode);
		return insbUsercommentDao.getNearestUserComment(params);
	}

	// wangyang
	@Override
	public List<INSBUsercomment> getNearestUserComment2(String instanceid, String inscomcode, String dqtaskcode) {
		// 组织参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("instanceid", instanceid);
		params.put("inscomcode", inscomcode);
		params.put("dqtaskcode", dqtaskcode);
		return insbUsercommentDao.getNearestUserComment2(params);
	}

	@Override
	/**
	 * 前端查询备注
	 */
	public List<Map<String, Object>> getNearestUserComment3(String instanceid, String inscomcode, String dqtaskcode, Integer commentsource) {
		if (StringUtil.isEmpty(instanceid) || StringUtil.isEmpty(inscomcode)) {
			LogUtil.info("前端查询备注非法查询条件 instanceid" + instanceid + " inscomcode" + inscomcode);
			return null;
		}
		// 组织参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("instanceid", instanceid);
		params.put("inscomcode", inscomcode);
		//params.put("dqtaskcode", dqtaskcode);
		params.put("commentsource", commentsource);//备注来源 1:业管 0:代理人
		return insbUsercommentDao.getNearestUserComment3(params);
	}

	@Override
	public List<INSBUsercomment> getNearestUserComment4(String instanceid, String inscomcode) {
		// 组织参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("instanceid", instanceid);
		params.put("inscomcode", inscomcode);
		return insbUsercommentDao.getNearestUserComment4(params);
	}

	@Override
	public List<INSBUsercomment> getNearestInsureBack(String instanceid, String inscomcode) {
		// 组织参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("instanceid", instanceid);
		params.put("inscomcode", inscomcode);
		return insbUsercommentDao.getNearestInsureBack(params);
	}

	@Override
	public List<INSBUsercomment> getAllUserComment(String instanceid, String inscomcode) {
		// 组织参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("instanceid", instanceid);
		params.put("inscomcode", inscomcode);
		return insbUsercommentDao.getAllUserComment(params);
	}

	@Override
	public List<String> getUserComment(String instanceid, String inscomcode) {
		List<String> notiList = new ArrayList<String>();
		List<INSBUsercomment> insbUsercommentList = this.getAllUserComment(instanceid, inscomcode);
		if (insbUsercommentList != null && insbUsercommentList.size() > 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
			for (INSBUsercomment insbUsercomment : insbUsercommentList) {
				// 判断给用户备注内容是否为空或者空格
				if (insbUsercomment.getCommentcontent() != null
						&& !"".equals(insbUsercomment.getCommentcontent().trim())) {
					String noti = "";
					// 判断有无修改时间
					if (insbUsercomment.getModifytime() != null) {
						noti = insbUsercomment.getCommentcontent() + "-" + insbUsercomment.getOperator() + "-"
								+ sdf.format(insbUsercomment.getModifytime());
					} else {
						noti = insbUsercomment.getCommentcontent() + "-" + insbUsercomment.getOperator() + "-"
								+ sdf.format(insbUsercomment.getCreatetime());
					}
					notiList.add(noti);
				}
			}
		}
		return notiList;
	}

	@Override
	public List<Map<String, Object>> getNearestUserCommentAndType(String instanceid, String inscomcode) {
		// 组织参数
		if (StringUtil.isEmpty(instanceid) && StringUtil.isEmpty(inscomcode)) {
			return null;
		}
		Map<String, Object> params = new HashMap<String, Object>(2);
		Map<String, Object> result;
		List<Map<String, Object>> mapList = null;
		if (StringUtil.isNotEmpty(instanceid)) {
			params.put("instanceid", instanceid);
		}
		if (StringUtil.isNotEmpty(inscomcode)) {
			params.put("inscomcode", inscomcode);
		}
		Map<String, String> codeNamesMap = inscCodeDao.getCodeNamesMap("agentnoti", "agentnoti");
		List<INSBUsercomment> usercommentList = insbUsercommentDao.getNearestUserComment4(params);
		if (usercommentList != null && usercommentList.size() > 0) {
			String codeName = null;
			mapList = new ArrayList<Map<String, Object>>();
			for (INSBUsercomment usm : usercommentList) {
				if (usm == null || usm.getCommentcontent() == null || "".equals(usm.getCommentcontent().trim()) || codeNamesMap == null)
					continue;
				result = new HashMap<String, Object>();
				codeName = codeNamesMap.get(String.valueOf(usm.getCommentcontenttype()));
				if (codeName != null) {
					codeName = codeName.contains("(") ? codeName.substring(0, codeName.indexOf("(")) : codeName;
				}
				result.put("typeName", codeName);
				result.put("userComment", usm);
				mapList.add(result);
			}
		}
		return mapList;
	}

	@Override
	public List<Map<String, Object>> getUserCommentAndType(String instanceid, String inscomcode) {
		// 组织参数
		if (StringUtil.isEmpty(instanceid) && StringUtil.isEmpty(inscomcode)) {
			return null;
		}
		Map<String, Object> params = new HashMap<String, Object>(2);
		Map<String, Object> result;
		List<Map<String, Object>> userCommentList = null;
		if (StringUtil.isNotEmpty(instanceid)) {
			params.put("instanceid", instanceid);
		}
		if (StringUtil.isNotEmpty(inscomcode)) {
			params.put("inscomcode", inscomcode);
		}
		List<INSBUsercomment> usercomment = insbUsercommentDao.getNearestUserComment5(params);

		Map<String, String> codeNamesMap = inscCodeDao.getCodeNamesMap("commenttype", "commenttype");

		if (usercomment != null && usercomment.size() > 0) {
			String codeName = null;
			userCommentList = new ArrayList<Map<String, Object>>();
			for (INSBUsercomment usc : usercomment) {
				if (usc == null || usc.getCommentcontent() == null || "".equals(usc.getCommentcontent().trim()) || codeNamesMap == null)
					continue;
				result = new HashMap<String, Object>();
				codeName = codeNamesMap.get(String.valueOf(usc.getCommenttype()));
				if (codeName != null) {
					codeName = codeName.contains("(") ? codeName.substring(0, codeName.indexOf("(")) : codeName;
				}
				result.put("typeName", codeName);
				result.put("userComment", usc);
				userCommentList.add(result);
			}
		}
		return userCommentList;
	}

	@Override
	public String getCommentNameByCodeValue(String codeValue, String codeType, String parentCode) {
		String result = "";
		INSCCode inscCode = new INSCCode();
		inscCode.setCodetype(codeType);
		inscCode.setParentcode(parentCode);
		inscCode.setCodevalue(codeValue);
		INSCCode code = inscCodeDao.selectOne(inscCode);
		if (null != code) {
			String codeName = code.getCodename();
			result = codeName.contains("(") ? codeName.substring(0, codeName.indexOf("(")) : codeName;
		}
		return result;
	}
}