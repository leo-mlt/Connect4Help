package org.template.controller;

import org.template.interfaces.IUserController;
import org.template.model.PerformUser;
import org.template.model.RequestUser;
import org.template.model.User;
import org.template.util.OpenStreetMapUtils;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.*;
import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Victor Mezrin
 */
@Stateless(name = "UserControllerEJB")
public class UserControllerBean implements IUserController {

    @PersistenceContext(unitName = "MainPersistenceUnit")
    private EntityManager em;

    @Override
    public User getUser(int id) {
        Query q = this.em.createQuery(
                "SELECT u FROM User u where u.idUser=:id");
        q.setParameter("id", id);
        try {
            return (User) q.getSingleResult();
        } catch (NoResultException exc){
            return null;
        }
    }



    @Override
    public void updateUser(Integer id) {

    }


    @Override
    public List<User> findAllUser() {
        Query q = this.em.createQuery(
                "SELECT u FROM User u");
        try {
            return (List<User>) q.getResultList();
        } catch (NoResultException exc){
            return null;
        }
    }



    @Override
    public void createUser(String firstName, String lastName, String password, String email, String address,
                           Date dateOfBirth, String role, String telephone) {
        User user=null;
        if (role.equals("P")){
            user = new PerformUser();
        } else if (role.equals("R")){
            user = new RequestUser();
        }
        Map<String, Double> coords;
        coords = OpenStreetMapUtils.getInstance().getCoordinates(address);
        user.setLatitude(coords.get("lat"));
        user.setLongitude(coords.get("lon"));
        user.setName(firstName);
        user.setSurname(lastName);
        user.setPassword(password);
        user.setEmail(email);
        user.setAddress(address);
        user.setDateOfBirth(dateOfBirth);
        user.setRole(role);
        user.setTelephone(telephone);
        this.em.persist(user);
    }

    @Override
    public void deleteUser(int id) {
        em.remove(em.getReference(User.class, id));
    }


    @Override
    public User authenticate(String email, String password) {
        Query q = this.em.createQuery(
                "SELECT u FROM User u WHERE u.email = :email and u.password = :password");
        q.setParameter("email", email);
        q.setParameter("password", password);
        try {
            return (User) q.getSingleResult();
        } catch (NoResultException exc){
            return null;
        }

    }




}
