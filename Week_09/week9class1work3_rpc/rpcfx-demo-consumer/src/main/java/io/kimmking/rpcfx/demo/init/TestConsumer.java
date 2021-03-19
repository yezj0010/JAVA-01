package io.kimmking.rpcfx.demo.init;

import io.kimmking.rpcfx.demo.consumer.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * created by DengJin on 2021/3/17 18:04
 */
@Component
public class TestConsumer implements CommandLineRunner {
    @Autowired
    private ConsumerService consumerService;

    @Override
    public void run(String... args) throws Exception {
        //启动测试
        consumerService.test();
    }
}
