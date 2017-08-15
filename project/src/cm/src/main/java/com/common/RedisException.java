package com.common;

public class RedisException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	
	public RedisException(){
		
	}
	
	public RedisException(String message){
		super(message);
	}
}
