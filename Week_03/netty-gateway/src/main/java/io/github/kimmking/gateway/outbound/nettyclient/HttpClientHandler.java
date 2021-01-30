package io.github.kimmking.gateway.outbound.nettyclient;

import io.github.kimmking.gateway.filter.HttpResponseFilter;
import io.github.kimmking.gateway.outbound.ClientType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by Deng jin on 2021/1/30 21:27
 */
public class HttpClientHandler extends SimpleChannelInboundHandler<FullHttpResponse> {
    private String uri;
    private HttpResponseFilter filter;
    FullHttpRequest sourceInbound;//初始请求的
    ChannelHandlerContext sourceCtx;//初始请求的

    HttpClientHandler(final FullHttpRequest sourceInbound, final ChannelHandlerContext sourceCtx, String uri, HttpResponseFilter filter){
        this.uri = uri;
        this.filter = filter;
        this.sourceInbound = sourceInbound;
        this.sourceCtx = sourceCtx;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        URI url = new URI(uri);

        //配置HttpRequest的请求数据和一些配置信息
        FullHttpRequest request = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_0, HttpMethod.GET, url.toASCIIString());

        request.headers()
                .set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=UTF-8")
                //开启长连接
                .set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)
                //设置传递请求内容的长度
                .set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());

        //发送数据
        ctx.writeAndFlush(request);
    }

    /**
     * channelRead0 方法用于处理服务端返回给我们的响应，打印服务端返回给客户端的信息。
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) {

        FullHttpResponse response = msg;
        response.headers().get(HttpHeaderNames.CONTENT_TYPE);
        ByteBuf buf = response.content();
        System.out.println("netty客户端接收到服务器消息 = "+buf.toString(io.netty.util.CharsetUtil.UTF_8));
        handleResponse(sourceInbound, sourceCtx, buf.toString(io.netty.util.CharsetUtil.UTF_8).getBytes());
    }

    private void handleResponse(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, final byte[] resultBytes) {
        FullHttpResponse response = null;
        try {
            String s = new String(resultBytes, StandardCharsets.UTF_8);
            System.out.println();
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(resultBytes));
            response.headers().set("Content-Type", "application/json");
            response.headers().setInt("Content-Length", s.length());
            filter.filter(response, ClientType.NETTY);//【指明了使用哪种类型的http客户端进行转发】
        } catch (Exception e) {
            e.printStackTrace();
            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
            exceptionCaught(ctx, e);
        } finally {
            if (fullRequest != null) {
                if (!HttpUtil.isKeepAlive(fullRequest)) {
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    ctx.write(response);
                }
            }
            ctx.flush();
        }

    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}