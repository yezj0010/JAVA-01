package io.kimmking.rpcfx.demo.routebiz;

import io.kimmking.rpcfx.api.LoadBalancer;

import java.util.List;
import java.util.Random;

/**
 * created by DengJin on 2021/3/17 18:08
 */
public class RandomLoadBalancer implements LoadBalancer {
    @Override
    public String select(List<String> urls) {
        Random random = new Random();
        return urls.get(random.nextInt(urls.size()));
    }
}
