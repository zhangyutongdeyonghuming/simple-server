package com.simple.http;

import cn.hutool.core.util.URLUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.simple.context.Context;
import com.simple.handler.Handler;
import com.simple.server.Server;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MemoryAttribute;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    /**
     * 日志
     */
    private static final Log logger = LogFactory.get();

    /**
     * 服务器
     */
    private final Server server;

    public HttpServerHandler(Server server) {
        this.server = server;
    }

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
        // 处理路由
        Handler handler = server.getHandler(context.getMethod(), context.getUri());
        doHandler(handler, context);
    }

    private void doHandler(Handler handler, Context context) {
        // 404
        if (handler == null) {
            context.notFound();
            return;
        }
        try {
            handler.handle(context);
        } catch (Exception e) {
            context.serverError(e.getMessage());
        }
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
        logger.info("[{}]-{}", method, URLUtil.decode(uri));
        Context context = new Context();
        context.setMethod(method);
        context.setUri(uri);
        context.setCtx(ctx);
        context.setRequest(req);
        context.setParams(parseReqParams(req));
        return context;
    }

    /**
     * 解析请求参数
     *
     * @param req req
     * @return 参数
     */
    private Map<String, Object> parseReqParams(FullHttpRequest req) {
        Map<String, Object> params = new HashMap<>();
        if (req.method() == HttpMethod.GET) {
            QueryStringDecoder decoder = new QueryStringDecoder(req.uri());
            for (Map.Entry<String, List<String>> entry : decoder.parameters().entrySet()) {
                String key = entry.getKey();
                List<String> value = entry.getValue();
                params.put(key, value.get(0));
            }
        } else {
            // post
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), req);
            List<InterfaceHttpData> httpPostData = decoder.getBodyHttpDatas();
            for (InterfaceHttpData data : httpPostData) {
                if (data instanceof MemoryAttribute) {
                    MemoryAttribute attribute = (MemoryAttribute) data;
                    params.put(attribute.getName(), attribute.getValue());
                }
            }
        }
        logger.info("params:{}", params);
        return params;
    }

}
