package org.gladyshev.ru.authservice1.Config;


import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    @Value("${app.keycloak.serverUrl}")
    public String serverUrl;

    @Value("${app.keycloak.realm}")
    public String realm;

    @Value("${app.keycloak.admin.clientId}")
    public String clientId;

    @Value("${app.keycloak.admin.clientSecret}")
    public String clientSecret;

    @Bean
    public Keycloak keycloak(){

           return KeycloakBuilder.builder()
                   .clientSecret(clientSecret)
                   .clientId(clientId)
                   .grantType("client_credentials")
                   .realm(realm)
                   .serverUrl(serverUrl)
                   .build();
    }
}
