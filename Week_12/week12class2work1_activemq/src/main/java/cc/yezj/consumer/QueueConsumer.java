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
public class QueueConsumer {

    @JmsListener(destination= ActiveMqConfig.QUEUE_TEST,containerFactory = "queueListener")
    public void consumerMsg(String queueTestVO){
        log.info("接收到queue={}的消息,msg={}", ActiveMqConfig.QUEUE_TEST, queueTestVO);
    }
}
