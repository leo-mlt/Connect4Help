package org.template.rest.controller;

import org.template.controller.UserServiceControllerBean;
import org.template.interfaces.IUserServiceController;
import org.template.model.Service;
import org.template.rest.filter.JWTTokenNeeded;
import org.template.rest.filter.RequesterEndPoint;
import org.template.rest.model.ServerResponse;
import org.template.rest.model.ServiceRequest;
import org.template.rest.model.ServiceResponse;
import org.template.rest.util.DecodeToken;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;

@Stateless(name = "UserServiceControllerRestEJB")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserServiceControllerRestBean {
    @Inject
    DecodeToken dT;

    @Context
    private UriInfo uriInfo;

    @EJB(name = "UserServiceControllerEJB")
    private IUserServiceController serviceController;


    public UserServiceControllerRestBean() {
    }

    ///////////////////////////////////////////
    // endpoint dei servizi relativi all'utente
    /////////////////////////////////////////////
    @GET
    @JWTTokenNeeded
    @Path("/")
    public Response findUserServices(@Context HttpServletRequest requestContext,
                                     @PathParam("id") Integer id) {
        String authorizationHeader = requestContext.getHeader(HttpHeaders.AUTHORIZATION);
        try {
            dT.decodeToken(authorizationHeader);
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        if(!id.equals(dT.getId())){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        List<ServiceResponse> serviceResponses =new ArrayList<>();
        List<Service> services= new ArrayList<>(this.serviceController
                .getUserServices(id, dT.getRole()));

        for (Service s : services){
            serviceResponses.add(create_service_response(s));
        }
        return Response.ok(serviceResponses).build();

    }
    //crea un servizio
    @POST
    @RequesterEndPoint
    @Path("/")
    public Response createUserService(@Context HttpServletRequest requestContext,
                                      ServiceRequest s,
                                      @PathParam("id") Integer i){



        Boolean er=this.serviceController.createService(s.getAddress(),s.getDetails(),i,s.getCategory(),s.getStartSlot(),
                s.getEndSlot(),s.getExpirationDate());
        ServerResponse sr = new ServerResponse();
        sr.setResult(er);
        return Response.ok(sr).build();


    }
    //ricevi uno specifico servizio creato dall'utente id
    //PER ADESSO NON UTILIZZATA
    @GET
    @JWTTokenNeeded
    @Path("/{idService}")
    public Response findUserService(@Context HttpServletRequest requestContext,
                                    @PathParam("id") Integer idUser,
                                    @PathParam("idService") Integer idService){
        String authorizationHeader = requestContext.getHeader(HttpHeaders.AUTHORIZATION);
        try {
            dT.decodeToken(authorizationHeader);
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        if(!idUser.equals(dT.getId())){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        Service s=this.serviceController.getUserService(idService);

        if(s!=null)
            return Response.ok(create_service_response(s)).build();
        return Response.noContent().build();
    }

    //Cancella uno specifico servizio creato da un utente
    @DELETE
    @JWTTokenNeeded
    @Path("/{idService}")
    public Response deleteUserService(@Context HttpServletRequest requestContext,
                                      @PathParam("id") Integer idUser,
                                      @PathParam("idService") Integer idService){

        String authorizationHeader = requestContext.getHeader(HttpHeaders.AUTHORIZATION);
        try {
            dT.decodeToken(authorizationHeader);
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        if(!idUser.equals(dT.getId())){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        serviceController.deleteUserService(idService,dT.getRole());


        // Return the token on the response
        return Response.ok().build();
    }
    //Modifica uno specifico servizio creato da uno specifico utente
    @PUT
    @JWTTokenNeeded
    @Path("/{idService}")
    public Response modifyUserService(@Context HttpServletRequest requestContext,
                                      @PathParam("id") Integer idUser,
                                      @PathParam("idService") Integer idService){
        //implementare successivamente
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }


    ////////////////////////////
    ////PRIVATE METHOD
    ///////////////////////////


    private ServiceResponse create_service_response(Service s) {
        int performer=0;
        if(s.getPerformerUser()!=null)
            performer=s.getPerformerUser().getIdUser();
        ServiceResponse sr = new ServiceResponse();

        sr.setIdService(s.getIdService());
        sr.setNameRequester(s.getRequestUser().getName());
        sr.setSurnameRequester(s.getRequestUser().getSurname());
        sr.setAddress(s.getAddress());
        sr.setDetails(s.getDetails());
        sr.setCategory(s.getCategory().getName());
        sr.setPerformed(s.getPerformed());
        sr.setAssistance(s.getAssistance());
        sr.setStartSlot(s.getStartSlot());
        sr.setEndSlot(s.getEndSlot());
        sr.setExpirationDate(s.getExpirationDate());
        sr.setInsertionDate(s.getInsertionDate());
        sr.setAcceptanceDate(s.getAcceptanceDate());
        if(dT.getRole().equals("R")) {
            sr.setPerformerUser(performer);
            if(performer!=0) {
                sr.setNamePerformer(s.getPerformerUser().getName());
                sr.setSurnamePerformer(s.getPerformerUser().getSurname());
            }
        } else{
            sr.setPerformerUser(performer);
            sr.setNamePerformer(s.getPerformerUser().getName());
            sr.setSurnamePerformer(s.getPerformerUser().getSurname());
        }
        sr.setRequestUser(s.getRequestUser().getIdUser());
        sr.setLatitude(s.getLatitude());
        sr.setLongitude(s.getLongitude());
        return sr;
    }
}
