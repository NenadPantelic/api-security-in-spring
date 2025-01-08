package com.np.apisecurity.dto.response;

import com.np.apisecurity.entity.xss.XssArticle;

import java.util.List;

public record XssArticleSearchResult(int count,
                                     List<XssArticle> result) {
}
