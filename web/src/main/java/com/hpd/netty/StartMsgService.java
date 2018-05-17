package com.hpd.netty;


import com.hpd.netty.service.core.ServerThread;
import com.hpd.netty.tools.IniConf;
import io.netty.channel.ChannelFuture;
import com.hpd.netty.service.NewChatServer;

import java.net.InetSocketAddress;

public class StartMsgService {

	public static void main(String[] args) {
		new IniConf().iniConf();
		new Thread(new ServerThread()).start();

		final NewChatServer chatServer = new NewChatServer();
		InetSocketAddress address = new InetSocketAddress("127.0.0.1", 5544);
		ChannelFuture future = chatServer.start(address);

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				chatServer.destroy();
			}
		});

		future.channel().closeFuture().syncUninterruptibly();
	}
}
