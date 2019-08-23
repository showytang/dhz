package com.accp.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class MyFormAuthenticationFilter extends FormAuthenticationFilter {

	/**
	 * 适配sessionid，允许跨域，握手
	 */
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		if(request instanceof HttpServletRequest) {
			//form action="url" method="get post delete put OPTIONS"
			HttpServletRequest httpRequest = (HttpServletRequest)request;
			boolean bol = httpRequest.getMethod().toUpperCase().equals("OPTIONS");
			if(bol) {
				return true;
			}
		}
		return super.isAccessAllowed(request, response, mappedValue);
	}
}

