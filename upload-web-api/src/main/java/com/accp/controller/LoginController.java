package com.accp.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

	@GetMapping("/public/login")
	public Object login(String username, String password, HttpSession session) {
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			SecurityUtils.getSubject().login(token);
			Object user = SecurityUtils.getSubject().getPrincipal();
			map.put("token", session.getId());
			map.put("user", user);
			return map;
		} catch (AuthenticationException e) {
			e.printStackTrace();
			return null;
		}
	}

	@GetMapping("/public/verifyLogin")
	public Object verifyLogin() {
		Object obj = SecurityUtils.getSubject().getPrincipal();
		return obj;
	}
}
