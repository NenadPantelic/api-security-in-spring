package com.np.apisecurity.dto.internal;


import lombok.Data;

@Data
public class RedisToken {

    private String username;
    private String dummyAttribute;

    public RedisToken(String username) {
        this.username = username;
    }

    public RedisToken(String username, String dummyAttribute) {
        this.username = username;
        this.dummyAttribute = dummyAttribute;
    }
}
