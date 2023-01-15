package com.bence.mate.spring.xml;

public class IndexApp {
	
    private IService service;
        
    public IndexApp() {
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