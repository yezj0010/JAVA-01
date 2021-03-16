package io.kimmking.rpcfx.demo.api;

import io.kimmking.rpcfx.demo.entity.Order;

public interface OrderService {

    Order findOrderById(int id);

}
