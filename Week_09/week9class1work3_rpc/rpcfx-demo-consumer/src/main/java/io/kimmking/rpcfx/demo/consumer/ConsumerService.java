package io.kimmking.rpcfx.demo.consumer;

import io.kimmking.rpcfx.demo.api.OrderService;
import io.kimmking.rpcfx.demo.api.UserService;
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
    private UserService userService;
    @Autowired
    private OrderService orderService;

    public void test(){


        userService.findById(2222);


        orderService.findOrderById(999);
    }
}
