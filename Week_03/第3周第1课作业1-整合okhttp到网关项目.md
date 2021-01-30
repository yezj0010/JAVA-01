### 1-1.将第二周作业中的okhttp代码整合到该项目中
在原有项目基础上，完成了**OkhttpOutboundHandler**类的开发，将第二周作业中的okhttp替换掉了项目中原有的httpClient.  
详细内容见项目文件：
io.github.kimmking.gateway.outbound.okhttp.OKHttpUtils  
io.github.kimmking.gateway.inbound.HttpInboundInitializer  
io.github.kimmking.gateway.outbound.ClientType  
io.github.kimmking.gateway.outbound.IHttpClient  
io.github.kimmking.gateway.outbound.HttpClientFactory  
io.github.kimmking.gateway.outbound.okhttp.OkhttpOutboundHandler  
  
**主要代码片段如下：**    
初始化ChannelPipeline时，自动一的channelHandler创建增加了ClientType的参数，指定使用哪种http客户端做转发
```
@Override
	public void initChannel(SocketChannel ch) {
		ChannelPipeline p = ch.pipeline();
		p.addLast(new HttpServerCodec());
		p.addLast(new HttpObjectAggregator(1024 * 1024));
		p.addLast(new HttpInboundHandler(this.proxyServer, ClientType.OKHTTP));//这里增加了客户端类型入参
	}
```

增加了IHttpClient接口，OkhttpOutboundHandler和HttpOutboundHandler都实现了该接口
```
public class OkhttpOutboundHandler implements IHttpClient {
.......略

public class HttpOutboundHandler implements IHttpClient {
.......略
```

增加了HttpClientFactory工厂类，给定参数返回需要的http客户端，进行代码解耦  
```
public class HttpClientFactory {

    public static IHttpClient getHttpClient(ClientType type, List<String> backends){
        switch (type){
            case HTTP_CLIENT:
                return new HttpOutboundHandler(backends);
            case OKHTTP:
                return new OkhttpOutboundHandler(backends);
            case NETTY:
                throw new RuntimeException("尚未开发");
        }
        return null;
    }
}
```