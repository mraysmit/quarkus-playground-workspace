# Mutiny Quarkus Users API

This is a sample **Quarkus** application demonstrating reactive REST APIs using **SmallRye Mutiny**, **Hibernate Reactive with Panache**, and **PostgreSQL**.

It includes:

- ✅ Reactive REST endpoints using `Uni` and `Multi`
- ✅ Integration with PostgreSQL using Hibernate Reactive
- ✅ A Server-Sent Events (SSE) endpoint to demonstrate streaming with `Multi`
- ✅ Instructions to run with **Testcontainers** for local testing

---

## 📦 Tech Stack

- Java 17+
- Quarkus 3.8+
- RESTEasy Reactive
- SmallRye Mutiny
- Hibernate Reactive + Panache
- PostgreSQL
- Testcontainers (for integration testing)

---

## ▶️ How to Run the App

### 🧩 Prerequisites

- Java 17+
- Maven 3.8+
- Docker (for Testcontainers)

### 🔧 Configuring PostgreSQL

This app expects a PostgreSQL instance running on:

- URL: `localhost:5432`
- DB: `mydb`
- Username: `myuser`
- Password: `mypassword`

You can use Docker to start a PostgreSQL container:

```bash
docker run --name pg -e POSTGRES_USER=myuser \
  -e POSTGRES_PASSWORD=mypassword \
  -e POSTGRES_DB=mydb \
  -p 5432:5432 -d postgres:15
```

---

### 🚀 Run in Dev Mode

```bash
./mvnw quarkus:dev
```

Access:

- [http://localhost:8080/users](http://localhost:8080/users) – REST API
- [http://localhost:8080/users/stream](http://localhost:8080/users/stream) – SSE stream
- [http://localhost:8080/q/dev](http://localhost:8080/q/dev) – Dev UI

---

## 🧪 Running Tests with Testcontainers

Add this to your `pom.xml`:

```xml
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>postgresql</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-junit5</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>rest-assured</artifactId>
    <scope>test</scope>
</dependency>
```

Create a test class like this:

```java
@QuarkusTest
@Testcontainers
public class UserResourceTest {

    @Container
    static PostgreSQLContainer<?> db = new PostgreSQLContainer<>("postgres:15")
        .withDatabaseName("mydb")
        .withUsername("myuser")
        .withPassword("mypassword");

    @DynamicPropertySource
    static void configure(PropertiesRegistry registry) {
        registry.add("quarkus.datasource.reactive.url", db::getJdbcUrl);
        registry.add("quarkus.datasource.username", db::getUsername);
        registry.add("quarkus.datasource.password", db::getPassword);
    }

    @Test
    public void testCreateUser() {
        given()
            .contentType("application/json")
            .body("{\"name\":\"Alice\"}")
        .when()
            .post("/users")
        .then()
            .statusCode(200)
            .body("name", is("Alice"));
    }
}
```

Then run tests:

```bash
./mvnw test
```

---

## 📂 Project Structure

```
mutiny-quarkus-users/
├── src/
│   ├── main/
│   │   ├── java/org/example/
│   │   │   ├── User.java
│   │   │   └── UserResource.java
│   │   └── resources/
│   │       └── application.properties
├── pom.xml
└── README.md
```

---

## 🧼 Cleaning Up

To stop and remove the Docker container:

```bash
docker rm -f pg
```

---

## 🧠 Notes

- `Uni<T>` is used for async single-result APIs
- `Multi<T>` is used for streaming multiple results (like SSE)
- Quarkus automatically serializes `Uni` and `Multi` results as JSON
- Reactive DB operations are non-blocking

---

## ✨ Resources

- [Mutiny docs](https://smallrye.io/smallrye-mutiny/)
- [Quarkus reactive guide](https://quarkus.io/guides/reactive)
- [Hibernate Reactive](https://hibernate.org/reactive/)
- [Testcontainers](https://www.testcontainers.org/)