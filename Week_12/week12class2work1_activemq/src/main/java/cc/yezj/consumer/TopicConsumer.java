package cc.yezj.consumer;

import cc.yezj.config.ActiveMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * Created by Deng jin on 2021/4/10 20:38
 */
@Component
@Slf4j
public class TopicConsumer {

    @JmsListener(destination= ActiveMqConfig.TOPIC_TEST, containerFactory="topicListener")
    public void consumeTopic(String topicTestVO){
        log.info("接收到topic={}的消息,msg={}", ActiveMqConfig.TOPIC_TEST, topicTestVO);
    }
}
