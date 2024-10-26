package com.np.apisecurity.api.server.xss;

import com.np.apisecurity.dto.response.XssArticleSearchResult;
import com.np.apisecurity.repository.xss.XssArticleRepository;
import com.np.apisecurity.entity.xss.XssArticle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/xss/dangerous/v1/articles")
@CrossOrigin(origins = "http://localhost:3000")
public class XssArticleDangerousApi {

    private final XssArticleRepository xssArticleRepository;

    public XssArticleDangerousApi(XssArticleRepository xssArticleRepository) {
        this.xssArticleRepository = xssArticleRepository;
    }

    @PostMapping
    public String createArticle(@RequestBody XssArticle xssArticle) {
        log.info("Creating an article...");
        XssArticle savedArticle = xssArticleRepository.save(xssArticle);
        return savedArticle.getContent();
    }

    @GetMapping
    public XssArticleSearchResult search(@RequestParam String query) {
        log.info("Querying articles by query {}", query);
        var articles = xssArticleRepository.findByContentContainsIgnoreCase(query);
        return new XssArticleSearchResult(articles.size(), articles);
    }
}