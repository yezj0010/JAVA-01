package cc.yezj.service;

import cc.yezj.entity.TbOrder;
import cc.yezj.entity.TbOrderItem;
import cc.yezj.mapper.TbOrderItemMapper;
import cc.yezj.mapper.TbOrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created by Deng jin on 2021/3/6 20:22
 */
@Service
@Slf4j
public class WriteAndReadService{
    @Autowired
    private TbOrderMapper orderRepository;
    @Autowired
    private TbOrderItemMapper orderItemRepository;

    @Transactional
    @ShardingTransactionType(TransactionType.XA)
    public List<Map> insertData(String isThrowException){
        Long createTime = System.currentTimeMillis();
        Random random = new Random();
        List<Map> results = new ArrayList<>();
//        for(int i=0;i<10000;i++){
        for(int i=0;i<3;i++){
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
            orderRepository.save(order);

            log.info("新生成的订单，orderId={},userId={},所在库下标{}，所在表下标{}", order.getId(), order.getUserId(), userId%2, userId%16);

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

            orderItemRepository.save(orderItem);
            log.info("新生成的订单项id={},orderId={},userId={},所在库下标{}，所在表下标{}", orderItem.getId(), orderItem.getOrderId(), orderItem.getUserId(), userId%2, userId%16);

            Map map = new HashMap();
            map.put("数据库下标", userId%2);
            map.put("表下下标", userId%16);
            map.put("用户id", userId);
            results.add(map);
        }
        if("1".equals(isThrowException)){
            throw new RuntimeException("抛出异常，测试是否会回滚");
        }
        return results;
    }


    public Map findAll(){
        List<TbOrder> orders = orderRepository.findAll();
        List<TbOrderItem> items = orderItemRepository.findAll();
        Map map = new HashMap();
        map.put("ordersSize", orders.size());
        map.put("itemsSize", items.size());
        map.put("orders", orders);
        map.put("items", items);
        return map;
    }
}
