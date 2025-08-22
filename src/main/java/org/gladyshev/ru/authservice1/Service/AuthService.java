package org.gladyshev.ru.authservice1.Service;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gladyshev.ru.authservice1.DTO.UserRegistrationDTO;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {


    private final Keycloak keycloak;
    @Value("${app.keycloak.realm}")
    private String realm;

    public void registration(UserRegistrationDTO dto) {
        try {
            Map<String, List<String>> attributes = new HashMap<>();
            attributes.put("phone", List.of(dto.getPhone()));

            UserRepresentation user = new UserRepresentation();
            user.setEnabled(true);
            user.setUsername(dto.getUsername());
            user.setEmail(dto.getEmail());
            user.setEmailVerified(false);
            user.setAttributes(attributes);

            CredentialRepresentation cred = new CredentialRepresentation();
            cred.setValue(dto.getPassword());
            cred.setType(CredentialRepresentation.PASSWORD);
            cred.setTemporary(false);
            user.setCredentials(List.of(cred));



            UsersResource usersResource = getUsersResourse();
            Response response = usersResource.create(user);

            log.info("Status code: {}", response.getStatus());

            if (response.getStatus() == 201) {
                log.info("New user has been created successfully");
                // НЕ бросаем исключение при успехе!
            } else {
                // Читаем тело ошибки из response
                String errorMessage = response.readEntity(String.class);
                log.error("Failed to create user. Status: {}, Error: {}",
                        response.getStatus(), errorMessage);
                throw new RuntimeException("Failed to create user: " + errorMessage);
            }

        } catch (Exception e) {
            log.error("Error in user registration: {}", e.getMessage(), e);
            throw new RuntimeException("Registration failed: " + e.getMessage(), e);
        }
    }

    public void sendVerificationEmail(String userId) {
        UsersResource usersResource = getUsersResourse();
        UserResource user = usersResource.get(userId);
        user.sendVerifyEmail();

    }

    public void deleteUser(String userId) {
        UsersResource usersResource = getUsersResourse();
        usersResource.delete(userId);
    }

    public void forgotPassword(String email) {
        UsersResource usersResource = getUsersResourse();
        List<UserRepresentation> user = usersResource.searchByEmail(email, true);
        UserRepresentation userRepresentation = user.get(0);

        UserResource userResource = usersResource.get(userRepresentation.getId());

        userResource.executeActionsEmail(List.of("UPDATE_PASSWORD"));
    }

    public UserResource getUser(String userId) {
        UsersResource usersResource = getUsersResourse();

        return usersResource.get(userId);
    }

    private UsersResource getUsersResourse(){
      return   keycloak.realm(realm)
                .users();
    }




}
