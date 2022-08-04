package com.softline.csrv.security;

import io.jmix.rest.security.role.RestMinimalRole;
import io.jmix.security.model.SecurityScope;
import io.jmix.security.role.annotation.ResourceRole;

@ResourceRole(name = "PUZHC: rest minimal access", code = PuzhcRestMinimalAccessRole.CODE, scope = SecurityScope.API)
public interface PuzhcRestMinimalAccessRole extends PuzhcMinimalAccessRole, RestMinimalRole {

    String CODE = "puzhc-rest-minimal";

}