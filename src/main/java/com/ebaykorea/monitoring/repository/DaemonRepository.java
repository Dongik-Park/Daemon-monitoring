package com.ebaykorea.monitoring.repository;

import java.util.List;

import javax.persistence.Column;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ebaykorea.monitoring.model.Daemon;
import com.ebaykorea.monitoring.model.Log;

@Repository
public interface DaemonRepository extends JpaRepository<Daemon, Integer>{
	@Query(value = "SELECT * FROM MONITORING.DAEMON_INFO WHERE NAME = ?1 AND ON_OFF = 0", nativeQuery = true)
	public Daemon findByName(String name);
	@Query(value = "SELECT * FROM MONITORING.DAEMON_INFO WHERE SERVER_ID = ?1 AND ON_OFF = 0", nativeQuery = true)
	public List<Daemon> findByServerId(int serverId);
	@Query(value = "SELECT * FROM MONITORING.DAEMON_INFO WHERE CYCLE = ?1 AND ON_OFF = 0", nativeQuery = true)
	public List<Daemon> findByCycle(String cycle);
	@Query(value = "SELECT * FROM MONITORING.DAEMON_INFO WHERE ENABLED = ?1 AND ON_OFF = 0", nativeQuery = true)
	public List<Daemon> findByEnabled(byte enabled);
	@Query(value = "SELECT * FROM MONITORING.DAEMON_INFO WHERE RECENT_TIME = ?1 AND ON_OFF = 0", nativeQuery = true)
	public List<Daemon> findByRecentTime(String recentTime);
	@Query(value = "SELECT * FROM MONITORING.DAEMON_INFO WHERE NOTI_URL = ?1 AND ON_OFF = 0", nativeQuery = true)
	public List<Daemon> findByNotiUrl(String notiUrl);
	@Query(value = "SELECT * FROM MONITORING.DAEMON_INFO WHERE DESCRIPTION = ?1 AND ON_OFF = 0", nativeQuery = true)
	public List<Daemon> findByDescription(String description);
	@Query(value = "SELECT * FROM MONITORING.DAEMON_INFO WHERE RECEIVER = ?1 AND ON_OFF = 0", nativeQuery = true)
	public List<Daemon> findByReceiver(String receiver);
	@Query(value = "SELECT * FROM MONITORING.DAEMON_INFO WHERE ON_OFF = 0", nativeQuery = true)
	public List<Daemon> findAllDaemon();
}
