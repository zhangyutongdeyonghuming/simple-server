package com.simple.router;

import org.junit.jupiter.api.Test;

public class RouterTest {

    @Test
    public void testHandler() {
        route("get", "/", context -> {

        });
    }

    public void route(String method, String pattern, Handler handler) {
        System.out.println(method);
        System.out.println(handler);
    }
}