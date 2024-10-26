package com.np.apisecurity.api.server.xss;

import com.np.apisecurity.dto.response.XssArticleSearchResult;
import com.np.apisecurity.entity.xss.XssArticle;
import com.np.apisecurity.repository.xss.XssArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/xss/safe/v1/articles")
@CrossOrigin(origins = "http://localhost:3000")
public class XssArticleSafeApi {

    private final XssArticleRepository xssArticleRepository;

    public XssArticleSafeApi(XssArticleRepository xssArticleRepository) {
        this.xssArticleRepository = xssArticleRepository;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE) // validating the Content-Type explicitly
    public String createArticle(@RequestBody XssArticle xssArticle) {
        log.info("Creating an article...");
        XssArticle savedArticle = xssArticleRepository.save(xssArticle);
        return savedArticle.getContent();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public XssArticleSearchResult search(@RequestParam String query) {
        log.info("Querying articles by query {}", query);
        var articles = xssArticleRepository.findByContentContainsIgnoreCase(query);
        return new XssArticleSearchResult(articles.size(), articles);
    }
}
