package cc.yezj.service;

import cc.yezj.pub.Publisher;
import cc.yezj.vo.OrderInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Deng jin on 2021/4/3 15:47
 */
@Service
@Slf4j
public class OrderService {
    @Autowired
    private Publisher publisher;

    public void preCreateOrder(OrderInfo orderInfo){
        //do someting
        publisher.send(orderInfo);
        log.info("准备创建一个订单，发送消息。。。");
    }

    public void doCreateOrder(OrderInfo orderInfo){
        log.info("开始真正创建一个订单。。。,orderInfo={}", orderInfo);
        //do something

        log.info("订单创建成功！");
    }
}
