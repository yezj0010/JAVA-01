package io.github.kimmking.gateway.outbound;

import io.github.kimmking.gateway.filter.HttpRequestFilter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * Created by Deng jin on 2021/1/30 20:06
 */
public interface IHttpClient {
    //调用http获取请求，然后接到响应之后塞入ChannelHandlerContext中
    void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, HttpRequestFilter filter);
}
