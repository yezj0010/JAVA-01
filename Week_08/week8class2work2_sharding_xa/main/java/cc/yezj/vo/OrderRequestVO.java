package cc.yezj.vo;

import lombok.Data;

/**
 * created by DengJin on 2021/3/12 13:48
 */
@Data
public class OrderRequestVO {
    private Long orderId;

    private Long userId;

    private Integer state;
}
