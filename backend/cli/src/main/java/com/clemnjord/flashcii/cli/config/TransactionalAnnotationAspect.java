package com.clemnjord.flashcii.cli.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TransactionalAnnotationAspect {

    @Around("@annotation(com.clemnjord.flashcii.application.annotation.ApplicationTransactional) || " +
            "@within(com.clemnjord.flashcii.application.annotation.ApplicationTransactional)")
    public Object handleTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        // This will be handled by Spring's transaction management
        return joinPoint.proceed();
    }
}
