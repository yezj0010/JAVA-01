package cc.yezj.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Deng jin on 2021/4/3 14:32
 */
@Component
@Slf4j
public class Counter {

    private static final String STOCK_TOTAL_KEY = "stock_total_key";
    @Autowired
    private RedisLock redisLock;
    @Autowired
    private RedisComponent redisComponent;

    public void initStock(Integer cnt){
        redisComponent.setValue(STOCK_TOTAL_KEY, cnt);
    }

    /**
     * 减少库存,从redis中读取当前库存，减一，并修改
     * @return
     * true  --  减少库存成功
     * false --  已经没有库存
     */
    public boolean decreaseStock(){
        String lockKey = "stock_key";
        redisLock.tryLock(lockKey);
        try{
            Integer stock = (Integer)redisComponent.getValue(STOCK_TOTAL_KEY);
            if(stock<=0){
                return false;
            }
            stock--;
            redisComponent.setValue(STOCK_TOTAL_KEY, stock);
            return true;
        }finally {
            redisLock.unLock(lockKey);
        }
    }
}
