package cc.yezj.test;

import cc.yezj.constant.KafkaTopic;
import cc.yezj.service.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by Deng jin on 2021/4/18 13:03
 */
@Component
@Slf4j
public class Test implements CommandLineRunner {
    @Autowired
    private Producer producer;

    @Override
    public void run(String... args) throws Exception {
        log.info("发送消息到kafka,topic={}", KafkaTopic.TEST_TOPIC);
        for (int i = 1; i < 10; i++) {
            producer.sendMsg(KafkaTopic.TEST_TOPIC, "key" + i, "data" + i);
        }
    }
}
