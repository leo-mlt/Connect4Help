package org.template.rest.controller;

import org.template.rest.filter.*;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Victor Mezrin
 */
public class AppRestApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(AppRestFacadeBean.class);
        classes.add(RequesterEndPointFilter.class);
        classes.add(PerformerEndPointFilter.class);
        classes.add(UserControllerRestBean.class);
        classes.add(UserServiceControllerRestBean.class);
        classes.add(ServiceControllerRestBean.class);
        classes.add(CategoryControllerRestBean.class);
        return classes;
    }
}
