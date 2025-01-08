package com.np.apisecurity.entity.acl;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Builder
@Data
public class BasicAclUri {

    //@Id
    private int id;
    private String method;
    private String uri;
}

