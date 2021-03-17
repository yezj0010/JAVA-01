package io.kimmking.rpcfx.demo.config;

import io.kimmking.rpcfx.client.Rpcfx;
import io.kimmking.rpcfx.demo.api.OrderService;
import io.kimmking.rpcfx.demo.api.UserService;
import io.kimmking.rpcfx.demo.filter.CuicuiFilter;
import io.kimmking.rpcfx.demo.routebiz.RandomLoadBalancer;
import io.kimmking.rpcfx.demo.routebiz.TagRouter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * created by DengJin on 2021/3/17 18:55
 */
@Configuration
public class RpcProxyConfig {

    @Bean
    public UserService getUserService(){
        //2.使用AOP实现代理类逻辑
        UserService userService = Rpcfx.createFromRegistry(
                UserService.class, "localhost:2181",
                new TagRouter(),
                new RandomLoadBalancer(),
                new CuicuiFilter());
        return userService;
    }

    @Bean
    public OrderService getOrderService(){
        OrderService orderService = Rpcfx.createFromRegistry(
                OrderService.class, "localhost:2181",
                new TagRouter(),
                new RandomLoadBalancer(),
                new CuicuiFilter());
        return orderService;
    }
}
