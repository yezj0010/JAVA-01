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
@Table(name = "t_order")
@Entity
public class TbOrder {
    @Id
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    private Integer state;

    @Column(name = "create_time")
    private Long createTime;
}
