package io.kimmking.rpcfx.demo.aspect;

import io.kimmking.rpcfx.api.Filter;
import io.kimmking.rpcfx.api.LoadBalancer;
import io.kimmking.rpcfx.api.Router;
import io.kimmking.rpcfx.client.Rpcfx;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * created by DengJin on 2021/3/17 18:50
 */
@Slf4j
@Aspect
@Component
public class RpcAspect {
    @Autowired
    private Router router;
    @Autowired
    private LoadBalancer loadBalancer;

    @Autowired
    private Filter[] filters;

//    @Pointcut("execution(* io.kimmking.rpcfx.demo.entity..*.*(..))")
    @Pointcut("@annotation(io.kimmking.rpcfx.client.RpcInvoke)")
    public void rpcPointCut() {
    }

    /**
     * 在该方法中，实现代理
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("rpcPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();

        Class serviceClass = (Class)args[0];
        String methodStr = (String)args[1];
        Object[] params = (Object[])args[2];
        Class resultClass = (Class) args[3];

        Object result = Rpcfx.invoke(serviceClass, methodStr, params, resultClass, router, loadBalancer, filters);
        return result;

    }
}
