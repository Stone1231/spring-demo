package com.demo.model;

public class Callback {

	private String serviceName = null;
	private String methodName = null;
	private String beanName = null;

	public Callback(){
		
	}
	
	public Callback(Class<?> clazz){
		serviceName = clazz.getName();
		beanName = clazz.getSimpleName();
		methodName = "onCompletion";
	}
	
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getBeanName() {
		return beanName;
	}
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

}