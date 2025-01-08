package com.np.apisecurity.repository.basicauth;

import com.np.apisecurity.entity.basicauth.BasicAuthUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BasicAuthUserRepository extends CrudRepository<BasicAuthUser, Integer> {

    Optional<BasicAuthUser> findByUsername(String encryptedUsername);
}
