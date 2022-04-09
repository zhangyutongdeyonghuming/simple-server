package com.simple.router;

import com.simple.handler.Handler;

/**
 * router
 */
public interface Router {

    /**
     * 添加路由
     *
     * @param method  http 方法
     * @param path    请求路径
     * @param handler 处理器
     */
    void route(String method, String path, Handler handler);

    /**
     * 获取路由
     *
     * @param method http 方法
     * @param path   请求路径
     * @return 处理器
     */
    Handler getHandler(String method, String path);
}
