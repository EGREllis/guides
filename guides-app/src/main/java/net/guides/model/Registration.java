package net.guides.model;

public class Registration {
    private int registrationId;
    private String username;
    private String password;

    public Registration(int registrationId, String username, String password) {
        this.registrationId = registrationId;
        this.username = username;
        this.password = password;
    }

    public int getRegistrationId() {
        return registrationId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return String.format("RegistrationId:%1$d Username:%2$s", registrationId, username);
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof Registration) {
            Registration other = (Registration)obj;
            result = username.equals(other.username) && password.equals(other.password);
        }
        return result;
    }

    public int hashCode() {
        return username.hashCode() * 17 + password.hashCode();
    }
}
