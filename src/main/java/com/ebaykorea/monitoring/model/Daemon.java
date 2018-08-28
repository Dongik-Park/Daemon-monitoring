package com.ebaykorea.monitoring.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

// 이 어노테이션을 생략하면 No serializer found for class org.hibernate.proxy.pojo.javassist.JavassistLazyInitializer and no properties discovered to create BeanSerializer 에러 발생..
// 이유는 Hibernate가 DB에서 객체들을 로드할때, 이 Entity 타입처럼 보이는 proxied된 오브젝트를 반환하는데 이것이 jackson serilazer를 제거하기 때문에 introspection에 의존하여 객체의 속성을 찾게 된다.
// 결론 : JSON형태로 데이터를 송수신하지 않기 때문에 얘를 통해 Json이 아닌 원본 데이터를 이용하도록 하기 위해 사용함.
// 참고 - https://stackoverflow.com/questions/18470438/exception-thrown-when-serializing-hibernate-object-to-json 
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
@Data
@Entity
@Table(name="daemon_info")
public class Daemon {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="server_id")
	private int serverId;
	
	@Column(name="name")
	private String name;
	
	@Column(name="cycle")
	private String cycle;
	
	@Column(name="status")
	private int status;
	
	@Column(name="enabled")
	private byte enabled;
	
	@Column(name="noti_type")
	private int notiType;
	
	@Column(name="recent_time")
	private String recentTime;
	
	@Column(name="noti_url")
	private String notiUrl;
	
	@Column(name="description")
	private String description;
	
	@Column(name="receiver")
	private String receiver;
	
	@Column(name="on_off")
	private byte onOff;

	public Daemon() {
		super();
		this.id = 0;
		this.name = "Unnammed";
		this.serverId = 0;
		this.cycle = "* * * * * *";
		this.status = 1;
		this.enabled = 1;
		this.notiType = 1;
		this.notiUrl = "http://slack.com";
		this.description = "None";
		this.receiver = "None";
		java.util.Date dt = new java.util.Date();
		java.text.SimpleDateFormat sdf = 
		     new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.recentTime = sdf.format(dt);
	}

	public Daemon(int id) {
		this.id = id;
		this.name = "Unnammed";
		this.serverId = 0;
		this.cycle = "* * * * * *";
		this.status = 1;
		this.enabled = 1;
		this.notiType = 1;
		this.notiUrl = "http://slack.com";
		this.description = "None";
		this.receiver = "None";
		java.util.Date dt = new java.util.Date();
		java.text.SimpleDateFormat sdf = 
		     new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.recentTime = sdf.format(dt);
	}
	
	public Daemon(int serverId, String name) {
		this.name = name;
		this.serverId = serverId;
		this.cycle = "* /10 * * * *";
		this.status = 1;
		this.enabled = 1;
		this.notiType = 1;
		this.notiUrl = "http://slack.com";
		this.description = "None";
		this.receiver = "None";
		java.util.Date dt = new java.util.Date();
		java.text.SimpleDateFormat sdf = 
		     new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.recentTime = sdf.format(dt);
	}
	
	public Daemon(int serverId, String name, String cycle, int status, byte enabled,
			int notiType, String recentTime, String notiUrl, String description, String receiver) {
		super();
		this.serverId = serverId;
		this.name = name;
		this.cycle = cycle;
		this.status = status;
		this.enabled = enabled;
		this.notiType = notiType;
		this.recentTime = recentTime;
		this.notiUrl = notiUrl;
		this.description = description;
		this.receiver = receiver;
	}
	
	public Daemon(int id, int serverId, String name, String cycle, int status, byte enabled,
			int notiType, String recentTime, String notiUrl, String description, String receiver) {
		super();
		this.id = id;
		this.serverId = serverId;
		this.name = name;
		this.cycle = cycle;
		this.status = status;
		this.enabled = enabled;
		this.notiType = notiType;
		this.recentTime = recentTime;
		this.notiUrl = notiUrl;
		this.description = description;
		this.receiver = receiver;
	}
	
	public Daemon(int id, int serverId, String name, String cycle, int status, byte enabled, int notiType,
			String recentTime, String notiUrl, String description, String receiver, byte onOff) {
		super();
		this.id = id;
		this.serverId = serverId;
		this.name = name;
		this.cycle = cycle;
		this.status = status;
		this.enabled = enabled;
		this.notiType = notiType;
		this.recentTime = recentTime;
		this.notiUrl = notiUrl;
		this.description = description;
		this.receiver = receiver;
		this.onOff = onOff;
	}
	
	public Daemon(Daemon daemon) {
		super();
		this.id = daemon.getId();
		this.serverId = daemon.getServerId();
		this.name = daemon.getName();
		this.cycle = daemon.getCycle();
		this.status = daemon.getStatus();
		this.enabled = daemon.getEnabled();
		this.notiType = daemon.getNotiType();
		this.recentTime = daemon.getRecentTime();
		this.notiUrl = daemon.getNotiUrl();
		this.description = daemon.getDescription();
		this.receiver = daemon.getReceiver();
		this.onOff = daemon.getOnOff();
	}
	

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getServerId() {
		return serverId;
	}
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public int getNotiType() {
		return notiType;
	}
	public void setNotiType(int notiType) {
		this.notiType = notiType;
	}
	public String getRecentTime() {
		return recentTime;
	}
	public void setRecentTime(String recentTime) {
		this.recentTime = recentTime;
	}
	public String getNotiUrl() {
		return notiUrl;
	}
	public void setNotiUrl(String notiUrl) {
		this.notiUrl = notiUrl;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public byte getOnOff() {
		return onOff;
	}

	public void setOnOff(byte onOff) {
		this.onOff = onOff;
	}
	
}
