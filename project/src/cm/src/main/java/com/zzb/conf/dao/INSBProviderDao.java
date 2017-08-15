package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.model.ProOrParModel;

public interface INSBProviderDao extends BaseDao<INSBProvider> {
	public List<Map<Object, Object>> selectProviderLitByMap(Map<String, Object> map);
	
	public List<INSBProvider> selectByParentProTreeCode(String parentcode);
	public List<INSBProvider> selectByParentProTreeCodeStair();
	public List<INSBProvider> selectByParentProTreeCode2(ProOrParModel parentcode);
	public List<INSBProvider> selectByInsureAreaCode(String insureAreaCode);
	
	/**
	 * 根据父节点拿到所有
	 * 
	 * @param parentcode
	 * @return
	 */
	public List<Map<String,String>> selectByParentProTreeCode3(String parentcode);
	
	public int addProData(INSBProvider pro);
	
	public INSBProvider queryProinfoById(String id);
	
	public INSBProvider queryByPrvcode(String prvcode);
	
	public int updateProById(String id);
	
	public int updateProByIddel(String id);
	
	public List<INSBProvider> queryListPro(String ediid);
	
	
	/**
	 * 按修改时间查询
	 * 
	 * @param modifytime
	 * @return
	 */
	public List<INSBProvider> selectByModifytime(String modifytime);
	
	public int getQuotationValidityById(String id);
	
	/**
	 * 查询顶级 供应商
	 */
	public INSBProvider selectFatherProvider(String sonPrvId);

	/**查询顶级供应商
	 * @return
	 */
	public List<INSBProvider> selectProvider();

	/**
	 * EDI能力配置，供应商树
	 * @param userdept
	 * @return
	 */
	public List<Map<String, String>> selectEdiProvider(String userdept);
	/**
	 * EDI能力配置，供应商树
	 * @param userdept
	 * @return
	 */
	public List<Map<String, String>> selectEdiAllProvider(String userdept);

	public List<INSBProvider> selectByInsureAreaCode2(Map<String, Object> map);
	@Deprecated
	public List<INSBProvider> selectAll();
	public List<INSBProvider> selectListIn(Map<String, Object> params);
	
	
}