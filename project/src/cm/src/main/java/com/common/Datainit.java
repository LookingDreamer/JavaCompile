package com.common;

import java.util.ResourceBundle;

public class Datainit {
	
	public Datainit(){
	
	ResourceBundle resourceBundle = ResourceBundle
			.getBundle("config/config");
	String brokers = resourceBundle.getString("kafka.brokers");
	System.setProperty("kafka.brokers", brokers);

	}
}
