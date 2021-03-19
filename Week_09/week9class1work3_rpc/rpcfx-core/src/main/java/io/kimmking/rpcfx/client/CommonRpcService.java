package io.kimmking.rpcfx.client;

/**
 * created by DengJin on 2021/3/19 11:47
 */
public class CommonRpcService {

    @RpcInvoke
    public <T> T invoke(Class tClass, String method, Object[] args, Class<T> resultClass){
        return null;
    }
}
