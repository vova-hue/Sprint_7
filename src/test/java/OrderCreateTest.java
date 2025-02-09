import client.REST;
import com.github.javafaker.Faker;
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
    Faker faker = new Faker();
    private ObjectMapper objectMapper = new ObjectMapper();
    private final String firstName = faker.name().firstName();
    private final String lastName = faker.name().lastName();
    private final String address = faker.address().fullAddress();
    private final int metroStation = faker.number().numberBetween(1, 20);
    private final String phone = faker.phoneNumber().phoneNumber();
    private final int rentType = faker.number().numberBetween(1, 10);
    private final String deliveryDate = "2020-06-06"; //я не понимаю, как тут надо использовать faker
    private final String comment = faker.name().fullName();
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
