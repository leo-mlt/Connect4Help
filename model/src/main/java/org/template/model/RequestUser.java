package org.template.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;


@Entity
@DiscriminatorValue(value = "R")
public class RequestUser extends User {
    private Collection<Service> requestedServices;

    public RequestUser() {
        requestedServices=new ArrayList<>();
    }

    public RequestUser(int idUser, String name, String surname, String email, String password, String address, Date dateOfBirth, String role, String telephone) {
        super(idUser, name, surname, email, password, address, dateOfBirth, role, telephone);
    }

    @OneToMany(mappedBy = "requestUser", fetch = FetchType.EAGER)
    public Collection<Service> getRequestedServices() {
        return requestedServices;
    }

    public void setRequestedServices(Collection<Service> requestedServices) {
        this.requestedServices = requestedServices;
    }
}
