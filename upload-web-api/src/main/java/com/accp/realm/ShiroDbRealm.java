package com.accp.realm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.accp.dao.SysPermissionsDao;
import com.accp.dao.SysUsersDao;
import com.accp.entity.SysPermissions;
import com.accp.entity.SysUsers;
import com.accp.utils.bcrypt.BCryptPasswordEncoder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

@Component
public class ShiroDbRealm extends AuthorizingRealm {

	@Autowired
	private SysUsersDao userDao;

	@Autowired
	SysPermissionsDao perDao;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SysUsers user = (SysUsers) getAuthenticationCacheKey(principals);
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		QueryWrapper<SysPermissions> perQuery = new QueryWrapper<SysPermissions>();
		perQuery.lambda().in(SysPermissions::getRoleid, user.getRoleid());
		List<SysPermissions> perList = perDao.selectList(perQuery);
		List<String> listPermissions = new ArrayList<String>();
		for (SysPermissions p : perList) {
			listPermissions.add(p.getPermission());
		}
		info.setStringPermissions(new HashSet<String>(listPermissions));
		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken userToken = (UsernamePasswordToken) token;
		String username = userToken.getUsername();
		String password = new String(userToken.getPassword());
		QueryWrapper<SysUsers> userQuery = new QueryWrapper<SysUsers>();
		userQuery.lambda().eq(SysUsers::getUsername, username);
		SysUsers users = userDao.selectOne(userQuery);
		BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
		if (bc.matches(password, users.getPassword())) {
			// 简单认证信息对象
			AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(users, password, getName());
			// 认证
			return authenticationInfo;
		} else {
			throw new UnknownAccountException("找不到用户（" + username + "）的帐号信息");
		}
	}
}
