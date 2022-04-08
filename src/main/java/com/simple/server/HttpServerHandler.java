package com.simple.server;

import com.simple.context.Context;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) {
        //100 Continue
        if (HttpUtil.is100ContinueExpected(req)) {
            ctx.write(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE));
        }
        // 创建上下文
        Context context = doCreateContext(ctx, req);
        // 获取路由

        // 处理路由

    }

    /**
     * 处理请求
     *
     * @param ctx netty 上下文
     * @param req req
     * @return 上下文
     */
    private Context doCreateContext(ChannelHandlerContext ctx, FullHttpRequest req) {
        // 获取请求的uri
        String uri = req.uri();
        // 获取请求的method
        String method = req.method().name();
        Context context = new Context();
        context.setMethod(method);
        context.setUri(uri);
        context.setCtx(ctx);
        context.setRequest(req);
        return context;
    }
}
