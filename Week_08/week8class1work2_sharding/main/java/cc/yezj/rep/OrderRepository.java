package cc.yezj.rep;

import cc.yezj.entity.TbOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * created by DengJin on 2021/3/11 16:55
 */
public interface OrderRepository extends JpaRepository<TbOrder, Long> {

    @Query(nativeQuery = true, value = "select o.id,o.user_id,o.total_amount,i.product_price,o.state from t_order o left join t_order_item i on o.id=i.order_id where o.user_id=?1")
    List<Map> findAllWithItem(Long userId);

    List<TbOrder> findAllByUserId(Long userId);

    TbOrder findByIdAndUserId(Long orderId, Long userId);

    void deleteByIdAndUserId(Long orderId, Long userId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update t_order set state=?3 where id=?1 and user_id=?2")
    int uuuuuuuu(Long orderId, Long userId,Integer state);
}
