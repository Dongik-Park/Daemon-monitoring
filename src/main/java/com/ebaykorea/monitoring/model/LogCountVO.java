package com.ebaykorea.monitoring.model;

public class LogCountVO extends Log{
	private int today;
	private int week;
	private int quarter;
	private int total;
	private int serverId;
	
	public LogCountVO() {
		super();
		this.today=-1;
		this.week=-1;
		this.quarter = -1;
		this.total = -1;
		this.serverId = -1;
	}

	public LogCountVO(int today, int week, int quarter, int total) {
		super();
		this.today = today;
		this.week = week;
		this.quarter = quarter;
		this.total = total;
		this.serverId = -1;
	}
	
	public LogCountVO(int today, int week, int quarter, int total, int serverId) {
		super();
		this.today = today;
		this.week = week;
		this.quarter = quarter;
		this.total = total;
		this.serverId = serverId;
	}

	public int getToday() {
		return today;
	}

	public void setToday(int today) {
		this.today = today;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public int getQuarter() {
		return quarter;
	}

	public void setQuarter(int quarter) {
		this.quarter = quarter;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
	
	
}
