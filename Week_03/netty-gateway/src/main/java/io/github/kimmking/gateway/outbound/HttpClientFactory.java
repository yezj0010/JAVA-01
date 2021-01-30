package io.github.kimmking.gateway.outbound;

import io.github.kimmking.gateway.outbound.httpclient4.HttpOutboundHandler;
import io.github.kimmking.gateway.outbound.nettyclient.NettyClientOutboundHandler;
import io.github.kimmking.gateway.outbound.okhttp.OkhttpOutboundHandler;
import io.github.kimmking.gateway.router.RouterType;

import java.util.List;

/**
 * Created by Deng jin on 2021/1/30 20:05
 * 转发请求客户端工厂，需要传入后端url
 */
public class HttpClientFactory {

    public static IHttpClient getHttpClient(ClientType type, List<String> backends, RouterType routerType){
        switch (type){
            case HTTP_CLIENT:
                return new HttpOutboundHandler(backends, routerType);
            case OKHTTP:
                return new OkhttpOutboundHandler(backends, routerType);
            case NETTY:
                return new NettyClientOutboundHandler(backends, routerType);
        }
        return null;
    }
}
