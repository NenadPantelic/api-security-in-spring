package com.np.apisecurity.oauth.api.server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class OktaController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping(value = "/welcome")
    public String welcome() {
        return "welcome";
    }
}
