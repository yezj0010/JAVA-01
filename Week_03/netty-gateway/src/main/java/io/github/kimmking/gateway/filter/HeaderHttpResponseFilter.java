package io.github.kimmking.gateway.filter;

import io.github.kimmking.gateway.outbound.ClientType;
import io.netty.handler.codec.http.FullHttpResponse;

public class HeaderHttpResponseFilter implements HttpResponseFilter {
    @Override
    public void filter(FullHttpResponse response) {
        response.headers().set("kk", "java-1-nio");
    }

    @Override
    public void filter(FullHttpResponse response, ClientType type) {
        response.headers().set("Dispatch-Client-Type", type);
    }
}
