package com.ebaykorea.monitoring.model;

import com.ebaykorea.monitoring.enumeration.ServiceType;

public class DomainVO {
	
	private int id;
	
	private String name;
	
	private ServiceType type;
	
	public DomainVO() {
		this.id = 0;
		this.name = "Unnamed";
		this.type = ServiceType.NONE;
	}
	
	public DomainVO(ServiceType type) {
		this.id = 0;
		this.name = "Unnamed";
		this.type = type;
	}
	
	public DomainVO(String name, ServiceType type) {
		this.id = 0;
		this.name = name;
		this.type = type;
	}
	
	public DomainVO(int id, String name, ServiceType type) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ServiceType getType() {
		return type;
	}
	public void setType(ServiceType type) {
		this.type = type;
	}
	
}
