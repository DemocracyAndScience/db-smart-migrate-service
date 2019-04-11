package com.system.utils.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.system.utils.Result;

/**
 * Dao层的aop
 * 
 * @author noah
 *
 */
@Component
@Aspect
public class ControllerExceptionAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionAspect.class);

	@Pointcut("execution(* com.system.controller.*.*(..))")
	public void exceptionPointCut() {
	}

	@Around(value = "com.system.utils.aspects.ControllerExceptionAspect.exceptionPointCut()")
	public Object around(ProceedingJoinPoint joinPoint) {
		
		Object proceed = null;
		try {
			try {
				proceed = joinPoint.proceed();
			} catch (Throwable e) {
				e.printStackTrace();
				LOGGER.info(e.getMessage());
				return Result.fail(e.getMessage());
			}
			return proceed;
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			return Result.fail(e.getMessage());
		}
	}
}
