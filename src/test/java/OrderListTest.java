import client.REST;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Assert;
import org.junit.Test;
import ru.yandex.praktikum.Order;

import java.util.List;

public class OrderListTest {
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru/";

    @Test
    @DisplayName("Получение испска заказов с limit=2")
    public void getOrders() {
        REST client = new REST(BASE_URI);
        List<Order> orders = client.getOrders().extract().jsonPath().getList("orders", Order.class);
        countOrders(orders, 2);
    }

    @Step("Количество заказов в ответе")
    public void countOrders(List list, int count) {
        Assert.assertEquals(list.size(), count);
    }
}
