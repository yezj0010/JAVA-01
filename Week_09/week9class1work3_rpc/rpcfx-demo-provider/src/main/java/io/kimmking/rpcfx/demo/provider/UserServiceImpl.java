package io.kimmking.rpcfx.demo.provider;

import io.kimmking.rpcfx.demo.anotation.RpcService;
import io.kimmking.rpcfx.demo.api.UserService;
import io.kimmking.rpcfx.demo.entity.User;

@RpcService(name = "io.kimmking.rpcfx.demo.api.UserService", port = 2901)
public class UserServiceImpl implements UserService {

    @Override
    public User findById(int id) {
        return new User(id, "KK" + System.currentTimeMillis());
    }
}
