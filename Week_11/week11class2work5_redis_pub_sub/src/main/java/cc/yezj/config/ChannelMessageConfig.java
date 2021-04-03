package cc.yezj.config;

import cc.yezj.sub.OrderSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class ChannelMessageConfig {
    @Autowired
    private OrderSubscriber orderSubscriber;
 
    @Bean
    MessageListenerAdapter listenerAdapter() {
        return new MessageListenerAdapter(orderSubscriber);
    }
 
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter)
    {
        RedisMessageListenerContainer container=new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter,new PatternTopic("*Order"));
 
        return container;
    }
}