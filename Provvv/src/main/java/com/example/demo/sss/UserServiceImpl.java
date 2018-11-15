package com.example.demo.sss;


import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;
import com.example.demo.bbb.UserDao;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zhaohy on 2018/11/7.
 */
@Service(version = "1.0.0")
public class UserServiceImpl implements UserService {
	@Autowired
	UserDao userDao;
	@Override
	public String saveUser(User user) {
		user.setUserId(1);
		System.out.println("调用中");
		String a = "sdfsdf";
		// 获取调用方IP地址
		String clientIP = RpcContext.getContext().getRemoteHost();
		System.out.println("调用者ip" + clientIP);
		userDao.test();
		return a;
	}
}
