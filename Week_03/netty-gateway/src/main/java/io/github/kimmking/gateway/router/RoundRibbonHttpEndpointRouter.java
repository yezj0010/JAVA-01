package io.github.kimmking.gateway.router;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Deng jin on 2021/1/30 23:04
 * 逐一遍历
 */
public class RoundRibbonHttpEndpointRouter implements HttpEndpointRouter {
    private AtomicInteger integer = new AtomicInteger(-1);
    @Override
    public String route(List<String> urls) {
        integer.getAndIncrement();
        int size = urls.size();
        return urls.get(integer.get()%size);
    }
}
