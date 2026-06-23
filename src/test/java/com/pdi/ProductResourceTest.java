package com.pdi;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductResourceTest {

    static Long createdId;

    // ── LIST ALL ──────────────────────────────────────────────────────────────

    @Test
    @Order(1)
    void listAll_shouldReturn200AndInitialProducts() {
        given()
            .when().get("/products")
            .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(3))
            .body("[0].name", not(emptyOrNullString()));
    }

    // ── FIND BY ID ────────────────────────────────────────────────────────────

    @Test
    @Order(2)
    void findById_shouldReturn200WhenFound() {
        given()
            .when().get("/products/1")
            .then()
            .statusCode(200)
            .body("id", is(1))
            .body("name", is("Notebook"))
            .body("price", notNullValue());
    }

    @Test
    @Order(3)
    void findById_shouldReturn404WhenNotFound() {
        given()
            .when().get("/products/9999")
            .then()
            .statusCode(404);
    }

    // ── CREATE ────────────────────────────────────────────────────────────────

    @Test
    @Order(4)
    void create_shouldReturn201AndPersistProduct() {
        String body = """
                {
                  "name": "Headset Gamer",
                  "description": "Headset com cancelamento de ruído",
                  "price": 399.90,
                  "active": true
                }
                """;

        createdId = given()
            .contentType(ContentType.JSON)
            .body(body)
            .when().post("/products")
            .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("name", is("Headset Gamer"))
            .body("description", is("Headset com cancelamento de ruído"))
            .body("price", is(399.90f))
            .body("active", is(true))
            .extract().jsonPath().getLong("id");
    }

    @Test
    @Order(5)
    void create_shouldPersistAndBeRetrievableById() {
        given()
            .when().get("/products/" + createdId)
            .then()
            .statusCode(200)
            .body("name", is("Headset Gamer"));
    }

    @Test
    @Order(6)
    void create_shouldAppearInListAll() {
        given()
            .when().get("/products")
            .then()
            .statusCode(200)
            .body("name", hasItem("Headset Gamer"));
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────

    @Test
    @Order(7)
    void update_shouldReturn200AndReflectChanges() {
        String body = """
                {
                  "name": "Headset Pro",
                  "description": "Headset sem fio com cancelamento ativo",
                  "price": 599.90,
                  "active": true
                }
                """;

        given()
            .contentType(ContentType.JSON)
            .body(body)
            .when().put("/products/" + createdId)
            .then()
            .statusCode(200)
            .body("name", is("Headset Pro"))
            .body("price", is(599.90f));
    }

    @Test
    @Order(8)
    void update_shouldReturn404WhenNotFound() {
        String body = """
                {
                  "name": "Inexistente",
                  "price": 1.00,
                  "active": false
                }
                """;

        given()
            .contentType(ContentType.JSON)
            .body(body)
            .when().put("/products/9999")
            .then()
            .statusCode(404);
    }

    // ── DELETE ────────────────────────────────────────────────────────────────

    @Test
    @Order(9)
    void delete_shouldReturn204AndRemoveProduct() {
        given()
            .when().delete("/products/" + createdId)
            .then()
            .statusCode(204);
    }

    @Test
    @Order(10)
    void delete_shouldMakeProductUnreachable() {
        given()
            .when().get("/products/" + createdId)
            .then()
            .statusCode(404);
    }

    @Test
    @Order(11)
    void delete_shouldReturn404WhenNotFound() {
        given()
            .when().delete("/products/9999")
            .then()
            .statusCode(404);
    }
}
