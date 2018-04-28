package com.yumi.butler.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by teddyzhou on 2017/5/24.
 */
@Aspect
@Order(5)
@Component
public class WebLogAspect {
    private static final Logger logger = LoggerFactory
            .getLogger(WebLogAspect.class);

    ThreadLocal<Long> startTime = new ThreadLocal<Long>();

    @Pointcut("execution(public * com.iyumi.roomservice.web.*.*(..))")
    public void webLog(){}

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        startTime.set(System.currentTimeMillis());

        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 记录下请求内容
        Map<String, String> requestInfo = new HashMap<String, String>();
        requestInfo.put("URL ", request.getRequestURL().toString());
        requestInfo.put("IP ", request.getRemoteAddr());
        requestInfo.put("CLASS_METHOD ", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        requestInfo.put("ARGS ", Arrays.toString(joinPoint.getArgs()));
        logger.info(new ObjectMapper().writeValueAsString(requestInfo));

        //获取所有参数方法一：
        Enumeration<String> enu=request.getParameterNames();
        while(enu.hasMoreElements()){
            String paraName=(String)enu.nextElement();
            System.out.println(paraName+": "+request.getParameter(paraName));
        }
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        logger.info(new ObjectMapper().writeValueAsString(ret));
        //logger.info("耗时（毫秒） :" + (System.currentTimeMillis() - startTime.get()));
    }

    @AfterThrowing(throwing="ex",pointcut="execution(* com.iyumi.roomservice.web.*.*(..))")
    public void doRecoveryActions(Throwable ex){
        logger.error("目标方法中抛出的异常:"+ex);
        System.out.println("模拟抛出异常后的增强处理...");
    }
}
