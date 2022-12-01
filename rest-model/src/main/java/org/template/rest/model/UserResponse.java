package org.template.rest.model;

import org.template.model.User;

import java.sql.Date;
import java.sql.Timestamp;

public class UserResponse  {
    private int idUser;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String address;
    private Date dateOfBirth;
    private String role;
    private String telephone;
    private Timestamp timeStamp;

    public UserResponse() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setField(User user){
        this.setIdUser(user.getIdUser());
        this.setName(user.getName());
        this.setSurname(user.getSurname());
        this.setEmail(user.getEmail());
        this.setAddress(user.getAddress());
        this.setDateOfBirth(user.getDateOfBirth());
        this.setRole(user.getRole());
        this.setTelephone(user.getTelephone());
        this.setTimeStamp(user.getTimeStamp());
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }
}
