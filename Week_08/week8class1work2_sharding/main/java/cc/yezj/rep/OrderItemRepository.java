package cc.yezj.rep;

import cc.yezj.entity.TbOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * created by DengJin on 2021/3/12 14:00
 */
public interface OrderItemRepository extends JpaRepository<TbOrderItem, Long> {

    List<TbOrderItem> findAllByUserId(Long userId);

    TbOrderItem findByOrderIdAndUserId(Long orderId, Long userId);

    void deleteByOrderIdAndUserId(Long orderId, Long userId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update t_order_item set state=?3 where order_id=?1 and user_id=?2")
    void uuuuuuuu(Long orderId, Long userId, Integer state);
}
