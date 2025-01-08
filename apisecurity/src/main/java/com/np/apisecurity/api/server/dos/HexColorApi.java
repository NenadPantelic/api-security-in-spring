package com.np.apisecurity.api.server.dos;

import com.np.apisecurity.dto.response.HexColor;
import com.np.apisecurity.dto.response.HexColorPaginationResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@RestController
@RequestMapping("/api/dos/v1")
@Validated
public class HexColorApi {

    private static final int COLORS_SIZE = 1_000_000;

    private final List<HexColor> hexColors;

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

    @GetMapping(value = "/random-colors-pagination", produces = MediaType.APPLICATION_JSON_VALUE)
    public HexColorPaginationResponse randomColors(@RequestParam(name = "page") int page,
                                                   @Valid @Min(0) @Max(100) @RequestParam(name = "size") int size) {
        var startIndex = (page - 1) * size;
        var sublist = hexColors.subList(startIndex, startIndex + size);


        return new HexColorPaginationResponse(
                sublist,
                page,
                size,
                (int) Math.ceil(1.0 * COLORS_SIZE / size)
        );
    }
}
