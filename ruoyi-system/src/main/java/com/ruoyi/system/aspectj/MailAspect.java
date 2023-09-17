package com.ruoyi.system.aspectj;

import com.ruoyi.common.core.service.MailService;
import com.ruoyi.system.handle.MailContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 注入Mail上下文对象
 *
 * @author hexm
 * @date 2023/2/3
 */
@Slf4j
@Aspect
@Component
public class MailAspect {

    @Autowired
    private MailService mailService;

    @Around("@within(mailContextCache) || @annotation(mailContextCache)")
    public Object process(ProceedingJoinPoint point, MailContextCache mailContextCache) throws Throwable {
        try {
            MailContextHolder.setAccount(mailService.getAccount());
            return point.proceed();
        } finally {
            MailContextHolder.removeAccount();
        }
    }
}
