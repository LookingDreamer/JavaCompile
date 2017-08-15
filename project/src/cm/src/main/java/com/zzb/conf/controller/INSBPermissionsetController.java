package com.zzb.conf.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.common.ConfigUtil;
import com.zzb.conf.dao.INSBPermissionsetDao;
import com.zzb.conf.entity.*;
import com.zzb.conf.service.INSBAgreementService;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.QueryBean;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCDeptService;
import com.common.PagingParams;
import com.zzb.conf.controller.vo.BaseVo;
import com.zzb.conf.service.INSBPermissionService;
import com.zzb.conf.service.INSBPermissionallotService;
import com.zzb.conf.service.INSBPermissionsetService;
 
/**
 * 功能包管理
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/pset/*")
public class INSBPermissionsetController extends BaseController {

	@Resource
	private INSBPermissionsetService service;

	@Resource
	private INSCDeptService deptService;

	@Resource
	private INSBPermissionService insbPermissionService;

	@Resource
	private INSBPermissionallotService insbPermissionallotService;

	@Resource
	private INSBAgreementService insbAgreementService;
	@Resource
	private INSBPermissionsetDao insbPermissionsetDao;

	public static final String TRIAL_PERMISSION_SETID = "trial.permission.setid";

	/**
	 * 菜单转到列表页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "menu2list", method = RequestMethod.GET)
	public ModelAndView menu2list() {
		ModelAndView result = new ModelAndView("zzbconf/permissionsetlist");
		return result;
	}

	/**
	 * 初始化权限包列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "initpermissionsetlistpage", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> initpermissionSetlistpage(HttpSession session,
			@ModelAttribute PagingParams para,
			@ModelAttribute INSBPermissionset permissionSet,
			@ModelAttribute INSCDept dept, @ModelAttribute QueryBean querybean) {
		if(permissionSet.getDeptid() == null || "".equals(permissionSet.getDeptid())){
			INSCUser user = (INSCUser) session.getAttribute("insc_user");
			permissionSet.setDeptid(user.getUserorganization());
		}
		Map<String, Object> map = BeanUtils.toMap(permissionSet, 
				querybean, para);
		return service.getPermissionsetListByPage(map);

	}

	/**
	 * 查询条件 机构树（异步）
	 * 
	 * @return
	 */
	@RequestMapping(value = "initdepttree", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> initDeptTree(
			@RequestParam(value = "id", required = false) String parentcode) {
		return deptService.queryTreeList(parentcode);
	}
	
	/**
	 * 根据登陆代理人所在机构查询条件 机构树（异步）
	 * 
	 * @return
	 */
	@RequestMapping(value = "initDeptTreeByAgent", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> initDeptTreeByAgent(HttpSession session,
			@RequestParam(value = "id", required = false) String parentcode) {
		INSCUser user = (INSCUser) session.getAttribute("insc_user");
		if(parentcode == null || "".equals(parentcode)){
			parentcode = user.getUserorganization();
			INSCDept dept = deptService.getOrgDept(parentcode);
			if(dept != null){
				parentcode = dept.getUpcomcode();
			}
		}
		return deptService.queryTreeList4Data2(parentcode,user.getUserorganization());
	}

	/**
	 * 转到功能包编辑页面
	 * 
	 * @param flag
	 *            当前操作类型
	 * @param permissionsetId
	 *            返回id
	 * @return
	 */
	@RequestMapping(value = "mian2edit", method = RequestMethod.GET)
	public ModelAndView mian2edit(String flag, String permissionsetId) {
		ModelAndView result = new ModelAndView("zzbconf/permissionsetedit");
		if ("".equals(permissionsetId) || permissionsetId == null) {
			result.addObject("result", new HashMap<String, Object>());
			result.addObject("flag", flag);
		} else {
			Map<String, Object> set = service.main2edit(permissionsetId);
			result.addObject("result", set);
			result.addObject("flag", flag);
		}
		return result;
	}

	/**
	 * 初始化功能包 权限 关系表
	 * 
	 * @param session
	 * @param permissionsetId
	 *            当前功能包id 新增时为空
	 * @param para
	 *            分页信息
	 * @param istry
	 *            是否是试用权限
	 * @param permission
	 *            （列表查询 留得参数）
	 * @param dept
	 *            组织机构
	 * @param querybean
	 *            （没有源码 暂且不用）
	 * @return
	 */
	@RequestMapping(value = "initpermissionlistpage", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> initpermissionListPage(HttpSession session,
			String permissionsetId, @ModelAttribute PagingParams para,
			@ModelAttribute INSBPermission permission,
			@ModelAttribute INSCDept dept, @ModelAttribute QueryBean querybean) {
		Map<String,Object> map=BeanUtils.toMap(permission,dept,querybean,para);
		return service.getpermissionListByPage(map, permissionsetId);
	}

	@RequestMapping(value="getagreementbycomcode",method=RequestMethod.GET)
	@ResponseBody
	public String getAgreementByComcode(@ModelAttribute PagingParams para,String comcode, String agentId, String setid){
		Map<Object, Object> initMap = new HashMap<Object, Object>();
		Map<String, Object> map = BeanUtils.toMap(para);
		if (StringUtil.isEmpty(comcode) && StringUtil.isNotEmpty(agentId)) {
			INSBPermissionset permissionset = insbPermissionallotService.getPermissionsetByAgentId(agentId);
			if (permissionset != null) {
				comcode = permissionset.getDeptid();
				setid = permissionset.getId();
			}
		}
		map.put("setid", setid);
		if (StringUtil.isEmpty(comcode) && StringUtil.isNotEmpty(setid)) {
			INSBPermissionset permissionset = insbPermissionsetDao.selectById(setid);
			agentId = "admin";
			if (permissionset != null) {
				comcode = permissionset.getDeptid();
			}
		}
		map.put("comcode", comcode);
		map.put("agentId", agentId);
		initMap.put("records", "10000");
		initMap.put("page", 1);
		List<INSBAgreement> list = insbAgreementService.queryAgreementByComecode(map);
		initMap.put("rows", list);
		initMap.put("total", list.size());
		JSONObject jsonObject = JSONObject.fromObject(initMap);
		return jsonObject.toString();
	}

	@RequestMapping(value = "initpermissionsetUser", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> initpermissionsetUser(String permissionsetId, @ModelAttribute PagingParams para,
													 @ModelAttribute INSBAgent agent) {

		Map<String, Object> map = BeanUtils.toMap(agent,para);
		return service.getUserListByPage(map, permissionsetId);
	}

	/**
	 * 初始化当前功能包供应商
	 * 
	 * @return
	 */
	@RequestMapping(value = "initprovidertree", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> initProviderTree(
			@RequestParam(value = "id", required = false) String parentcode,
			@RequestParam(value = "checked", required = false) String checked,
			String permissionSetId) {
		return service
				.getProviderTreeList(parentcode, permissionSetId, checked);
	}

	/**
	 * 编辑权限功能包关系表信息
	 * 
	 * @param id
	 *            权限id 关系表id
	 * @return
	 */
	@RequestMapping(value = "permissionset2allot", method = RequestMethod.GET)
	public ModelAndView permissionset2allot(String id, String pId, String setId,String pName) {
		ModelAndView result = new ModelAndView("zzbconf/permissionallotedit");
		// 获得id为空 新增关系表信息
		if ("".equals(id) || id == null) {

			INSBPermission peremissionId = insbPermissionService.queryById(pId);

			INSBPermissionallot allot = new INSBPermissionallot();
			allot.setSetid(setId);
			allot.setPermissionid(pId);
			allot.setPermissionname(peremissionId.getPermissionname());
			allot.setPermissionname(pName);
			result.addObject("allotData", allot);
			return result;

			// 修改关系表信息
		} else {
			// 查询关系表信息
			INSBPermissionallot allot = insbPermissionallotService
					.queryById(id);
			allot.setPermissionname(pName);
			result.addObject("allotData", allot);
			return result;
		}

	}

	/**
	 * 编辑权限分配表信息
	 * 
	 * TODO
	 * 
	 * 权限分配信息保存
	 * 
	 * 通过permissionset 查出对应的 代理人
	 * 
	 * 把当前权限信息保存到 代理人 权限关系表中
	 * 
	 * @return
	 */
	@RequestMapping(value = "updatepermissionallot", method = RequestMethod.POST)
	@ResponseBody
	public String updatePermissionAllot(HttpSession session, @ModelAttribute INSBPermissionallot allot) {
		INSCUser user = (INSCUser) session.getAttribute("insc_user");
		String result = null;
		try {
			result = insbPermissionallotService.saveOrUpdate(allot,user);
			LogUtil.info(this.getClass().getName()+".updatePermissionAllot:"+ user.getName()+"在时间"+new Date().toLocaleString()+ "修改了权限包:"+allot);
			//releaseBanding(allot);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	/**
	 * 
	 * 修改功能包权限
	 * 
	 * 解绑功能包，功能包状态至为停用
	 * 
	 * @param allot
	 * @return
	 */
	@RequestMapping(value = "deletepermissionallot", method = RequestMethod.POST)
	@ResponseBody
	public BaseVo deletePermissionAllot(HttpSession session, @ModelAttribute INSBPermissionallot allot) {
		BaseVo bv = new BaseVo();
		INSCUser user = (INSCUser) session.getAttribute("insc_user");
		LogUtil.info(this.getClass().getName()+".deletePermissionAllot:"+ user.getName()+"在时间"+new Date().toLocaleString()+ "取消绑定权限包:"+allot);
		try {
			//insbPermissionallotService.deleteById(allot.getId());
			
			//releaseBanding(allot);
			bv.setStatus("1");
			bv.setMessage("操作成功！");
		} catch (Exception e) {
			bv.setStatus("2");
			bv.setMessage("操作失败！请稍候重试");
			e.printStackTrace();
		}
		return bv;
	}

	/**
	 * 保存功能包 基本信息
	 * 
	 * 修改、新增功能包  功能包状态变为未启用，需要重新开启
	 * 
	 * 
	 * 
	 * @param set
	 * @return
	 */
	@RequestMapping(value = "savebasepermissionset", method = RequestMethod.POST)
	@ResponseBody
	public String savebasePermissionSet(HttpSession session ,@ModelAttribute INSBPermissionset set) {
		
		String operator = ((INSCUser)session.getAttribute("insc_user")).getUsercode();
		
		set.setOperator(operator);
		set.setCreatetime(new Date());

		//泛华财险营业集团 试用权限包 部门状态 类型等不能修改
		if (INSBPermissionsetController.TRIAL_PERMISSION_SETID.equals(set.getId())) {
			INSBPermissionset entity = service.queryById(set.getId());
			set.setDeptid(entity.getDeptid());
			set.setAgentkind(entity.getAgentkind());
			set.setStatus(entity.getStatus());
		}

		String id = service.saveOrUpdateSetReturnId(set);
		
		//service.releaseBandingByLogicId(set);
		
		return id;
	}
	/**
	 * 验证权限包代码的唯一性
	 * @param setcode
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "checksetcode", method = RequestMethod.POST)
	@ResponseBody
	public boolean checkSetCode(String setcode,String id){
		int setcodeCount =  service.selectCountBySetCode(setcode, id);
		if(setcodeCount == 0){
			return  true ;
		}else{
			return false;
		}		
	}

	/**
	 * 根据id删除功能包
	 * 
	 * 级联删除关系表
	 * 
	 * 删除代理人关系表信息
	 * 
	 * 
	 * @param setId
	 * @return
	 */
	@RequestMapping(value = "deletepermissionsetbyid", method = RequestMethod.GET)
	@ResponseBody
	public BaseVo deletepermissionSetById(HttpSession session, String setId) {
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		BaseVo bv = new BaseVo();
		try {
			//泛华财险营业集团 试用权限包 不能删除
			if (INSBPermissionsetController.TRIAL_PERMISSION_SETID.equals(setId)) {
				bv.setStatus("3");
				bv.setMessage("泛华财险营业集团顶级试用权限包，不能删除！");
				return bv;
			}
			LogUtil.info("功能包删除id为%s,操作人:%s", setId, operator.getUsercode());
			int tempRessult = service.deleteJudge(setId);
			if (tempRessult == 1) {
				bv.setStatus("1");
				bv.setMessage("操作成功！");
			} else if (tempRessult == 2) {
				bv.setStatus("3");
				bv.setMessage("不能删除！");
			}

		} catch (Exception e) {
			bv.setStatus("2");
			bv.setMessage("操作失败！请稍候重试");
			e.printStackTrace();
		}
		return bv;
	}

	/**
	 * 1：新增功能包 1.1：按照逻辑主键查找所有符合条件的代理人 ，为代理人添加功能包id 1.2：代理人对应的权限关系表 权限等于
	 * 功能包中设置的权限 1.3：代理人修改权限 没有的新增 有的不动 多的的删除
	 * 
	 * 
	 * 2：新增代理人 选择功能包（权限 供应商都不能修改） 2.1：自定义权限（全部是新增没有关系）
	 * 
	 * 3：修改权限包 权限修改（只是维护代理人中有权限包这类人） 3.1：需要同步关系表
	 * 
	 * 
	 * 需要判断当前的功能包是新增（需要查询逻辑主键 并为代理人插入功能包id） 还是修改(不再为代理人新增功能包id) 需要字段去区分
	 * 
	 * @return
	 */
	@RequestMapping(value = "savesetproviderallotdata", method = RequestMethod.POST)
	@ResponseBody
	public BaseVo saveSetProviderAllotData(String setId, String providerIds,
			String opFlag) {
		BaseVo bv = new BaseVo();
		try {
			service.saveSetProviderAllotData(setId, providerIds, opFlag);
			
			//修改功能包状态 解绑功能包
			//INSBPermissionset set =  service.queryById(setId);

			//service.releaseBandingByLogicId(set);
			
			bv.setStatus("1");
			bv.setMessage("操作成功！");
		} catch (Exception e) {
			bv.setStatus("2");
			bv.setMessage("操作失败！请稍候重试");
			e.printStackTrace();
		}
		return bv;
	}

	/**
	 * 同步代理人关系表
	 * 
	 * 更新功能包状态
	 * 
	 * @param setId
	 *            功能包id
	 * @param isNew
	 *            是否是新功能包
	 * @return
	 */
	@RequestMapping(value = "applicpermissionset", method = RequestMethod.POST)
	@ResponseBody
	public BaseVo applicPermissionSet(String setId, String isNew) {
		BaseVo bv = new BaseVo();
		try {
			service.updateAgentTable(setId, isNew);
			bv.setStatus("1");
			bv.setMessage("操作成功！");
		} catch (Exception e) {
			bv.setStatus("2");
			bv.setMessage("操作失败！请稍候重试");
			e.printStackTrace();
		}
		return bv;
	}

	/**
	 * 解绑功能包
	 * 
	 * 1：删除代理人功能包id
	 * 2:删除代理人权限关系表数据
	 * 
	 * @param setId
	 * @return
	 */
	@RequestMapping(value = "stoppermissionset", method = RequestMethod.POST)
	@ResponseBody
	public BaseVo stopPermissionSet(HttpSession session, String setId) {
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		BaseVo bv = new BaseVo();
		try {
			//泛华财险营业集团 试用权限包 不能停用
			if (INSBPermissionsetController.TRIAL_PERMISSION_SETID.equals(setId)) {
				bv.setStatus("3");
				bv.setMessage("泛华财险营业集团顶级试用权限包，不能停用！");
				return bv;
			}
			INSBPermissionset set =  service.queryById(setId);
			set.setOperator(operator.getUsercode());
			service.releaseBandingByLogicId(set);
			bv.setStatus("1");
			bv.setMessage("操作成功！");
		} catch (Exception e) {
			bv.setStatus("2");
			bv.setMessage("操作失败！请稍候重试");
			e.printStackTrace();
		}
		return bv;
	}
	
	/**
	 * 页面操作权限 解绑功能包权限
	 * 
	 * @param allot
	 */
	private void releaseBanding(INSBPermissionallot allot){

		if (INSBPermissionsetController.TRIAL_PERMISSION_SETID.equals(allot.getSetid())) {
			return;
		}
		//解绑功能包
		INSBPermissionset set =  service.queryById(allot.getSetid());
		service.releaseBandingByLogicId(set);
	}
	
	/**
	 * 批量关联权限包弹出页面
	 * 
	 * 杨威  
	 */
	@RequestMapping(value = "showbulkpackage", method = RequestMethod.GET)
	@ResponseBody
	public List<INSBPermissionset> showbulkpackage(HttpSession session){
		//ModelAndView view=new ModelAndView("zzbconf/addRightPackage");
		List<INSBPermissionset> result=new ArrayList<INSBPermissionset>();
		INSCUser user = (INSCUser)session.getAttribute("insc_user");
		String userorganization = user.getUserorganization();
		LogUtil.info("业管："+user.getName()+"所在的机构id为："+userorganization);
		result=service.selectPermissionset(userorganization);
		LogUtil.info("业管："+user.getName()+"所在的机构启动的权限包有"+result.size()+"个");
		//view.addObject("result",result);
		return result;
	}
}