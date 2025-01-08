package com.np.apisecurity.oauth.api.server;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api/math")
public class MathApi {

    @GetMapping("/random")
    public String random() {
        return String.format("Random number: %s", ThreadLocalRandom.current().nextInt());
    }

    @GetMapping(value = "/add", produces = MediaType.TEXT_PLAIN_VALUE)
    @PreAuthorize("hasAuthority('SCOPE math:add')")
    public String add(@RequestParam("a") int a, @RequestParam("b") int b) {
        return String.format("Result: %d", a + b);
    }

    @GetMapping(value = "/subtract", produces = MediaType.TEXT_PLAIN_VALUE)
    @PreAuthorize("hasAuthority('SCOPE math:subtract')")
    public String subtract(@RequestParam("a") int a, @RequestParam("b") int b) {
        return String.format("Result: %d", a - b);
    }

    @GetMapping(value = "/multiply", produces = MediaType.TEXT_PLAIN_VALUE)
    @PreAuthorize("hasAuthority('SCOPE math:multiply')")
    public String multiply(@RequestParam("a") int a, @RequestParam("b") int b) {
        return String.format("Result: %d", a * b);
    }

    @GetMapping(value = "/divide", produces = MediaType.TEXT_PLAIN_VALUE)
    @PreAuthorize("hasAuthority('SCOPE math:divide')")
    public String divide(@RequestParam("a") int a, @RequestParam("b") int b) {
        return String.format("Result: %d", a / b);
    }

}
