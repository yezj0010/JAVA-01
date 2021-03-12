package cc.yezj.mapper;

import cc.yezj.entity.TbOrderItem;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Deng jin on 2021/3/13 0:12
 */
@Service
public interface TbOrderItemMapper {

    @Insert("insert into t_order_item(id,order_id,user_id,product_id,product_price,product_num,state,create_time) values(#{id},#{orderId},#{userId},#{productId},#{productPrice},#{productNum},#{state},#{createTime})")
    void save(TbOrderItem orderItem);

    @Select("select * from t_order_item where user_id=#{userId}")
    List<TbOrderItem> findAllByUserId(Long userId);

    @Select("select * from t_order_item where order_id=#{orderId} and user_id=#{userId}")
    TbOrderItem findByOrderIdAndUserId(Map map);

    @Delete("delete from t_order_item where order_id=#{orderId} and user_id=#{userId}")
    void deleteByOrderIdAndUserId(Map map);

    @Update("update t_order_item set state=#{state} where order_id=#{orderId} and user_id=#{userId}")
    void uuuuuuuu(Map map);
}
