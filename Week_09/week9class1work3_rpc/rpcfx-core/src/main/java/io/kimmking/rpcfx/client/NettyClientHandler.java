package io.kimmking.rpcfx.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;

/**
 * created by DengJin on 2021/3/19 14:10
 */
@Slf4j
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    private String url;

    private RpcfxRequest req;

    public NettyClientHandler(String url, RpcfxRequest req){
        this.url = url;
        this.req = req;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String reqJson = JSON.toJSONString(req);
        log.info("req json: {}", reqJson);

        URI uri = new URI(url);
        DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.POST, uri.toASCIIString());
        request.headers().add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        request.headers().add(HttpHeaderNames.CONTENT_TYPE,"application/json");

        ByteBuf byteBuf = Unpooled.copiedBuffer(JSONObject.toJSONString(req).getBytes());
        request.content().writeBytes(byteBuf);
        request.headers().set(HttpHeaderNames.CONTENT_LENGTH,request.content().readableBytes());
        //发送
        log.info("发送post请求");
        ChannelFuture channelFuture = ctx.writeAndFlush(request);
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if(future.isSuccess()) {
                    log.info("===========发送成功");
                }else {
                    log.info("------------------发送失败");
                }
            }
        });
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        log.info("msg -> {}", msg);
        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            ByteBuf buf = response.content();
            String result = buf.toString(CharsetUtil.UTF_8);
            log.info("resp json: {}", result);
            RpcfxResponse rpcfxResponse = JSONObject.parseObject(result, RpcfxResponse.class);
            SyncFuture syncFuture = NettyHolder.futureCache.get(req.getTraceId());
            syncFuture.setResponse(rpcfxResponse);
        }else{
            log.info("未知返回数据");
        }
    }
}
