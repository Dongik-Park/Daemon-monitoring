package com.ebaykorea.monitoring.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebaykorea.monitoring.enumeration.ServiceType;
import com.ebaykorea.monitoring.model.Domain;
import com.ebaykorea.monitoring.model.DomainVO;
import com.ebaykorea.monitoring.repository.ServiceRepository;

@Service
public class DomainService {
	
	@Autowired
	private ServiceRepository serviceRepository;
	/**
	 * 도메인 추가 transaction
	 * @param request 추가할 서비스 모델
	 * @return 정상적으로 추가된 서비스 정보
	 */
	public Domain addService(Domain request) {
		// 반환하기 위한 서비스 정보 선언
		Domain service = null;
		
		try {
			// 서비스 생성 및 추가 트랜잭션
			service = new Domain(request.getName(),request.getType());
			serviceRepository.save(service);
		}
		catch(Exception e) {
			// 익셉션 발생 시 디폴트 생성자 반환
			service = new Domain();
		}
		
		return service;
	}
	/**
	 * 도메인 수정 transaction
	 * @param request 수정할 서비스 모델
	 * @return 정상적으로 수정된 서비스 정보
	 */
	public Domain updateService(Domain request) {
		// 반환하기 위한 서비스 정보 선언
		Domain service = null;
		
		try {
			// 동일한 서비스 이름 검색
			service = serviceRepository.getOne(request.getId());
			
			// 서비스 생성 및 추가 트랜잭션
			service = new Domain(request.getId(), request.getName(),request.getType());
			serviceRepository.save(service);
		}
		catch(Exception e) {
			// 익셉션 발생 시 디폴트 생성자 반환
			service = new Domain();
		}
		
		return service;
	}
	/**
	 * 도메인 id와 일치하는 도메인 정보 반환
	 * @param id 검색할 도메인 id
	 * @return id와 일치하는 도메인 정보
	 */
	public Domain searchServiceById(int id) {
		// 반환하기 위한 서비스 정보 선언
		Domain service = null;
		
		try {
			// Id와 일치하는 서비스 정보 반환
			service = serviceRepository.getOne(id);
		}
		catch(Exception e) {
			// 익셉션 발생 시 디폴트 생성자 반환
			service = new Domain();
		}
		
		return service;
	}
	/**
	 * 도메인 id와 일치하는 도메인 정보를 VO타입으로 반환
	 * @param id 검색할 서비스 id
	 * @return id와 일치하는 서비스 VO
	 */
	public DomainVO searchServiceByIdToVO(int id) {
		// 반환하기 위한 서비스 정보 선언
		DomainVO serviceVo = new DomainVO();
		
		try {
			// Id와 일치하는 서비스 정보 반환
			Domain temp = serviceRepository.getOne(id);
			// 서비스 타입에 따른 enum value 지정
			serviceVo.setType(ServiceType.valueOf(temp.getType()));
		}
		catch(Exception e) {
			// 익셉션 발생 시 디폴트 생성자 반환
			serviceVo = new DomainVO();
		}
		
		return serviceVo;
	}
	/**
	 * 모든 도메인 목록을 읽어와 반환
	 * @return db에 존재하는 모든 도메인 목록
	 */
	public List<Domain> searchAllService() {
		// 반환할 서비스 리스트 선언 및 생성
		List<Domain> serviceList = new ArrayList<>();
		
		try {
			// 모든 서비스 정보 반환
			serviceList = serviceRepository.findAll();			
		}
		catch(Exception e) {
			// 익셉션 발생 시 디폴트 생성자가 추가된 리스트 반환
			if(serviceList.size() == 0) {
				serviceList.add(new Domain());
			}
		}
		
		return serviceList;
	}
	/**
	 * 도메인 이름과 타입에 맞는 도메인 정보 반환
	 * @param name 검색할 도메인 이름
	 * @param type 검색할 도메인 타입
	 * @return name, type과 일치하는 도메인 목록
	 */
	public List<Domain> searchServiceByNameAndType(String name, String type) {
		// 반환할 서비스 리스트 선언 및 생성
		List<Domain> serviceList = new ArrayList<>();
		
		try {
			// 서비스 이름과 타입과 일치하는 서비스 정보 반환
			Domain dto = serviceRepository.findByNameAndType(name, Integer.parseInt(type));
			serviceList.add(dto);
		}
		catch(Exception e) {
			// 익셉션 발생 시 디폴트 생성자가 추가된 리스트 반환
			if(serviceList.size() == 0) {
				serviceList.add(new Domain());
			}
		}
		
		return serviceList;
	}
	/**
	 * 도메인 이름과 일치하는 도메인 목록 반환
	 * @param name 검색할 도메인 이름
	 * @return name과 일치하는 도메인 목록
	 */
	public List<Domain> searchServiceByName(String name) {
		// 반환할 서비스 리스트 선언 및 생성
		List<Domain> serviceList = new ArrayList<>();
		
		try {
			serviceList = serviceRepository.findByName(name);
		}
		catch(Exception e) {
			// 익셉션 발생 시 디폴트 생성자가 추가된 리스트 반환
			if(serviceList.size() == 0) {
				serviceList.add(new Domain());
			}
		}
		
		return serviceList;
	}
	/**
	 * 모든 도메인 목록을 VO타입으로 반환
	 * @return 도메인 VO 목록
	 */
	public List<DomainVO> searchAllServiceToVO(){
		// 반환할 서비스 리스트 선언 및 생성
		List<DomainVO> returnList = new ArrayList<>();
		
		try {
			// 모든 서비스 정보 반환
			List<Domain> list = serviceRepository.findAll();
			
			// 각 서비스 정보를 VO 타입으로 변환
			for(int i = 0; i < list.size(); ++i) {
				DomainVO serviceVo = new DomainVO();
				serviceVo.setId(list.get(i).getId());
				serviceVo.setName(list.get(i).getName());
				
				switch (ServiceType.valueOf(list.get(i).getType())) {
					case GMARKET:
						serviceVo.setType(ServiceType.GMARKET);
						break;
					case AUCTION:
						serviceVo.setType(ServiceType.AUCTION);
						break;
					case COMMON:
						serviceVo.setType(ServiceType.COMMON);
						break;						
					default:
						serviceVo.setType(ServiceType.NONE);
						break;
				}					
				
				// 반환 리스트에 추가
				returnList.add(serviceVo);
			}
		}
		catch (Exception e) {
			// 익셉션 발생 시 디폴트 생성자가 추가된 리스트 반환
			if(returnList.size() == 0) {
				returnList.add(new DomainVO());
			}
		}
		
		return returnList;
	}
	/**
	 * 도메인 이름과 일치하는 도메인 목록을 VO 타입으로 반환
	 * @param name 검색할 도메인 이름
	 * @return name과 일치하는 도메인 목록
	 */
	public List<DomainVO> searchServiceByNameToVO(String name){
		// 서비스 이름과 일치하는 서비스 정보 반환
		List<Domain> list = searchServiceByName(name);
		// 반환할 리스트 선언 및 생성
		List<DomainVO> returnList = new ArrayList<>();
		
		// 각 리스트를 VO 타입으로 변환
		for(int i = 0; i < list.size(); ++i) {
			DomainVO serviceVo = new DomainVO();
			serviceVo.setId(list.get(i).getId());
			serviceVo.setName(list.get(i).getName());
			
			switch (ServiceType.valueOf(list.get(i).getType())) {
				case GMARKET:
					serviceVo.setType(ServiceType.GMARKET);
					break;
				case AUCTION:
					serviceVo.setType(ServiceType.AUCTION);
					break;
				case COMMON:
					serviceVo.setType(ServiceType.COMMON);
					break;						
				default:
					serviceVo.setType(ServiceType.NONE);
					break;
			}
			
			// 반환 리스트에 추가
			returnList.add(serviceVo);
		}
		
		return returnList;
	}
	/**
	 * 도메인 이름 목록 반환
	 * @return 도메인 이름 목록
	 */
	public List<Domain> searchServiceGroup(){
		List<Domain> serviceList = new ArrayList<>();
		
		try {
			serviceList = serviceRepository.findDomainName();
		}
		catch (Exception e) {
			serviceList = new ArrayList<>();
			serviceList.add(new Domain());
		}
		
		return serviceList;
	}
}
