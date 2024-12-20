package com.np.apisecurity.entity.basicauth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BasicAuthUser {

    @Id
    private int id;
    private String username;
    private String passwordHash;
    private String salt;
    private String displayName;
}
