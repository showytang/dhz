package com.accp.aop;

import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.accp.controller.annotation.SysRequiresPermissions;

@Component
@Aspect
public class PermAop {

	@Pointcut("execution(* com.accp.controller..*(..))")
	public void aspect() {

	}

	@Pointcut("@annotation(com.accp.controller.annotation.SysRequiresPermissions)")
	public void aspectTwo() {

	}

	@Around("aspect() && aspectTwo()")
	public Object around(ProceedingJoinPoint point) {
		try {
			Signature sig = point.getSignature();
			MethodSignature msig = null;
			if (!(sig instanceof MethodSignature)) {
				throw new IllegalArgumentException("该注解只能用于方法");
			}
			msig = (MethodSignature) sig;
			Object target = point.getTarget();
			Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
			SysRequiresPermissions perm = currentMethod.getAnnotation(SysRequiresPermissions.class);
			System.out.println(currentMethod);
			if(perm!=null) {
				if(StringUtils.isNotEmpty(perm.value())) {
					boolean bol = SecurityUtils.getSubject().isPermitted(perm.value());
					if(bol) {
						return point.proceed();
					}else {
						throw new RuntimeException("权限不足，请联系管理员。");
					}
				}else {
					return point.proceed();
				}
			}else {
				return point.proceed();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

}
