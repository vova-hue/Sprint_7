package client;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.Courier;
import ru.yandex.praktikum.Credentials;
import ru.yandex.praktikum.Order;

import static io.restassured.RestAssured.given;

public class REST {
    private String baseURI;

    public REST(String baseURI) {
        this.baseURI = baseURI;
    }

    @Step("Создание курьера")
    public ValidatableResponse create(Courier courier) {
        return given()
                .filter(new AllureRestAssured())
                .log()
                .all()
                .baseUri(baseURI)
                .header("Content-Type", "application/json")
                .body(courier)
                .post("/api/v1/courier")
                .then()
                .log()
                .all();
    }

    @Step("Создание курьера без обязательных параметров")
    public ValidatableResponse create(Credentials credentials) {

        return given()
                .filter(new AllureRestAssured())
                .log()
                .all()
                .baseUri(baseURI)
                .header("Content-Type", "application/json")
                .body(credentials)
                .post("/api/v1/courier")
                .then()
                .log()
                .all();
    }

    @Step("Проверка наличия курьера в системе")
    public ValidatableResponse loginCourier(Credentials credentials) {
        return given()
                .filter(new AllureRestAssured())
                .log()
                .all()
                .baseUri(baseURI)
                .header("Content-Type", "application/json")
                .body(credentials)
                .post("/api/v1/courier/login")
                .then()
                .log()
                .all();
    }

    @Step("Удаление курьера")
    public ValidatableResponse deleteCourier(String id) {
        return given()
                .log()
                .all()
                .baseUri(baseURI)
                .delete("/api/v1/courier/" + id)
                .then()
                .log()
                .all();
    }

    @Step("Создание заказа")
    public ValidatableResponse orderCreate(Order order) {
        return given()
                .log()
                .all()
                .baseUri(baseURI)
                .header("Content-Type", "application/json")
                .body(order)
                .post("/api/v1/orders")
                .then()
                .log()
                .all();
    }

    @Step("Получение списка заказов")
    public ValidatableResponse getOrders() {
        return given()
                .filter(new AllureRestAssured())
                .log()
                .all()
                .baseUri(baseURI)
                .header("Content-Type", "application/json")
                .queryParam("limit", "2")
                .get("/api/v1/orders")
                .then()
                .log()
                .all();
    }
}
