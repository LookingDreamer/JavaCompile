package com.zzb.extra.dao.impl;

import com.zzb.extra.dao.INSBMarketPrizeDao;
import com.zzb.extra.entity.INSBMarketPrize;
import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;

import java.util.List;
import java.util.Map;

@Repository
public class INSBMarketPrizeDaoImpl extends BaseDaoImpl<INSBMarketPrize> implements
        INSBMarketPrizeDao {

        @Override
        public List<INSBMarketPrize> getPrizeList(Map<String, Object> map) {
                return this.sqlSessionTemplate.selectList("INSBMarketPrize_getPrizeList",map);
        }

        @Override
        public void saveObject(INSBMarketPrize iNSBMarketPrize) {
                 this.sqlSessionTemplate.insert("INSBMarketPrize_insert",iNSBMarketPrize);
        }

        @Override
        public void updateObject(INSBMarketPrize iNSBMarketPrize) {
                this.sqlSessionTemplate.update("INSBMarketPrize_updateById", iNSBMarketPrize);
        }

        @Override
        public void deleteObject(String id) {
                this.sqlSessionTemplate.delete("INSBMarketPrize_deleteById", id);
        }

        @Override
        public List<Map<Object, Object>> queryPrizeList(Map<String, Object> map){
               return this.sqlSessionTemplate.selectList("INSBMarketPrize_select", map);
        }

        @Override
        public Map findById(String id) {
                return this.sqlSessionTemplate.selectOne("INSBMarketPrize_selectById", id);
        }

        @Override
        public long queryPrizeListCount(Map<String, Object> map) {
                return this.sqlSessionTemplate.selectOne("INSBMarketPrize_selectCount", map);
        }
        //<!--add refreshrefresh-->
}