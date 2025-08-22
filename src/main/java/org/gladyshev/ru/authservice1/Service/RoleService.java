package org.gladyshev.ru.authservice1.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
@Slf4j
public class RoleService {

    private final AuthService authService;
    private final Keycloak keycloak;
    @Value("${app.keycloak.realm}")
    private String realm;

    public void assignRole(String userId, String roleName) {
        UserResource user = authService.getUser(userId);
        RolesResource roles = getRolesResource();
        RoleRepresentation roleRepresentation = roles.get(roleName).toRepresentation();



        user.roles().realmLevel().add(Collections.singletonList(roleRepresentation));
    }

    private RolesResource getRolesResource() {
          return keycloak.realm(realm).roles();
    }
}
