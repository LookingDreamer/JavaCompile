package com.zzb.conf.component;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import com.common.HttpClientUtil;
import com.zzb.model.CifRiskModel;

/**
 *cm系统向云平台推送险种变更信息
 */
@Repository
@PropertySource("classpath:config/config.properties")
public class INSBRiskUpdate4Cif {

	@Autowired
	private Environment env;
	
	/**
	 * 修改数据
	 * 
	 * @param model
	 * @return
	 */
	public String updateRiskData(CifRiskModel model) {
		String result=null;
		model.setType("01");
		Map<String, String> params = new HashMap<String, String>();
		
		
		String json=JSONObject.fromObject(model).toString();
		params.put("cifRiskStr", json);
		try {
			String url = env.getProperty("cloudupdate.url");
			result = HttpClientUtil.doPost(url, params);
		} catch (Exception e) {
			
			result="cif更新失败:"+model.getContend();
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 新增数据
	 * 
	 * @param model
	 * @return
	 */
	public String addRiskData(CifRiskModel model){
		String result=null;
		
		model.setType("02");
		Map<String, String> params = new HashMap<String, String>();
		
		String json=JSONObject.fromObject(model).toString();
		params.put("cifRiskStr", json);
		try {
			String url = env.getProperty("cloudupdate.url");
			System.out.println("===========url======="+url);
			System.out.println("===========params======="+params);
			result = HttpClientUtil.doPost(url, params);
		} catch (Exception e) {
			
			result="cif更新失败:"+model.getContend();
			e.printStackTrace();
		}
		 return result;
	}

	/**
	 * 删除数据
	 * 
	 * @param model
	 * @return
	 */
	public String deleteRiskData(CifRiskModel model) {
		String result=null;
		
		model.setType("03");
		Map<String, String> params = new HashMap<String, String>();
		
		String json=JSONObject.fromObject(model).toString();
		params.put("cifRiskStr", json);

		try {
			String url = env.getProperty("cloudupdate.url");
			result =  HttpClientUtil.doPost(url, params);
		} catch (Exception e) {
			
			result="cif更新失败:"+model.getContend();
			e.printStackTrace();
		}
		 
		 return result;
	}
}
