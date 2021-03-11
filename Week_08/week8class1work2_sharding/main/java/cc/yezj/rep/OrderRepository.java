package cc.yezj.rep;

import cc.yezj.entity.TbOrder;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * created by DengJin on 2021/3/11 16:55
 */
public interface OrderRepository extends JpaRepository<TbOrder, String> {
}
