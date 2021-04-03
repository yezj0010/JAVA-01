package cc.yezj.inti;

import cc.yezj.service.OrderService;
import cc.yezj.vo.OrderInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by Deng jin on 2021/4/3 15:01
 */
@Component
@Slf4j
public class Test implements CommandLineRunner {
    @Autowired
    private OrderService orderService;

    @Override
    public void run(String... args) throws Exception {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUserId(11);
        orderInfo.setProductId(1934);
        orderInfo.setPrice(new BigDecimal(4999.00));
        orderInfo.setOtherInfoDemo("其他信息");
        orderService.preCreateOrder(orderInfo);
    }
}
