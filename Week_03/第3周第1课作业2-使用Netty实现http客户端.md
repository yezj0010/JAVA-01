### 1-2.使用netty实现后端http访问（代替上一步骤）
与另外两个http客户端保持名称一致，增加类NettyClientOutboundHandler，实现IHttpClient接口，里面大致逻辑与OkhttpOutboundHandler和HttpOutboundHandler一样  
只是转发时http调用使用的是netty客户端，然后去掉了使用线程池去异步调用，因为netty本身就是异步的。    
详细内容见项目文件：    
io.github.kimmking.gateway.inbound.HttpInboundInitializer  
io.github.kimmking.gateway.outbound.nettyclient.NettyClientOutboundHandler  
io.github.kimmking.gateway.outbound.nettyclient.NettyClient  
io.github.kimmking.gateway.outbound.nettyclient.HttpClientHandler  

**主要代码片段如下：**  
初始化ChannelPipeline时，自定义的channelHandler创建增加了ClientType的参数，指定使用哪种http客户端做转发      
```
@Override
	public void initChannel(SocketChannel ch) {
		ChannelPipeline p = ch.pipeline();
		p.addLast(new HttpServerCodec());
		p.addLast(new HttpObjectAggregator(1024 * 1024));
		p.addLast(new HttpInboundHandler(this.proxyServer, ClientType.NETTY));//这里修改为ClientType.NETTY
	}
```

实现了IHttpClient接口，由于netty是异步的，所以返回给调用方代码放到了NettyClient代码中
```
public class NettyClientOutboundHandler implements IHttpClient {
    private List<String> backendUrls;

    HttpResponseFilter filter = new HeaderHttpResponseFilter();
    HttpEndpointRouter router = new RandomHttpEndpointRouter();

    public NettyClientOutboundHandler(List<String> backends) {
        this.backendUrls = backends.stream().map(this::formatUrl).collect(Collectors.toList());
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
```

该类新建netty客户端，并根据入参的url使用getHostAndPort进行解析，http调用逻辑在HttpClientHandler中实现
```
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
```

HttpClientHandler构造方法传入了原始netty服务端中http的请求产生的FullHttpRequest和ChannelHandlerContext，目的是为了将netty客户端接收到的响应数据返回原始调用方
```
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
     * channelRead0 方法用于处理backend服务端返回给我们的响应，打印服务端返回给客户端的信息。
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
            filter.filter(response, ClientType.NETTY);
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
```

修改了HttpClientFactory类，将选项NETTY修改为创建Netty的ChannelHandler对象
```
public class HttpClientFactory {

    public static IHttpClient getHttpClient(ClientType type, List<String> backends){
        switch (type){
            case HTTP_CLIENT:
                return new HttpOutboundHandler(backends);
            case OKHTTP:
                return new OkhttpOutboundHandler(backends);
            case NETTY:
                return new NettyClientOutboundHandler(backends);//增加了Netty选项的返回，
        }
        return null;
    }
}

```