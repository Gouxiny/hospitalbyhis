package com.gxy.controller;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gxy.entity.SysUser;
import com.gxy.form.SysLoginForm;
import com.gxy.model.RData;
import com.gxy.service.ShiroService;
import com.gxy.service.SysUserService;
import com.gxy.service.SysUserTokenService;
import com.gxy.utils.RedisUtils;

@RestController
public class LoginController {
	
	@Autowired
	private SysUserService sysuserservice;
	@Autowired
	private SysUserTokenService userToken;
	@Autowired
	private RedisUtils redis;
	@Autowired
	private ShiroService shiroServic;
	
	@RequestMapping("/Register")
	public RData RegisterDef(@RequestBody SysLoginForm form) {
		String password = new SimpleHash("SHA-1", form.getUsername(), form.getPassword()).toString();
		form.setPassword(password);
		RData r = new RData();
		Boolean turef = sysuserservice.saveUser(form);
		if(turef) {
			r.put("code", 200);
		}else {
			r.put("code", 200);
			r.put("res","当前用户已存在,请登录");
		}
		return r;
		
	}
	
	@RequestMapping("/login")
	public RData LoginDef(@RequestBody SysLoginForm form) {
		RData r = new RData();
		SysUser sysuser;
		try {
			 sysuser = sysuserservice.findByUsername(form.getUsername());
		}catch (Exception e) {
			// TODO: handle exception
			sysuser = null;
		}
		//判断是否存在
		if(sysuser == null) {
			r.put("code", 500);
			r.put("msg", "账户不存在或账户未绑定单位");
			return r;
		}
		//判断密码是否正确
		if(sysuser.getUsername() != null && sysuser.getPassword() != null) {
			String passwd = new SimpleHash("SHA-1", form.getUsername(), form.getPassword()).toString();
			if(sysuser.getPassword().equals(passwd)) {
				String companyId = sysuser.getUserid();
				String token = userToken.createToken(String.valueOf(sysuser.getUserid()));
				r.put("code", 200);
				r.put("companyId",companyId);
				r.put("token", token);
				r.put("msg", "OK");
				redis.set(token, token);
				redis.set(companyId,sysuser);
				return r;
			}
		r.put("code", 500);
		r.put("msg", "密码不正确");
		}
		return r;
	}

}
