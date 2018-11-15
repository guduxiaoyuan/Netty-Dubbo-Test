package com.example.demo.bbb;

import com.alibaba.dubbo.rpc.RpcContext;
import org.springframework.stereotype.Component;

/**
 * Created by zhaohy on 2018/11/8.
 */
@Component
public class UserDaoImpl implements  UserDao {

	@Override
	public void test() {
		String index = RpcContext.getContext().getAttachment("index");
		System.out.println(">>>>>>>>>"+ index);
		System.out.println("111111111");
	}
}
