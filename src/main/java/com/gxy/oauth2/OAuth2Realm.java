package com.gxy.oauth2;


import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.google.gson.annotations.JsonAdapter;
import com.gxy.entity.SysUser;
import com.gxy.entity.SysUserToken;
import com.gxy.service.ShiroService;
import com.gxy.utils.RedisUtils;


@Component
public class OAuth2Realm extends AuthorizingRealm {

	@Autowired
	private RedisUtils redisutil;
	
	@Autowired
	private ShiroService shiroService;
	
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof OAuth2Token;
    }

    /**
     * *授权(验证权限时调用)
     */
//    @Override
//    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
//        SysUser user =  (SysUser)principals.getPrimaryPrincipal();
//        int userId = user.getUserId();
//        //用户权限列表
//        Set<String> permsSet = shiroService.getUserPermissions(userId);
//        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//        info.setStringPermissions(permsSet);
//        return info;
//    }

    /**
     ** 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String accessToken = (String) token.getPrincipal();

        //根据accessToken，
        //查询用户信息
        SysUserToken userToken = (SysUserToken) redisutil.get("token"+accessToken);
        //token失效
        if(((SysUserToken) userToken) == null || ((SysUserToken) userToken).getExpireTime().getTime() < System.currentTimeMillis()){
            throw new IncorrectCredentialsException("token失效，请重新登录");
        }
        SysUser user = new SysUser();
        try{
        	 user = (SysUser) redisutil.get(((SysUserToken) userToken).getUserId());
        }catch (Exception e) {
			// TODO: handle exception
        	 user = shiroService.queryByUser(((SysUserToken) userToken).getUserId());
        	 redisutil.set(((SysUserToken) userToken).getUserId(), user);
		}
        if(user.getStatus().toString() == "0"){
            throw new LockedAccountException("账号已被锁定,请联系管理员");
        }
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, accessToken, getName());
        return info;
    }

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// TODO Auto-generated method stub
		return null;
	}
}
