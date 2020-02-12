package net.guides.storage;

import net.guides.model.Registration;

import java.util.List;

public interface RegistrationRepository {
    List<Registration> getRegistrations();
    boolean addRegistration(Registration registration);
}
