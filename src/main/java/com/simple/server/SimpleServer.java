package com.simple.server;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

public class SimpleServer implements Server {
    /**
     * 日志
     */
    private static final Log logger = LogFactory.get();

    /**
     * 端口
     */
    private int port;

    public SimpleServer() {}

    public SimpleServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        logger.info("程序初始化!");
    }
}
