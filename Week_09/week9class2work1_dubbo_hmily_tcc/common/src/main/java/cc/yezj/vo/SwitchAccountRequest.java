package cc.yezj.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Deng jin on 2021/3/20 15:26
 * 将source用户与target用户的资金进行交换
 */
@Data
public class SwitchAccountRequest implements Serializable {
    private int sourceUserId;//转出方用户id 1
    private String sourceType;//转出方货币类型 CNY
    private BigDecimal sourceAmt;//转出方金额 7

    private int targetUserId;//转入方用户id 2
    private String targetType;//转入方货币类型 USD
    private BigDecimal targetAmt;//转入方金额 1

}
