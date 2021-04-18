package cc.yezj.service;

import cc.yezj.constant.KafkaTopic;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Created by Deng jin on 2021/4/18 12:54
 */
@Component
@Slf4j
public class Consumer {

    @KafkaListener(topics = KafkaTopic.TEST_TOPIC)
    public void consumerMsg(ConsumerRecord<?, ?> consumer){
        log.info("topic名称:{},key:{},分区位置:{}, 下标{}",consumer.topic(),consumer.key(),consumer.partition(),consumer.offset());
        log.info("msg={}", consumer.value());
    }
}
