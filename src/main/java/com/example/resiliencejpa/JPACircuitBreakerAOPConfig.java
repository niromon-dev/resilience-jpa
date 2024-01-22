package com.example.resiliencejpa;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.net.ConnectException;

@Aspect
@Component
@EnableAspectJAutoProxy
@Slf4j
public class JPACircuitBreakerAOPConfig {

  private final CircuitBreaker circuitBreaker;

  JPACircuitBreakerAOPConfig(CircuitBreakerRegistry circuitBreakerRegistry){
    this.circuitBreaker = circuitBreakerRegistry.circuitBreaker("jpa");
  }

  /*
   * @Repository Annotation
   */
  @Pointcut("@within(org.springframework.stereotype.Repository)")
  public void repositoryAnnotationPointCut() {}

  /*
   * JpaRepository Interface
   */
  @Pointcut("execution(* org.springframework.data.jpa.repository.JpaRepository+.*(..))")
  public void jpaRepositoryInterfacePointCut() {}

  /*
   * CrudRepository Interface
   */
  @Pointcut("execution(* org.springframework.data.repository.CrudRepository+.*(..))")
  public void crudRepositoryAnnotationPointCut() {}

  @Around("crudRepositoryAnnotationPointCut() || jpaRepositoryInterfacePointCut() || repositoryAnnotationPointCut()")
  public Object repositoryAround(ProceedingJoinPoint pjp) throws Throwable {
    if (log.isTraceEnabled()) {
      log.trace("JPA Circuit Breaker for - {}, method - {}", pjp.getSignature().getDeclaringType().getName(), pjp.getSignature().getName());
    }

    return CircuitBreaker.decorateCheckedSupplier(circuitBreaker, () -> {
      try {
        return pjp.proceed();
      } catch (Throwable e) {
        log.debug("JPA Circuit Breaker for - %s, method - %S - throw an Exception".formatted(pjp.getSignature().getDeclaringType().getName(), pjp.getSignature().getName()), e);
        // Filter only Database connection issues
        if (e.getCause() instanceof ConnectException || e.getCause() instanceof JDBCConnectionException) {
          log.error("Database Access issue detected, a " + DatabaseAccessException.class.getSimpleName() + " will be thrown", e);
          throw new DatabaseAccessException(e);
        } else {
          throw e;
        }
      }
    }).get();
  }

}