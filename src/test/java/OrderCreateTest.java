import client.REST;
import io.qameta.allure.Step;
import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.qameta.allure.internal.shadowed.jackson.databind.JsonNode;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.Order;

import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class OrderCreateTest {
    private ObjectMapper objectMapper = new ObjectMapper();
    private final String firstName = "Naruto";
    private final String lastName = "Uchiha";
    private final String address = "Konoha, 142 apt.";
    private final int metroStation = 4;
    private final String phone = "+7 800 355 35 35";
    private final int rentType = 5;
    private final String deliveryDate = "2020-06-06";
    private final String comment = "Saske, come back to Konoha";
    private final List<String> color;
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru/";

    public OrderCreateTest(List color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {List.of("BLACK", "GREY")},
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of()},
        };
    }

    @Test
    @DisplayName("Создание заказа с разными цветами")
    public void createOrder() throws JsonProcessingException {
        REST client = new REST(BASE_URI);
        Order order = new Order(firstName, lastName, address, metroStation, phone, rentType, deliveryDate, comment, color);
        ValidatableResponse response = client.orderCreate(order);
        orderOk(response, 201, "track");
    }

    @Step("Заказ создан, в ответе присутствует параметр trakc")
    public void orderOk(ValidatableResponse response, int code, String track) throws JsonProcessingException {
        response.assertThat().statusCode(code);
        String jsonString = response.extract().asString();
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        boolean idExists = jsonNode.has(track);
        assertTrue(idExists);
    }
}
