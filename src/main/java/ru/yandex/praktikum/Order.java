package ru.yandex.praktikum;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Order {
    private String firstName;
    private String lastName;
    private String address;
    private int metroStation;
    private String phone;
    private int rentType;
    private String deliveryDate;
    private String comment;
    private List<String> color;
}
