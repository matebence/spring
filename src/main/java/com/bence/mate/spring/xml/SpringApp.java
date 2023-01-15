package com.bence.mate.spring.xml;

public class SpringApp {
	
    private IService service;
     
    public SpringApp(IService service) {
		this.service = service;
	}

	public String callTheService() {
    	return this.service.serve();
    }
}
