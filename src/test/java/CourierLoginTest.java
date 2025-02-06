import client.REST;
import io.qameta.allure.Step;
import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.qameta.allure.internal.shadowed.jackson.databind.JsonNode;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.Courier;
import ru.yandex.praktikum.Credentials;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertTrue;

public class CourierLoginTest {
    private ObjectMapper objectMapper = new ObjectMapper();
    private final String login = "LoginCourier";
    private final String badLogin = "LodinCourier";
    private final String password = "password";
    private final String badPassword = "drowssap";
    private final String firstName = "courier";
    private int idLogin;
    Courier courier = new Courier(login, password, firstName);
    Courier badLoginCourier = new Courier(badLogin, password, firstName);
    Courier badPasswordCourier = new Courier(login, badPassword, firstName);
    REST client = new REST(BASE_URI);


    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru/";

    @Before
    public void before() {
        ValidatableResponse response = client.create(courier);
        Assume.assumeTrue(response.extract().statusCode() == 201);
    }

    @Test
    @DisplayName("Логин курьера")
    public void login_ok() throws JsonProcessingException {
        Credentials credentials = Credentials.fromCourier(courier);
        ValidatableResponse response = client.loginCourier(credentials);
        idIsPresent(response, 200, "id");
    }

    @Test
    @DisplayName("Логин курьера без параметра login")
    public void login_withoutParamLogin() {
        Credentials credentials = Credentials.fromCourierWithoutLogin(courier);
        ValidatableResponse response = client.loginCourier(credentials);
        paramMissing(response, 400, "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Логин курьера без парметра password")
    public void login_witoutParamPassword() {
        Credentials credentials = Credentials.fromCourierWithoutPassword(courier);
        ValidatableResponse response = client.loginCourier(credentials);
        paramMissing(response, 400, "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Логин курьера с неверным login")
    public void login_badLogin() {
        Credentials credentials = Credentials.fromCourier(badLoginCourier);
        ValidatableResponse response = client.loginCourier(credentials);
        badParam(response, 404, "Учетная запись не найдена");
    }

    @Test
    @DisplayName("Логин курьера с неверным password")
    public void login_badPassword() {
        Credentials credentials = Credentials.fromCourier(badPasswordCourier);
        ValidatableResponse response = client.loginCourier(credentials);
        badParam(response, 404, "Учетная запись не найдена");
    }

    @After
    public void after() {
        Credentials credentials = Credentials.fromCourier(courier);
        ValidatableResponse response = client.loginCourier(credentials);
        idLogin = response.extract().jsonPath().getInt("id");
        String id = Integer.toString(idLogin);
        client.deleteCourier(id);
    }

    @Step("В ответе на запрос /login статус 200, в теле ответа присутствует параметр id")
    public void idIsPresent(ValidatableResponse response, int code, String id) throws JsonProcessingException {
        response.assertThat().statusCode(code);
        String jsonString = response.extract().asString();
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        boolean idExists = jsonNode.has(id);
        assertTrue(idExists);
    }

    @Step("Отсутствует один или несколько парметров")
    public void paramMissing(ValidatableResponse response, int code, String message) {
        response.assertThat().statusCode(code).body("message", equalTo(message));
    }

    @Step("Не существует пара логин-пароль")
    public void badParam(ValidatableResponse response, int code, String message) {
        response.assertThat().statusCode(code).body("message", equalTo(message));
    }
}
