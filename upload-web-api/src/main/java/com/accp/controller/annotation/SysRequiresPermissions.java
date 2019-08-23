package com.accp.controller.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.core.annotation.AliasFor;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SysRequiresPermissions {

	String name();

	String cname();

	String path() default "";
	
	String value() default "";
	
	int level() default 3;
	
	boolean hidden() default true;
}
