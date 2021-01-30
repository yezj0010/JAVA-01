package io.github.kimmking.gateway.filter;

import io.github.kimmking.gateway.outbound.ClientType;
import io.netty.handler.codec.http.FullHttpResponse;

public interface HttpResponseFilter {

    void filter(FullHttpResponse response);

    void filter(FullHttpResponse response, ClientType type);

}
