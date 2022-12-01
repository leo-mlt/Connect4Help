package org.template.rest.controller;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Path;

/**
 * @author Victor Mezrin
 */
@Stateless
@Path("/")
public class AppRestFacadeBean {

    @EJB(name = "UserControllerRestEJB")
    UserControllerRestBean user;

    @EJB(name = "UserServiceControllerRestEJB")
    UserServiceControllerRestBean userService;

    @EJB(name = "ServiceControllerRestEJB")
    ServiceControllerRestBean service;

    @EJB(name = "CategoryControllerRestEJB")
    CategoryControllerRestBean category;


    @Path("users")
    public UserControllerRestBean getUserControllerRestBean() {
        return this.user;
    }

    @Path("users/{id: \\d+}/services")
    public UserServiceControllerRestBean getUserServiceControllerRestBean() {
        return this.userService;
    }

    @Path("services")
    public ServiceControllerRestBean getServiceControllerRestBean() {
        return this.service;
    }

    @Path("categories")
    public CategoryControllerRestBean getCategoryControllerRestBean(){return this.category;}
}
