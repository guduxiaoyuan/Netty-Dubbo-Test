package com.example.demo.Server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by zhaohy on 2018/11/10.
 */
public class Server {
	public static void main(String[] args) throws Exception {
		// 1 创建线两个事件循环组
		// 一个是用于处理服务器端接收客户端连接的
		// 一个是进行网络通信的（网络读写的）
		EventLoopGroup pGroup = new NioEventLoopGroup();
		EventLoopGroup cGroup = new NioEventLoopGroup();
		EventLoopGroup businessGroup  = new NioEventLoopGroup(10);
		// 2 创建辅助工具类ServerBootstrap，用于服务器通道的一系列配置
		ServerBootstrap b = new ServerBootstrap();
		b.group(pGroup, cGroup) // 绑定俩个线程组
				.channel(NioServerSocketChannel.class) // 指定NIO的模式.NioServerSocketChannel对应TCP, NioDatagramChannel对应UDP
				.option(ChannelOption.SO_BACKLOG, 1024) // 设置TCP缓冲区
				.option(ChannelOption.SO_SNDBUF, 32 * 1024) // 设置发送缓冲大小
				.option(ChannelOption.SO_RCVBUF, 32 * 1024) // 这是接收缓冲大小
				.option(ChannelOption.SO_KEEPALIVE, true) // 保持连接

				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel sc) throws Exception {  //SocketChannel建立连接后的管道
						// 3 在这里配置 通信数据的处理逻辑, 可以addLast多个...
						sc.pipeline().addLast( new ServerHandler());
					}
				});

		// 4 绑定端口, bind返回future(异步), 加上sync阻塞在获取连接处
		ChannelFuture cf1 = b.bind(8765).sync();
		//ChannelFuture cf2 = b.bind(8764).sync();   //可以绑定多个端口

		cf1.addListener(new ChannelFutureListener(){
			@Override
			public void operationComplete(ChannelFuture channelFuture) throws Exception {
				System.out.println("channel fucture 监听ing");
			}
		});

		// 5 等待关闭, 加上sync阻塞在关闭请求处
		cf1.channel().closeFuture().sync();

		//cf2.channel().closeFuture().sync();
		pGroup.shutdownGracefully();
		cGroup.shutdownGracefully();
	}


}
