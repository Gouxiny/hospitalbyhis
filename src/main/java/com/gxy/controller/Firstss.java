package com.gxy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gxy.form.SysLoginForm;
import com.gxy.model.RData;
import com.gxy.utils.RedisUtils;


@RequestMapping
@RestController
public class Firstss {
	
	@Autowired
	private RedisUtils redis;
	
	@RequestMapping("/sd")
	public RData firstDef(@RequestBody SysLoginForm form) {
		RData r = new RData();
		return r;
	}
	
	
}
