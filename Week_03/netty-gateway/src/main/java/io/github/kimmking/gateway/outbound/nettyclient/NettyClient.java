package io.github.kimmking.gateway.outbound.nettyclient;

import io.github.kimmking.gateway.filter.HttpResponseFilter;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import lombok.Data;

/**
 * Created by Deng jin on 2021/1/30 21:25
 */
public class NettyClient extends ChannelInboundHandlerAdapter {

    public static void getResultBytes(final FullHttpRequest inbound, final ChannelHandlerContext ctx, String url, HttpResponseFilter filter){
        //客户端一个足够了
        EventLoopGroup group = new NioEventLoopGroup();
        HostInfo hostAndPort = getHostAndPort(url);

        try {
            //服务端启动辅助类使用的是 ServerBootstrap，而客户端换成了 Bootstrap
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    //使用匿名内部类创建了和服务端一样的ChannelInitializer
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new HttpClientCodec());
                            pipeline.addLast(new HttpObjectAggregator(65536));
                            pipeline.addLast(new HttpClientHandler(inbound, ctx, hostAndPort.getUri(), filter));
                        }
                    });

            // 启动客户端.   最后通过 connect 方法连接服务端。
            ChannelFuture f = b.connect(hostAndPort.getHost(), hostAndPort.getPort()).sync();
            f.channel().closeFuture().sync();
        } catch (Exception e){
            e.printStackTrace();
        }
        finally {
            group.shutdownGracefully();
        }
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