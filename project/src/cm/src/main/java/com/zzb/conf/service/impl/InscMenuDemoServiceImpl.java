package com.zzb.conf.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zzb.conf.entity.Demo;
import com.zzb.conf.service.InscMenuDemoService;

@Service
@Transactional
public class InscMenuDemoServiceImpl implements InscMenuDemoService{

	@Override
	public List<Demo> getData() {
		List<Demo> data = new ArrayList<Demo>();
		for(int i=0;i<5;i++){
			Demo d = new Demo();
			d.setId("id  "+i);
			d.setName("jie  "+i);
			data.add(d);
		}
		return data;
	}

	
}
