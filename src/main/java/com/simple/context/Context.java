package com.simple.context;

import cn.hutool.json.JSONUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.Data;

/**
 * 应用主体, 代替req/res暴露
 */
@Data
public class Context {

    /**
     * 方法
     */
    private String method;

    /**
     * 请求url
     */
    private String uri;

    /**
     * netty req
     */
    private FullHttpRequest request;

    /**
     * netty 上下文
     */
    private ChannelHandlerContext ctx;

    /**
     * 创建json响应
     *
     * @param data 响应数据
     */
    public void json(Object data) {
        String dataJsonStr = JSONUtil.toJsonStr(data);
        // 创建http响应
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.copiedBuffer(dataJsonStr, CharsetUtil.UTF_8));
        // 设置头信息
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=UTF-8");
        // write到客户端
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

}
