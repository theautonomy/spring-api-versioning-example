package com.example.versioning;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.example.versioning.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserApiRestClientTest {

    @LocalServerPort private int port;

    private RestClient restClient;

    @BeforeEach
    void setUp() {
        restClient = RestClient.builder().baseUrl("http://localhost:" + port).build();
    }

    @Test
    void testGetUsersVersion1_0() {
        List<User> users =
                restClient
                        .get()
                        .uri("/api/users")
                        .header("X-API-Version", "1.0")
                        .retrieve()
                        .body(new ParameterizedTypeReference<List<User>>() {});

        assertThat(users).isNotNull();
        assertThat(users).hasSize(2);
        assertThat(users.get(0).getUsername()).isEqualTo("john_doe");
        assertThat(users.get(0).getFullName()).isNull();
    }

    @Test
    void testGetUsersVersion1_1() {
        List<User> users =
                restClient
                        .get()
                        .uri(
                                uriBuilder ->
                                        uriBuilder
                                                .path("/api/users")
                                                .queryParam("page", 0)
                                                .queryParam("size", 10)
                                                .build())
                        .header("X-API-Version", "1.1")
                        .retrieve()
                        .body(new ParameterizedTypeReference<List<User>>() {});

        assertThat(users).isNotNull();
        assertThat(users).hasSize(2);
        assertThat(users.get(0).getUsername()).isEqualTo("john_doe");
        assertThat(users.get(0).getFullName()).isNull();
    }

    @Test
    void testGetUsersVersion2_0() {
        List<User> users =
                restClient
                        .get()
                        .uri("/api/users")
                        .header("X-API-Version", "2.0")
                        .retrieve()
                        .body(new ParameterizedTypeReference<List<User>>() {});

        assertThat(users).isNotNull();
        assertThat(users).hasSize(2);
        assertThat(users.get(0).getUsername()).isEqualTo("john_doe");
        assertThat(users.get(0).getFullName()).isEqualTo("John Doe");
    }

    @Test
    void testGetUserByIdVersion1_0() {
        User user =
                restClient
                        .get()
                        .uri("/api/users/{id}", 1L)
                        .header("X-API-Version", "1.0")
                        .retrieve()
                        .body(User.class);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUsername()).isEqualTo("john_doe");
        assertThat(user.getFullName()).isNull();
    }

    @Test
    void testGetUserByIdVersion2_0() {
        User user =
                restClient
                        .get()
                        .uri("/api/users/{id}", 1L)
                        .header("X-API-Version", "2.0")
                        .retrieve()
                        .body(User.class);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUsername()).isEqualTo("john_doe");
        assertThat(user.getFullName()).isEqualTo("John Doe");
    }

    @Test
    void testCreateUserVersion1_0() {
        User newUser = new User(null, "test_user", "test@example.com");

        User createdUser =
                restClient
                        .post()
                        .uri("/api/users")
                        .header("X-API-Version", "1.0")
                        .body(newUser)
                        .retrieve()
                        .body(User.class);

        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getId()).isEqualTo(3L);
        assertThat(createdUser.getUsername()).isEqualTo("test_user");
        assertThat(createdUser.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void testCreateUserVersion2_0() {
        User newUser = new User(null, "test_user", "test@example.com", "Test User");

        User createdUser =
                restClient
                        .post()
                        .uri("/api/users")
                        .header("X-API-Version", "2.0")
                        .body(newUser)
                        .retrieve()
                        .body(User.class);

        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getId()).isEqualTo(3L);
        assertThat(createdUser.getUsername()).isEqualTo("test_user");
        assertThat(createdUser.getFullName()).isEqualTo("Test User");
    }
}
