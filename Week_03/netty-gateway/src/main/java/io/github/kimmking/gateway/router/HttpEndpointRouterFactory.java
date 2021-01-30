package io.github.kimmking.gateway.router;

import java.util.HashMap;

/**
 * Created by Deng jin on 2021/1/30 23:08
 */
public class HttpEndpointRouterFactory {
    private static HashMap<RouterType, HttpEndpointRouter> routerMap = new HashMap<>();
    static {
        routerMap.put(RouterType.Random, new RandomHttpEndpointRouter());
        routerMap.put(RouterType.RoundRibbon, new RoundRibbonHttpEndpointRouter());
    }

    public static HttpEndpointRouter getRouterByType(RouterType type){
        HttpEndpointRouter httpEndpointRouter = routerMap.get(type);
        if(httpEndpointRouter == null){
            throw new RuntimeException("暂未实现该类型的路由器");
        }
        return httpEndpointRouter;
    }

}
