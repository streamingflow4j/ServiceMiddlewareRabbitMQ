package com.service.middleware.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationContextProvider implements ApplicationContextAware {

	private static ApplicationContext applicationContext = null;
	 
	private ApplicationContextProvider(){}

	public static Object getBean(String beanName) {
		return applicationContext.getBean(beanName);
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ApplicationContextProvider.applicationContext = applicationContext;
		
	}
	    
 
}
