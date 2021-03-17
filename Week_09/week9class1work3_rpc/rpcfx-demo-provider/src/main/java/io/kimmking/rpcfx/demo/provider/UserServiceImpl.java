package io.kimmking.rpcfx.demo.provider;

import io.kimmking.rpcfx.demo.anotation.RpcService;
import io.kimmking.rpcfx.demo.api.UserService;
import io.kimmking.rpcfx.demo.entity.User;
import org.springframework.stereotype.Service;

@RpcService(name = "io.kimmking.rpcfx.demo.api.UserService", port = 9013)
@Service("io.kimmking.rpcfx.demo.api.UserService")
public class UserServiceImpl implements UserService {

    @Override
    public User findById(int id) {
        return new User(id, "KK" + System.currentTimeMillis());
    }
}
