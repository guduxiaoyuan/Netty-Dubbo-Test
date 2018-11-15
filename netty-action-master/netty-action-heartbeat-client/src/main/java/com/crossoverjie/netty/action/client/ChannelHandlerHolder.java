package com.crossoverjie.netty.action.client;

/**
 * Created by zhaohy on 2018/11/15.
 */

import io.netty.channel.ChannelHandler;

public interface ChannelHandlerHolder {
	ChannelHandler[] handlers();
}
