package com.eurodyn.qlack.util.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import java.util.concurrent.ConcurrentHashMap;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;


/**
 * An aspect to check {@link Predicate} in method arguments for nullability and
 * in such cases replace it with a non-query Predicate. Theoretically, this
 * should not be necessary according to https://jira.spring.io/browse/DATACMNS-1168,
 * however it does not seem to work.
 */
@Aspect
@Component
public class EmptyPredicateCheckAspect {

  // A local cache to not recheck methods for Predicate args.
  private ConcurrentHashMap<String, Byte> argsMap = new ConcurrentHashMap<>();

  @Around("@annotation(emptyPredicateCheck)")
  public Object fixEmptyPredicate(final ProceedingJoinPoint pjp,
    EmptyPredicateCheck emptyPredicateCheck)
    throws Throwable {
    final MethodSignature methodSignature = (MethodSignature) pjp
      .getSignature();
    Object[] args = pjp.getArgs();

    // Find the order for the Predicate argument in method's arguments.
    Byte predicateOrder = argsMap.get(methodSignature.toLongString());
    if (predicateOrder == null) {
      byte i = 0;
      for (Class c : methodSignature.getMethod().getParameterTypes()) {
        if (c.isAssignableFrom(Predicate.class)) {
          this.argsMap.put(methodSignature.toLongString(), i);
          predicateOrder = i;
          break;
        }
        i++;
      }
    }

    // Replace a null Predicate with a non-query one.
    if (predicateOrder != null && args[predicateOrder] == null) {
      args[predicateOrder] = new BooleanBuilder(null);
    }

    return pjp.proceed(args);
  }
}
