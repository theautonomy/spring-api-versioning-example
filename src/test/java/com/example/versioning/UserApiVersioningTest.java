package com.example.versioning;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.example.versioning.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.ApiVersionInserter;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserApiVersioningTest {

    @LocalServerPort private int port;

    private UserService userService;

    @BeforeEach
    void setUp() {
        RestClient restClient =
                RestClient.builder()
                        .baseUrl("http://localhost:" + port)
                        .apiVersionInserter(ApiVersionInserter.useHeader("X-API-Version"))
                        .build();

        HttpServiceProxyFactory factory =
                HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient)).build();

        userService = factory.createClient(UserService.class);
    }

    @Test
    void testGetUsersVersion1_0() {
        List<User> users = userService.getUsers();

        assertThat(users).isNotNull();
        assertThat(users).hasSize(2);
        assertThat(users.get(0).getUsername()).isEqualTo("john_doe");
        assertThat(users.get(0).getFullName()).isNull(); // v1.0 doesn't have fullName
    }

    @Test
    void testGetUsersVersion1_1() {
        List<User> users = userService.getUsersV1_1();

        assertThat(users).isNotNull();
        assertThat(users).hasSize(2);
        assertThat(users.get(0).getUsername()).isEqualTo("john_doe");
        assertThat(users.get(0).getFullName()).isNull(); // v1.1 still doesn't have fullName
    }

    @Test
    void testGetUsersVersion2_0() {
        List<User> users = userService.getUsersV2();

        assertThat(users).isNotNull();
        assertThat(users).hasSize(2);
        assertThat(users.get(0).getUsername()).isEqualTo("john_doe");
        assertThat(users.get(0).getFullName()).isEqualTo("John Doe"); // v2.0 has fullName
    }

    @Test
    void testGetUserByIdVersion1_0() {
        User user = userService.getUserById(1L);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUsername()).isEqualTo("john_doe");
        assertThat(user.getFullName()).isNull(); // v1.0 doesn't have fullName
    }

    @Test
    void testGetUserByIdVersion2_0() {
        User user = userService.getUserByIdV2(1L);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUsername()).isEqualTo("john_doe");
        assertThat(user.getFullName()).isEqualTo("John Doe"); // v2.0 has fullName
    }

    @Test
    void testCreateUserVersion1_0() {
        User newUser = new User(null, "test_user", "test@example.com");
        User createdUser = userService.createUser(newUser);

        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getId()).isEqualTo(3L);
        assertThat(createdUser.getUsername()).isEqualTo("test_user");
        assertThat(createdUser.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void testCreateUserVersion2_0() {
        User newUser = new User(null, "test_user", "test@example.com", "Test User");
        User createdUser = userService.createUserV2(newUser);

        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getId()).isEqualTo(3L);
        assertThat(createdUser.getUsername()).isEqualTo("test_user");
        assertThat(createdUser.getFullName()).isEqualTo("Test User");
    }
}
