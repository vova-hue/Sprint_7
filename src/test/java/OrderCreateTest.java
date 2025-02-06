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
    private final String firstName;
    private final String lastName;
    private final String address;
    private final int metroStation;
    private final String phone;
    private final int rentType;
    private final String deliveryDate;
    private final String comment;
    private final List<String> color;
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru/";

    public OrderCreateTest(String firstName, String lastName, String address, int metroStation, String phone, int rentType, String deliveryDate, String comment, List color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentType = rentType;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {"Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", List.of("BLACK", "GREY")},
                {"Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", List.of("BLACK")},
                {"Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", List.of("GREY")},
                {"Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", List.of()},
        };
    }

    @Test
    @DisplayName("Создание заказа с разными цветами")
    public void createOrder() throws JsonProcessingException {
        REST client = new REST(BASE_URI);
        Order order = new Order(firstName, lastName, address, metroStation, phone, rentType, deliveryDate, comment, color);
        ValidatableResponse response = client.orderCreate(order);
        order_ok(response, 201, "track");
    }

    @Step("Заказ создан, в ответе присутствует параметр trakc")
    public void order_ok(ValidatableResponse response, int code, String track) throws JsonProcessingException {
        response.assertThat().statusCode(code);
        String jsonString = response.extract().asString();
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        boolean idExists = jsonNode.has(track);
        assertTrue(idExists);
    }
}
