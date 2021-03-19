package io.kimmking.rpcfx.demo.consumer;

import io.kimmking.rpcfx.client.CommonRpcService;
import io.kimmking.rpcfx.demo.api.OrderService;
import io.kimmking.rpcfx.demo.api.UserService;
import io.kimmking.rpcfx.demo.entity.Order;
import io.kimmking.rpcfx.demo.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * created by DengJin on 2021/3/17 18:05
 */
@Service
@Slf4j
public class ConsumerService {
    @Autowired
    private CommonRpcService commonRpcService;

    public void test(){
        log.info("测试rpc");

        User user = commonRpcService.invoke(UserService.class, "findById", new Object[]{8888}, User.class);
        log.info("", user);

        Order order = commonRpcService.invoke(OrderService.class, "findOrderById", new Object[]{888}, Order.class);
        log.info("", order);
    }
}
