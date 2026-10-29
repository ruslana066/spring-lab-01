package kz.iitu.springlab.web;

import kz.iitu.springlab.aspect.CallCounterAspect;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AspectStatsController {

    private final CallCounterAspect callCounterAspect;

    public AspectStatsController(CallCounterAspect callCounterAspect) {
        this.callCounterAspect = callCounterAspect;
    }

    @GetMapping("/api/lab4/stats")
    public Map<String, Integer> statistics() {
        return callCounterAspect.getStatistics();
    }
}