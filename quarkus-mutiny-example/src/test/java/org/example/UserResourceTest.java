package org.example;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@Testcontainers
public class UserResourceTest {


    @Container
    static PostgreSQLContainer<?> db = new PostgreSQLContainer<>("postgres:15")
        .withDatabaseName("mydb")
        .withUsername("myuser")
        .withPassword("mypassword");

    static {
        // Start the container explicitly
        db.start();

        // Configure Quarkus to use the Testcontainer
        System.setProperty("quarkus.datasource.reactive.url", "postgresql://" + db.getHost() + ":" + db.getMappedPort(5432) + "/" + db.getDatabaseName());
        System.setProperty("quarkus.datasource.username", db.getUsername());
        System.setProperty("quarkus.datasource.password", db.getPassword());
    }

    @Test
    public void testCreateUser() {
        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body("{\"name\":\"Alice\"}")
        .when()
            .post("/users")
        .then()
            .statusCode(200)
            .body("name", is("Alice"));
    }

    @Test
    public void testGetAllUsers() {
        // First create a user
        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body("{\"name\":\"Bob\"}")
        .when()
            .post("/users");

        // Then verify we can get all users
        given()
        .when()
            .get("/users")
        .then()
            .statusCode(200);
    }
}
