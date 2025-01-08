package com.np.apisecurity.api.server.auth.acl;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/acl/v1")
public class BasicAclApi {

    @GetMapping(value = "/one", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getOne() {
        return "GET One";
    }

    @DeleteMapping(value = "/one", produces = MediaType.TEXT_PLAIN_VALUE)
    public String deleteOne() {
        return "DELETE One";
    }

    @PostMapping(value = "/anypath/two", produces = MediaType.TEXT_PLAIN_VALUE)
    public String postTwo() {
        return "GET Two";
    }

    @PutMapping(value = "/somepath/morepath/three", produces = MediaType.TEXT_PLAIN_VALUE)
    public String putThree() {
        return "PUT Three";
    }

    @GetMapping(value = "/anypath/evenmorepath/four", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getFour() {
        return "GET Four";
    }
}
