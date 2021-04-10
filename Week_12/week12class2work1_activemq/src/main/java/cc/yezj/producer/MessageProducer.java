package cc.yezj.producer;

import cc.yezj.config.ActiveMqConfig;
import cc.yezj.vo.QueueTestVO;
import cc.yezj.vo.TopicTestVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Queue;
import javax.jms.Topic;

/**
 * Created by Deng jin on 2021/4/10 20:38
 */
@Component
@Slf4j
public class MessageProducer {
    private ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private JmsMessagingTemplate jms;

    @Autowired
    private Queue queue;

    @Autowired
    private Topic topic;


    public void sendMsgToQueue(QueueTestVO queueTestVO) {
        try{
            log.debug("向queue中发送消息--{}", queueTestVO);
            String s = mapper.writeValueAsString(queueTestVO);
            jms.convertAndSend(queue, s);
        }catch (Exception e){
            log.error("", e);
        }
    }

    public void sendMsgToTopic(TopicTestVO topicTestVO) {
        try{
            log.debug("向queue中发送消息--{}", topicTestVO);
            String s = mapper.writeValueAsString(topicTestVO);
            jms.convertAndSend(topic, s);
        }catch (Exception e){
            log.error("", e);
        }

    }

}
