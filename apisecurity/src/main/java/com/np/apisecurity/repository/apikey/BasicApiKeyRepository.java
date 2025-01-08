package com.np.apisecurity.repository.apikey;

import com.np.apisecurity.entity.apikey.BasicApiKey;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface BasicApiKeyRepository extends CrudRepository<BasicApiKey, Integer> {

    Optional<BasicApiKey> findByApiKeyAndExpiresAtAfter(String apiKey, LocalDateTime now);
}
