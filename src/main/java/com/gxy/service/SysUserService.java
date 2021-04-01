package com.gxy.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gxy.dao.SysUserDao;
import com.gxy.entity.SysUser;
import com.gxy.form.SysLoginForm;

@Service
public class SysUserService {
	
	@Autowired
	private SysUserTokenService sysUserTokenService;
	
	@Autowired
	private SysUserDao sysUserDao;
	
	public Boolean saveUser(SysLoginForm form) {
		SysUser userif = sysUserDao.findByUsername(form.getUsername());
		if(userif == null) {
			sysUserDao.save(form);
			SysUser r = sysUserDao.findByUsername(form.getUsername());
			sysUserTokenService.createToken((String) r.getUserid());
			return true;
		}else {
			return false;
		}
	}
	
	public SysUser findByUsername(String username) {
		SysUser user = sysUserDao.findByUsername(username);
		return user;
	}
	
	
	
}
