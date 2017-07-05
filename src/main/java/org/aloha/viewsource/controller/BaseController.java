package org.aloha.viewsource.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by aloha on 2017/7/4.
 */
@RestController
public class BaseController {
	@RequestMapping("/")
	@ResponseBody
	String home() {
		return "Hello World!";
	}

}
