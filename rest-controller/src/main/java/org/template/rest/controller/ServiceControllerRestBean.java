package org.template.rest.controller;

import org.template.interfaces.IServiceController;
import org.template.model.Service;
import org.template.rest.filter.PerformerEndPoint;
import org.template.rest.model.ServiceResponse;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.List;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Stateless(name = "ServiceControllerRestEJB")

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ServiceControllerRestBean {

    @EJB(name = "ServiceControllerEJB")
    IServiceController serviceController;

    @Context
    private UriInfo uriInfo;




    public ServiceControllerRestBean() {
    }

    @GET
    @PerformerEndPoint
    @Path("/")
    public Response findServices(@Context HttpServletRequest requestContext,
                                 @DefaultValue("-1") @QueryParam("address") String address,
                                 @DefaultValue("1") @QueryParam("radius") int radius) {

        if(address.equals("-1"))
            return Response.status(NOT_FOUND).build();
        List<ServiceResponse> serviceResponses =new ArrayList<>();
        List<Service> services= new ArrayList<>(this.serviceController
                .getServices(address, radius));

        for (Service s : services){
            serviceResponses.add(create_service_response(s));
        }
        return Response.ok(serviceResponses).build();

    }


    @PUT
    @PerformerEndPoint
    @Path("/{idService}/users/{idUser}")
    public Response doService(@Context HttpServletRequest requestContext,
                              @PathParam("idService") Integer idService,
                              @PathParam("idUser") Integer idUser) {

        Boolean er = this.serviceController.acceptService(idService,idUser);
        if(er==true)
            return Response.ok().build();
        else
            return Response.status(Response.Status.UNAUTHORIZED).build();

    }





    private ServiceResponse create_service_response(Service s) {

        ServiceResponse sr = new ServiceResponse();
        sr.setIdService(s.getIdService());
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
        sr.setPerformerUser((s.getPerformerUser() == null)? 0 :s.getPerformerUser().getIdUser());
        sr.setRequestUser(s.getRequestUser().getIdUser());
        sr.setNameRequester(s.getRequestUser().getName());
        sr.setSurnameRequester(s.getRequestUser().getSurname());
        sr.setLatitude(s.getLatitude());
        sr.setLongitude(s.getLongitude());
        return sr;
    }
}
