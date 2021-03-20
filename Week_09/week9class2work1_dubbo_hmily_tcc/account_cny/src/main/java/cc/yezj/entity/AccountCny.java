package cc.yezj.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by Deng jin on 2021/3/20 17:34
 */
@Data
public class AccountCny {
    private int userId;
    private BigDecimal amt;
    private int state;
    private int version;
}
