package com.hpicorp.bcs.common;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider implements ApplicationContextAware  {

	private static ApplicationContext context;	
	
	public static void setContext(ApplicationContext context) {
		ApplicationContextProvider.context = context;
	}

	public static ApplicationContext getApplicationContext() {
        return context;
    }
	
	@Override
	public void setApplicationContext(ApplicationContext arg0) {
		setContext(arg0);
	}
	
}
