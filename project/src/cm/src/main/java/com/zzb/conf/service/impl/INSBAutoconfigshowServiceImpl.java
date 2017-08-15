package com.zzb.conf.service.impl;

import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.entity.INSCUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCDept;
import com.zzb.conf.dao.INSBAutoconfigshowDao;
import com.zzb.conf.dao.INSBEdiconfigurationDao;
import com.zzb.conf.dao.INSBElfconfDao;
import com.zzb.conf.entity.INSBAutoconfigshow;
import com.zzb.conf.entity.INSBAutoconfigshowQueryModel;
import com.zzb.conf.entity.INSBEdiconfiguration;
import com.zzb.conf.entity.INSBElfconf;
import com.zzb.conf.service.INSBAutoconfigshowService;

@Service
@Transactional
public class INSBAutoconfigshowServiceImpl extends BaseServiceImpl<INSBAutoconfigshow> implements
		INSBAutoconfigshowService {
    public static final String CONTENT_ID = "contentid";
    public static final String QUOTE_TYPE = "quotetype";
    @Resource
	private INSBAutoconfigshowDao insbAutoConfigShowDao;
	@Resource
	private INSCDeptDao deptDao;
	@Resource
	private INSBElfconfDao elfconfDao;
	@Resource
	private INSBEdiconfigurationDao ediconfigurationDao;

	@Override
	protected BaseDao<INSBAutoconfigshow> getBaseDao() {
		return insbAutoConfigShowDao;
	}

	@Override
	public int deleteautoshow(String proid, String deptid) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("providerid", proid);
		map.put("deptid", deptid);
		return insbAutoConfigShowDao.deleteautoshow(map);
	}

	@Override
	public int deleterepetitionautoshow(String parentcodes, String proid,String deptid) {
		Map<String, String> para = new HashMap<String, String>();
		para.put("parentcodes", parentcodes+"+"+deptid);
		para.put("providerid", proid);
		para.put("deptid", deptid);
		return insbAutoConfigShowDao.deleterepetitionautoshow(para);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<INSBAutoconfigshow> autoOrArtificial(INSBAutoconfigshowQueryModel queryModel) {
		if (queryModel == null) {
			return null;
		}
		if (StringUtils.isEmpty(queryModel.getDeptId())) {
			return null;
		}
		if (StringUtils.isEmpty(queryModel.getProviderid())) {
			return null;
		}
		
		
		// 得到deptid集合
		List<String> allDeptCodesList = getParentDeptIds4Show(queryModel.getDeptId());
		if(allDeptCodesList==null||allDeptCodesList.isEmpty()){
			return null;
		}
		
		
		// 去show表中查询排序
		Map<String, Object> tempParam = new HashMap<String, Object>();
		tempParam.put("quoteList", queryModel.getQuoteList());
		tempParam.put("providerid", queryModel.getProviderid());
		tempParam.put("deptList", allDeptCodesList);
		if(!StringUtils.isEmpty(queryModel.getConftype())){
			tempParam.put("conftype", queryModel.getConftype());
		}
		//LogUtil.info("selectDataByDeptIds4New参数："+tempParam.toString());
		List<INSBAutoconfigshow> showModel = insbAutoConfigShowDao.selectDataByDeptIds4New(tempParam);

		if (showModel == null) {
			return null;
		}
		return showModel;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<INSBAutoconfigshow> getOneAbilityByDeptId(String deptId, String conftypeStr) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("deptId", deptId);
		param.put("conftype", conftypeStr);
		return insbAutoConfigShowDao.selectOneAbilityByDeptid(param);
	}
	
	//重构上面的，加上quotetype过滤
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<INSBAutoconfigshow> getOneAbilityByDeptIdQuotype(String deptId, String conftypeStr, String quotype) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("deptId", deptId);
		param.put("conftype", conftypeStr);
		param.put("quotype", quotype);
		return insbAutoConfigShowDao.selectOneAbilityByDeptid(param);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Map<String, Object> getElfEdiByNearestDept(INSBAutoconfigshowQueryModel queryModel) {
		Map<String, Object> result = new HashMap<String, Object>();

		List<INSBAutoconfigshow> showModelList = autoOrArtificial(queryModel);
		if (null != showModelList && showModelList.size() > 0) {
			INSBAutoconfigshow showModel = showModelList.get(0);
			INSCDept date = deptDao.selectById(showModel.getDeptid());
			if ("02".equals(queryModel.getQuoteList().get(0))) {
				INSBElfconf elfModel = elfconfDao.selectById(showModel.getContentid());
				if (!StringUtil.isEmpty(date)) {
					elfModel.setNoti(date.getDeptinnercode().substring(0, 5));
				}
				result.put("elf", elfModel);

			} else if ("01".equals(queryModel.getQuoteList().get(0))) {
				INSBEdiconfiguration ediModel = ediconfigurationDao.selectById(showModel.getContentid());
				if (!StringUtil.isEmpty(date)) {
					ediModel.setNoti(date.getDeptinnercode().substring(0, 5));
				}
				result.put("edi", ediModel);
			}
		}
		return result;
	}	
	
	@Override
	public List<String> getParentDeptIds4Show(String DeptId){
		INSCDept deptModel = deptDao.selectParentCodeByCode(DeptId);
		String paranetCodes = deptModel.getParentcodes();
		if (StringUtils.isEmpty(paranetCodes)) {
			return null;
		}

		String[] parentCodesArray = paranetCodes.split("\\u002B");
		List<String> allDeptCodesList = new ArrayList<String>(Arrays.asList(parentCodesArray));
		if(allDeptCodesList.contains("p")){
			allDeptCodesList.remove("p");
		}
		if(allDeptCodesList.contains("1000000000")){
			allDeptCodesList.remove("1000000000");
		}
		if(allDeptCodesList.contains("1200000000")){
			allDeptCodesList.remove("1200000000");
		}
		allDeptCodesList.add(DeptId);
		
		return allDeptCodesList;
	}

	public static void main(String[] args) {
		String aa = "123456+123455";
		String[] vv = aa.split("\\u002B");
		System.out.println(aa.substring(0, 5)+"==="+Arrays.asList(vv));
	}
	
	
	/**
	 * 根据供应商id查询自动化配置信息
	 * @return
	 */
	@Override
	public List<String> queryByProId(Map<String, Object> map){
		return insbAutoConfigShowDao.queryByProId(map);
	}

	@Override
	public int deleteElfAutoConfigShowByElfId(String elfId) {
        HashMap<String, String> map = new HashMap<>();
        map.put(CONTENT_ID, elfId);
        map.put(QUOTE_TYPE, INSBAutoconfigshow.ELF_QUOTE_TYPE);
        return insbAutoConfigShowDao.deleteautoshow(map);
	}

	/**
	 * 根据confshow表id删除能力配置表
	 * 
	 */
    @Override
    public int deleteAutoByShowId(String showid, String parentcodes, String deptid) {
        INSCDept dept = new INSCDept();
        dept.setParentcodes(parentcodes);
        List<INSCDept> deptlist = deptDao.selectDeptlikeparentcodes(parentcodes + "+" + deptid, deptid);
        int sum = 0;
        for (int i = 0; i < deptlist.size(); i++) {
            dept = deptlist.get(i);
            Map<String, String> map = new HashMap<String, String>();
            map.put("codevalue", showid);
            map.put("deptid", dept.getId());
            insbAutoConfigShowDao.deleteautobyshowid(map);
            sum++;
        }
        return sum;
    }

    @Override
    public Map<String,String> saveEdiConfig(String type, String showId, String providerId, String deptId, String codeValue, String confType, INSCUser user) {
        Map<String,String> result = new HashMap<>();

        // 查出当前已经存在的网点
        Map<String, String> param = new HashMap<>();
        param.put(CONTENT_ID, codeValue);
        param.put("providerid", providerId);
        List<Map<String,String>> oldList = insbAutoConfigShowDao.selectComcodeBbyContenIdAndProviderId(param);


        List<String> addDeptIdList = new ArrayList<String>();
        List<String> oldDeptIdList = new ArrayList<String>();
        List<String> updateDeptIdList = new ArrayList<String>();
        for(Map<String,String> map:oldList){
            oldDeptIdList.add(map.get("deptid"));
        }


        INSCDept deptModel = deptDao.selectByComcode(deptId);
        if (!deptModel.getComtype().equals("05")) {
            // 得到当前机构下所有网点级子机构
            List<String> allComcode = deptDao.selectComCodeByComtypeAndParentCodes4EDI(deptId);

            addDeptIdList.addAll(allComcode);
            updateDeptIdList.addAll(allComcode);


            //不存在的新增
            addDeptIdList.removeAll(oldDeptIdList);
            if (addDeptIdList != null && !addDeptIdList.isEmpty()) {
                List<INSBAutoconfigshow> addList = new ArrayList<INSBAutoconfigshow>();
                for (String deptCode : addDeptIdList) {
                    INSBAutoconfigshow autoModel = new INSBAutoconfigshow();
//					autoModel.setCodevalue(codeValue);
                    autoModel.setDeptid(deptCode);
                    autoModel.setCreatetime(new Date());
                    autoModel.setProviderid(providerId);
                    autoModel.setConftype(confType);
                    LogUtil.info(codeValue +"--1---codeValue");
                    autoModel.setContentid(codeValue);
                    // 默认是传统的 TODO
                    autoModel.setQuotetype(type);
                    autoModel.setOperator(user.getUsercode());
                    autoModel.setCodevalue(showId);
                    addList.add(autoModel);
                }
                insbAutoConfigShowDao.insertInBatch(addList);
            }

            //重复的修改
            updateDeptIdList.retainAll(oldDeptIdList);
            if (updateDeptIdList != null && !updateDeptIdList.isEmpty()) {
                for(String updateDeptId:updateDeptIdList){
                    for(Map<String,String> map:oldList){
                        if(updateDeptId.equals(map.get("deptid"))){
                            INSBAutoconfigshow autoModel = new INSBAutoconfigshow();
                            autoModel.setId(map.get("id"));
//							autoModel.setCodevalue(codeValue);
                            autoModel.setDeptid(updateDeptId);
                            autoModel.setCreatetime(new Date());
                            autoModel.setProviderid(providerId);
                            autoModel.setConftype(confType);
                            LogUtil.info(codeValue +"--2---codeValue");
                            autoModel.setContentid(codeValue);
                            // 默认是传统的 TODO
                            autoModel.setQuotetype(type);
                            autoModel.setOperator(user.getUsercode());
                            autoModel.setCodevalue(showId);
                            insbAutoConfigShowDao.updateById(autoModel);
//							addList.add(autoModel);
                            break;
                        }
                    }
                }
//				insbAutoConfigShowDao.updateInBatch(addList);
            }
            result.put("msg", "修改成功");
            return result;
        }else{
            //新增
            if(oldDeptIdList==null || oldDeptIdList.isEmpty()){
                INSBAutoconfigshow autoModel = new INSBAutoconfigshow();
//				autoModel.setCodevalue(codeValue);
                autoModel.setDeptid(deptId);
                autoModel.setCreatetime(new Date());
                autoModel.setProviderid(providerId);
                autoModel.setConftype(confType);
                // 默认是传统的 TODO
                autoModel.setQuotetype(type);
                autoModel.setOperator(user.getUsercode());
                LogUtil.info(codeValue +"--3---codeValue");
                autoModel.setCodevalue(showId);
                autoModel.setContentid(codeValue);
                insbAutoConfigShowDao.insert(autoModel);
                result.put("msg", "新增成功");
                return result;
            }else if(oldDeptIdList.contains(deptId)){
                for(Map<String,String> map:oldList){
                    if(deptId.equals(map.get("deptid"))){
                        INSBAutoconfigshow autoModel = new INSBAutoconfigshow();
                        autoModel.setId(map.get("id"));
//						autoModel.setCodevalue(codeValue);
                        autoModel.setDeptid(deptId);
                        autoModel.setCreatetime(new Date());
                        autoModel.setProviderid(providerId);
                        autoModel.setConftype(confType);
                        LogUtil.info(codeValue +"--4---codeValue");
                        autoModel.setContentid(codeValue);
                        // 默认是传统的 TODO
                        autoModel.setQuotetype(type);
                        autoModel.setOperator(user.getUsercode());
                        autoModel.setCodevalue(showId);
                        insbAutoConfigShowDao.updateById(autoModel);
                    }
                }
                result.put("msg", "修改成功！");
                return result;
            }else{
                INSBAutoconfigshow autoModel = new INSBAutoconfigshow();
//				autoModel.setCodevalue(codeValue);
                autoModel.setDeptid(deptId);
                autoModel.setCreatetime(new Date());
                autoModel.setProviderid(providerId);
                autoModel.setConftype(confType);
                LogUtil.info(codeValue +"--5---codeValue");
                autoModel.setContentid(codeValue);
                // 默认是传统的 TODO
                autoModel.setQuotetype(type);
                autoModel.setOperator(user.getUsercode());
                autoModel.setCodevalue(showId);
                insbAutoConfigShowDao.insert(autoModel);
                result.put("msg", "新增成功");
                return result;
            }
        }
    }

    @Override
    public void deleteAutoByShowId(String id, String quotetype) {
        insbAutoConfigShowDao.deleteautobyshowid(id,quotetype);
    }
}