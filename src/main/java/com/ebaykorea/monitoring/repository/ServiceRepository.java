package com.ebaykorea.monitoring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ebaykorea.monitoring.model.Domain;

@Repository
public interface ServiceRepository extends JpaRepository<Domain, Integer>{
	@Query(value = "SELECT * FROM SERVICE_INFO GROUP BY NAME", nativeQuery = true)
	public List<Domain> findDomainName();
	public List<Domain> findByName(String name);
	public List<Domain> findByType(String type);
	@Query(value = "SELECT * FROM SERVICE_INFO WHERE NAME = ?1 AND TYPE = ?2", nativeQuery = true)
	public Domain findByNameAndType(String name, int type);
}
