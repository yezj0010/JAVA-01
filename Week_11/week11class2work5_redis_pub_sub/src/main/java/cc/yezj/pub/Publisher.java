package cc.yezj.pub;

import cc.yezj.vo.OrderInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by Deng jin on 2021/4/3 15:47
 */
@Component
@Slf4j
public class Publisher {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 发送订单信息
     * @param orderInfo
     */
    public void send(OrderInfo orderInfo){
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            String s = objectMapper.writeValueAsString(orderInfo);
            redisTemplate.convertAndSend("createOrder", s);
        }catch (Exception e){
            log.error("", e);
        }
    }
}
