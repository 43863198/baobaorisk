/**
 * 
 */
package com.aizhixin.baobaorisk.common.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 统一日志记录配置，目前只记录的web端入口地方的日志
 * 日志拦截器
 * @author zhen.pan
 *
 */
@Aspect
@Component
@Slf4j
public class AspectLog {

    @Pointcut("execution(public * com.aizhixin..controller..*Controller.*(..))")
    public void aspectLog(){}


    @Around("aspectLog()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            long starttime = System.currentTimeMillis();
//            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//            RequestMapping rm = (RequestMapping)methodSignature.getDeclaringType().getAnnotation(RequestMapping.class);
//            StringBuilder sb = new StringBuilder();
//            if (null != rm) {
//                String[] hs = rm.value();
//                if (null != hs && hs.length > 0) {
//                    sb.append(hs[0]);
//                }
//            }
//            Method method = methodSignature .getMethod();
//            rm = method.getAnnotation(RequestMapping.class);
//            if (null != rm) {
//                String[] vs = rm.value();
//                if (null != vs && vs.length > 0) {
//                    sb.append(vs[0]);
//                }
//            }
//            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();//获取request
//            String head = sb.append(" ").append(request.getServletPath()).toString();

            Object result = joinPoint.proceed();

            if(log.isDebugEnabled()) {
                log.debug("{} {} Params[{}] Response[{}] speed time:[{}]ms", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()), result, (System.currentTimeMillis() - starttime));
            }
            return result;
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    @AfterThrowing(pointcut = "aspectLog()",throwing= "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        log.warn("{} {} Params[{}] ,throws: [{}]", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()), error.getMessage());
    }
}
