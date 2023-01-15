package com.bence.mate.spring.xml;

public class XmlApp {
	
    private IService service;
    
    public XmlApp(IService service) {
		this.service = service;
	}

	public String callTheService() {
    	return this.service.serve();
    }
}
