package io.kimmking.rpcfx.client;

import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Deng jin on 2021/1/30 21:25
 */
@Slf4j
public class NettyClient {

    public static RpcfxResponse post(String url, RpcfxRequest req){
        HostInfo hostAndPort = getHostAndPort(url);

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            //初始化资源
            NettyHolder.futureCache.put(req.getTraceId(), new SyncFuture());
            //新建一个连接，因为每次ip端口都可能不一样
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<Channel>() {

                        @Override
                        protected void initChannel(Channel channel)
                                throws Exception {
                            //channel.pipeline().addLast(new HttpRequestEncoder());
                            //channel.pipeline().addLast(new HttpResponseDecoder());
                            channel.pipeline().addLast(new HttpClientCodec());
                            channel.pipeline().addLast(new HttpObjectAggregator(65536));
                            channel.pipeline().addLast(new HttpContentDecompressor());
                            channel.pipeline().addLast(new NettyClientHandler(url, req));
                        }
                    });
            ChannelFuture future = bootstrap.connect(hostAndPort.getHost(), hostAndPort.getPort()).sync();
            future.channel().closeFuture().sync();
            //获取结果
            SyncFuture syncFuture = NettyHolder.futureCache.get(req.getTraceId());
            RpcfxResponse rpcfxResponse = syncFuture.get();
            return rpcfxResponse;
        } catch (Exception e) {
            log.info("", e);
        }finally{
            group.shutdownGracefully();
            NettyHolder.futureCache.invalidate(req.getTraceId());
        }
        return null;
    }

    private static HostInfo getHostAndPort(String url){
        int i = url.indexOf("//");
        String afterInfo = url.substring(i+2);
        String[] split = afterInfo.split("/");
        String[] split1 = split[0].split(":");
        String host = "";
        int port = 80;
        host = split1[0];
        if(split1.length>1){
            port = Integer.parseInt(split1[1]);
        }
        int i1 = afterInfo.indexOf("/");
        String uri = "/";
        if(i1 != -1){
            uri = afterInfo.substring(i1);
        }

        HostInfo hostInfo = new HostInfo();
        hostInfo.setHost(host);
        hostInfo.setPort(port);
        hostInfo.setUri(uri);
        return hostInfo;
    }

    @Data
    static class HostInfo{
        private String host;
        private int port;
        private String uri;// 域名后的请求路径，如：/test
    }
}