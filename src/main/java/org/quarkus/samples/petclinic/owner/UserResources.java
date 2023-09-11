package org.quarkus.samples.petclinic.owner;

import org.quarkus.samples.petclinic.model.User;
import org.quarkus.samples.petclinic.system.TemplatesLocale;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.BeanParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/users")
public class UserResources {

    @Inject
    Validator validator;

    @RolesAllowed("ADMIN")
    @POST
    @Path("new")
    @Produces(MediaType.TEXT_HTML)
    @Transactional

    public boolean addUser(@BeanParam User user) {
        final Set<ConstraintViolation<Owner>> violations = validator.validate(user);
        final Map<String, String> errors = new HashMap<>();
        if (!violations.isEmpty()) {
            return false;
        } else {
            user.persist();
            return true;
        }
    }
}
