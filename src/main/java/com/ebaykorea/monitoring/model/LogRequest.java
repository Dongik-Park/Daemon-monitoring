package com.ebaykorea.monitoring.model;

import java.time.LocalDateTime;

public class LogRequest {

    private String hostname;
    private String daemon;
    private String source;
    private String log;
    private LocalDateTime timestamp;
    
    
    
	public LogRequest() {
		super();
	}



	public LogRequest(String hostname) {
		super();
		this.hostname = hostname;
	}



	public LogRequest(String hostname, String daemon, String source, String log, LocalDateTime timestamp) {
		super();
		this.hostname = hostname;
		this.daemon = daemon;
		this.source = source;
		this.log = log;
		this.timestamp = timestamp;
	}



	public String getHostname() {
		return hostname;
	}



	public void setHostname(String hostname) {
		this.hostname = hostname;
	}



	public String getDaemon() {
		return daemon;
	}



	public void setDaemon(String daemon) {
		this.daemon = daemon;
	}



	public String getSource() {
		return source;
	}



	public void setSource(String source) {
		this.source = source;
	}



	public String getLog() {
		return log;
	}



	public void setLog(String log) {
		this.log = log;
	}



	public LocalDateTime getTimestamp() {
		return timestamp;
	}



	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}



	@Override
	public String toString() {
		return "LogRequest [hostname=" + hostname + ", daemon=" + daemon + ", source=" + source + ", log=" + log
				+ ", timestamp=" + timestamp + "]";
	}
}
