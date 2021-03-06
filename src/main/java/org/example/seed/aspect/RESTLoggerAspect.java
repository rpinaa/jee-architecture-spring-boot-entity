package org.example.seed.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Arrays;

/**
 * Created by Ricardo Pina Arellano on 21/01/2017.
 */
@Aspect
@Component
public class RESTLoggerAspect {

  private final Logger log = LoggerFactory.getLogger(this.getClass());
  private final ObjectMapper objectMapper = new ObjectMapper();

  public RESTLoggerAspect() {
    this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
  }

  @Pointcut("within(org.example.seed.rest..*)")
  public void rest() { }

  @Pointcut("execution(public * *(..))")
  protected void allMethod() { }

  @Before("rest() && allMethod()")
  public void logBefore(final JoinPoint joinPoint) throws JsonProcessingException {

    log.warn("Entering in Method :  " + joinPoint.getSignature().getName());
    log.warn("Class Name :  " + joinPoint.getSignature().getDeclaringTypeName());
    log.warn("Arguments :  " + this.objectMapper.writeValueAsString(joinPoint.getArgs()));
  }

  @AfterReturning(pointcut = "rest() && allMethod()", returning = "deferred")
  public void logAfter(final JoinPoint joinPoint, final Object deferred) throws Exception {

    final DeferredResult<Object> response = (DeferredResult<Object>) deferred;

    log.warn("Method Return value : " + this.objectMapper.writeValueAsString(response.getResult()));
  }

  @AfterThrowing(pointcut = "rest() && allMethod()", throwing = "exception")
  public void logAfterThrowing(final JoinPoint joinPoint, final Throwable exception) {


    log.error("An exception has been thrown in " + joinPoint.getSignature().getName() + " ()");
    log.error("Cause : " + Arrays.toString(exception.getStackTrace()));
  }

  @Around("rest() && allMethod()")
  public Object logAround(final ProceedingJoinPoint joinPoint) throws Throwable {

    final long start = System.currentTimeMillis();

    try {
      final String className = joinPoint.getSignature().getDeclaringTypeName();
      final String methodName = joinPoint.getSignature().getName();
      final Object result = joinPoint.proceed();
      final long elapsedTime = System.currentTimeMillis() - start;

      log.warn("Method " + className + "." + methodName + " ()" + " execution time : " + elapsedTime + " ms");

      return result;
    } catch (final IllegalArgumentException e) {
      log.error("Illegal argument " + Arrays.toString(joinPoint.getArgs()) + " in " + joinPoint.getSignature().getName() + "()");

      throw e;
    }
  }
}
