package io.kimmking.rpcfx.demo.filter;

import io.kimmking.rpcfx.api.Filter;
import io.kimmking.rpcfx.api.RpcfxRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * created by DengJin on 2021/3/17 18:07
 */
@Slf4j
@Component
public class CuicuiFilter implements Filter {
    @Override
    public boolean filter(RpcfxRequest request) {
        log.info("filter {} -> {}", this.getClass().getName(), request.toString());
        return true;
    }
}
