package com.crossoverjie.netty.action.client;

import com.alibaba.fastjson.JSON;
import com.crossoverjie.netty.action.client.init.CustomerHandleInitializer;
import com.crossoverjie.netty.action.client.vo.req.GoogleProtocolVO;
import com.crossoverjie.netty.action.common.pojo.CustomProtocol;
import com.crossoverjie.netty.action.common.protocol.BaseRequestProto;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.HashedWheelTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * Function:
 *
 * @author crossoverJie
 *         Date: 22/05/2018 14:19
 * @since JDK 1.8
 */
@Component
public class HeartbeatClient {

	private final static Logger LOGGER = LoggerFactory.getLogger(HeartbeatClient.class);

	private EventLoopGroup group = new NioEventLoopGroup();
	private static Bootstrap boot;

	protected final HashedWheelTimer timer = new HashedWheelTimer();

	private final ConnectorIdleStateTrigger idleStateTrigger = new ConnectorIdleStateTrigger();

	@Value("${netty.server.port}")
	private int nettyPort;

	@Value("${netty.server.host}")
	private String host;

	private SocketChannel channel;

	@PostConstruct
	public void start() throws InterruptedException {
//        Bootstrap bootstrap = new Bootstrap();
//        bootstrap.group(group)
//                .channel(NioSocketChannel.class)
//                .handler(new CustomerHandleInitializer())
//        ;
//
//        ChannelFuture future = bootstrap.connect(host, nettyPort).sync();
//        if (future.isSuccess()) {
//            LOGGER.info("启动 Netty 成功");
//        }
//        channel = (SocketChannel) future.channel();
		EventLoopGroup group = new NioEventLoopGroup();
		boot = new Bootstrap();
		boot.group(group)
				.channel(NioSocketChannel.class)
				.handler(new CustomerHandleInitializer());

		final ConnectionWatchdog watchdog = new ConnectionWatchdog(boot, timer, nettyPort, host, true) {
			public ChannelHandler[] handlers() {
				return new ChannelHandler[]{
						this,
						new IdleStateHandler(0, 4, 0, TimeUnit.SECONDS),
						idleStateTrigger,
						new HeartBeatClientHandler()
				};
			}
		};

		ChannelFuture future;
		try {
			synchronized (boot) {//进行连接
				boot.handler(new ChannelInitializer<Channel>() {
					//初始化channel
					@Override
					protected void initChannel(Channel ch) throws Exception {
						ch.pipeline().addLast(watchdog.handlers());
					}
				});
				future = boot.connect(host, nettyPort);
			}

			// 以下代码在synchronized同步块外面是安全的
			future.sync();
		} catch (Throwable t) {
			LOGGER.info("异常=============");
		}
	}


	/**
	 * 发送消息
	 *
	 * @param customProtocol
	 */
	public void sendMsg(CustomProtocol customProtocol) {
		ChannelFuture future = channel.writeAndFlush(customProtocol);
		future.addListener((ChannelFutureListener) channelFuture ->
				LOGGER.info("客户端手动发消息成功={}", JSON.toJSONString(customProtocol)));

	}

	/**
	 * 发送消息字符串
	 *
	 * @param msg
	 */
	public void sendStringMsg(String msg) {
		ByteBuf message = Unpooled.buffer(msg.getBytes().length);
		message.writeBytes(msg.getBytes());
		ChannelFuture future = channel.writeAndFlush(message);
		future.addListener((ChannelFutureListener) channelFuture ->
				LOGGER.info("客户端手动发消息成功={}", msg));

	}

	/**
	 * 发送 Google Protocol 编解码字符串
	 *
	 * @param googleProtocolVO
	 */
	public void sendGoogleProtocolMsg(GoogleProtocolVO googleProtocolVO) {

		BaseRequestProto.RequestProtocol protocol = BaseRequestProto.RequestProtocol.newBuilder()
				.setRequestId(googleProtocolVO.getRequestId())
				.setReqMsg(googleProtocolVO.getMsg())
				.build();


		ChannelFuture future = channel.writeAndFlush(protocol);
		future.addListener((ChannelFutureListener) channelFuture ->
				LOGGER.info("客户端手动发送 Google Protocol 成功={}", googleProtocolVO.toString()));

	}
}
