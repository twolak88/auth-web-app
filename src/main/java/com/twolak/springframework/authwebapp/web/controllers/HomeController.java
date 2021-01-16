package com.twolak.springframework.authwebapp.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author twolak
 */
@Controller
@RequestMapping({"/", "/home"})
public class HomeController {

	@RequestMapping(method = RequestMethod.GET)
	public String home() {
		return "home";
	}
}
