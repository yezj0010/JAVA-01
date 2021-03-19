package io.kimmking.rpcfx.demo.routebiz;

import io.kimmking.rpcfx.api.LoadBalancer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Random;

/**
 * created by DengJin on 2021/3/17 18:08
 */
@Component
public class RandomLoadBalancer implements LoadBalancer {
    @Override
    public String select(List<String> urls) {
        if(CollectionUtils.isEmpty(urls)){
            throw new RuntimeException("未找到对应的服务列表");
        }
        Random random = new Random();
        return urls.get(random.nextInt(urls.size()));
    }
}
