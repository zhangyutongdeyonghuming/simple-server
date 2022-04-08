package com.simple.server;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.AllArgsConstructor;

import java.net.InetSocketAddress;

@AllArgsConstructor
public class SimpleServer implements Server {
    /**
     * 日志
     */
    private static final Log logger = LogFactory.get();

    /**
     * 端口
     */
    private int port;

    @Override
    public void run() {
        logger.info("程序初始化!");
        try {
            doStartup();
        } catch (InterruptedException e) {
            logger.error("执行启动异常!", e);
        }
    }

    /**
     * 启动socket
     */
    private void doStartup() throws InterruptedException {
        // 启动引导器
        ServerBootstrap bootstrap = new ServerBootstrap();
        // 时间循环器
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup work = new NioEventLoopGroup();
        bootstrap.group(boss, work)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .channel(NioServerSocketChannel.class)
                .childHandler(new HttpInitializer());
        ChannelFuture f = bootstrap.bind(new InetSocketAddress(port)).sync();
        System.out.println("server start up on port : " + port);
        f.channel().closeFuture().sync();
    }
}
