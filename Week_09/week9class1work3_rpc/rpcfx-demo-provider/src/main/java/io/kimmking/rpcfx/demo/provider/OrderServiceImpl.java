package io.kimmking.rpcfx.demo.provider;

import io.kimmking.rpcfx.demo.anotation.RpcService;
import io.kimmking.rpcfx.demo.entity.Order;
import io.kimmking.rpcfx.demo.api.OrderService;
import org.springframework.stereotype.Service;

//@Service("io.kimmking.rpcfx.demo.api.OrderService")
@RpcService(name = "io.kimmking.rpcfx.demo.api.OrderService", port = 2901)
public class OrderServiceImpl implements OrderService {

    @Override
    public Order findOrderById(int id) {
        return new Order(id, "Cuijing" + System.currentTimeMillis(), 9.9f);
    }
}
