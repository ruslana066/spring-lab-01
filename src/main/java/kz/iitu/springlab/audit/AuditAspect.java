package kz.iitu.springlab.audit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Aspect
@Component
@Order(1)
public class AuditAspect {

    private static final Logger log = LoggerFactory.getLogger(AuditAspect.class);

    @Around("@annotation(audited)")
    public Object audit(ProceedingJoinPoint pjp, Audited audited) throws Throwable {
        String action = audited.action();
        String timestamp = Instant.now().toString();

        log.info("[AUDIT] start {}", action);

        try {
            Object result = pjp.proceed();

            log.info("[AUDIT] {} success", action);

            return result;
        } catch (Throwable ex) {
            log.info("[AUDIT] {} failure error={}", action, ex.getMessage());

            throw ex;
        }
    }
}
