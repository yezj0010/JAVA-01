package cc.yezj.service;

import cc.yezj.entity.TbOrder;
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
    private DataSource dataSource;

    public List findAll() throws Exception{
        String sql = "select * from t_order";
        Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        ResultSet resultSet = statement.executeQuery();
        List orders =null;
        if(resultSet != null){
            orders = new ArrayList<>();
            while (resultSet.next()){
                TbOrder order = new TbOrder(resultSet.getString("id"),
                        resultSet.getString("user_id"),
                        resultSet.getBigDecimal("total_amount"),
                        resultSet.getInt("state"),
                        resultSet.getLong("create_time")
                );
                orders.add(order);
            }
        }
        return orders;//读出的数据一直是配置的读库
    }

    public void save(TbOrder order) throws Exception{
        String sql = "insert into t_order (id,user_id,total_amount,state,create_time) values(?,?,?,?,?)";
        Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, order.getId());
        statement.setString(2, order.getUserId());
        statement.setBigDecimal(3, order.getTotalAmount());
        statement.setInt(4, order.getState());
        statement.setLong(5, order.getCreateTime());
        statement.execute();//插入数据的一直是配置的写库
    }

}
