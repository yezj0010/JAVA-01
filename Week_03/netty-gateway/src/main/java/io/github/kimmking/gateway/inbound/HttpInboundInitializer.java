package io.github.kimmking.gateway.inbound;

import io.github.kimmking.gateway.outbound.ClientType;
import io.github.kimmking.gateway.router.RouterType;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

import java.util.List;

public class HttpInboundInitializer extends ChannelInitializer<SocketChannel> {
	
	private List<String> proxyServer;
	
	public HttpInboundInitializer(List<String> proxyServer) {
		this.proxyServer = proxyServer;
	}
	
	@Override
	public void initChannel(SocketChannel ch) {
		ChannelPipeline p = ch.pipeline();
		p.addLast(new HttpServerCodec());
		p.addLast(new HttpObjectAggregator(1024 * 1024));


		//可以修改为ClientType.NETTY，但是高频访问会请求不到，说客户端断开连接，估计哪里写的还有问题
		//路由器类型也可以选择
		p.addLast(new HttpInboundHandler(this.proxyServer, ClientType.OKHTTP, RouterType.RoundRibbon));
	}
}
