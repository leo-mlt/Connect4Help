package org.template.interfaces;

import org.template.model.PerformUser;
import org.template.model.RequestUser;
import org.template.model.Service;
import org.template.model.User;

import javax.ejb.Local;
import java.sql.Date;
import java.util.List;

/**
 * @author Victor Mezrin
 */
@Local
public interface IUserController {

    public void createUser(String firstName, String lastName, String password, String email, String address,
                            Date dateOfBirth,String role, String telephone);
    public void deleteUser(int id);
    public User authenticate(String email, String password) ;
    public User getUser(int id);
    public List<User> findAllUser();
    void updateUser(Integer id);
}
