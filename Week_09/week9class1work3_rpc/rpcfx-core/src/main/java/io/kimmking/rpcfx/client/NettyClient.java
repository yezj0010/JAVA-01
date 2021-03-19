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

import java.util.concurrent.CountDownLatch;

/**
 * Created by Deng jin on 2021/1/30 21:25
 */
@Slf4j
public class NettyClient extends ChannelInboundHandlerAdapter {

    public static void post(String url, RpcfxRequest req){
        HostInfo hostAndPort = getHostAndPort(url);
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        try {
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
        } catch (Exception e) {
            log.info("", e);
        }finally{
            group.shutdownGracefully();
        }
    }

    /**
     * 获取结果
     * @return
     */
    public static RpcfxResponse get(){
//        try{
//            log.info("等待结果返回");
//            CountDownLatch countDownLatch = NettyHolder.countDownLatchThreadLocal.get();
//            countDownLatch.await();
//            NettyHolder.countDownLatchThreadLocal.remove();
//            log.info("等到结果，获取结果");
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        RpcfxResponse rpcfxResponse = NettyHolder.responseThread.get();
        while (rpcfxResponse==null){
            try{
                log.info("休眠100ms");
                Thread.sleep(100);
            }catch (Exception e){
                log.error("休眠异常", e);
            }
        }
        NettyHolder.responseThread.remove();
        return rpcfxResponse;
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