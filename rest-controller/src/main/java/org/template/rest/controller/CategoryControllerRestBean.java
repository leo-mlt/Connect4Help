package org.template.rest.controller;

import org.template.interfaces.ICategoryController;
import org.template.model.Category;
import org.template.rest.filter.RequesterEndPoint;
import org.template.rest.model.CategoryResponse;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;


@Stateless(name = "CategoryControllerRestEJB")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CategoryControllerRestBean {

    @EJB(name = "CategoryControllerEJB")
    ICategoryController categoryController;

    public CategoryControllerRestBean(){

    }

    @GET
    @RequesterEndPoint
    @Path("/")
    public Response findCategories() {

        List<CategoryResponse> categoryResponses =new ArrayList<>();
        List<Category> categories= new ArrayList<>(this.categoryController
                .getCategories());

        for (Category c : categories){
            categoryResponses.add(create_category_response(c));
        }
        return Response.ok(categoryResponses).build();

    }

    private CategoryResponse create_category_response(Category c) {

        CategoryResponse cr = new CategoryResponse();
        cr.setCategoryName(c.getName());
        cr.setPhoto(c.getPhoto());

        return cr;
    }
}
