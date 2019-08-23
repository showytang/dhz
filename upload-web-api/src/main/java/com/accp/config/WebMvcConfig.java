package com.accp.config;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;


@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {


	/**
	 * 静态资源路径配置，注意：如果不配置，则会404
	 */
	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
	}

	/**
	 * 解决普通字符串通过ajax字符串消息转换器中文乱码
	 * 
	 * @return
	 */
	@Bean
	public StringHttpMessageConverter stringHttpMessageConverter() {
		return new StringHttpMessageConverter(StandardCharsets.UTF_8);
	}

	@Bean
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
		return new MappingJackson2HttpMessageConverter();
	}
	/**
	 * 默认的jackson，改变成fastjson
	 * 
	 * @return
	 *//*
		 * @Bean public FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
		 * //创建FastJson信息转换对象 FastJsonHttpMessageConverter fastJsonHttpMessageConverter
		 * = new FastJsonHttpMessageConverter();
		 * 
		 * //创建Fastjosn对象并设定序列化规则 FastJsonConfig fastJsonConfig = new FastJsonConfig();
		 * fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat); //
		 * 中文乱码解决方案 List<MediaType> mediaTypes = new ArrayList<>();
		 * mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);//设定json格式且编码为UTF-8
		 * fastJsonHttpMessageConverter.setSupportedMediaTypes(mediaTypes);
		 * 
		 * //规则赋予转换对象 fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
		 * return fastJsonHttpMessageConverter; }
		 */

	/**
	 * 添加拦截器
	 */
	@Override
	protected void addInterceptors(InterceptorRegistry registry) {
		super.addInterceptors(registry);
	}
	
	

	@Override
	protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(stringHttpMessageConverter());
		converters.add(mappingJackson2HttpMessageConverter());
		converters.add(new ByteArrayHttpMessageConverter());
		super.configureMessageConverters(converters);
	}

	@Override
	protected void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*");
		super.addCorsMappings(registry);
	}

}
