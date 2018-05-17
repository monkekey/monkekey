package com.hpd.netty.service;

import com.hpd.netty.model.Data;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.ImmediateEventExecutor;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * Created by teddyzhou on 2017/10/8.
 */
@Component
public class NewChatServer {
    private final ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workGroup = new NioEventLoopGroup();
    private Channel channel;

    public ChannelFuture start(InetSocketAddress address) {
        ServerBootstrap boot = new ServerBootstrap();

        boot.group(bossGroup, workGroup).channel(NioServerSocketChannel.class);
        boot.childHandler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                //处理日志
                //channel.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
                //处理心跳
                channel.pipeline().addLast(new IdleStateHandler(0, 0, 1800, TimeUnit.SECONDS));
                channel.pipeline().addLast(new ChatHeartbeatHandler());

                // 编码模块
                channel.pipeline().addLast(new HttpServerCodec());
                // 大数据内容写入模块
                channel.pipeline().addLast(new ChunkedWriteHandler());
                // 组装Http头信息保证完整性
                channel.pipeline().addLast(new HttpObjectAggregator(64 * 1024));
                // 上下文判断
                channel.pipeline().addLast(new WebSocketHandler(Data.sysConfig.get("context").toString()));
                // 对websocket握手的支持 io.netty 提供
                channel.pipeline().addLast(new WebSocketServerProtocolHandler(Data.sysConfig.get("context").toString()));
                // 处理文本消息
                channel.pipeline().addLast(new MsgHandler(channelGroup));
            }
        });
        ChannelFuture future = boot.bind(address).syncUninterruptibly();
        channel = future.channel();

        return future;
    }

    public void destroy() {
        if(channel != null) {
            channel.close();
        }

        channelGroup.close();
        workGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }

    public static void main(String[] args) {
        final NewChatServer server = new NewChatServer();
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 5544);
        ChannelFuture future = server.start(address);

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                server.destroy();
            }
        });

        future.channel().closeFuture().syncUninterruptibly();
    }
}
