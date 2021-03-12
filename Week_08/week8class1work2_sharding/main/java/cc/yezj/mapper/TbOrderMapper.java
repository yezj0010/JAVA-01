package cc.yezj.mapper;

import cc.yezj.entity.TbOrder;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Deng jin on 2021/3/13 0:02
 */
@Service
public interface TbOrderMapper {

    @Insert({"insert into t_order(id,user_id,total_amount,state,create_time) values(#{id},#{userId},#{totalAmount},#{state},#{createTime})"})
    void save(TbOrder order);

    @Select("select count(*) from t_order")
    Integer count();

    @Select("select * from t_order where user_id=#{userId}")
    List<TbOrder> findAllByUserId(Long userId);

    @Select("select * from t_order where id=#{orderId} and user_id=#{userId}")
    TbOrder findByIdAndUserId(Map map);

    @Delete("delete from t_order where id=#{orderId} and user_id=#{userId}")
    void deleteByIdAndUserId(Map map);

    @Update("update t_order set state=#{state} where id=#{orderId} and user_id=#{userId}")
    int uuuuuuuu(Map map);
}
