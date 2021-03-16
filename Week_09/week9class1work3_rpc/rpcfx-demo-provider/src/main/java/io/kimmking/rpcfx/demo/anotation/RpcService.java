package io.kimmking.rpcfx.demo.anotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * created by DengJin on 2021/3/16 17:30
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RpcService {
    String name();

    int port();
}
