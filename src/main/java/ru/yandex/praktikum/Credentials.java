package ru.yandex.praktikum;

public class Credentials {
    private String login;
    private String password;
    private String firstName;


    public Credentials(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    public static Credentials fromCourier(Courier courier) {
        return new Credentials(courier.getLogin(), courier.getPassword(), null);
    }

    public static Credentials fromCourierWithoutLogin(Courier courier) {
        return new Credentials(null, courier.getPassword(), null);
    }

    public static Credentials fromCourierWithoutPassword(Courier courier) {
        return new Credentials(courier.getLogin(), null, null);
    }

    public static Credentials courierWithoutLogin(Courier courier) {
        return new Credentials(null, courier.getPassword(), courier.getFirstName());
    }

    public static Credentials courierWithoutPassword(Courier courier) {
        return new Credentials(courier.getLogin(), null, courier.getFirstName());
    }

    public static Credentials courierWithoutFirstName(Courier courier) {
        return new Credentials(courier.getLogin(), courier.getPassword(), null);
    }
}
