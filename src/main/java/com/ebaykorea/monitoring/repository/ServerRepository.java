package com.ebaykorea.monitoring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebaykorea.monitoring.model.Server;

@Repository
public interface ServerRepository extends JpaRepository<Server, Integer>{
	public List<Server> findByServiceId(int serviceId);
	public Server findByHostName(String hostName);
	public Server findByIp(String ip);
	public List<Server> findByCycle(String cycle);
	public List<Server> findByStatus(int status);
	public List<Server> findByEnabled(byte enabled);
} 
