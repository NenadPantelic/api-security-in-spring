package com.np.apisecurity.api.server.cookieexample;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cookie-example/v1")
public class CookieExampleApi {

    @GetMapping(value = "/cookie", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> cookie() {

        var cookie = ResponseCookie
                .from("myCookie", "dummyCookieValue")
                .sameSite("None") // SameSite - None
                .secure(true) // only https
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("cookie");
    }
}
