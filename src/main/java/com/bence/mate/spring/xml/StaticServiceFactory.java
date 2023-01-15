package com.bence.mate.spring.xml;

public class StaticServiceFactory {
	
    public static IService getService(int number) {
        if (number == 1) {
        	return new IndexService();
        }
        return null;
    }
}
