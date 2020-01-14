package com.stackroute.datamunger.query.parser;

/*
 * This class is used for storing name of field, condition and value for 
 * each conditions
 * */
public class Restriction {

	public String propertyName=null;
	public String propertyValue=null;
	public String condition=null;
	
	
	
	public Restriction(String propertyName, String propertyValue, String condition) {
	//	super();
		this.propertyName = propertyName;
		this.propertyValue = propertyValue;
		this.condition = condition;
	}
	public Restriction() {
		// TODO Auto-generated constructor stub
	}
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public String getPropertyValue() {
		return propertyValue;
	}
	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	@Override
	public String toString() {
		return "Restriction [propertyName=" + propertyName + ", propertyValue=" + propertyValue + ", condition="
				+ condition + "]";
	}
	
	
//	
//	public String getPropertyName() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public String getPropertyValue() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public String getCondition() {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
	

}
