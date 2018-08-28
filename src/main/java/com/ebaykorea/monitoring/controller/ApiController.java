package com.ebaykorea.monitoring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ebaykorea.monitoring.model.Daemon;
import com.ebaykorea.monitoring.model.Log;
import com.ebaykorea.monitoring.model.LogRequest;
import com.ebaykorea.monitoring.model.Server;
import com.ebaykorea.monitoring.model.Domain;
import com.ebaykorea.monitoring.service.DaemonService;
import com.ebaykorea.monitoring.service.DomainService;
import com.ebaykorea.monitoring.service.LogService;
import com.ebaykorea.monitoring.service.MonitoringService;
import com.ebaykorea.monitoring.service.ServerService;
import com.ebaykorea.monitoring.websocket.EchoHandler;


@RestController
@RequestMapping("/api")
public class ApiController {
	
	@Autowired
	private DomainService serviceService;
	
	@Autowired
	private ServerService serverService;
	
	@Autowired
	private DaemonService daemonService;
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private MonitoringService monitoringService;

	// ----- Add area start -----

	@RequestMapping(value = "/service", method = RequestMethod.POST)
	@ResponseBody
	public Domain addService(Domain request) {
		// server add transaction
		Domain resultService = serviceService.addService(request);
		return resultService;
	}
	
	@RequestMapping(value = "/server", method = RequestMethod.POST)
	@ResponseBody
	public Server addServer(Server request) {	
		// server add transaction
		Server resultServer = serverService.addServer(request);	
		return resultServer;
	}
	
	@RequestMapping(value = "/daemon", method = RequestMethod.POST)
	@ResponseBody
	public Daemon addDaemon(Daemon request){
		// daemon add transaction
		Daemon resultDaemon = daemonService.addDaemon(request);	
		// notify to browser clients
		EchoHandler.notifyToUser(request);
		// return transaction result
		return resultDaemon;
	}

	@RequestMapping(value = "/log", method = RequestMethod.POST)
	@ResponseBody
	public Log addLog(@RequestBody Log request) {
		return logService.addLog(request);
	}
	
	// ----- Search area start -----
	
	@RequestMapping(value = "/service", method = RequestMethod.GET)
	@ResponseBody
	public List<Domain> searchService(@RequestParam(name="name", required=false, defaultValue="Unnamed") String name,
			@RequestParam(name="type", required=false, defaultValue="0") String type) {
		return serviceService.searchServiceByNameAndType(name, type);
	}	

	@RequestMapping(value = "/server", method = RequestMethod.GET)
	@ResponseBody 
	public Server searchServer(@RequestParam(name="name", required=false, defaultValue="Unnamed") String name) {
		return serverService.searchServerByName(name);
	}
	
	@RequestMapping(value = "/daemon", method = RequestMethod.GET)
	@ResponseBody
	public Daemon searchDaemon(@RequestParam(name="name", required=false, defaultValue="Unnamed") String name,
			@RequestParam(name="hostName", required=false, defaultValue="Unnamed") String hostName, 
			@RequestParam(name="serviceId", required=false, defaultValue="0") String serviceId, 
			@RequestParam(name="searchType", required=false, defaultValue="Unnamed") String searchType) {
		return daemonService.searchDaemonByName(hostName);
	}
	
	
	@RequestMapping(value = "/log", method = RequestMethod.GET)
	@ResponseBody
	public List<Log> searchLog(@RequestParam(name="name", required=false, defaultValue="Unnamed") String daemonName,
			@RequestParam(name="serverId", required=false, defaultValue="Unnamed") String serverId,
			@RequestParam(name="searchType", required=false, defaultValue="Unnamed") String searchType) {
		return logService.searchLogByDaemonName(daemonName);
	}
	
	// ----- Update area start -----

	@RequestMapping(value = "/service", method = RequestMethod.PUT)
	@ResponseBody
	public Domain updateService(@RequestBody Domain request) {
		return serviceService.updateService(request);
	}	
	

	@RequestMapping(value = "/server", method = RequestMethod.PUT)
	@ResponseBody
	public Server updateServer(@RequestBody Server request) {
		return serverService.updateServer(request);
	}	
	
	@RequestMapping(value = "/daemon", method = RequestMethod.PUT)
	@ResponseBody
	public Daemon updateDaemon(Daemon request) {
		// daemon update transaction
		Daemon resultDaemon = daemonService.updateDaemon(request);
		// notify to browser clients
		EchoHandler.notifyToUser(request);
		// return transaction result
		return resultDaemon;
	}
	
	@RequestMapping(value = "/log", method = RequestMethod.PUT)
	@ResponseBody
	public Log updateLog(@RequestBody Log request) {
		return logService.updateLog(request);
	}	
	
	// ----- Delete area start -----

	@RequestMapping(value = "/daemon", method = RequestMethod.DELETE)
	@ResponseBody
	public Daemon deleteDaemon(@RequestBody Daemon request) {
		return daemonService.deleteDaemon(request.getId());
	}	
	
	@RequestMapping(value = "/log", method = RequestMethod.DELETE)
	@ResponseBody
	public Log deleteLog(@RequestBody Log request) {
		return logService.deleteLog(request);
	}	
	
	// ----- Logstash test area -----

	@RequestMapping(value = "/logstash", method = RequestMethod.POST)
	@ResponseBody
    public LogRequest Logging(@RequestBody LogRequest logRequest){
    	System.out.println(logRequest);
    	if(monitoringService.logging(logRequest) != null) {
    		// notify to browser clients
    		EchoHandler.notifyToUser(new Daemon());
    	}
        return logRequest;
    }
}
