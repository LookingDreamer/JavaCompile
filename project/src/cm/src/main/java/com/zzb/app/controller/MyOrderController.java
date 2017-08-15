package com.zzb.app.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cninsure.core.exception.ControllerException;

@Controller
@RequestMapping("/cpmap/*")
public class MyOrderController {

	

	/**
	 * 获取顶单接口  
	 * 请求方式 POST
	 * 请求地址 /cpmap/access
	 * MSM_HEADER=CmdType=FHBX;CmdModule=MULTIQUOTES;CmdId=Search;CmdVer=1.0.0;Token=
	 * @param MSM_PARAM=uuid=B88C62ADF49D4343AA76829209CCD614;max=12;offset=0;licensePlate=;insuredName=;status=;dateCreatedStart="
	 *     必须的参数 
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/access_zjj", method = RequestMethod.POST, params = "MSM_HEADER=CmdType=FHBX;CmdModule=MULTIQUOTES;CmdId=Search;CmdVer=1.0.0;Token=")
	@ResponseBody
	public String orderList(@RequestParam String MSM_PARAM)
			throws ControllerException {
		//调用service方法
		return "{success}";
	}
	
	

}
