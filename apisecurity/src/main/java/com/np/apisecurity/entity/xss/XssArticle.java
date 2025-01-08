package com.np.apisecurity.entity.xss;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class XssArticle {

    @Id
    private int id;
    private String content;
}
