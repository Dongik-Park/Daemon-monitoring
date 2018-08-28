package com.ebaykorea.monitoring.model;

import com.ebaykorea.monitoring.enumeration.ErrorLogType;

public class DaemonDetailVO extends Daemon{
	private String hostName;
	private String errorTime;
	private String serviceName;
	private ErrorLogType errorType;
	
	public DaemonDetailVO() {
		super();
		this.hostName = "Unnnamed";
		java.util.Date dt = new java.util.Date();
		java.text.SimpleDateFormat sdf = 
		     new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.errorTime = sdf.format(dt);
		this.errorType = ErrorLogType.NONE;
	}

	public DaemonDetailVO(String hostName, String errorTime) {
		super();
		this.hostName = hostName;
		this.errorTime = errorTime;
		this.serviceName = "Unnamed";
		this.errorType = ErrorLogType.NONE;
	}

	public DaemonDetailVO(Daemon daemon, String hostName, String errorTime) {
		super(daemon);
		this.hostName = hostName;
		this.errorTime = errorTime;
		this.serviceName = "Unnamed";
		this.errorType = ErrorLogType.NONE;
	}

	public DaemonDetailVO(Daemon daemon, String hostName, String errorTime, ErrorLogType errorType) {
		super(daemon);
		this.hostName = hostName;
		this.errorTime = errorTime;
		this.serviceName = "Unnamed";
		this.errorType = ErrorLogType.NONE;
		this.errorType = errorType;
	}
	
	public DaemonDetailVO(Daemon daemon, String hostName, String errorTime, String serviceName) {
		super(daemon);
		this.hostName = hostName;
		this.errorTime = errorTime;
		this.serviceName = serviceName;
		this.errorType = ErrorLogType.NONE;
	}

	public DaemonDetailVO(Daemon daemon, String hostName, String errorTime, String serviceName, ErrorLogType errorType) {
		super(daemon);
		this.hostName = hostName;
		this.errorTime = errorTime;
		this.serviceName = serviceName;
		this.errorType = errorType;
	}
	
	public ErrorLogType getErrorType() {
		return errorType;
	}

	public void setErrorType(ErrorLogType errorType) {
		this.errorType = errorType;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getErrorTime() {
		return errorTime;
	}

	public void setErrorTime(String errorTime) {
		this.errorTime = errorTime;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	
	
}
