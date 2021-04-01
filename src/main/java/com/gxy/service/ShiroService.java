package com.gxy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gxy.dao.SysUserDao;
import com.gxy.dao.SysUserTokenDao;
import com.gxy.entity.SysUser;
import com.gxy.entity.SysUserToken;


@Service
public class ShiroService {

	@Autowired
	private SysUserTokenDao sysUserTokenDao;
	@Autowired
	private SysUserDao sysUserDao;

	public SysUserToken queryUser(String token) {
		return sysUserTokenDao.queryByToken(token);
	}
	
	public SysUser queryByUser(String userId) {
		return sysUserDao.findById(userId);
	}
	
}
