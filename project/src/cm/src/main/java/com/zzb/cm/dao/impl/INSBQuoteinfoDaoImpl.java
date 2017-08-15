package com.zzb.cm.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.dao.INSBQuoteinfoDao;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.conf.entity.INSBAgreement;

@Repository
public class INSBQuoteinfoDaoImpl extends BaseDaoImpl<INSBQuoteinfo> implements
        INSBQuoteinfoDao {
    /**
     * 获取规则信息
     * (non-Javadoc)
     *
     * @see com.zzb.conf.dao.INSBPolicyitemDao#queryAgreementIdByTaksId(java.lang.String)
     */
    @Override
    public INSBAgreement queryAgreementIdByTaksId(Map<String, String> para) {
        return this.sqlSessionTemplate.selectOne(this.getSqlName("queryAgreementIdByTaksId"), para);
    }

    /**
     * 获取出单网点
     * (non-Javadoc)
     *
     * @see com.zzb.cm.dao.INSBQuoteinfoDao#querydeptcode(java.util.Map)
     */
    @Override
    public String querydeptcode(Map<String, String> map) {
        return this.sqlSessionTemplate.selectOne(this.getSqlName("querydeptcode"), map);
    }

    /* (non-Javadoc)
     * @see com.zzb.cm.dao.INSBQuoteinfoDao#queryQuoteinfoByWorkflowinstanceid(java.lang.String)
     */
    @Override
    public INSBQuoteinfo queryQuoteinfoByWorkflowinstanceid(
            String workflowinstanceid) {
        return this.sqlSessionTemplate.selectOne(this.getSqlName("queryQuoteinfoByWorkflowinstanceid"), workflowinstanceid);
    }

    @Override
    public String selectDeptcodeByProviderIdTatolId(Map<String, String> param) {
        return this.sqlSessionTemplate.selectOne(this.getSqlName("selectDeptcodeByProviderIdTatolId"), param);
    }

    @Override
    public INSBQuoteinfo selectQuoteinfoByQuotetotalinfoidAndinscomcode(Map<String, String> param) {
        return this.sqlSessionTemplate.selectOne(this.getSqlName("selectQuoteinfoByQuotetotalinfoidAndinscomcode"), param);
    }


    public int closeTask(Map<String, String> params) {
        return this.sqlSessionTemplate.selectOne(this.getSqlName("closeTask"), params);
    }

    @Override
    public INSBQuoteinfo getByTaskidAndCompanyid(Map<String, String> param) {
        return this.sqlSessionTemplate.selectOne(this.getSqlName("selectByTaskidAndCompanyid"), param);
    }

    @Override
    public List<Map<String, String>> selectSubInstanceIdProviderIdByTotalId(
            String quotetotalinfoid) {
        return this.sqlSessionTemplate.selectList(this.getSqlName("selectSubInstanceIdProviderIdByMainInstanceId"), quotetotalinfoid);
    }

    @Override
    public List<String> selectDeptIdByQuotetotalIdAndComCode4Task(
            Map<String, String> param) {
        return this.sqlSessionTemplate.selectList(this.getSqlName("selectDeptIdByQuotetotalIdAndComCode4Task"), param);
    }

    /**
     *  通过子流程id查询该子流程规则试算后的总保费
     *
     * @param subInstanceId
     * @return
     */
    @Override
    public Double getTotalDiscountAmountPrice(String subInstanceId) {
        return this.sqlSessionTemplate.selectOne(this.getSqlName("getTotalDiscountAmountPrice"), subInstanceId);
    }

    @Override
    public Map<String, String> selectMainInstanceIdProviderBySubInstanceId(
            String subInstancId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Map<String, String>> selectWorkflowinstanceid(String taskid) {
        return this.sqlSessionTemplate.selectList(this.getSqlName("selectWorkflowinstanceid"), taskid);
    }

    @Override
    public List<Map<String, String>> selectByTaskid(String taskid) {
        return this.sqlSessionTemplate.selectList(this.getSqlName("selectByTaskid"), taskid);
    }

    @Override
    public List<Map<String, Object>> selectComcodeByTaskid(Map<String, Object> map) {
        return this.sqlSessionTemplate.selectList(this.getSqlName("selectComcodeByTaskid"), map);
    }

    @Override
    public int deleteByObj(INSBQuoteinfo insbQuoteinfo) {
        return this.sqlSessionTemplate.delete(this.getSqlName("deleteByObj"), insbQuoteinfo);
    }

    @Override
    public List<Map<String, String>> selectTaskInfo(Map<String, Object> map) {
        return this.sqlSessionTemplate.selectList(this.getSqlName("selectTaskInfo"), map);
    }
	
	@Override
    public List<Map<String, String>> queryDeptAllQuoteCount(Map<String,String> param){
    	return this.sqlSessionTemplate.selectList(this.getSqlName("queryDeptAllQuoteCount"), param);
    }
}