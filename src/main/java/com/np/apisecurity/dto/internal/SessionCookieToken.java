package com.np.apisecurity.dto.internal;


import lombok.Data;

@Data
public class SessionCookieToken {

    private String username;
    private String dummyAttribute;

    public SessionCookieToken(String username) {
        this.username = username;
    }

    public SessionCookieToken(String username, String dummyAttribute) {
        this.username = username;
        this.dummyAttribute = dummyAttribute;
    }
}
