package io.github.kimmking.gateway.outbound;

/**
 * Created by Deng jin on 2021/1/30 20:42
 */
public enum ClientType{
    HTTP_CLIENT,//使用apache的HttpClient
    OKHTTP,//使用okHttp
    NETTY,//使用自己手写的Netty
}