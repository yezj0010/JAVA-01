package cc.yezj.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by Deng jin on 2021/4/3 14:36
 */
@Component
public class RedisComponent {
    @Autowired
    private RedisTemplate redisTemplate;

    public Object getValue(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public void setValue(String key, Object value){
        redisTemplate.opsForValue().set(key, value);
    }
}
