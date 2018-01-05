package org.reactome.server.tools.analysis.exporter.playground.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */

@Aspect
public class MonitorAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorAspect.class);

    @Pointcut("@annotation(org.reactome.server.tools.analysis.exporter.playground.aspectj.Monitor)")
    public void check() {
    }

    @Around("org.reactome.server.tools.analysis.exporter.playground.aspectj.MonitorAspect.check()")
    public void checkCall(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = Instant.now().toEpochMilli();
        joinPoint.proceed();
        long end = Instant.now().toEpochMilli();
        LOGGER.info("finish {} in {}ms", joinPoint.getTarget().getClass().getSimpleName(), end - start);
    }
}
