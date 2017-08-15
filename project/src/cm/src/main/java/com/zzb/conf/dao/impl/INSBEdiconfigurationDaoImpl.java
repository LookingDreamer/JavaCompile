package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBEdiconfigurationDao;
import com.zzb.conf.entity.INSBEdiconfiguration;
import com.zzb.conf.entity.INSBEdidescription;
import com.zzb.model.EDIConfModel;

@Repository
public class INSBEdiconfigurationDaoImpl extends BaseDaoImpl<INSBEdiconfiguration> implements
		INSBEdiconfigurationDao {
	
	@Override
	public List<Map<Object, Object>> selectEDIListPaging(Map<String, Object> map) {
		
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectrownum"),map);
	}

	@Override
	public int addedi(EDIConfModel edimodel) {
		int ediconf = this.sqlSessionTemplate.insert(this.getSqlName("insertconf"),edimodel);
//		Date date = new Date();
//		List<INSBEdidescription> listdesc = new ArrayList<INSBEdidescription>();
//		INSBEdidescription desc = new INSBEdidescription();
//		for(int j = 0;j < edimodel.getInsbedidescription().size();j++){
//			desc.setId(UUIDUtils.random());
//			desc.setModifytime(date);
//			desc.setCreatetime(date);
//			desc.setEdiid(edimodel.getEdiconfid());
//			desc.setOperator(edimodel.getOperator());
//			desc.setName(edimodel.getInsbedidescription().get(j).getName());
//			desc.setIppath(edimodel.getInsbedidescription().get(j).getIppath());
//			listdesc.add(desc);
//			Map<String, Object> map = BeanUtils.toMap(desc);
//			this.sqlSessionTemplate.insert(this.getSqlName("insertdesc"),map);
//		}
		return ediconf;
	}
	
	@Override
	public int updateedi(EDIConfModel edimodel) {
		int ediconf = this.sqlSessionTemplate.update(this.getSqlName("updateconf"),edimodel);
//		Date date = new Date();
//		String ediid = edimodel.getEdiconfid();
//		for(int j = 0;j < edimodel.getInsbedidescription().size();j++){
//			Map<String, String> para = new HashMap<String, String>();
//			String name = edimodel.getInsbedidescription().get(j).getName();
//			para.put("ediid", ediid);
//			para.put("name", name);
//			INSBEdidescription desc = this.sqlSessionTemplate.selectOne(this.getSqlName("querydescriptionbyediid"), para);
//			desc.setModifytime(date);
//			desc.setOperator(edimodel.getOperator());
//			desc.setIppath(edimodel.getInsbedidescription().get(j).getIppath());
//			this.sqlSessionTemplate.update(this.getSqlName("updatedesc"),desc);
//		}
		return ediconf;
	}
	
	@Override
	public List<INSBEdidescription> queryediinfoByid(String id) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("queryediinfo"),id);
	}

	@Override
	public int deleteDetail(String ediid) {
		return this.sqlSessionTemplate.insert(this.getSqlName("deletedetail"),ediid);
	}

}