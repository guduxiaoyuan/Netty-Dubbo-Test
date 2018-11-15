package com.dubbo.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.common.utils.Assert;
import com.alibaba.dubbo.rpc.RpcContext;
import com.example.demo.sss.User;
import com.example.demo.sss.UserService;

/**
 * Created by zhaohy on 2018/11/7.
 */
@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/test/{name}")
	public String sayHello(@PathVariable("name") String name) {
		User u = new User();
		System.out.println("调用");
		// 隐式传参，后面的远程调用都会隐式将这些参数发送到服务器端，类似cookie，用于框架集成，不建议常规业务使用
		RpcContext.getContext().setAttachment("index", "1123123123");
		String result = userService.saveUser(u);
		Object o = null;
//		Assert.notNull(o, new RuntimeException());
		String serverIP = RpcContext.getContext().getRemoteHost();
		int port = RpcContext.getContext().getRemotePort();
		System.out.println(serverIP + ":" + port);

		return result;
	}
}
