package cc.yezj.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by Deng jin on 2021/4/18 12:54
 */
@Component
@Slf4j
public class Producer {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMsg(String topic, String key, String msg){
        kafkaTemplate.send(topic, key, msg);
    }
}
