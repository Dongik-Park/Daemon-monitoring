package com.ebaykorea.monitoring.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebaykorea.monitoring.model.Server;
import com.ebaykorea.monitoring.model.Domain;
import com.ebaykorea.monitoring.repository.ServerRepository;
import com.ebaykorea.monitoring.repository.ServiceRepository;

@Service
public class ServerService {
	
	@Autowired
	private ServiceRepository serviceRepository;
	
	@Autowired
	private ServerRepository serverRepository;
	
	public Server addServer(Server request) {
		// 반환할 서버 정보
		Server server = null;
		
		try {
			// 서버가 존재하는 지 확인
			Domain service = serviceRepository.getOne(request.getServiceId());
			// 서버 생성
			server = new Server(service.getId(), 
					request.getHostName(), 
					request.getIp(), 
					request.getCycle(), 
					request.getStatus(), 
					request.getEnabled());
			// 생성된 서버 정보 추가 트랜잭션
			serverRepository.save(server);
		}
		catch(Exception e) {
			// 익셉션 발생 시 디폴트 생성자 반환
			server = new Server();
		}
		
		return server;
	}
	
	public Server updateServer(Server request) {
		// 반환할 서버 정보
		Server server = null;
		
		try {
				// 서버 정보 추출
				server = serverRepository.getOne(request.getId());
				// 서비스 정보 추출
				Domain service = serviceRepository.getOne(request.getId());
				// 서버 정보 생성
				server = new Server(service.getId(), 
						request.getHostName(), 
						request.getIp(), 
						request.getCycle(), 
						request.getStatus(), 
						request.getEnabled());
				// 서버 수정 트랜잭션
				serverRepository.save(server);
		}
		catch(Exception e) {
			// 익셉션 발생 시 디폴트 생성자 반환
			server = new Server();
		}
		
		return server;
	}
	
	public Server searchServerByServerId(int serverId) {
		// 반환할 서버 정보
		Server server = null;
		
		try {
			// 서버 정보 추출
			server = serverRepository.getOne(serverId);
		}
		catch(Exception e) {
			// 익셉션 발생 시 디폴트 생성자 반환
			server = new Server();
		}
		
		return server;
	}
	
	public Server searchServerByName(String name) {
		// 반환할 서버 생성 및 초기화
		Server server = null;
		
		try {
			// 서버 이름과 일치하는 서버 정보 반환
			server = serverRepository.findByHostName(name);
		}
		catch(Exception e){
			// 익셉션 발생 시 디폴트 생성자 반환
			server = new Server();
		}
		
		return server;
	}
	
	public List<Server> searchServerByServiceName(String serviceName){
		// 반환할 리스트 생성 및 초기화
		List<Server> serverList = new ArrayList<>();
		
		try {
			// 서비스 아이디와 일치하는 서비스 리스트 반환
			List<Domain> services = serviceRepository.findByName(serviceName);
			// 각 서비스와 일치하는 서버 목록 반환
			for(int i = 0; i < services.size(); ++i) {
				// 서비스 정보에 따른 서버 리스트 반환
				List<Server> temp = serverRepository.findByServiceId(services.get(i).getId());
				// 서버 정보를 반환 리스트에 추가
				for(int j = 0; j < temp.size(); ++j) {
					serverList.add(temp.get(j));
				}
			}
		}
		catch(Exception e) {
			// 익셉션 발생 시 디폴트 생성자가 추가된 리스트 반환
			if(serverList.size() == 0) {
				serverList.add(new Server());
			}
		}
		
		return serverList;
	}
	
	public List<Server> searchServerByServiceId(int serviceId){
		// 반환할 리스트 생성 및 초기화
		List<Server> serverList = new ArrayList<>();
		
		try {
			// 서비스 아이디와 일치하는 서버리스트 반환
			serverList = serverRepository.findByServiceId(serviceId);
		}
		catch(Exception e) {
			// 익셉션 발생 시 디폴트 생성자가 추가된 리스트 반환
			if(serverList.size() == 0) {
				serverList.add(new Server());
			}
		}
		
		return serverList;		
	}
}
