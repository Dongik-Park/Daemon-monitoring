package com.ebaykorea.monitoring.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebaykorea.monitoring.enumeration.ErrorLogType;
import com.ebaykorea.monitoring.model.Daemon;
import com.ebaykorea.monitoring.model.DaemonDetailVO;
import com.ebaykorea.monitoring.model.Log;
import com.ebaykorea.monitoring.model.Server;
import com.ebaykorea.monitoring.repository.DaemonRepository;
import com.ebaykorea.monitoring.repository.LogRepository;
import com.ebaykorea.monitoring.repository.ServerRepository;

@Service
public class DaemonService {
	
	@Autowired
	private ServerRepository serverRepository;
	
	@Autowired
	private DaemonRepository daemonRepository;
	
	@Autowired
	private LogRepository logRepository;
	/**
	 * 데몬 추가 transaction
	 * @param request 추가할 데몬 모델
	 * @return 정상적으로 추가된 데몬 정보
	 */
	public Daemon addDaemon(Daemon request) {
		Daemon daemon = null;
		
		try {
			// 데몬이 포함된 서버 반환
			Server server = serverRepository.getOne(request.getServerId());
			
			// 새로운 데몬 생성
			daemon = new Daemon(server.getId(), 
					request.getName(),
					request.getCycle(), 
					request.getStatus(), 
					request.getEnabled(), 
					request.getNotiType(), 
					request.getRecentTime(), 
					request.getNotiUrl(), 
					request.getDescription(), 
					request.getReceiver());
			
			// 데몬 테이블로 추가 트랜잭션
			daemonRepository.save(daemon);
		}
		catch(Exception e) {
			// 익셉션 발생시 디폴트 생성자로 반환
			daemon = new Daemon();
		}
		
		return daemon;
	}
	/**
	 * 데몬 수정 transaction
	 * @param request 편집할 데몬 모델
	 * @return 정상적으로 수정된 데몬 정보
	 */
	public Daemon updateDaemon(Daemon request) {
		Daemon daemon = null;
		
		try {
			// 데몬 기존 데몬이 존재할 경우
			if(daemonRepository.existsById(request.getId())){
				// 새로운 데몬 생성
				daemon = new Daemon(request.getId(),
						request.getServerId(), 
						request.getName(),
						request.getCycle(), 
						request.getStatus(), 
						request.getEnabled(), 
						request.getNotiType(), 
						request.getRecentTime(), 
						request.getNotiUrl(), 
						request.getDescription(), 
						request.getReceiver());
				
				// 데몬 테이블로 편집 트랜잭션
				daemonRepository.save(daemon);
			}
			else {
				throw new Exception();
			}
		}
		catch(Exception e) {
			// 익셉션 발생시 디폴트 생성자로 반환
			daemon = new Daemon();
		}
		
		return daemon;
	}
	/**
	 * 데몬 enable column 수정 transaction
	 * @param daemonId 수정할 데몬 id
	 * @return 정상적으로 수정된 데몬 정보
	 */
	public Daemon changeDaemonEnabled(int daemonId) {
		Daemon daemon = null;
		
		try {
			// 데몬 검색
			daemon = daemonRepository.getOne(daemonId);
			
			if(daemon != null) {
				// 1과 비교하여 같을 경우 0으로, 0일 경우 1로 셋팅
				daemon.setEnabled((byte)(Byte.compare((byte)1, daemon.getEnabled())));
				// 데몬 테이블로 편집 트랜잭션
				daemonRepository.save(daemon);
			}
			else {
				throw new Exception();
			}
		}
		catch (Exception e) {
			// 익셉션 발생시 디폴트 생성자로 반환
			daemon = new Daemon();
		}
		
		return daemon;
	}
	/**
	 * 데몬 on/off column 수정 transaction
	 * @param id 수정할 데몬 id
	 * @return 정상적으로 수정된 데몬 정보
	 */
	public Daemon deleteDaemon(int id) {
		Daemon daemon = null;
		
		try {
			// 데몬 정보 반환
			daemon = daemonRepository.getOne(id);
			
			if(daemon != null) {
				daemon.setOnOff((byte)1);
				// 데몬 On/Off 컬럼 비활성화
				daemonRepository.save(daemon);
			}
			else {
				daemon = new Daemon();
			}
		}
		catch(Exception e) {
			// 익셉션 발생시 디폴트 생성자로 반환
			daemon = new Daemon();
		}
		
		return daemon;
	}
	/**
	 * 데몬 id와 일치하는 데몬 정보 반환
	 * @param daemonId 검색할 데몬 id
	 * @return id와 일치하는 데몬 정보
	 */
	public Daemon searchDaemonById(int daemonId) {
		Daemon daemon = null;
		
		try {
			// 데몬 정보 추출
			daemon = daemonRepository.getOne(daemonId);
		}
		catch(Exception e) {
			// 익셉션 발생시 디폴트 생성자로 반환
			daemon = new Daemon();
		}
		
		return daemon;
	}
	/**
	 * 서비스 id와 일치하는 데몬 목록을 VO 타입으로 반환
	 * @param serviceId 검색할 서비스 id
	 * @return id와 일치하는 데몬 목록
	 */
	public List<DaemonDetailVO> searchDaemonByService(int serviceId) {
		// 반환할 데몬 리스트 선언 및 생성
		List<DaemonDetailVO> daemonList = new ArrayList<>();
		
		try {
			// 서버스 정보에 따른 서버 리스트 정보 추출
			List<Server> servers = serverRepository.findByServiceId(serviceId);
			
			// 각 서버에 소속된 데몬 리스트 정보 추출
			for(int i = 0; i < servers.size(); ++i) {
				List<Daemon> temp = daemonRepository.findByServerId(servers.get(i).getId());
				// 반환할 데몬 리스트에 데이터 추가
				for(int j = 0; j < temp.size(); ++j) {
					daemonList.add(new DaemonDetailVO(temp.get(j), servers.get(i).getHostName(), ""));
				}
			}
		}
		catch(Exception e) {
			// 익셉션 발생시 디폴트 생성자가 추가된 리스트로 반환
			if(daemonList.size() == 0) {
				daemonList.add(new DaemonDetailVO());
			}
		}
		
		return daemonList;
	}
	/**
	 * 데몬 이름과 일치하는 데몬 정보 반환
	 * @param name 검색할 데몬 이름
	 * @return 이름과 일치하는 데몬 정보
	 */
	public Daemon searchDaemonByName(String name) {
		// 반환할 데몬 선언 및 생성
		Daemon daemon = null;
		
		try {
			// 이름을 통해 데몬 정보 추출
			daemon = daemonRepository.findByName(name);
		}
		catch(Exception e) {
			// 익셉션 발생시 디폴트 생성자로 반환
			daemon = new Daemon();
		}
		
		return daemon;
	}
	/**
	 * 서버 id와 일치하는 데몬 목록을 VO 타입으로 반환
	 * @param serverId 검색할 서비스 id
	 * @return 서비스 id와 일치하는 데목 목록
	 */
	public List<DaemonDetailVO> searchDaemonByServerId(int serverId) {
		// 반환할 데몬 리스트 선언 및 생성
		List<Daemon> daemonList = null;
		List<DaemonDetailVO> returnDaemonList = new ArrayList<>();
		
		try {
			// 서비스정보에 따른 서버 정보 추출
			Server serverDTO = serverRepository.getOne(serverId);
			// 서버 정보에 따른 데몬 리스트 정보 추출
			daemonList = daemonRepository.findByServerId(serverDTO.getId());
			
			// 각 데몬 리스트에 따른 로그 정보 추출
			for(int i = 0; i < daemonList.size(); ++i) {
				// 로그 정보 추출
				Log log = logRepository.findByDaemonIdRecent(daemonList.get(i).getId());
				
				// 로그가 없을 경우
				if(log == null) {
					log = new Log(daemonList.get(i).getId(),daemonList.get(i).getStatus(),"None");
					log.setStatus(0);
				}
				
				// 화면에 뿌려주기 위한 VO타입 생성
				DaemonDetailVO vo = new DaemonDetailVO(daemonList.get(i), serverDTO.getHostName(), log.getErrorTime(), ErrorLogType.values()[log.getStatus()]);
				
				// 리스트에 VO 추가
				returnDaemonList.add(vo);
			}
		}
		catch(Exception e) {
			// 익셉션 발생시 디폴트 생성자가 추가된 리스트로 반환
			if(returnDaemonList.size() == 0) {
				returnDaemonList.add(new DaemonDetailVO());
			}
		}
		
		return returnDaemonList;
	}
	/**
	 * 모든 데몬 목록을 읽어와 반환
	 * @return db에 존재하는 모든 데몬 목록
	 */
	public List<Daemon> searchAllDaemon(){
		// 반환할 데몬 리스트 선언 및 생성
		List<Daemon> daemonList = new ArrayList<>();
		
		try {
			// 모든 데몬 정보 추출
			daemonList = daemonRepository.findAllDaemon();
			if(daemonList == null) {
				daemonList = new ArrayList<>();
			}
		}
		catch(Exception e) {
			// 익셉션 발생시 디폴트 생성자가 추가된 리스트로 반환
			if(daemonList.size() == 0) {
				daemonList.add(new Daemon());
			}
		}
		
		return daemonList;
	}
}
