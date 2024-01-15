package com.example.resiliencejpa;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

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
  public Object repositoryAround(ProceedingJoinPoint pjp) {
    if (log.isTraceEnabled()) {
      log.trace("JPA Circuit Breaker for - {}, method - {}", pjp.getSignature().getDeclaringType().getName(), pjp.getSignature().getName());
    }

    return CircuitBreaker.decorateSupplier(circuitBreaker, () -> {
      try {
        return pjp.proceed();
      } catch (Throwable e) {
        log.error("JPA Circuit Breaker for - %s, method - %S - throw an Exception".formatted(pjp.getSignature().getDeclaringType().getName(), pjp.getSignature().getName()), e);
        //FIXME
        throw new RuntimeException(e);
      }
    }).get();
  }

}