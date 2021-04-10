package cc.yezj.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

import javax.jms.Queue;
import javax.jms.Topic;


/**
 * Created by Deng jin on 2021/4/10 20:42
 */
@Configuration
public class ActiveMqConfig {
    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;

    public static final String QUEUE_TEST = "test.queue";
    public static final String TOPIC_TEST = "test.topic";



    @Bean
    public Queue testQueue() {
        return new ActiveMQQueue(QUEUE_TEST);
    }

    @Bean
    public Topic testTopic() {
        return new ActiveMQTopic(TOPIC_TEST);
    }

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory(brokerUrl);
    }

    //Queue模式连接注入
    @Bean
    public JmsListenerContainerFactory<?> queueListener(ActiveMQConnectionFactory connectionFactory){
        DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
        bean.setConnectionFactory(connectionFactory);
        return bean;
    }

    //Topic模式连接注入
    @Bean
    public JmsListenerContainerFactory<?> topicListener(ActiveMQConnectionFactory connectionFactory){
        DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
        //设置为发布订阅方式, 默认情况下使用的生产消费者方式
        bean.setPubSubDomain(true);
        bean.setConnectionFactory(connectionFactory);
        return bean;
    }
}
