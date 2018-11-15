package com.example.demo.Server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhaohy on 2018/11/10.
 */
@ChannelHandler.Sharable
public class ServerHandler extends ChannelHandlerAdapter {
	private final static ThreadPoolExecutor executorService = new ThreadPoolExecutor(1, 1, 1L, TimeUnit.MINUTES,
			new ArrayBlockingQueue<Runnable>(1), new ThreadPoolExecutor.DiscardPolicy());

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		System.out.println("SimpleChatClient:" + incoming.remoteAddress() + "在线");
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf = (ByteBuf) msg;
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		String body = new String(req, "utf-8");
//		if(Thread.currentThread().getName().equals("nioEventLoopGroup-1-0")){
//			System.out.println("线程休眠开始" + Thread.currentThread().getName());
//			Thread.sleep(10000);
//			System.out.println("线程休眠结束" + Thread.currentThread().getName());
//		}
//		System.out.println("线程名称" +Thread.currentThread().getName());

		System.out.println("Server :" + body + Thread.currentThread().getName());
		String response = "返回给客户端的响应：" + body + Thread.currentThread().getName();
		ChannelFuture future = ctx.writeAndFlush(Unpooled.copiedBuffer(response.getBytes()));

		future.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture channelFuture) throws Exception {
				System.out.println("返回结果成功");
			}
		});
		Thread.sleep(5000);



		// 第一步 网购厨具
//		Callable<String> onlineShopping = new Callable<String>() {
//			@Override
//			public String call() throws Exception {
//				System.out.println("第一步：下单");
//				System.out.println("第一步：等待送货");
//				Thread.sleep(5000);  // 模拟送货时间
//				System.out.println("第一步：快递送到哒哒哒哒哒哒多多多多所所所所所所SSSDFDDSDF ");
//				return "sdfsdf";
//			}
//		};
//		System.out.println(">>>>>>>>>>>>>>>>");
//		FutureTask<String> task = new FutureTask<String>(onlineShopping);
////		new Thread(task).start();
//
//		executorService.submit(task);
//		String aaa = task.get();

		// future完成后触发监听器, 此处是写完即关闭(短连接). 因此需要关闭连接时, 要通过server端关闭. 直接关闭用方法ctx[.channel()].close()
		//.addListener(ChannelFutureListener.CLOSE);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx)
			throws Exception {
//		System.out.println("读完了");
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable t)
			throws Exception {
		ctx.close();
	}
}
