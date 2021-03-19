package io.kimmking.rpcfx.demo.config;

import io.kimmking.rpcfx.client.CommonRpcService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * created by DengJin on 2021/3/17 18:55
 */
@Configuration
public class RpcProxyConfig {

    @Bean
    public CommonRpcService getCommonRpcService(){
        return new CommonRpcService();
    }
}
