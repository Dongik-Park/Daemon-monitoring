package com.ebaykorea.monitoring.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ebaykorea.monitoring.model.Daemon;
import com.ebaykorea.monitoring.model.DaemonDetailVO;
import com.ebaykorea.monitoring.model.LogCountVO;
import com.ebaykorea.monitoring.model.LogRequest;
import com.ebaykorea.monitoring.model.Domain;
import com.ebaykorea.monitoring.service.DaemonService;
import com.ebaykorea.monitoring.service.DomainService;
import com.ebaykorea.monitoring.service.LogService;
import com.ebaykorea.monitoring.service.MonitoringService;
import com.ebaykorea.monitoring.service.ServerService;
import com.ebaykorea.monitoring.websocket.EchoHandler;

@Controller
@RequestMapping("/monitor")
public class MonitoringController {

	@Autowired
	DomainService serviceService;

	@Autowired
	ServerService serverService;
	
	@Autowired
	DaemonService daemonService ;
	
	@Autowired
	LogService logService;
	
	@Autowired
	MonitoringService monitoringService;

	@RequestMapping(value="/headers", method = RequestMethod.GET)
	public ModelAndView showMonitoringHeaders() {
		
		// 모든 서비스 정보 추출
		List<Domain> domains = serviceService.searchServiceGroup();

		// ModelAndView 속성 설정
		ModelAndView modelAndView = new ModelAndView("headers_fragment")
							.addObject("domains", domains);
		
		return modelAndView;
	}
	
	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public ModelAndView showMonitoringMain(@RequestParam(name="domain", required=false, defaultValue="all") String domain) {
		
		// 반환할 맵 정보
		Map<String, Map<String, List<DaemonDetailVO>>> daemonMap = null;
		
		if(domain.equals("all")) {
			// 서비스와 관계 없이 모든 데몬 정보를 맵 타입으로 불러온다.
			daemonMap = monitoringService.loadDaemonsByAllService();
		}
		else {
			// 서비스명에 해당하는 모든 데몬 정보를 맵 타입으로 불러온다.
			daemonMap = monitoringService.loadDaemonsByServiceName(domain);
		}
		
		// ModelAndView 속성 설정
		ModelAndView modelAndView = new ModelAndView("service")
							.addObject("daemonMap", daemonMap);
		
		return modelAndView;
	}

	@RequestMapping(value = "/main/shipping", method = RequestMethod.GET)
	public ModelAndView showMonitoringShipping() {
		
		// 서비스명에 해당하는 모든 데몬 정보를 맵 타입으로 불러온다.
		Map<String, Map<String, List<DaemonDetailVO>>> daemonMap = monitoringService.loadDaemonsByService("shipping");
		
		// ModelAndView 속성 설정
		ModelAndView modelAndView = new ModelAndView("service")
							.addObject("daemonMap", daemonMap);
		
		return modelAndView;
	}

	@RequestMapping(value = "/main/crex", method = RequestMethod.GET)
	public ModelAndView showMonitoringCrex() {
		
		// 서비스명에 해당하는 모든 데몬 정보를 맵 타입으로 불러온다.
		Map<String, Map<String, List<DaemonDetailVO>>> daemonMap = monitoringService.loadDaemonsByService("crex");
		
		// ModelAndView 속성 설정
		ModelAndView modelAndView = new ModelAndView("service")
							.addObject("daemonMap", daemonMap);
		
		return modelAndView;
	}
	
	@RequestMapping(value = "/daemonDetail", method = RequestMethod.GET)
	public ModelAndView showMonitoringDetail(@RequestParam(name="serverId", required=false, defaultValue="0") int serverId) {
		
		// 서버 Id와 일치하는 데몬 목록을 VO로 불러온다.
		List<DaemonDetailVO> daemons = daemonService.searchDaemonByServerId(serverId);
		
		// 각 서버를 기준으로 로그의 개수 정보를 VO 타입으로 불러온다.
		LogCountVO logCounts = logService.searchLogCountByServerId(serverId);
		
		// ModelAndView 속성 설정
		ModelAndView modelAndView = new ModelAndView("monitoring_sub")
				.addObject("logCount", logCounts)
				.addObject("daemons", daemons);
		
		return modelAndView;
	}
	
	@RequestMapping(value = "/logging", method = RequestMethod.POST)
	@ResponseBody
    public LogRequest Logging(@RequestBody LogRequest logRequest){
		Daemon result = monitoringService.logging(logRequest);
    	if(result != null) {
    		// notify to browser clients
    		EchoHandler.notifyToUser(result);
    	}
        return logRequest;
    }
}
