package org.aloha.viewsource.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by aloha on 2017/7/4.
 */
@Controller
public class BaseController {

	@RequestMapping("/")
	public String home() {
		return "index";
	}

}
