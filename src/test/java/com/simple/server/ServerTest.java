package com.simple.server;

import com.simple.http.HttpMethod;

import java.util.Map;

public class ServerTest {

    public static void main(String[] args) {
        Server server = new SimpleServer(8080);
        server.route(HttpMethod.GET, "/", context -> context.json("hello world!"));
        server.route(HttpMethod.POST, "/", context -> {
            Map<String, Object> params = context.getParams();
            context.json(params);
        });
        server.run();
    }
}
