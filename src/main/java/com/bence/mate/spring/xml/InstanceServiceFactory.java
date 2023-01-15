package com.bence.mate.spring.xml;

public class InstanceServiceFactory {

    public static IService getService(int number) {
        if (number == 1) {
        	return new IndexService();
        }
        return null;
    }
}
