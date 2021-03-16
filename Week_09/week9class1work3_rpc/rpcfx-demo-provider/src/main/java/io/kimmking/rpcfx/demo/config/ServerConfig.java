package io.kimmking.rpcfx.demo.config;

import io.kimmking.rpcfx.api.RpcfxResolver;
import io.kimmking.rpcfx.server.RpcfxInvoker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * created by DengJin on 2021/3/16 17:41
 */
@Configuration
public class ServerConfig {
    @Bean
    public RpcfxInvoker createInvoker(@Autowired RpcfxResolver resolver){
        return new RpcfxInvoker(resolver);
    }

    @Bean
    public RpcfxResolver createResolver(){
        return new DemoResolver();
    }
}
