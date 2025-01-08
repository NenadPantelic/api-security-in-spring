package com.np.apisecurity.dto.internal;


import lombok.Data;

@Data
public class JweData {

    private String username;
    private String dummyAttribute;

    public JweData(String username) {
        this.username = username;
    }

    public JweData(String username, String dummyAttribute) {
        this.username = username;
        this.dummyAttribute = dummyAttribute;
    }
}
