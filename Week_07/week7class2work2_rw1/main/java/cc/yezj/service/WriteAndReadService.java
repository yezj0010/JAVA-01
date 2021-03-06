package cc.yezj.service;

import cc.yezj.annotation.DynamicSwitch;
import cc.yezj.rep.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Deng jin on 2021/3/6 20:22
 */
@Service
public class WriteAndReadService {
    @Autowired
    private OrderRepository orderRepository;

    public List findInWrite(){
        return orderRepository.findAll();
    }

    @DynamicSwitch(name = "read")
    public List findInRead(){
        return orderRepository.findAll();
    }

}
