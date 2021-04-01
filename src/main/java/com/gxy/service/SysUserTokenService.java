package com.gxy.service;


import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gxy.dao.SysUserTokenDao;
import com.gxy.entity.SysUserToken;
import com.gxy.oauth2.TokenGenerator;
import com.gxy.utils.RedisUtils;


@Service
public class SysUserTokenService {

	@Autowired
	private SysUserTokenDao sysUserTokenDao;
	@Autowired
	private RedisUtils redis;
	
	 //12小时后过期
    private final static int EXPIRE = 3600 * 12;
	
	public String createToken(String userId) {
		String token = TokenGenerator.generateValue();
        //当前时间
        Date now = new Date();
        //过期时间
        Date expireTime = new Date(now.getTime() + EXPIRE * 1000);
        
        Map<String,Object> usertokenmap = sysUserTokenDao.queryById(userId);
        
        if(usertokenmap == null){
        	SysUserToken usertoken = new SysUserToken();
        	usertoken.setToken(token);
        	usertoken.setUserId(userId);
        	usertoken.setExpireTime(expireTime);
        	usertoken.setUpdateTime(now);
        	redis.set("token"+token, usertoken);
            sysUserTokenDao.save(usertoken);
        }else {
        	SysUserToken sysUserToken = new SysUserToken();
        	sysUserToken.setToken(token);                                                                                      
        	sysUserToken.setUserId(userId);
        	sysUserToken.setExpireTime(expireTime);
        	sysUserToken.setUpdateTime(now);
        	redis.set("token"+token, sysUserToken);
            sysUserTokenDao.edit(sysUserToken);
        }
        return token;
	}
	 public String getuserName(String token) {
	    	SysUserToken userToken = sysUserTokenDao.queryByToken(token);
	    	String id;
	    	try {
	    		id = userToken.getUserId();
	    	}catch (Exception e) {
				// TODO: handle exception
	    		id ="0";
			}
	    	return id;
	    }
	    
    public void logout(String userId){
        //生成一个token
        String token = TokenGenerator.generateValue();
        SysUserToken userToken = new SysUserToken();
        userToken.setToken(token);
        userToken.setUserId(userId);
        sysUserTokenDao.edit(userToken);
    }
    
}
