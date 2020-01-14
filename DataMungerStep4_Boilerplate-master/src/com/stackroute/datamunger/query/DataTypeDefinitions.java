package com.stackroute.datamunger.query;

import java.util.Arrays;

public class DataTypeDefinitions {

	/*
	 * This class should contain a member variable which is a String array, to hold
	 * the data type for all columns for all data types
	 */

	public String[] dataTypes;
	
	public void setDataTypes(String[] data) {
		this.dataTypes=data;
	}
	
	public String[] getDataTypes() {
		return this.dataTypes;
	}

	@Override
	public String toString() {
		return "DataTypeDefinitions [data=" + Arrays.toString(dataTypes) + "]";
	}
	
	public DataTypeDefinitions(){
		
	}
}
