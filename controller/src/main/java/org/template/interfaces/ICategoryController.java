package org.template.interfaces;

import org.template.model.Category;

import javax.ejb.Local;
import java.util.Collection;
@Local
public interface ICategoryController {

    Collection<Category> getCategories();
}
