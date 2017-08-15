package com.redis;

import java.io.Serializable;

public class PersonF implements Serializable {
	private static final long serialVersionUID = -9138105938710788421L;
	private int id;
	private String name;
	public PersonF(){
		
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public PersonF(int id, String name) {
		this.id = id;
		this.name = name;
	}
	@Override
	public String toString() {
		return "PersonF [id=" + id + ", name=" + name + "]";
	}
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}

}
