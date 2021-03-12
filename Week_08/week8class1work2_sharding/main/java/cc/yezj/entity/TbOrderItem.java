package cc.yezj.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Created by Deng jin on 2021/3/6 19:23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_order_item")
@Entity
public class TbOrderItem {
    @Id
    private Long id;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "product_price")
    private BigDecimal productPrice;

    @Column(name = "product_num")
    private Integer productNum;

    private Integer state;

    @Column(name = "create_time")
    private Long createTime;
}
