package com.alexm.bearspendings.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import static com.alexm.bearspendings.BearSpendingsApplication.API_URL;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.OK;

/**
 * @author AlexM
 * Date: 4/15/20
 **/
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebConfigTest {

    TestRestTemplate restTemplate;
    URL url;
    @LocalServerPort
    int port;

    @DisplayName("request of bills count requires to be authorized")
    @Test
    void authorizeAnyRequest() {
        restTemplate = new TestRestTemplate("admin", "manager");
        url = billsCountUrl();
        final ResponseEntity<String> responseEntity = restTemplate.getForEntity(url.toString(), String.class);
        assertEquals(OK, responseEntity.getStatusCode());
        assertFalse(Objects.requireNonNull(responseEntity.getBody()).contains("Sign in"));
    }

    @lombok.SneakyThrows()
    private URL billsCountUrl() {
        return new URL("http://localhost:" + port + API_URL + "bills/count");
    }

    @DisplayName("/login is public")
    @Test
    void loginIsPublic() throws MalformedURLException {
        restTemplate = new TestRestTemplate();
        url = new URL("http://localhost:" + port + "/login");
        final ResponseEntity<String> responseEntity = restTemplate.getForEntity(url.toString(), String.class);
        assertEquals(OK, responseEntity.getStatusCode());
        assertTrue(Objects.requireNonNull(responseEntity.getBody()).contains("Sign in"));
    }

    @DisplayName("request with wrong credentials - redirect to login")
    @Test
    void wrongCredentials() {
        restTemplate = new TestRestTemplate("bad user", "password");
        url = billsCountUrl();
        final ResponseEntity<String> responseEntity = restTemplate.getForEntity(url.toString(), String.class);
        assertTrue(Objects.requireNonNull(responseEntity.getBody()).contains("Sign in"));
    }

}