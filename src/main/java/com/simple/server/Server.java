package com.simple.server;

import com.simple.router.Router;

/**
 * 服务器接口
 */
public interface Server extends Router {

    /**
     * 启动服务器
     */
    void run();

}
