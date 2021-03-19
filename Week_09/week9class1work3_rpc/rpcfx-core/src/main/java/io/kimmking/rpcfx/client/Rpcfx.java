package io.kimmking.rpcfx.client;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import io.kimmking.rpcfx.api.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
public final class Rpcfx {

    public static final MediaType JSONTYPE = MediaType.get("application/json; charset=utf-8");

    static {
        ParserConfig.getGlobalInstance().addAccept("io.kimmking");
    }

    /**
     * 新增加的aop使用的方法
     * @param tClass
     * @param method
     * @param args
     * @param resultClass
     * @param <T>
     * @return
     */
    public static <T> T invoke(Class tClass, String method, Object[] args, Class<T> resultClass,
                               Router router, LoadBalancer loadBalancer, Filter... filters) throws Exception{
        // 加filte之一

        // curator Provider list from zk
        List<String> invokers = new ArrayList<>();
        invokers.add("localhost:2181");//临时做法
        // 1. 简单：从zk拿到服务提供的列表
        // 2. 挑战：监听zk的临时节点，根据事件更新这个list（注意，需要做个全局map保持每个服务的提供者List）
        List<String> urls = router.route(invokers, tClass);

        String url = loadBalancer.select(urls); // router, loadbalance

        // 加filter地方之二
        // mock == true, new Student("hubao");

        RpcfxRequest request = new RpcfxRequest();
        request.setServiceClass(tClass.getName());
        request.setMethod(method);
        request.setParams(args);
        request.setTraceId(UUID.randomUUID()+"");

        if (null!=filters) {
            for (Filter filter : filters) {
                if (!filter.filter(request)) {
                    return null;
                }
            }
        }

        RpcfxResponse response = postByNetty(request, url);

        // 加filter地方之三
        // Student.setTeacher("cuijing");

        // 这里判断response.status，处理异常
        // 考虑封装一个全局的RpcfxException
        if(response.isStatus()){
            return JSON.parseObject(response.getResult().toString(), resultClass);
        }else{
            throw response.getException();
        }
    }


    public static <T, filters> T createFromRegistry(final Class<T> serviceClass, final String zkUrl, Router router, LoadBalancer loadBalance, Filter filter) {

        // 加filte之一

        // curator Provider list from zk
        List<String> invokers = new ArrayList<>();
        invokers.add(zkUrl);//临时做法
        // 1. 简单：从zk拿到服务提供的列表
        // 2. 挑战：监听zk的临时节点，根据事件更新这个list（注意，需要做个全局map保持每个服务的提供者List）
        List<String> urls = router.route(invokers, serviceClass);

        String url = loadBalance.select(urls); // router, loadbalance

        return (T) create(serviceClass, url, filter);
    }

    public static <T> T create(final Class<T> serviceClass, final String url, Filter... filters) {
        // 0. 替换动态代理 -> AOP
        return (T) Proxy.newProxyInstance(Rpcfx.class.getClassLoader(), new Class[]{serviceClass}, new RpcfxInvocationHandler(serviceClass, url, filters));
    }

    public static class RpcfxInvocationHandler implements InvocationHandler {

        private final Class<?> serviceClass;
        private final String url;
        private final Filter[] filters;

        public <T> RpcfxInvocationHandler(Class<T> serviceClass, String url, Filter... filters) {
            this.serviceClass = serviceClass;
            this.url = url;
            this.filters = filters;
        }

        // 可以尝试，自己去写对象序列化，二进制还是文本的，，，rpcfx是xml自定义序列化、反序列化，json: code.google.com/p/rpcfx
        // int byte char float double long bool
        // [], data class

        @Override
        public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {

            // 加filter地方之二
            // mock == true, new Student("hubao");

            RpcfxRequest request = new RpcfxRequest();
            request.setServiceClass(this.serviceClass.getName());
            request.setMethod(method.getName());
            request.setParams(params);

            if (null!=filters) {
                for (Filter filter : filters) {
                    if (!filter.filter(request)) {
                        return null;
                    }
                }
            }

            RpcfxResponse response = post(request, url);

            // 加filter地方之三
            // Student.setTeacher("cuijing");

            // 这里判断response.status，处理异常
            // 考虑封装一个全局的RpcfxException

            return JSON.parse(response.getResult().toString());
        }
    }

    private static RpcfxResponse post(RpcfxRequest req, String url) throws IOException {
        String reqJson = JSON.toJSONString(req);
        System.out.println("req json: "+reqJson);

        // 1.可以复用client
        // 2.尝试使用httpclient或者netty client
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSONTYPE, reqJson))
                .build();
        String respJson = client.newCall(request).execute().body().string();
        System.out.println("resp json: "+respJson);
        return JSON.parseObject(respJson, RpcfxResponse.class);
    }


    private static RpcfxResponse postByNetty(RpcfxRequest req, String url) throws IOException {
        log.info("通过netty发送post请求");
        RpcfxResponse rpcfxResponse = NettyClient.post(url, req);
        return rpcfxResponse;
    }
}
