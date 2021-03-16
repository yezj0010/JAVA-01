package io.kimmking.rpcfx.demo.api;

import io.kimmking.rpcfx.demo.entity.User;

public interface UserService {

    User findById(int id);

}
