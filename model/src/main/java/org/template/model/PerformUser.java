package org.template.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@DiscriminatorValue(value = "P")
public class PerformUser extends User {
    private Collection<Service> acceptedServices;

    public PerformUser() {
        this.acceptedServices = new ArrayList<>();
    }

    public PerformUser(int idUser, String name, String surname, String email, String password, String address, Date dateOfBirth, String role, String telephone) {
        super(idUser, name, surname, email, password, address, dateOfBirth, role, telephone);
    }

    @OneToMany(mappedBy = "performerUser", fetch = FetchType.EAGER)
    public Collection<Service> getAcceptedServices() {
        return acceptedServices;
    }

    public void setAcceptedServices(Collection<Service> acceptedServices) {
        this.acceptedServices = acceptedServices;
    }

}
