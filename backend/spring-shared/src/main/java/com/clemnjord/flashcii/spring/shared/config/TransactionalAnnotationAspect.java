package com.clemnjord.flashcii.spring.shared.config;

import com.clemnjord.flashcii.application.annotation.ApplicationTransactional;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.lang.reflect.Method;

/**
 * Aspect that intercepts methods annotated with @ApplicationTransactional
 * and applies Spring transaction management.
 * <p>
 * This provides a custom transaction management solution that wraps
 *
 * @ApplicationTransactional annotations with proper transaction boundaries.
 */

@Aspect
@Component
public class TransactionalAnnotationAspect {

    private final PlatformTransactionManager transactionManager;

    public TransactionalAnnotationAspect(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Around("@annotation(com.clemnjord.flashcii.application.annotation.ApplicationTransactional) || " +
            "@within(com.clemnjord.flashcii.application.annotation.ApplicationTransactional)")
    public Object handleTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        // Get the annotation from method or class
        ApplicationTransactional annotation = getApplicationTransactionalAnnotation(joinPoint);

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        def.setIsolationLevel(TransactionDefinition.ISOLATION_DEFAULT);
        def.setReadOnly(annotation != null && annotation.readOnly());

        TransactionStatus status = transactionManager.getTransaction(def);

        try {
            Object result = joinPoint.proceed();
            transactionManager.commit(status);
            return result;
        } catch (Exception ex) {
            transactionManager.rollback(status);
            throw ex;
        }
    }

    private ApplicationTransactional getApplicationTransactionalAnnotation(ProceedingJoinPoint joinPoint) {
        try {
            // First try to get from method
            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            ApplicationTransactional annotation = method.getAnnotation(ApplicationTransactional.class);

            if (annotation != null) {
                return annotation;
            }

            // If not found on method, try class
            return joinPoint.getTarget().getClass().getAnnotation(ApplicationTransactional.class);
        } catch (Exception e) {
            return null;
        }
    }
}