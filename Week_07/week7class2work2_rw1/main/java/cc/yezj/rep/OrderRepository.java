package cc.yezj.rep;

import cc.yezj.entity.TbOrder;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Deng jin on 2021/3/6 19:56
 */
public interface OrderRepository extends JpaRepository<TbOrder, String> {
}
