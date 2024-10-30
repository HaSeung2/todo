package com.sparta.todo.aop;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j(topic = "TodoAop")
@Aspect
@Component
@RequiredArgsConstructor
public class TodoAop {

    @Pointcut("execution(* com.sparta.todo.domain.user.controller..*(..))")
    public void user() {
    }

    @Pointcut("execution(* com.sparta.todo.domain.comment.controller..*(..))")
    public void comment() {
    }

    @Pointcut("execution(* com.sparta.todo.domain.manager.controller..*(..))")
    public void manager() {
    }

    @Pointcut("execution(* com.sparta.todo.domain.todo.controller..*(..))")
    public void todo() {
    }

    @Around("user() || todo() || comment() || manager()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        LocalDateTime requestTime = LocalDateTime.now();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Long userId = (Long) request.getAttribute("userId");
        log.info("Request : {} {} {} {}", request.getMethod(), request.getRequestURI(),
            params(joinPoint), requestTime);
        log.info("사용자 Id : {}", userId);

        Object obj = joinPoint.proceed();

        request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        log.info("Response : {} {}", request.getRequestURI(), obj);
        return obj;
    }

    private Map<String, Object> params(JoinPoint joinPoint) {
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        String[] paramNames = codeSignature.getParameterNames();
        Object[] paramValues = joinPoint.getArgs();
        Map<String, Object> params = new HashMap<>();
        for (int i = 0; i < paramNames.length; i++) {
            params.put(paramNames[i], paramValues[i]);
        }
        return params;
    }
}
