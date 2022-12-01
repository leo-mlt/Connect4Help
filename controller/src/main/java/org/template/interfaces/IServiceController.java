package org.template.interfaces;

import org.template.model.Service;

import javax.ejb.Local;
import javax.ejb.Stateless;
import java.util.Collection;
@Local
public interface IServiceController {
    Collection<Service> getServices(String address, int radius);

    Boolean acceptService(Integer idService, Integer idUser);
}
