package com.np.apisecurity.api.server.xss;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;

@RestController
@RequestMapping("/api/xss/dangerous/v1")
@CrossOrigin(origins = "http://localhost:3000")
public class XssSimpleDangerousApi {

    @GetMapping(value = "/greeting")
    public String greeting(@RequestParam(value = "name") String name) {
        var nowHour = LocalTime.now().getHour();
        String periodOfDay = "night";

        if (nowHour >= 5 && nowHour < 10) {
            periodOfDay = "morning";
        } else if (nowHour >= 10 && nowHour < 19) {
            periodOfDay = "day";
        }

        return String.format("Good %s %s", periodOfDay, name);
    }

    @GetMapping(value = "/file")
    public Resource downloadFile() {
        return new ClassPathResource("static/file-with-xss.csv");
    }
}
