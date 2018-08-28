package com.ebaykorea.monitoring.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="daemon_server_info")
public class Server {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="service_id")
	private int serviceId;
	
	@Column(name="hostname")
	private String hostName;
	
	@Column(name="ip")
	private String ip;
	
	@Column(name="cycle")
	private String cycle;
	
	@Column(name="status")
	private int status;
	
	@Column(name="enabled")
	private byte enabled;
	
	public Server() {
		super();
		this.id = 0;
		this.serviceId = 0;
		this.hostName = "Unnammed";
		this.ip = "127.0.0.1";
		this.cycle = "* * * * * *";
		this.status = 1;
		this.enabled = 1;
	}
	
	public Server(int serviceId, String hostName, String ip, String cycle, int status,
			byte enabled) {
		super();
		this.serviceId = serviceId;
		this.hostName = hostName;
		this.ip = ip;
		this.cycle = cycle;
		this.status = status;
		this.enabled = enabled;
	}
	
	public Server(int id, int serviceId, String hostName, String ip, String cycle, int status,
			byte enabled) {
		super();
		this.id = id;
		this.serviceId = serviceId;
		this.hostName = hostName;
		this.ip = ip;
		this.cycle = cycle;
		this.status = status;
		this.enabled = enabled;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getServiceId() {
		return serviceId;
	}
	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getCycle() {
		return cycle;
	}
	public void setCycle(String cycle) {
		this.cycle = cycle;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public byte getEnabled() {
		return enabled;
	}
	public void setEnabled(byte enabled) {
		this.enabled = enabled;
	}
	
	
}
