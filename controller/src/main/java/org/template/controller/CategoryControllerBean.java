package org.template.controller;

import org.template.interfaces.ICategoryController;
import org.template.interfaces.IServiceController;
import org.template.model.Category;
import org.template.model.User;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collection;

@Stateless(name = "CategoryControllerEJB")
public class CategoryControllerBean implements ICategoryController {

    @PersistenceContext(unitName = "MainPersistenceUnit")
    private EntityManager em;

    public CategoryControllerBean(){

    }

    @Override
    public Collection<Category> getCategories() {
        Query q = this.em.createQuery(
                "SELECT c FROM Category c");
        try {
            return (Collection<Category>) q.getResultList();
        } catch (NoResultException exc){
            return null;
        }
    }
}
