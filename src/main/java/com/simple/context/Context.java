package com.simple.context;

import cn.hutool.json.JSONUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.Map;

/**
 * 应用主体, 代替req/res暴露
 */
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
     * 参数
     */
    private Map<String, Object> params;

    /**
     * 请求头
     */
    private Map<String, String> headers;

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

    public void notFound() {
        String message = "404 NOT FOUND";
        FullHttpResponse response = createResponse(message, HttpResponseStatus.NOT_FOUND);
        // write到客户端
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    public void serverError(String message) {
        FullHttpResponse response = createResponse(message, HttpResponseStatus.NOT_FOUND);
        // write到客户端
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    public FullHttpResponse createResponse(String message, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer(message, CharsetUtil.UTF_8));
        // 设置头信息
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
        return response;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public FullHttpRequest getRequest() {
        return request;
    }

    public void setRequest(FullHttpRequest request) {
        this.request = request;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
