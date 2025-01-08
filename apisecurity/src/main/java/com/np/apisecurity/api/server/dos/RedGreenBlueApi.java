package com.np.apisecurity.api.server.dos;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/dos/v1")
public class RedGreenBlueApi {

    @GetMapping("/green")
    public String green() {
        log.info("Green...");
        return "green";
    }

    @GetMapping("/blue")
    public String blue() {
        log.info("Green...");
        return "green";
    }

    @GetMapping("/red")
    public String red() {
        log.info("Red...");
        for (int i = Integer.MIN_VALUE; i < Integer.MAX_VALUE; i++) {
            // do nothing, just eat some CPU
        }
        return "red";
    }


}
