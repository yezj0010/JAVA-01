package io.kimmking.rpcfx.client;

import java.lang.annotation.*;

/**
 * created by DengJin on 2021/3/19 10:29
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RpcInvoke {
}
