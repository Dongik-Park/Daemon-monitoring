package com.ebaykorea.monitoring.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import com.ebaykorea.monitoring.enumeration.ServiceType;
import com.ebaykorea.monitoring.model.Daemon;
import com.ebaykorea.monitoring.model.DaemonDetailVO;
import com.ebaykorea.monitoring.model.Domain;
import com.ebaykorea.monitoring.model.DomainVO;
import com.ebaykorea.monitoring.model.Log;
import com.ebaykorea.monitoring.model.LogRequest;
import com.ebaykorea.monitoring.model.Server;
import com.ebaykorea.monitoring.websocket.EchoHandler;

@Service
@EnableScheduling
public class MonitoringService {
	
	@Autowired
	DomainService serviceService;
	
	@Autowired
	ServerService serverService;
	
	@Autowired
	DaemonService daemonService;
	
	@Autowired
	LogService logService;
	/**
	 * Cron 식을 파싱할 parser 객체
	 */
	private CronParser parser;
	/**
	 * 에러 판별 시간 버퍼 ( 분 단위 )
	 */
	private final int CYCLE_BUFFER = 1;
	/**
	 * 주기 에러 메세지
	 */
	private final String CYCLE_ERROR = "The log was not collected normally.";
	/**
	 * 오류 에러 수집 메세지
	 */
	private final String INTERNAL_ERROR = "An error log was collected.";
	/**
	 * Default 생성자 
	 */
	public MonitoringService() {
		// Cron parser 생성을 위한 Default 생성자
		// Cron 정의 객체 생성
		 CronDefinition cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ);
		// 정의 객체 based parser 생성
		parser = new CronParser(cronDefinition);
	}
	
	@Scheduled(cron = "*/60 * * * * *")
	public void monitoringScheduler() {
		// 모든 데몬 정보 추출
		List<Daemon> daemonList = daemonService.searchAllDaemon();
		// Notification daemon : 데몬의 상태 변화가 있을 경우 broadcast할 데몬
		Daemon noti_daemon = null;
		
		// 각 데몬들의 정보에서 로그가 정상적으로 축적되었는지 확인
		for(int i = 0; i < daemonList.size(); ++i) {
			Daemon daemon = daemonList.get(i);
			
			try {

				if(daemon.getEnabled() != 1 || daemon.getCycle().equals("* * * * * *")) {
					continue;
				}
				
				// 현재 시간을 기준으로 Cron 식에 기반하여 데몬이 정상적으로 동작하였는지 체크
				Date current = new Date();
				boolean flag = checkDaemonCron(daemon, current);
				
				// 알림할 데몬 설정
				noti_daemon = daemon;
				
				// 거짓일 경우 데몬 상태 변경, 에러 발생 및 로그 추가 트랜잭션
				if(!flag) {
					// 에러가 이미 발생되어 있는 상태일 경우 종료
					if(daemon.getStatus() == 2) {
						continue;
					}
					
					// 데몬 정보 갱신
					daemon.setStatus(2);
					// 데몬 정보 update
					daemonService.updateDaemon(daemon);
					
					// 로그 추가 트랜잭션 ( ErrorType : CYCLE, 현재 시간으로 로그 에러발생 시간 지정)
					logService.addLog(new Log(daemon.getId(), 2, getCurrentTimeFormat()));
					
					pushNotification(daemon, CYCLE_ERROR);
				}
				else {
					// 정상 동작 세팅
					daemon.setStatus(1);
					// 데몬 정보 update
					daemonService.updateDaemon(daemon);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// 데몬의 상태 변화가 있었을 경우
		if(noti_daemon != null) {
			// 웹 페이지 갱신 broadcast
			EchoHandler.notifyToUser(noti_daemon);	
		}
		
		return;
	}
	
	/**
	 * 모든 도메인의 데몬 정보 반환
	 * @return 모든 도메인의 호스트로 그룹핑 된 데목 목록
	 */
	public Map<String,Map<String, List<DaemonDetailVO>>> loadDaemonsByAllService() {
		// 반환할 맵 생성 및 초기화
		Map<String,Map<String, List<DaemonDetailVO>>> map = new HashMap<>();
		
		try {
			// 도메인 이름과 일치하는 도메인 리스트 반환
			List<Domain> services = serviceService.searchAllService();
			
			// 각 서버마다 데몬 정보 추출
			for(int i = 0; i < services.size(); ++i) {				
				// 1차 key = 도메인 이름.도메인 타입
				String key = services.get(i).getName() +"."+ ServiceType.valueOf(services.get(i).getType());
				
				// 2차 key = 호스트명
				if(map.get(key) == null) {
					map.put(key, new HashMap<>());
				}
				
				// 서버 도메인로부터 서버 목록 반환
				List<Server> servers = serverService.searchServerByServiceId(services.get(i).getId());				
				for(int j = 0; j < servers.size(); ++j) {
					List<DaemonDetailVO> daemons = daemonService.searchDaemonByServerId(servers.get(j).getId());
					map.get(key).put(servers.get(j).getHostName(), daemons);
				}
				
				// key 값으로 정렬하기 위한 임시맵
				Map<String, List<DaemonDetailVO>> sortedMapByKeys = new TreeMap<String,List<DaemonDetailVO>>(map.get(key));
				
				// map에 다시 추가
				map.put(key, sortedMapByKeys);
			}	
		}
		catch(Exception e) {
			// 익셉션 발생 시 디폴트 정보 반환
			map.put("Unnammed",new HashMap<>());
			map.get("Unnammed").put("Unnamed", new ArrayList<>());
		}
		
		return map;
	}
	
	/**
	 * 도메인 이름과 일치하는 데몬 목록 반환 
	 * @param serviceName 검색할 도메인 이름
	 * @return 도메인 이름과 일치하는 호스트로 그룹핑 된 데목 목록
	 */
	public Map<String,Map<String, List<DaemonDetailVO>>> loadDaemonsByService(String serviceName) {
		// 반환할 맵 생성 및 초기화
		Map<String,Map<String, List<DaemonDetailVO>>> map = new HashMap<>();
		
		try {
			// 도메인 이름과 일치하는 도메인 리스트 반환
			List<Domain> services = serviceService.searchServiceByName(serviceName);	
			
			// 각 서버마다 데몬 정보 추출
			for(int i = 0; i < services.size(); ++i) {				
				// 1차 key = 도메인 이름.도메인 타입
				String key = services.get(i).getName() +"."+ ServiceType.valueOf(services.get(i).getType());
				
				// 2차 key = 호스트명
				if(map.get(key) == null) {
					map.put(key, new HashMap<>());
				}
				
				// 서버 도메인로부터 서버 목록 반환
				List<Server> servers = serverService.searchServerByServiceId(services.get(i).getId());
				
				for(int j = 0; j < servers.size(); ++j) {
					List<DaemonDetailVO> daemons = daemonService.searchDaemonByServerId(servers.get(j).getId());
					map.get(key).put(servers.get(j).getHostName(), daemons);
				}
				
				// key 값으로 정렬하기 위한 임시맵
				Map<String, List<DaemonDetailVO>> sortedMapByKeys = new TreeMap<String,List<DaemonDetailVO>>(map.get(key));
				// map에 다시 추가
				map.put(key, sortedMapByKeys);
			}	
		}
		catch(Exception e) {
			// 익셉션 발생 시 디폴트 정보 반환
			map.put("Unnammed",new HashMap<>());
			map.get("Unnammed").put("Unnamed", new ArrayList<>());
		}
		
		return map;
	}
	/**
	 * 도메인 이름과 일치하는 데몬 목록 반환
	 * @param serviceName 검색할 도메인 이름
	 * @return 도메인 이름과 일치하는 데몬 목록
	 */
	public Map<String,Map<String, List<DaemonDetailVO>>> loadDaemonsByServiceName(String domain) {
		// 반환할 맵 생성 및 초기화
		Map<String,Map<String, List<DaemonDetailVO>>> map = new HashMap<>();
		
		try {
			// 도메인 이름과 일치하는 도메인 리스트 반환
			List<Domain> services = serviceService.searchServiceByName(domain);
			
			// 각 서버마다 데몬 정보 추출
			for(int i = 0; i < services.size(); ++i) {				
				// 1차 key = 도메인 이름.도메인 타입
				String key = services.get(i).getName() +"."+ ServiceType.valueOf(services.get(i).getType());
				
				// 2차 key = 호스트명
				if(map.get(key) == null) {
					map.put(key, new HashMap<>());
				}
				
				// 서버 도메인로부터 서버 목록 반환
				List<Server> servers = serverService.searchServerByServiceId(services.get(i).getId());				
				for(int j = 0; j < servers.size(); ++j) {
					List<DaemonDetailVO> daemons = daemonService.searchDaemonByServerId(servers.get(j).getId());
					map.get(key).put(servers.get(j).getHostName(), daemons);
				}
				
				// key 값으로 정렬하기 위한 임시맵
				Map<String, List<DaemonDetailVO>> sortedMapByKeys = new TreeMap<String,List<DaemonDetailVO>>(map.get(key));
				
				// map에 다시 추가
				map.put(key, sortedMapByKeys);
			}	
		}
		catch(Exception e) {
			// 익셉션 발생 시 디폴트 정보 반환
			map.put("Unnammed",new HashMap<>());
			map.get("Unnammed").put("Unnamed", new ArrayList<>());
		}
		
		return map;
	}
	/**
	 * DB에 존재하는 모든 데몬 목록 반환
	 * @return 모든 데몬 목록
	 */
	public List<DaemonDetailVO> loadAllDaemonByService(){
		// 반환할 데몬 리스트 선언 및 생성
		List<DaemonDetailVO> daemons = new ArrayList<>();
		
		try {
			// 모든 도메인 정보 반환
			List<Domain> services = serviceService.searchAllService();
			// 각 도메인와 일치하는 데몬 정보 반환
			for(int i = 0; i < services.size(); ++i) {
				// 도메인 id와 일치하는 데몬 정보 반환
				List<DaemonDetailVO> tempDaemons = daemonService.searchDaemonByService(services.get(i).getId());
				
				// 데몬 리스트에 추가
				for(int j = 0; j < tempDaemons.size(); ++j) {
					daemons.add(tempDaemons.get(j));
				}
			}
		}
		catch(Exception e) {
			// 익셉션 발생시 디폴트 생성자가 추가된 리스트 반환
			if(daemons.size() == 0) {
				daemons.add(new DaemonDetailVO());
			}
		}		
		
		return daemons;				
	}
	/**
	 * 도메인 이름과 일치하는 서버 목록 반환
	 * @param serviceName 검색할 도메인 이름
	 * @return 도메인 이름과 일치하는 서버 목록
	 */
	public Map<String, List<Server>> loadServersByServiceName(String serviceName){
		// 반환할 맵 생성 및 초기화
		HashMap<String, List<Server>> serverMap = new HashMap<>();
		
		try {
			// 도메인 이름과 일치하는 도메인 리스트 반환
			List<DomainVO> list = serviceService.searchServiceByNameToVO(serviceName);
			
			for(int i = 0; i < list.size(); ++i) {
				// 도메인 정보 추출
				DomainVO vo = list.get(i);
				// 도메인 id와 일치하는 서버 리스트 추출
				List<Server> serverList = serverService.searchServerByServiceId(vo.getId());
				// Key : 도메인 명 . 도메인 타입
				// Value : 해당 도메인에 대한 server list 
				serverMap.put(vo.getName()+"."+vo.getType(), serverList);
			}
		}
		catch (Exception e) {
			// 익셉션 발생 시 디폴트 정보 반환
			serverMap.put("Unnammed",new ArrayList<>());
		}
		
		return serverMap;
	}
	/**
	 * Logstash로부터 전송받은 로그 정보에 대해 에러 여부 판별 및 로그와 데몬에 대한 transaction 수행
	 * @param logRequest 에러 여부를 판별한 로그 정보
	 * @return 에러 존재 여부
	 */
	public Daemon logging(LogRequest logRequest) {
		Daemon resultDaemon = null;
		
		try {
			// 데몬 이름을 기준으로 데몬을 찾는다.
			Daemon daemon = daemonService.searchDaemonByName(logRequest.getDaemon());
			
			// 존재하지 않는 데몬일 경우 종료
			if(daemon == null) {
				System.out.println("MonitoringService - logging : " + logRequest.getDaemon() +" not exists.");
				return resultDaemon;
			}	

			// LogRequest 객체의 timestamp를 통해 filebeat의 현재 시간 객체 생성
			ZonedDateTime temp = ZonedDateTime.of(logRequest.getTimestamp(), ZoneId.of("Asia/Seoul"));
			// Date 객체 생성 및 포맷 생성
			Date requestDate = Date.from(temp.toInstant());
			// 오차 버퍼 2분
			Calendar cal = Calendar.getInstance();
			cal.setTime(requestDate);
			// 한국시간 count
			cal.add(Calendar.HOUR_OF_DAY, 9);
			requestDate = new Date(cal.getTimeInMillis());
			SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 	
			String requestTimeFormat = dt.format(requestDate);
			
			// log에 ERROR 정보가 있을 경우 로그 추가
			if(logRequest.getLog().contains("ERROR")) {
				System.out.println("MonitoringService - logging : " + logRequest.getDaemon() +" error detected.");
				
				// 로그 추가 트랜잭션 ( ErrorType : INTERNAL )
				logService.addLog(new Log(daemon.getId(), 1, requestTimeFormat));
				
				// 정상 상태인 경우 에러 동작
				if(daemon.getStatus() == 1) {
					daemon.setStatus(2);
					// 데몬 정보 update
					daemonService.updateDaemon(daemon);
				}
				
				// 에러 로그 푸쉬
				pushNotification(daemon, INTERNAL_ERROR);
			
				return daemon;
			}
			else {
				System.out.println("MonitoringService - logging : " + logRequest.getDaemon() +" normally executed.");
				// 에러 상태인 경우 정상 동작
				daemon.setStatus(1);
				daemon.setRecentTime(requestTimeFormat);
				
				// 데몬 정보 update 및 결과 지정
				daemonService.updateDaemon(daemon);
				resultDaemon = daemon;
			}			
			
		}
		catch(Exception e){
			
		}
		
		return resultDaemon;
	}
	/**
	 * 데몬의 크론식과 매개변수 시간과의 차이가 오차범위(CYCLE_BUFFER) 안에 포함되는지 판별
	 * @param daemon 비교할 데몬 정보
	 * @param current 비교할 시간 정보
	 * @return 현재 시간 < 다음 실행 예정 시간 = 데몬 최근 실행시간 + 크론식을 더한 시간
	 */
	private boolean checkDaemonCron(Daemon daemon, Date current) {
		
		boolean flag = true;
		// 참 조건 : 현재 시간 < 다음 실행 예정 시간 = 데몬 최근 실행시간 + 크론식을 더한 시간 
		try {
			// LocalDateTime 객체로 변환하기 위한 formatter
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");				
			//convert String to LocalDate
			LocalDateTime localDateTime = LocalDateTime.parse(daemon.getRecentTime(), formatter);
			// Daemon의 Cycle을 기준으로 생성
			Cron testCron = parser.parse(daemon.getCycle());	
			// 데몬의 timestamp로 객체 생성
			ZonedDateTime daemonDate = ZonedDateTime.of(localDateTime, ZoneId.of("Asia/Seoul"));
			// 데몬 크론식의 Date 객체 생성
			Date originDate = Date.from(daemonDate.toInstant());
			// 데몬의 크론식을 통해 실행시간 객체 생성
			ExecutionTime executionTime = ExecutionTime.forCron(testCron);
			// 다음 실행시간 객체 생성
			ZonedDateTime nextExecution = executionTime.nextExecution(daemonDate).get();
			// 다음 실행시간 객체 생성
			Date nextDate = Date.from(nextExecution.toInstant());	
			
			// 크론식을 더한 시간과 원래 origin 시간과의 차이 계산 = 정상 범위 확인
			//long cron_min = (nextDate.getTime() - originDate.getTime()) / 60000;
			
			// 오차 버퍼 CYCLE_BUFFER분
			Calendar cal = Calendar.getInstance();
			cal.setTime(nextDate);
			// 크론식 시간 + CYCLE_BUFFER분 더하기
			//cal.add(Calendar.MINUTE, (int)cron_min + CYCLE_BUFFER);
			cal.add(Calendar.MINUTE, CYCLE_BUFFER);
			nextDate = new Date(cal.getTimeInMillis());
			
			// 데몬이 정상적으로 작동한 경우 (현재 시간 < 다음 실행 예정 시간 = 데몬 최근 실행시간 + 크론식을 더한 시간 + 오차 버퍼 시간-1분)
			if(current.compareTo(nextDate) <= 0) {
				flag = true;
			}
			// 데몬이 비정상적으로 작동한 경우 
			else {
				flag = false;
				System.err.println("[ERROR-CYCLE]" + daemon.getName() +" : "+ current +", "+nextDate);
			}
		}
		catch(Exception e) { 
			e.printStackTrace();
		}
		
		return flag;
	}
	/**
	 * 데몬에 지정된 URL로 SLACK 오류 메세지 전달
	 * @param daemon push할 데몬의 정보
	 * @param errorMessage push할 에러 메세지 내용
	 */
	public void pushNotification(Daemon daemon, String errorMessage) {
		
		try {
			// URL 객체 생성
			URL url = new URL(daemon.getNotiUrl());
			// Connection객체 생성 및 세팅
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST"); // 보내는 타입
			conn.setRequestProperty("Accept-Language", "ko-kr,ko;q=0.8,en-us;q=0.5,en;q=0.3");
			
			// 데이터 설정
			JSONObject text = new JSONObject();
			text.put("text", "[ERROR] " + daemon.getName()+" : " +errorMessage);
			
			// 전송스트립 객체 생성
			OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
			osw.write(text.toString());
			osw.flush();
			
			// 응답
			BufferedReader br = null;
			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			
			String line = null;
			while ((line = br.readLine()) != null) {
				if(line.contains("ok")) {
					System.out.println(daemon.getName() + " error successfully pushed to Slack.");
				}
			}
			// 닫기
			osw.close();
			br.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 현재 시간을 'yyyy-MM-dd HH:mm:ss' 포맷으로 반환
	 * @return 현재 시간 포맷
	 */
	private String getCurrentTimeFormat() {
		Date current = new Date();
		// 오차 버퍼 1분
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 	
		return dt.format(current);
	}
}
