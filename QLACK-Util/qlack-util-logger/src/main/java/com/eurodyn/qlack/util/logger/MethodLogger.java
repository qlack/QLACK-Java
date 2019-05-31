package com.eurodyn.qlack.util.logger;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Logs method execution for debugging purposes
 *
 * @author European Dynamics
 */
@Slf4j
@Aspect
@Component
public class MethodLogger {

  /**
   * Enable performance log (method execution time in msec)
   */
  @Value("${qlack.util.logger.logPerformance:null}")
  private Boolean logPerformance;

  /**
   * Enable logging actual parameter values. If disabled the parameters type is shown
   */
  @Value("${qlack.util.logger.logParamValues:null}")
  private Boolean logParamValues;

  /**
   * Includes all {@link Logged} annotated methods
   */
  @Pointcut("execution(@Logged * *.*(..))")
  public void loggedAnnotatedMethod() {
  }

  /**
   * Includes all methods of a {@link Logged} annotated class
   */
  @Pointcut("execution(* (@Logged *).*(..))")
  public void loggedMethodOfAnnotatedClass() {
  }

  @Around("loggedMethodOfAnnotatedClass() && !loggedAnnotatedMethod() && @within(Logged)")
  private Object logMethodOfAnnotatedClass(ProceedingJoinPoint point) throws Throwable {
    return logged(point);
  }

  @Around("loggedAnnotatedMethod() && @annotation(Logged)")
  private Object logAnnotatedMethod(ProceedingJoinPoint point) throws Throwable {
    return logged(point);
  }


  /**
   * Logs method execution, parameters and performance (in msec) in the following format
   * mypackage.MyClass##method([params]): in XX[msec]
   *
   * @param point the {@link ProceedingJoinPoint}
   *
   * @return the result of the execution
   *
   * @throws Throwable in case of error during {@link ProceedingJoinPoint#proceed()}
   */
  private Object logged(ProceedingJoinPoint point) throws Throwable {
    long start = System.currentTimeMillis();
    Object result = point.proceed();

    Method method = ((MethodSignature) point.getSignature()).getMethod();
    String methodName = method.getName();
    String className = method.getDeclaringClass().getName();

    boolean annoParamValues = method.getAnnotation(Logged.class) != null
      ? method.getAnnotation(Logged.class).paramValues()
      : method.getDeclaringClass().getAnnotation(Logged.class).paramValues();

    boolean annoPerformance = method.getAnnotation(Logged.class) != null
      ? method.getAnnotation(Logged.class).performance()
      : method.getDeclaringClass().getAnnotation(Logged.class).performance();

    Object[] typeOrValArray;

    typeOrValArray = checkPrecedence(logParamValues, annoParamValues)
      ? point.getArgs()
      : method.getParameterTypes();

    if (checkPrecedence(logPerformance, annoPerformance)) {
      log.debug(String.format("%s##%s(%s): in %s[msec]",
                              className,
                              methodName,
                              Arrays.toString(typeOrValArray),
                              System.currentTimeMillis() - start
      ));
    } else {
      log.debug(String.format("%s##%s(%s)",
                              className,
                              methodName,
                              Arrays.toString(typeOrValArray)
      ));

    }
    return result;
  }

  /**
   * Checks precedence between flags declared in application.properties (global)
   * and flags declared as parameters of the {@link com.eurodyn.qlack.util.logger.Logged} annotation.
   *
   * @param globalProp property flag declared in application.properties file
   * @param annotationProp property flag declared as parameter of the {@link com.eurodyn.qlack.util.logger.Logged}
   * annotation.
   *
   * @return true/false
   */
  private boolean checkPrecedence(Boolean globalProp, Boolean annotationProp) {
    if (globalProp != null) {
      if (!annotationProp.equals(globalProp)) {
        return annotationProp;
      }
      return globalProp;
    }
    return annotationProp;
  }
}
