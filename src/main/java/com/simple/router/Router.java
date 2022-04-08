package com.simple.router;

import com.simple.context.Context;

@FunctionalInterface
public interface Router {

    /**
     * 处理器
     *
     * @param context 上下文
     */
    void handle(Context context);
}
