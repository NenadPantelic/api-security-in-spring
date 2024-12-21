package com.np.apisecurity.entity.basicauth;

import com.np.apisecurity.entity.acl.BasicAclUserUriRef;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;

import java.util.Set;

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
//    @MappedCollection(idColumn = "id")
//    private Set<BasicAclUserUriRef> allowedUris;
    /*
    * Create these two users:
    * 1. User 1
    * Username: admin@apisecurity.com
    * Password plain: Blue181Ocean
    *
    * 2. User 2
    * Username: elsa@apisecurity.com
    * Password plain: Green529Forest
    *
    **** ACLs ****
    * Admin:
    * DELETE /one
    * POST /anypath/two
    * PUT /somepath/morepath/three
    * GET /anypath/evenmorepath/four
    *
    * Elsa:
    * GET /one
    * DELETE /one
    * PUT /somepath/morepath/three
    * */
}
