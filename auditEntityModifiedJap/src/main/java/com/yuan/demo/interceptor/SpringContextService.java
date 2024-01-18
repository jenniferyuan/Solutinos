package com.yuan.demo.interceptor;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

// https://blog.csdn.net/cxh6863/article/details/128174470
// Spring 普通类获取Spring容器的bean的方法

@Component
public class SpringContextService implements ApplicationContextAware {
	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (SpringContextService.applicationContext == null) {
			SpringContextService.applicationContext = applicationContext;
		}
	}

	public static <T> T getBean(Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}

	public static Object getBean(String beanName) {
		return applicationContext.getBean(beanName);
	}
}
