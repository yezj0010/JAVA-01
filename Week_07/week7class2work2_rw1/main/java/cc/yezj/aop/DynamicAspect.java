package cc.yezj.aop;

import cc.yezj.annotation.DynamicSwitch;
import cc.yezj.config.DynamicDataSource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created by Deng jin on 2021/3/6 21:22
 */
@Slf4j
@Aspect
@Component
public class DynamicAspect {

    @Pointcut("@annotation(cc.yezj.annotation.DynamicSwitch)")
    public void dataSourcePointCut() {

    }

    /**
     * TODO 该方法中可以实现负载均衡算法
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        DynamicSwitch ds = method.getAnnotation(DynamicSwitch.class);
        if (ds == null) {
            DynamicDataSource.setDataSource("write");
            log.info("默认数据源为" + "write");
        } else {
            DynamicDataSource.setDataSource(ds.name());
            log.info("当前指定数据源为" + ds.name());
        }

        try {
            return point.proceed();
        } finally {
            DynamicDataSource.clearDataSource();
            log.debug("当前数据源线程清除，避免影响后续使用");
        }
    }
}
