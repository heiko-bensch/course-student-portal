package de.bensch.course;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

import java.util.Collections;

@TestConfiguration
public class TestOAuth2Config {

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        // Dummy-Client-Registrierung hinzufügen
        ClientRegistration clientRegistration = ClientRegistration
                .withRegistrationId("dummy")
                .clientId("dummy-client-id")
                .clientSecret("dummy-client-secret")
                .redirectUri("http://localhost:8080/login/oauth2/code/dummy")
                .authorizationGrantType(org.springframework.security.oauth2.core.AuthorizationGrantType.AUTHORIZATION_CODE)
                .tokenUri("https://dummy.token.uri")
                .authorizationUri("https://dummy.authorization.uri")
                .scope("read", "write")
                .clientName("Dummy Client")
                .build();

        return new InMemoryClientRegistrationRepository(Collections.singletonList(clientRegistration));
    }
}
