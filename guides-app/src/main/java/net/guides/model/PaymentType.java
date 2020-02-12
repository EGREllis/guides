package net.guides.model;

public enum PaymentType {
    CASH(0),
    CARD(1);

    private int id;

    PaymentType(int id) {
        this.id = id;
    }
}
