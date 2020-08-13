package com.springbootdemo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.springbootdemo.model.StatusUpdate;
import com.springbootdemo.service.StatusUpdateService;

@Controller
public class PageController {
	
	@Autowired
	private StatusUpdateService statusUpdateService;
	
	@Value("${message.error.forbidden}")
	private String forbiddenMessage;
	
	@RequestMapping("/")
	ModelAndView home(ModelAndView modelAndView)
	{
		StatusUpdate statusUpdate = statusUpdateService.getLatest();
		modelAndView.getModel().put("statusUpdate", statusUpdate);
		
		modelAndView.setViewName("app.homepage");
		return modelAndView;
	}
	
	@RequestMapping("/about")
	String about()
	{
		return "app.about";
	}
	
	@RequestMapping("/403")
	ModelAndView invalidUser(ModelAndView modelAndView)
	{		
		modelAndView.getModel().put("message", forbiddenMessage);
		modelAndView.setViewName("app.message");
		return modelAndView;
	}
}
