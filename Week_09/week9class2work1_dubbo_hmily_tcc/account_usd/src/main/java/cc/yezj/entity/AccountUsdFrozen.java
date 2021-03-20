package cc.yezj.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by Deng jin on 2021/3/20 17:34
 */
@Data
public class AccountUsdFrozen {
    private int id;
    private int userId;
    private BigDecimal amt;
    private int state;//0-已失效 1-冻结
}
