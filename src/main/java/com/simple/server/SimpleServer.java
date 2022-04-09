package com.simple.server;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.simple.handler.Handler;
import com.simple.http.HttpInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class SimpleServer implements Server {

    /**
     * 日志
     */
    private static final Log logger = LogFactory.get();

    /**
     * 端口
     */
    private final int port;

    public SimpleServer(int port) {
        this.port = port;
        routerMap = new HashMap<>();
    }

    /**
     * 路由Map
     */
    private final Map<String, Handler> routerMap;

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
                .childHandler(new HttpInitializer(this));
        ChannelFuture f = bootstrap.bind(new InetSocketAddress(port)).sync();
        logger.info("server start up on port : " + port);
        f.channel().closeFuture().sync();
    }

    @Override
    public void route(String method, String path, Handler handler) {
        routerMap.put(key(method, path), handler);
    }

    @Override
    public Handler getHandler(String method, String path) {
        return routerMap.get(key(method, path));
    }

    /**
     * 获取router的key
     *
     * @param method 方法
     * @param path   uri
     * @return key
     */
    protected String key(String method, String path) {
        return method + "-" + path;
    }
}
