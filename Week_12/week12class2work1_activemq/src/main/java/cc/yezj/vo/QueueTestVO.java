package cc.yezj.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by Deng jin on 2021/4/10 20:45
 */
@Data
public class QueueTestVO {
    private String code;
    private int cnt;
    private LocalDateTime createTime;
}
