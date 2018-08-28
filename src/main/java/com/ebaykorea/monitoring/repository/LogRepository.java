package com.ebaykorea.monitoring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ebaykorea.monitoring.model.Log;

@Repository
public interface LogRepository extends JpaRepository<Log, Long>{
	public List<Log> findByDaemonId(int daemonId);
	public List<Log> findByStatus(int status);
	public List<Log> findByErrorTime(String errorTime);
	@Query(value = "SELECT * FROM MONITORING.LOG_ERROR_INFO WHERE DAEMON_ID = ?1 ORDER BY ERROR_TIME DESC LIMIT 1", nativeQuery = true)
	public Log findByDaemonIdRecent(int daemonId);	
	@Query(value = "SELECT * FROM MONITORING.LOG_ERROR_INFO WHERE DAEMON_ID = ?1 AND DATE(ERROR_TIME) BETWEEN ?2 AND ?3", nativeQuery = true)
	public List<Log> findByDaemonIDAndTime(int daemonId, String time, String curTime);
}
