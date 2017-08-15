package com.zzb.app.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.common.HttpClientUtil;
import com.zzb.cm.entity.INSBFlowerror;
import com.zzb.cm.service.INSBFlowerrorService;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.service.AppGeneralSettingService;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class AppVehicleServiceTest{
	@Resource
	private AppGeneralSettingService generalSettingService;
	@Resource
	private AppVehicleService service;
	@Resource
	INSBFlowerrorService fs;
	//掌中保推核心批量推送
	@Test
	public void testQueryVehicleByType1() throws InterruptedException{
		int i=0;
		INSBFlowerror flowerror = new INSBFlowerror();
		flowerror.setFlowcode("-3");
		List<INSBFlowerror> queryAll = fs.queryList(flowerror);
		for (INSBFlowerror insbFlowerror : queryAll) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("taskid", insbFlowerror.getTaskid());
			map.put("companyid", insbFlowerror.getInscomcode());
			HttpClientUtil.doGet("http://cm.52zzb.com/cm/policyInterface/pushtocore",map);
			i++;
			fs.deleteById(insbFlowerror.getId());
			System.out.println(i);
			Thread.sleep(3000);
		}
		System.out.println(11111);
	}

//	@Test
	public void testQueryVehicleByType(){
		String header = "";
		
		String param = "key=高;count=10;off=1";
		
		String result = null;
		try {
			result = service.queryVehicleByType(header, param);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(result);
	}
	
	
	/**
	 * 按照品牌查找车辆信息 及车型规格
	 */
//	@Test
	public void testQueryVehicleByBrandName(){
		service.queryVehicleByBrandName("","key=宝马Z3");
	}
	@Test
	public void testQueryVehicleByBrandNamed(){
		CommonModel ad=generalSettingService.getCommonOutDept("620500571");
		List<Map<String,String>> deptList=(List<Map<String, String>>) ad.getBody();
		System.out.println(deptList.size());
		for (Map<String, String> map : deptList) {
			System.out.println(map.get("name"));
		}
		System.out.println(ad.getBody());
	}
}
