package cc.yezj.service;

import cc.yezj.entity.TbOrder;
import cc.yezj.entity.TbOrderItem;
import cc.yezj.rep.OrderItemRepository;
import cc.yezj.rep.OrderRepository;
import cc.yezj.vo.OrderRequestVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Deng jin on 2021/3/6 20:22
 */
@Service
@Slf4j
public class WriteAndReadService{
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    public long count(){
        return orderRepository.count();
    }

    public void init1wData(){
        Long createTime = System.currentTimeMillis();
        Random random = new Random();

//        for(int i=0;i<10000;i++){
        for(int i=0;i<1;i++){
            long time = System.currentTimeMillis();
            long orderId = time;
            long userId = random.nextInt(100);//1万笔订单中，总有重复的
            String productId = random.nextInt(100000)+"";
            BigDecimal totalAmount = new BigDecimal(random.nextInt(1000)+"."+random.nextInt(100));
            totalAmount.setScale(2, RoundingMode.HALF_UP);

            TbOrder order = new TbOrder();
            order.setId(orderId);
            order.setUserId(userId);
            order.setTotalAmount(totalAmount);
            order.setState(0);
            order.setCreateTime(createTime);
            order = orderRepository.save(order);

            log.info("新生成的订单，orderId={},userId={}", order.getId(), order.getUserId());

            TbOrderItem orderItem = new TbOrderItem();
            long orderItemId = time+1;
            orderItem.setId(orderItemId);
            orderItem.setOrderId(orderId);
            orderItem.setProductPrice(totalAmount);
            orderItem.setProductNum(1);
            orderItem.setProductId(productId);
            orderItem.setUserId(userId);
            orderItem.setState(0);
            orderItem.setCreateTime(createTime);

            orderItem = orderItemRepository.save(orderItem);
            log.info("新生成的订单项id={},orderId={},userId={}", orderItem.getId(), orderItem.getOrderId(), orderItem.getUserId());
        }
    }

    public Map findAll(OrderRequestVO requestVO){
        //TODO 调用findAllWithItem方法会报错，后期排查原因
        List<TbOrder> orders = orderRepository.findAllByUserId(requestVO.getUserId());
        List<TbOrderItem> items = orderItemRepository.findAllByUserId(requestVO.getUserId());
        Map map = new HashMap();
        map.put("orders", orders);
        map.put("items", items);
        return map;
    }

    public void updateState(OrderRequestVO requestVO){
        Long orderId = requestVO.getOrderId();
        Long userId = requestVO.getUserId();
        Integer state = requestVO.getState();
        TbOrder order = orderRepository.findByIdAndUserId(orderId, userId);
        TbOrderItem orderItem = orderItemRepository.findByOrderIdAndUserId(orderId, userId);
        if(order==null || orderItem==null){
            throw new RuntimeException("未找到订单");
        }

        order.setState(state);
        orderItem.setState(state);

//        orderRepository.save(order);
//        orderItemRepository.save(orderItem);

        orderRepository.uuuuuuuu(orderId, userId, state);
        orderItemRepository.uuuuuuuu(orderId, userId, state);
    }

    public void deleteOrder(OrderRequestVO requestVO){
        Long orderId = requestVO.getOrderId();
        Long userId = requestVO.getUserId();
        orderRepository.deleteByIdAndUserId(orderId, userId);
        orderItemRepository.deleteByOrderIdAndUserId(orderId, userId);
    }

    public static void main(String[] args) {
        System.out.println(1615537123290l%16);
    }

}
