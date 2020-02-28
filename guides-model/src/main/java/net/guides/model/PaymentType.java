package net.guides.model;

public class PaymentType {
    private final Integer id;
    private final String description;

    public PaymentType(Integer id, String description) {
        this.id = id;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public PaymentType replaceId(int paymentTypeId) {
        return new PaymentType(paymentTypeId, description);
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
