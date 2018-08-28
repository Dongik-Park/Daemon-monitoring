package com.ebaykorea.monitoring.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
@Entity
@Table(name="log_error_info")
public class Log {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Column(name="daemon_id")
	private int daemonId;
	
	@Column(name="status")
	private int status;
	
	@Column(name="error_time", columnDefinition = "DATETIME")
	private String errorTime;
	
	public Log() {
		this.id = (long) 0;
		this.daemonId = 0;
		this.status = 1;
		java.util.Date dt = new java.util.Date();
		java.text.SimpleDateFormat sdf = 
		     new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.errorTime = sdf.format(dt);
	}
	
	public Log(int daemonId, int status, String errorTime) {
		super();
		this.daemonId = daemonId;
		this.status = status;
		this.errorTime = errorTime;
	}
	
	public Log(Long id, int daemonId, int status, String errorTime) {
		super();
		this.id = id;
		this.daemonId = daemonId;
		this.status = status;
		this.errorTime = errorTime;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getDaemonId() {
		return daemonId;
	}
	public void setDaemonId(int daemonId) {
		this.daemonId = daemonId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getErrorTime() {
		return errorTime;
	}
	public void setErrorTime(String errorTime) {
		this.errorTime = errorTime;
	}
	
	
}
