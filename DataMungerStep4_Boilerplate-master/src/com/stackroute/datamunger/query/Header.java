package com.stackroute.datamunger.query;

import java.util.Arrays;

public class Header {

	/*
	 * This class should contain a member variable which is a String array, to hold
	 * the headers.
	 */
	public String[] headers;
	
	public String[] getHeaders() {
		return 	this.headers;
	}
	
	public void setHeaders(String[] data) {
		this.headers=data;
	}

	@Override
	public String toString() {
		return "Header [hederData=" + Arrays.toString(headers) + "]";
	}
	
	public Header(){
		
	}
	
	

}
