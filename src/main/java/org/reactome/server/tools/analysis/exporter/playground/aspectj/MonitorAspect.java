package org.reactome.server.tools.analysis.exporter.playground.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */

@Aspect
public class MonitorAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorAspect.class);
    private static Object currentObject = null;

    /**
     * to set the point cut annotation class {@see Monitor}
     */
    @Pointcut("@annotation(monitor)")
    public void callAt(Monitor monitor) {
    }

    /**
     * this method is to callAt the performance of method that to render the report.
     *
     * @param joinPoint join point get from context can perform the target method.
     * @return return result will be ignore.
     * @throws Throwable
     */
    @Around("callAt(monitor)")
    public Object around(ProceedingJoinPoint joinPoint, Monitor monitor) throws Throwable {
        if (currentObject == null) {
            currentObject = joinPoint.getTarget();
        }

        long start = Instant.now().toEpochMilli();
        Object result = joinPoint.proceed();
        long end = Instant.now().toEpochMilli();

        if (joinPoint.getTarget() != null && !currentObject.equals(joinPoint.getTarget())) {
            LOGGER.info("Finish [{}] in [{}]ms", monitor.name(), end - start);
            currentObject = joinPoint.getTarget();
        }
        return result;
    }
}