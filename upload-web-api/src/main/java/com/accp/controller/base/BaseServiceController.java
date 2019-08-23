package com.accp.controller.base;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.accp.utils.ApplicationContextProvider;
import com.baomidou.mybatisplus.extension.service.IService;

public class BaseServiceController<E, T> {
	private Class<T> thisService;
	
	public BaseServiceController() {
		Type t = this.getClass().getGenericSuperclass();
		Type[] args = ((ParameterizedType) t).getActualTypeArguments();
		Class<T> clazz = (Class<T>) args[1];
		this.thisService = clazz;
	}
	
	public Class<T> getThisService(){
		return this.thisService;
	}

	@SuppressWarnings("all")
	IService service;

	@SuppressWarnings("all")
	public IService getService() {
		if (service != null) {
			return service;
		}
		service = (IService) ApplicationContextProvider.getBean(this.getThisService());
		return service;
	}
}
