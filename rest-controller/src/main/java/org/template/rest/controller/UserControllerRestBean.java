package org.template.rest.controller;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.template.interfaces.IUserController;
import org.template.model.User;
import org.template.rest.filter.JWTTokenNeeded;
import org.template.rest.model.*;
import org.template.rest.util.DecodeToken;
import org.template.rest.util.KeyGenerator;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

/**
 * @author Victor Mezrin
 */
@Stateless(name = "UserControllerRestEJB")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserControllerRestBean {

    @Inject
    DecodeToken dT;


    @EJB(name = "UserControllerEJB")
    IUserController userController;

    @Context
    private UriInfo uriInfo;

    @Inject
    private KeyGenerator keygen;
    ////////////////////////////////////////
    //endpoint generiche per utente
    /////////////////////////////////////////
    @GET
    @Path("/")
    public Response findAllUsers() {
        List<User> allUsers = this.userController.findAllUser();
        if (allUsers == null)
            return Response.status(NOT_FOUND).build();
        List<UserResponse> users = new ArrayList<>();
        for (User user : allUsers){
            UserResponse usr = new UserResponse();
            usr.setField(user);
            users.add(usr);
        }
        return Response.ok(users).build();
    }

    @POST
    @Path("/")
    public Response createUser(@Context HttpServletRequest requestContext,
                               UserResponse request) {
        this.userController.createUser(request.getName(),request.getSurname(),request.getPassword(),
                request.getEmail(),request.getAddress(),request.getDateOfBirth(),
                request.getRole(),request.getTelephone());

        return Response.created(uriInfo.getAbsolutePathBuilder().path(Integer.toString(
                request.getIdUser()
        )).build()).build();
    }

    @GET
    @Path("/{id}")
    @JWTTokenNeeded
    public Response findUser(@PathParam("id") Integer id) {
        User user = this.userController.getUser(id);
        if (user == null)
            return Response.status(NOT_FOUND).build();
        UserResponse ur = new UserResponse();
        ur.setField(user);
        return Response.ok(ur).build();
    }

    @DELETE
    @Path("/{id}")
    @JWTTokenNeeded
    public Response deleteUser(@Context HttpServletRequest requestContext,@PathParam("id") Integer id) {
        String authorizationHeader = requestContext.getHeader(HttpHeaders.AUTHORIZATION);
        try {
            dT.decodeToken(authorizationHeader);
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        if(dT.getRole()=="A" || id==dT.getId()){
            this.userController.deleteUser(id);
            return Response.noContent().build();
        }

        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    //NON ANCORA IMPLEMENTATA
    @PUT
    @Path("/{id}")
    @JWTTokenNeeded
    public Response updateUser(@Context HttpServletRequest requestContext,@PathParam("id") Integer id) {
        String authorizationHeader = requestContext.getHeader(HttpHeaders.AUTHORIZATION);
        try {
            dT.decodeToken(authorizationHeader);
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        if(dT.getRole()=="A" || id==dT.getId()){
            this.userController.updateUser(id);
            return Response.noContent().build();
        }

        return Response.status(Response.Status.UNAUTHORIZED).build();
    }



    @POST
    @Path("/login")
    public Response authenticateUser(@Context HttpServletRequest requestContext,
                                     RequestLogin request) {
        try {
            // Authenticate the user using the credentials provided
            User u=userController.authenticate(request.getEmail(), request.getPassword());
            if (u==null)
                throw new SecurityException("Invalid email/password");
            String token = issueToken(request.getEmail(),u.getIdUser(),u.getRole());
            JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
            jsonObjBuilder.add( "auth_token", token );
            jsonObjBuilder.add("id",u.getIdUser());
            jsonObjBuilder.add("role",u.getRole());
            jsonObjBuilder.add("address",u.getAddress());
            JsonObject jsonObj = jsonObjBuilder.build();

            // Return the token on the response
            return Response.ok().header(AUTHORIZATION, "Bearer " + token).entity(jsonObj.toString() ).build();
        } catch (Exception e) {
            return Response.status(UNAUTHORIZED).build();
        }
    }


    private String issueToken(String email,Integer id,String role) {
        Key key = keygen.generateKey();
        Map<String,Object> cl =new HashMap<>();
        cl.put("id",id);
        cl.put("role",role);
        return Jwts.builder()
                .setSubject(email)
                .setClaims(cl)
                .setIssuer(uriInfo.getAbsolutePath().toString())
                .setIssuedAt(new Date())
                .setExpiration(toDate(LocalDateTime.now().plusMinutes(3600L)))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();

    }




    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    private Response.ResponseBuilder getNoCacheResponseBuilder( Response.Status status ) {
        CacheControl cc = new CacheControl();
        cc.setNoCache( true );
        cc.setMaxAge( -1 );
        cc.setMustRevalidate( true );

        return Response.status( status ).cacheControl( cc );
    }
}
