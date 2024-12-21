package com.np.apisecurity.api.server.cors;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cors/v1")
public class MadMaxApi {

    @GetMapping(value = "/mad", produces = MediaType.TEXT_PLAIN_VALUE)
    @CrossOrigin(origins = "http://127.0.0.1:8080")
    public String mad() {
        return "MAD";
    }

    @PostMapping(value = "/max", produces = MediaType.TEXT_PLAIN_VALUE)
    @CrossOrigin(origins = {"http://127.0.0.1:8080", "http://192.168.0.21:8080"})
    public String max() {
        return "MAX";
    }
}
