package com.zzb.mobile.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import com.zzb.mobile.model.CommonModel;

public interface AppRegisteredService {
	/**
	 * 验证图片验证码并发送手机验证码，验证手机号是否重复,发送验证码
	 * 
	 * @param request
	 * @param uuid
	 *            获取验证码图片时返回的uuid
	 * @param code
	 *            验证码
	 * @param phoneNo
	 *            -手机号
	 * @return
	 */

	public CommonModel sendValidateCode(HttpServletRequest request, String uuid, String code, String phoneNo);

	/**
	 * 提交注册信息
	 * 
	 * @param regInfoJSON
	 *            "{ ""phone"":""phone"", ""name"":""name"",//姓名
	 *            ""passWord:""passWord"" ""refNum"":refNum,//推荐人工号
	 *            ""provinceCode"":provinceCode, //所在地区 省级code
	 *            ""cityCode"":cityCode, //所在地区 市级code
	 *            ""countyCode"":countyCode,//所在地区 区级code }"
	 * @return
	 */
	public CommonModel submitRegInfo(String regInfoJSON);

	/**
	 * 绑定手机号
	 * @param bindPhoneJSON
	 * @return
     */
	public CommonModel bindPhone(String bindPhoneJSON);

	/**
	 * 绑定工号
	 * 
	 * @param bindInfoJSON
	 *            -"{ ""JobNumOrIdCard"":工号或者身份证号, ""jobNumPassword"
	 *            ":jobNumPassword //工号密码}"
	 * @return
	 */
	public CommonModel bindWorkCode1(String JobNumOrIdCard, String jobNumPassword);

	/**
	 * 绑定工号
	 * 
	 * @param bindInfoJSON
	 *            -"{ tempJobNum:临时工号""JobNumOrIdCard"":工号或者身份证号, "
	 *            "jobNumPassword"":jobNumPassword //工号密码}"
	 * @return
	 */
	public CommonModel bindWorkCode(String tempJobNum, String JobNumOrIdCard, String jobNumPassword);

	/**
	 * 认证
	 * 
	 * @param jobNumOrIdCard
	 *            工号或身份证号
	 * @param mainBiz
	 *            主业务
	 * @param bankName
	 *            主业务
	 * @param depositBank
	 *            主业务
	 * @param accountName
	 *            主业务
	 * @param accountNum
	 *            主业务
	 * @param idCardPhotoA
	 *            身份证正面照
	 * @param idCardPhotoB
	 *            身份证反面照
	 * @param bankCardA
	 *            银行卡正面照
	 * @param qualificationA
	 *            资格证照片页
	 * @param qualificationPage
	 *            资格证信息页
	 * @param noti
	 * @param agree
	 *            是否同意
	 * @return
	 */
	public CommonModel certification(String jobNumOrIdCard, String mainBiz, String idCardPhotoA, String idCardPhotoB,
			String bankCardA, String qualificationA, String qualificationPage, String noti, String agree, String idno, String name,String bank);

	/**
	 * 多个证件照上传
	 * 
	 * @param files
	 *            上传的文件
	 * @param fileTypes
	 *            文件类型
	 * @param fileDescribes
	 *            文件描述
	 * @param jobNum
	 *            工号
	 * @return
	 */
	public CommonModel filesUpLoad(HttpServletRequest request, MultipartFile[] file, String[] fileType,
			String[] fileDescribes, String jobNum);

	/**
	 * 证件照上传
	 * 
	 * @param file
	 *            上传的文件
	 * @param fileType
	 *            文件类型
	 * @param fileDescribes
	 *            文件描述
	 * @param jobNum
	 *            工号
	 * @return
	 */
	public CommonModel fileUpLoad(HttpServletRequest request, MultipartFile file, String fileType, String fileDescribes,
			String jobNum);

	/**
	 * 获取验证码图片
	 * 
	 * @return
	 */
	public CommonModel getValidateCodeImg(HttpServletRequest request);

	/**
	 * 
	 * @param uuid
	 *            获取验证码图片时返回的uuid
	 * @param code
	 *            验证码
	 * @return
	 */
	public CommonModel validateCodeImg(HttpServletRequest request, String uuid, String code);

	/**
	 * 证件照上传
	 * 
	 * @param file
	 *            上传的文件(Base64格式)
	 * @param fileName
	 *            上传的文件名
	 * @param fileType
	 *            文件类型
	 * @param fileDescribes
	 *            文件描述
	 * @param jobNum
	 *            工号
	 * @return
	 */
	public CommonModel fileUpLoadBase64(HttpServletRequest request, String file, String fileName, String fileType,
			String fileDescribes, String jobNum, String taskId);

	/**
	 * 证件照上传 腾讯cos
	 * 
	 * @param file
	 *            上传的文件(Base64格式)
	 * @param fileName
	 *            上传的文件名
	 * @param fileType
	 *            文件类型
	 * @param fileDescribes
	 *            文件描述
	 * @param jobNum
	 *            工号
	 * @return
	 */
	public CommonModel fileUpLoadBase64ByCos(HttpServletRequest request, String file, String fileName, String fileType,
			String fileDescribes, String jobNum, String taskId);
	
	public CommonModel fileUpLoadWeChat(HttpServletRequest request, String mediaId, String fileName, String fileType,
			String fileDescribes, String jobNum, String taskId);

	/**
	 * 调用懒掌柜绑定一账通接口
	 * @param map
	 */
	public void bindingLzg(Map<String,String> map);


	/**
	 * 获得部门
	 * 
	 * @param province
	 * @param city
	 * @param country
	 * @return
	 */
	public CommonModel getDeptByArea(String province, String city, String country);

	/**
	 * 获得工号
	 * 
	 * @return
	 */
	// public String getJobNumber();

	/**
	 * 分配权限
	 * 
	 * @param deptId
	 * @param tempjobnum
	 * @return
	 */
	public int initTestAgentPermission(String deptId, String tempjobnum);

	public CommonModel coreAgentDock(String param);

	public CommonModel oauthRegist(String regInfoJSON);

	/**
	 * response 输出验证码图片流
	 * @param request
	 * @param response
	 */
	public void validateCodeImg(HttpServletRequest request, HttpServletResponse response);

	/**
	 * 统计出该代理人成功推荐的人数、被推荐人中的首单人数、这些人的姓名、所属平台、注册时间、首单时间
	 * @param jobNum
     * @return
     */
	CommonModel myRecommend(String jobNum, int limit, long offset);

	CommonModel validatePhone(String phone);
	CommonModel validateReferrer(JSONObject object);
	CommonModel agentRegion(JSONObject object);
	CommonModel bank(JSONObject object);
	CommonModel removeOpenid(JSONObject object, String token);
}
