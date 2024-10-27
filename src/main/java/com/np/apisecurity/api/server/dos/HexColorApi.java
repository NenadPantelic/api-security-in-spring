package com.np.apisecurity.api.server.dos;

import com.np.apisecurity.dto.response.HexColor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@RestController
@RequestMapping("/api/dos/v1")
public class HexColorApi {

    private static final int COLORS_SIZE = 1_000_000;

    private List<HexColor> hexColors;

    public HexColorApi() {
        hexColors = IntStream.range(1, COLORS_SIZE)
                .boxed()// to get stream of Integer
                .parallel()
                .map(x -> new HexColor(x, getRandomHexColor()))
                .collect(Collectors.toList());

    }

    private String getRandomHexColor() {
        var randInt = ThreadLocalRandom.current().nextInt(0xffffff + 1);
        return String.format("#%06x", randInt);
    }

    // huge payload problem
    @GetMapping(value = "random-colors", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<HexColor> randomColors() {
        return hexColors;
    }
}
