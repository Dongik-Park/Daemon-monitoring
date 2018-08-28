package com.ebaykorea.monitoring.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebaykorea.monitoring.enumeration.DateFormat;
import com.ebaykorea.monitoring.model.Daemon;
import com.ebaykorea.monitoring.model.Log;
import com.ebaykorea.monitoring.model.LogCountVO;
import com.ebaykorea.monitoring.model.Server;
import com.ebaykorea.monitoring.repository.DaemonRepository;
import com.ebaykorea.monitoring.repository.LogRepository;
import com.ebaykorea.monitoring.repository.ServerRepository;

@Service
public class LogService {
	
	@Autowired
	private LogRepository logRepository;
	
	@Autowired
	private DaemonRepository daemonRepository;
	
	@Autowired
	private ServerRepository serverRepository;
	/**
	 * 로그 추가 transaction
	 * @param request 추가할 로그 모델
	 * @return 정상적으로 추가된 로그 정보
	 */
	public Log addLog(Log request) {
		Log log = null;
		
		try {
			// 데몬 정보 추출
			Daemon daemon = daemonRepository.getOne(request.getDaemonId());
			
			// 데몬이 존재할 경우
			if(daemon != null) {
				int daemonId = daemon.getId();
				int status = request.getStatus();
				String errorTime = request.getErrorTime();
				// 로그 생성 및 트랜잭션
				log = new Log(daemonId, status, errorTime);
				
				logRepository.save(log);
			}
		} catch (Exception e) {
			// 익셉션 발생할 경우 디폴트 생성 반환
			log = new Log();
		}
		
		return log;
	}
	/**
	 * 로그 편집 transaction
	 * @param request 추가할 로그 모델
	 * @return 정상적으로 추가된 로그 정보
	 */
	public Log updateLog(Log request) {
		Log log = null;		
		
		try {
			// 데몬 정보 추출
			Daemon daemon = daemonRepository.getOne(request.getDaemonId());
			
			// 아이디와 일치하는 로그 반환
			log = logRepository.getOne(request.getId());
			
			// 로그 생성 및 수정 트랜잭션
			log = new Log(
					log.getId(),
					daemon.getId(),
					request.getStatus(),
					request.getErrorTime()
				);
			
			logRepository.save(log);
		} catch (Exception e) {
			// 익셉션 발생할 경우 디폴트 생성 반환
			log = new Log();
		}
		
		return log;
	}
	/**
	 * 로그 삭제 transaction
	 * @param request 삭제할 데몬 모델
	 * @return 정상적으로 삭제된 로그 정보
	 */
	public Log deleteLog(Log request) {
		Log log = null;
		
		try {
			// 로그 정보 추출
			log = logRepository.getOne(request.getId());
			// 로그 삭제 트랝개션
			logRepository.deleteById(log.getId());				
		} 
		catch (Exception e) {
			// 익셉션 발생할 경우 디폴트 생성 반환
			log = new Log();
		}
		
		return log;
	}
	/**
	 * 서버 id와 일치하는 데몬 목록의 로그 개수 정보 반환
	 * @param serverId 검색할 서버 id
	 * @return 오늘, 1주일, 3개월, 전체 로그 개수 정보 모델
	 */
	public LogCountVO searchLogCountByServerId(int serverId) {
		LogCountVO vo = null;
		
		try {
			// 서버 정보 추출
			Server server = serverRepository.getOne(serverId);
			// 서버 id와 일치하는 데몬 리스트 반환
			List<Daemon> daemons = daemonRepository.findByServerId(server.getId());
			
			// 누적변수 선언
			int t_cnt = 0;
			int w_cnt = 0;
			int q_cnt = 0;
			int total_cnt = 0;
			
			// 각 데몬에 따른 로그 결과 수집
			for(int i = 0; i < daemons.size(); ++i) {
				int daemon_id = daemons.get(i).getId();
				// 데몬 아이디와 일치하는 로그 반환
				List<Log> log = logRepository.findByDaemonId(daemon_id);
				
				//각 시간에 따른 로그 리스트 추출
				List<Log> t_error = logRepository.findByDaemonIDAndTime(daemon_id, dateFormat(DateFormat.TODAY), dateFormat(DateFormat.TOMMORROW));
				List<Log> w_error = logRepository.findByDaemonIDAndTime(daemon_id, dateFormat(DateFormat.WEEK_AGO), dateFormat(DateFormat.TOMMORROW));
				List<Log> q_error = logRepository.findByDaemonIDAndTime(daemon_id, dateFormat(DateFormat.QUARTER_AGO), dateFormat(DateFormat.TOMMORROW));			
				
				// 데이터 누적
				t_cnt += t_error.size();
				w_cnt += w_error.size();
				q_cnt += q_error.size();
				total_cnt += log.size();
			}
			
			// 반환 vo에 데이터 추가
			vo = new LogCountVO(
					t_cnt, 
					w_cnt, 
					q_cnt, 
					total_cnt,
					serverId);
		}
		catch(Exception e) {
			// 익셉션 발생할 경우 디폴트 생성 반환
			vo = new LogCountVO();
		}
		
		return vo;
	}
	/**
	 * 서버 id와 일치하는 데몬 목록의 로그 목록 반환
	 * @param serverId 검색할 서버 id
	 * @return 서버 id와 일치하는 로그 목록
	 */
	public  List<Log> searchLogByServerId(String serverId) {
		// 반환할 리스트 선언 및 초기화
		List<Log> list = new ArrayList<>();
		
		try {
			// 서버 정보 반환
			Server server = serverRepository.getOne(Integer.parseInt(serverId));
			// 서버 아이디와 일치하는 데몬 리스트 반환
			List<Daemon> daemons = daemonRepository.findByServerId(server.getId());
			
			// 각 데몬과 일치하는 로그 정보 추가
			for(int i = 0; i < daemons.size(); ++i) {
				int daemon_id = daemons.get(i).getId();
				
				// 데몬 아이디와 일치하는 로그 리스트 반환
				List<Log> log = logRepository.findByDaemonId(daemon_id);
				
				// 데몬 아이디와 일치하는 로그 정보 추가
				for(int j = 0; j < log.size(); ++j) {
					list.add(log.get(j));
				}
				
			}
		}
		catch(Exception e) {
			// 익셉션 발생할 경우 디폴트 생성자가 추가된 리스트 반환
			if(list.size() == 0) {
				list.add(new Log());
			}
		}
		
		return list;
	}
	/**
	 * 데몬 이름과 일치하는 로그 목록 반환
	 * @param daemonName 검색할 데몬 이름
	 * @return 데몬 이름과 일치하는 로그 목록
	 */
	public List<Log> searchLogByDaemonName(String daemonName) {
		// 반환할 리스트 선언 및 초기화
		List<Log> list = new ArrayList<>();
		
		try {
			// 데몬 이름과 일치하는 데몬 반환
			Daemon daemon = daemonRepository.findByName(daemonName);
			// 데몬 아이디와 일치하는 로그 정보 추가
			list = logRepository.findByDaemonId(daemon.getId());
		}
		catch(Exception e) {
			// 익셉션 발생할 경우 디폴트 생성자가 추가된 리스트 반환
			if(list.size() == 0) {
				list.add(new Log());
			}
		}
		
		return list;
	}
	/**
	 * 데몬 id와 일치하는 로그 목록 반환
	 * @param id 검색할 데몬 id
	 * @return 데몬 id와 일치하는 로그 목록
	 */
	public List<Log>  searchLogByDaemonId(int id) {
		List<Log> list = null;
		
		try{
			// 데몬 아이디와 일치하는 로그 리스트 반환
			list = logRepository.findByDaemonId(id);
		}
		catch(Exception e) {
			// 익셉션 발생할 경우 디폴트 생성자가 추가된 리스트 반환
			if(list.size() == 0) {
				list.add(new Log());
			}
		}
		
		return list;
	}
	/**
	 * 현재 시간을 기준으로 DateFormat과 일치하는 날짜 format 반환 
	 * @param date 반환할 DateFormat 타입
	 * @return date와 일치하는 날짜 format 문자열
	 */
	private String dateFormat(DateFormat date) {
		String format = "";
		
		// 캘린더 객체 default 설정
		Calendar calendar = new GregorianCalendar(Locale.KOREA);
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	    // 데이터 포맷 설정
		java.text.SimpleDateFormat sdf = 
		     new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		switch (date) {
			case TOMMORROW:
				// 다음날 객체
				calendar.add(Calendar.DAY_OF_MONTH, 1);
				format = sdf.format(calendar.getTime());
				break;	
			case WEEK_AGO:
				// 일주일 이전 객체
				calendar.add(Calendar.DAY_OF_MONTH, -7); 
			    format = sdf.format(calendar.getTime());
				break;				
			case QUARTER_AGO:
			    // 3개월 이전 객체
				calendar.add(Calendar.MONTH, -3); 
				format = sdf.format(calendar.getTime());
				break;				
			default:
				// 오늘 객체
				format = sdf.format(calendar.getTime());
				break;
		}
		
		return format;
	}
}
