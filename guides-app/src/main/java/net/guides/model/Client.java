package net.guides.model;

public class Client {
    private int clientId;
    private String firstName;
    private String lastName;
    private String sms;
    private String email;

    public Client(int clientId, String firstName, String lastName, String sms, String email) {
        this.clientId = clientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.sms = sms;
        this.email = email;
    }

    public int getClientId() {
        return clientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getSms() {
        return sms;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return String.format("ClientId:%1$d (%2$s, %3$s - %4$s, %5$s", clientId, firstName, lastName, sms, email);
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof Client) {
            Client other = (Client)obj;
            result = firstName == other.firstName && lastName == other.lastName && sms == other.sms && email == other.email;
        }
        return result;
    }

    @Override
    public int hashCode() {
        return clientId;
    }
}
