package net.guides.model;

public class PaymentType {
    private final int id;
    private final String description;

    public PaymentType(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof PaymentType) {
            PaymentType other = (PaymentType)obj;
            result = description.equals(other.description);
        }
        return result;
    }

    @Override
    public int hashCode() {
        return description.hashCode();
    }

    @Override
    public String toString() {
        return String.format("PaymentType:%1$d Description:%2$s", id, description);
    }
}
