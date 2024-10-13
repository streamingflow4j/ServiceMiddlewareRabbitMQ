package com.service.middleware.model;

import java.util.List;

public class Entity {
	
	private String id;
	private String type;
	private List<Attribute> attributes;

	public Entity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Entity(String id, String type, List<Attribute> attributes) {
		super();
		this.id = id;
		this.type = type;
		this.attributes = attributes;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}

	

}
