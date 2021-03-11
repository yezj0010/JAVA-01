package cc.yezj.service;

import cc.yezj.entity.TbOrder;
import cc.yezj.rep.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Deng jin on 2021/3/6 20:22
 */
@Service
public class WriteAndReadService{
    @Autowired
    private OrderRepository orderRepository;

    public List findAll() throws Exception{
        return orderRepository.findAll();
    }

    public void save(TbOrder order) throws Exception{
        orderRepository.save(order);
    }

}
