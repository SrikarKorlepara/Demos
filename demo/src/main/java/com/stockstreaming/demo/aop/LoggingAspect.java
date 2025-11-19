package com.stockstreaming.demo.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class LoggingAspect {
    @Around("execution(* com.stockstreaming.demo.service..*(..))")
    public Object logServiceMethods(ProceedingJoinPoint proceedingJoinPoint){
        String methodName = proceedingJoinPoint.getSignature().getName();
        Object[] args = proceedingJoinPoint.getArgs();

        log.info("Entering Demo method: {} with arguments: {}", methodName, args);
        long startTime = System.currentTimeMillis();
        try {
            Object result = proceedingJoinPoint.proceed();
            long endTime = System.currentTimeMillis();
            log.info("Exiting Demo method: {} with result: {}. Execution time: {} ms",
                    methodName, result, (endTime - startTime));
            return result;
        } catch (Throwable throwable) {
            long endTime = System.currentTimeMillis();
            log.error("Exception in Demo method: {} with message: {}. Execution time: {} ms",
                    methodName, throwable.getMessage(), (endTime - startTime));
            throw new RuntimeException(throwable);
        }
    }
}
