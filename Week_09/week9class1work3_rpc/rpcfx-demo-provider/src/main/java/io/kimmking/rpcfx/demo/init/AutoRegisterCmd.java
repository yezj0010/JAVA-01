package io.kimmking.rpcfx.demo.init;

import io.kimmking.rpcfx.demo.register.RegisterToZookeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * created by DengJin on 2021/3/16 17:26
 * 项目启动，带有@RpcService注解的服务，自动注册到zookeeper，名称为接口全限定名
 */
@Component
public class AutoRegisterCmd implements CommandLineRunner {
    @Autowired
    private RegisterToZookeeper registerToZookeeper;

    @Override
    public void run(String... args) throws Exception {
        registerToZookeeper.registerAllService();
    }
}
