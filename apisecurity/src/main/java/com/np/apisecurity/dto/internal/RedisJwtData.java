package com.np.apisecurity.dto.internal;


import lombok.Data;

@Data
public class RedisJwtData {

    private String username;
    private String dummyAttribute;

    public RedisJwtData(String username) {
        this.username = username;
    }

    public RedisJwtData(String username, String dummyAttribute) {
        this.username = username;
        this.dummyAttribute = dummyAttribute;
    }
}
