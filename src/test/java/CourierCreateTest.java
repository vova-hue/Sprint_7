import client.REST;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.*;
import ru.yandex.praktikum.Courier;
import ru.yandex.praktikum.Credentials;


import static org.hamcrest.CoreMatchers.equalTo;


public class CourierCreateTest {
    private final String login = "UniqCourier1";
    private final String password = "password";
    private final String firstName = "courier";

    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru/";

    Courier courier = new Courier(login, password, firstName);
    REST client = new REST(BASE_URI);

    @Test
    @DisplayName("Создание уникального курьера")
    public void createUniqCourier() {
        ValidatableResponse response = client.create(courier);
        createCourier(response, 201, true);
    }

    @Test
    @DisplayName("Создание двух одинаковых курьеров")
    public void createTwoIdenticalsCourier() {
        ValidatableResponse firstResponse = client.create(courier);
        firstResponse.assertThat().statusCode(201);
        ValidatableResponse secondRespoce = client.create(courier);
        createCourierDuplicate(secondRespoce, 409, "Этот логин уже используется");
    }

    @Test
    @DisplayName("Создание курьера без передачи firstName")
    public void createCourierWithoutFirstName() {
        Credentials credentials = Credentials.courierWithoutFirstName(courier);
        ValidatableResponse response = client.create(credentials);
        createCourierParamMissimg(response, 400, "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Создание курьера без передачи login")
    public void createCourierWithoutLogin() {
        Credentials credentials = Credentials.courierWithoutLogin(courier);
        ValidatableResponse response = client.create(credentials);
        createCourierParamMissimg(response, 400, "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Создание курьера без передачи password")
    public void createCourierWithoutPassword() {
        Credentials credentials = Credentials.courierWithoutPassword(courier);
        ValidatableResponse response = client.create(credentials);
        createCourierParamMissimg(response, 400, "Недостаточно данных для создания учетной записи");
    }

    @After
    public void after() {
        Credentials credentials = Credentials.fromCourier(courier);
        ValidatableResponse response = client.loginCourier(credentials);
        int statusCode = response.extract().statusCode();
        if (statusCode == 200) {
            String id = response.extract().jsonPath().getString("id");
            client.deleteCourier(id).statusCode(200);
        } else {
            System.out.println(response.extract().body().asString());
        }
    }

    @Step("Курьер успешно создан")
    public void createCourier(ValidatableResponse response, int code, boolean ok) {
        response.assertThat().statusCode(code).body("ok", equalTo(ok));
    }

    @Step("Курьер с таким логином уже существует")
    public void createCourierDuplicate(ValidatableResponse response, int code, String message) {
        response.assertThat().statusCode(code).body("message", equalTo(message));
    }

    @Step("Отсутствует один или несколько парметров")
    public void createCourierParamMissimg(ValidatableResponse response, int code, String message) {
        response.assertThat().statusCode(code).body("message", equalTo(message));
    }
}
