package cc.yezj.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Created by Deng jin on 2021/3/6 19:23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@Table(name = "t_order")
//@Entity
public class TbOrder {
//    @Id
    private Long id;

//    @Column(name = "user_id")
    private Long userId;

//    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    private Integer state;

//    @Column(name = "create_time")
    private Long createTime;
}
