package io.kimmking.rpcfx.demo.provider;

import io.kimmking.rpcfx.demo.anotation.RpcService;
import io.kimmking.rpcfx.demo.api.OrderService;
import io.kimmking.rpcfx.demo.entity.Order;
import org.springframework.stereotype.Service;

@RpcService(name = "io.kimmking.rpcfx.demo.api.OrderService", port = 9013)
@Service("io.kimmking.rpcfx.demo.api.OrderService")
public class OrderServiceImpl implements OrderService {

    @Override
    public Order findOrderById(int id) {
        return new Order(id, "Cuijing" + System.currentTimeMillis(), 9.9f);
    }
}
