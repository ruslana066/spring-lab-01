package kz.iitu.spring_lab_01.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HelloController {

    @Value("${app.owner:unknown}")
    private String owner;

    @GetMapping("/hello")
    public Greeting hello(@RequestParam(defaultValue = "world") String name) {
        return new Greeting("Hello, " + name + "!", owner, LocalDateTime.now());
    }

    @GetMapping("/info")
    public Info info() {
        return new Info(owner,
                System.getProperty("java.version"),
                Runtime.getRuntime().availableProcessors());
    }

    @GetMapping("/reverse")
    public Map<String, Object> reverse(@RequestParam String text) {
        String reversed = new StringBuilder(text).reverse().toString();

        return Map.of(
                "original", text,
                "reversed", reversed,
                "length", text.length()
        );
    }

    public record Greeting(String message, String owner, LocalDateTime timestamp) { }

    public record Info(String owner, String javaVersion, int cpuCores) { }
}
