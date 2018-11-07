package com.example.demo.sss;


import com.alibaba.dubbo.config.annotation.Service;
/**
 * Created by zhaohy on 2018/11/7.
 */
@Service(version = "1.0.0")
public class UserServiceImpl implements UserService {

	@Override
	public String saveUser(User user) {
		user.setUserId(1);
		System.out.println("调用中");
		String a = "sdfsdf";
		return "aaaa";
	}
}
