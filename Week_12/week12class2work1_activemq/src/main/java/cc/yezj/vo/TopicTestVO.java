package cc.yezj.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Created by Deng jin on 2021/4/10 20:45
 */
@Data
public class TopicTestVO {
    private String channel;
    private BigDecimal rate;
    private LocalDateTime createTime;
}
