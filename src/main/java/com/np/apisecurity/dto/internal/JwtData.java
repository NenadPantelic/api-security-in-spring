package com.np.apisecurity.dto.internal;


import lombok.Data;

@Data
public class JwtData {

    private String username;
    private String dummyAttribute;

    public JwtData(String username) {
        this.username = username;
    }

    public JwtData(String username, String dummyAttribute) {
        this.username = username;
        this.dummyAttribute = dummyAttribute;
    }
}
