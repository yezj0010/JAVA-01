package io.kimmking.rpcfx.demo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * created by DengJin on 2021/3/17 18:50
 */
@Slf4j
@Aspect
@Component
public class RpcAspect {

    @Pointcut("execution(* io.kimmking.rpcfx.demo.consumer.RpcProxyGenerator.generate(java.lang.Object,..))")
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
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Object[] args = point.getArgs();
        String serviceName = point.getTarget().getClass().getName();


        return null;

    }
}
