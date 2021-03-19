package io.kimmking.rpcfx.client;

import io.kimmking.rpcfx.api.RpcfxResponse;

import java.util.concurrent.CountDownLatch;

/**
 * created by DengJin on 2021/3/19 15:36
 */
public class NettyHolder {

    public static ThreadLocal<RpcfxResponse> responseThread = new ThreadLocal<>();
    public static ThreadLocal<CountDownLatch> countDownLatchThreadLocal = new ThreadLocal<CountDownLatch>(){
        @Override
        protected CountDownLatch initialValue() {
            return new CountDownLatch(1);
        }
    };
}
