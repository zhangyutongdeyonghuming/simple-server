package com.simple.handler;

import com.simple.context.Context;

@FunctionalInterface
public interface Handler {

    /**
     * 处理器
     *
     * @param context 上下文
     */
    void handle(Context context);
}
