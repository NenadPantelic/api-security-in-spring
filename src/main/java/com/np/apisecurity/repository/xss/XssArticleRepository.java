package com.np.apisecurity.repository.xss;

import org.springframework.data.repository.CrudRepository;
import com.np.apisecurity.entity.xss.XssArticle;

import java.util.List;

public interface XssArticleRepository extends CrudRepository<XssArticle, Integer> {

    List<XssArticle> findByContentContainsIgnoreCase(String query);
}
