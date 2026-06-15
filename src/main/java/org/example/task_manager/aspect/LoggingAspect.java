package org.example.task_manager.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* org.example.task_manager.service.*.*(..))")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String method = joinPoint.getSignature().toShortString();
        log.info("Calling: {}", method);
        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - start;
            log.info("Completed: {} in {}ms", method, duration);
            return result;
        } catch (Exception ex) {
            log.error("Exception in {}: {}", method, ex.getMessage());
            throw ex;
        }
    }
}