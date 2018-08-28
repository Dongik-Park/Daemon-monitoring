package com.ebaykorea.monitoring.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HomeController {

	@RequestMapping("/")
    public ModelAndView home(){
		return new ModelAndView("redirect:" + "/main");
	}
	
	@RequestMapping("/main")
    public ModelAndView mainProcess(){
		return new ModelAndView("monitoring_main");
	}

	@RequestMapping("/admin")
    public ModelAndView adminProcess(){
		return new ModelAndView("monitoring_admin");
	}
	
	@RequestMapping("/websocket")
	public ModelAndView websocketProcess() {
		return new ModelAndView("websocket_test");
	}
}
