package com.accp.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.accp.filter.MyFormAuthenticationFilter;
import com.accp.realm.MySessionManager;
import com.accp.realm.ShiroDbRealm;

@Configuration
@Order()
public class ShiroConfig {
	
	@Bean(name = "shiroFilter")
	protected ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
		ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
		bean.setSecurityManager(securityManager);
		Map<String, String> filterChainDefinitionMap = new HashMap<>();
		filterChainDefinitionMap.put("/public/**", "anon");
		filterChainDefinitionMap.put("/system/**", "anon");
		bean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		Map<String, Filter> map = new HashMap<String, Filter>();
		map.put("authc", new MyFormAuthenticationFilter());
		bean.setFilters(map);
		return bean;
	}

	@Bean
	protected SecurityManager securityManager(ShiroDbRealm realm) {
		DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
		defaultWebSecurityManager.setRealm(realm);
		defaultWebSecurityManager.setSessionManager(new MySessionManager());
		defaultWebSecurityManager.setCacheManager(new EhCacheManager());
		return defaultWebSecurityManager;
	}

	@Bean
	protected LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	@Bean
	protected AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
		advisor.setSecurityManager(securityManager);
		return advisor;
	}
}
