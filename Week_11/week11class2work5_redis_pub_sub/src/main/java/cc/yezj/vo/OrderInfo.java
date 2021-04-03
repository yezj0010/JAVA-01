package cc.yezj.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Deng jin on 2021/4/3 15:51
 */
@Data
public class OrderInfo implements Serializable {
    private long userId;

    private long productId;

    private BigDecimal price;

    private String otherInfoDemo;
}
