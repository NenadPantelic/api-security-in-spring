package com.np.apisecurity.api.server.xss;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.Tika;
import org.owasp.encoder.Encode;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/xss/safe/v1")
@CrossOrigin(origins = "http://localhost:3000")
@Validated
public class XssSimpleSafeApi {

    private static final Tika TIKA = new Tika();

    @GetMapping(value = "/greeting", produces = MediaType.TEXT_PLAIN_VALUE)
    public String greeting(
//            @Valid @Pattern(regexp = "[A-Za-z]{5,20}")
            @RequestParam(value = "name") String name) {
        var nowHour = LocalTime.now().getHour();
        String periodOfDay = "night";

        if (nowHour >= 5 && nowHour < 10) {
            periodOfDay = "morning";
        } else if (nowHour >= 10 && nowHour < 19) {
            periodOfDay = "day";
        }

        var greeting = String.format("Good %s %s", periodOfDay, name);
        return Encode.forHtml(greeting);
    }

    @GetMapping(value = "/file")
    public ResponseEntity<Resource> downloadFile() throws IOException {
        var resource = new ClassPathResource("static/file-with-xss.csv");
        var determinedContentType = TIKA.detect(resource.getInputStream());
        // if it is unknown or unsafe, return it as plain text
        if (StringUtils.isBlank(determinedContentType) ||
                StringUtils.equalsIgnoreCase(determinedContentType, MediaType.TEXT_HTML_VALUE)) {
            determinedContentType = MediaType.TEXT_PLAIN_VALUE;
        }

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(determinedContentType))
                .body(resource);
    }
}
