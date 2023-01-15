package com.bence.mate.spring.xml;

public class JavaApp {
	
    private IService service;
        
    public JavaApp() {
	}

	public String callTheService() {
    	return this.service.serve();
    }

	public IService getService() {
		return service;
	}

	public void setService(IService service) {
		this.service = service;
	}
}
