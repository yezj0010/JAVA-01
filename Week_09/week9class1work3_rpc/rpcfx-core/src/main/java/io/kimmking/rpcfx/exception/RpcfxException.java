package io.kimmking.rpcfx.exception;

/**
 * created by DengJin on 2021/3/18 16:48
 */
public class RpcfxException extends RuntimeException {
    public RpcfxException(Throwable cause) {
        super(cause);
    }


    public RpcfxException(String message, Throwable cause) {
        super(message, cause);
    }
}
