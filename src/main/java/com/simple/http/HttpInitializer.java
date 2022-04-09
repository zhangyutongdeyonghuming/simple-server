package com.simple.http;

import com.simple.server.Server;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;

public class HttpInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * 服务器
     */
    private final Server server;

    public HttpInitializer(Server server) {
        this.server = server;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();
        /*
         * 或者使用HttpRequestDecoder & HttpResponseEncoder
         */
        p.addLast(new HttpServerCodec());
        /*
         * 在处理POST消息体时需要加上
         */
        p.addLast(new HttpObjectAggregator(1024 * 1024));
        p.addLast(new HttpServerExpectContinueHandler());
        /*
         * http handler
         */
        p.addLast(new HttpServerHandler(server));
    }
}
