package kz.iitu.springlab.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
@Order(4)
public class CallCounterAspect {

    private final ConcurrentHashMap<String, Integer> counters = new ConcurrentHashMap<>();

    @Before("kz.iitu.springlab.aspect.Pointcuts.serviceOperation()")
    public void countCall(JoinPoint jp) {
        String methodName = jp.getSignature().getName();
        counters.merge(methodName, 1, Integer::sum);
    }

    public Map<String, Integer> getStatistics() {
        return Map.copyOf(counters);
    }
}