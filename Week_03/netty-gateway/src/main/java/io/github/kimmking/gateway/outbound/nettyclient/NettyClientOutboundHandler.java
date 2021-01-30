package io.github.kimmking.gateway.outbound.nettyclient;

import io.github.kimmking.gateway.filter.HeaderHttpResponseFilter;
import io.github.kimmking.gateway.filter.HttpRequestFilter;
import io.github.kimmking.gateway.filter.HttpResponseFilter;
import io.github.kimmking.gateway.outbound.IHttpClient;
import io.github.kimmking.gateway.router.HttpEndpointRouter;
import io.github.kimmking.gateway.router.HttpEndpointRouterFactory;
import io.github.kimmking.gateway.router.RouterType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Deng jin on 2021/1/30 21:13
 */
public class NettyClientOutboundHandler implements IHttpClient {
    private List<String> backendUrls;

    HttpResponseFilter filter = new HeaderHttpResponseFilter();
    HttpEndpointRouter router;

    public NettyClientOutboundHandler(List<String> backends, RouterType routerType) {
        this.backendUrls = backends.stream().map(this::formatUrl).collect(Collectors.toList());
        this.router = HttpEndpointRouterFactory.getRouterByType(routerType);
    }

    @Override
    public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, HttpRequestFilter filter) {
        String backendUrl = router.route(this.backendUrls);
        final String url = backendUrl + fullRequest.uri();
        filter.filter(fullRequest, ctx);
        fetchGet(fullRequest, ctx, url, this.filter);
    }

    private void fetchGet(final FullHttpRequest inbound, final ChannelHandlerContext ctx, final String url, HttpResponseFilter filter) {
        NettyClient.getResultBytes(inbound, ctx, url, filter);
    }

    private String formatUrl(String backend) {
        return backend.endsWith("/")?backend.substring(0,backend.length()-1):backend;
    }
}
