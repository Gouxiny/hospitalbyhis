package com.gxy.dao;


import org.apache.ibatis.annotations.Mapper;

import com.gxy.entity.SysUser;
import com.gxy.form.SysLoginForm;
import com.gxy.model.RData;

@Mapper
public interface SysUserDao {
	
	SysUser findByUsername(String username);
	SysUser findById(String userId);
	void save(SysLoginForm form);
	void edit(SysLoginForm form);
}
