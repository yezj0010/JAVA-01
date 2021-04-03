package cc.yezj.sub;

import cc.yezj.service.OrderService;
import cc.yezj.vo.OrderInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

/**
 * Created by Deng jin on 2021/4/3 15:48
 */
@Component
@Slf4j
public class OrderSubscriber implements MessageListener {
    @Autowired
    private OrderService orderService;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        log.info("============>>>>> onMessage");
        byte[] channel = message.getChannel();
        byte[] body = message.getBody();

        try {
            String title = new String(channel, "UTF-8");
            String content = StringEscapeUtils.unescapeJava(new String(body, "utf-8"));
            content = content.substring(1 , content.length()-1);
            log.info("消息频道名称：{}", title);
            log.info("消息内容是:{}", content);

            if("createOrder".equals(title)){
                ObjectMapper mapper = new ObjectMapper();
                OrderInfo orderInfo = mapper.readValue(content, OrderInfo.class);
                orderService.doCreateOrder(orderInfo);
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }
}
