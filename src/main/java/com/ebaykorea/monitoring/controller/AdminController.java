package com.ebaykorea.monitoring.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ebaykorea.monitoring.enumeration.ServiceType;
import com.ebaykorea.monitoring.model.Daemon;
import com.ebaykorea.monitoring.model.DaemonDetailVO;
import com.ebaykorea.monitoring.model.Server;
import com.ebaykorea.monitoring.model.Domain;
import com.ebaykorea.monitoring.model.DomainVO;
import com.ebaykorea.monitoring.service.DaemonService;
import com.ebaykorea.monitoring.service.DomainService;
import com.ebaykorea.monitoring.service.LogService;
import com.ebaykorea.monitoring.service.MonitoringService;
import com.ebaykorea.monitoring.service.ServerService;
import com.ebaykorea.monitoring.websocket.EchoHandler;

@Controller
@RequestMapping("/monitor/admin")
public class AdminController {
	
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
	
	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public ModelAndView showAdminMain() {
		
		// 특정 서비스의 모든 데몬 목록 반환
		List<DaemonDetailVO> daemons = monitoringService.loadAllDaemonByService();
		
		// ModelAndView 속성 설정
		ModelAndView modelAndView = new ModelAndView("daemons")
									.addObject("daemons", daemons);
		
		return modelAndView;
	}

	@RequestMapping(value = "/service", method = RequestMethod.GET)
	public ModelAndView showAdminService() {
		
		// Enum 값의 서비스타입을 불러온다.
		List<DomainVO> serviceList = new ArrayList<>();
		for(ServiceType type : ServiceType.values()) {
			if(type != ServiceType.NONE) {
				serviceList.add(new DomainVO(type));
			}
		}
		
		// ModelAndView 속성 설정
		ModelAndView modelAndView = new ModelAndView("service_modal_body")
									.addObject("serviceList", serviceList);
		
		return modelAndView;
	}

	@RequestMapping(value = "/server", method = RequestMethod.GET)
	public ModelAndView showAdminServer() {
		
		// All service list
		List<DomainVO> serviceList = serviceService.searchAllServiceToVO();
		
		// ModelAndView 속성 설정
		ModelAndView modelAndView = new ModelAndView("server_modal_body")
									.addObject("serviceList", serviceList);		
		
		return modelAndView;
	}
	
	@RequestMapping(value = "/serverList", method = RequestMethod.GET)
	@ResponseBody
	public List<Server> showAdminServerListByServiceId(@RequestParam(name="serviceId", required=false, defaultValue="0") int serviceId) {
		
		// 특정 서비스의 서버 목록을 불러온다.
		List<Server> servers = serverService.searchServerByServiceId(serviceId);
		
		return servers;
	}
	
	@RequestMapping(value = "/daemon", method = RequestMethod.GET)
	public ModelAndView showAdminDaemon() {
		
		// 모든 서비스의 목록을 불러온다.
		List<DomainVO> serviceList = serviceService.searchAllServiceToVO();
		
		// ModelAndView 속성 설정
		ModelAndView modelAndView = new ModelAndView("daemon_modal_body")
									.addObject("serviceList",serviceList);
		
		return modelAndView;
	}
	
	@RequestMapping(value = "/daemon/edit", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView editAdminDaemon(@RequestParam(name="daemonId", required=false, defaultValue="0") int daemonId) {
		
		// Id를 이용하여 데몬 정보 불러온다.
		Daemon daemon = daemonService.searchDaemonById(daemonId);
		
		// 데몬에 존재하는 서버 Id를 통해 서버 정보를 불러온다.
		Server server = serverService.searchServerByServerId(daemon.getServerId());
		
		// 서버에 존재하는 서비스 Id를 통해 서비스 정보를 불러온다.
		Domain service = serviceService.searchServiceById(server.getServiceId());
		
		// 각 정보를 종합하여 VO로 생성한다.
		DaemonDetailVO daemonVO = new DaemonDetailVO(daemon, server.getHostName(), "", service.getName()+"."+service.getType());
		
		// 모든 서비스 정보를 VO로 생성한다.
		List<DomainVO> serviceList = serviceService.searchAllServiceToVO();
		
		// 각 서비스에 해당하는 서버 정보를 맵 타입으로 불러온다.
		Map<String, List<Server>> serverMap = monitoringService.loadServersByServiceName(server.getHostName());
		
		// ModelAndView 속성 설정
		ModelAndView modelAndView = new ModelAndView("daemon_edit_modal_body")
									.addObject("serviceList", serviceList)
									.addObject("map", serverMap)
									.addObject("daemon", daemonVO);		
		
		return modelAndView;
	}
	
	@RequestMapping(value = "/daemon/enable", method = RequestMethod.PUT)
	@ResponseBody
	public String changeDaemonEnabled(int daemonId) {
		// Change daemon enabled
		Daemon daemon = daemonService.changeDaemonEnabled(daemonId);
		
		if(daemon.getId() != 0) {
			// notify to browser clients
			EchoHandler.notifyToUser(daemon);
		}
		
		return daemon.getName();
	}

	@RequestMapping(value = "/daemon", method = RequestMethod.DELETE)
	@ResponseBody
	public String deleteDaemon(@RequestParam(name="daemonId", required=false, defaultValue="0") int daemonId) {
		// Change daemon onoff
		Daemon daemon = daemonService.deleteDaemon(daemonId);
		
		if(daemon.getId() != 0) {
			// notify to browser clients
			EchoHandler.notifyToUser(daemon);
		}
		
		return daemon.getName();
	}	
}
