package com.example.demo.Controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.example.demo.sss.User;
import com.example.demo.sss.UserService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhaohy on 2018/11/7.
 */
@RestController
public class UserController {
	@Reference(version = "1.0.0")
	private UserService userService;

	@RequestMapping(value="/test/{name}")
	public String sayHello(@PathVariable("name") String name){
		User u = new User();
		String result = userService.saveUser(u);
		return result;
	}
}
