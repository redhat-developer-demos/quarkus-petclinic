package org.quarkus.samples.petclinic.owner;

import io.quarkus.security.Authenticated;
import org.quarkus.samples.petclinic.system.Templates;
import org.quarkus.samples.petclinic.system.TemplatesLocale;
import org.quarkus.samples.petclinic.visit.Visit;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.quarkus.qute.TemplateInstance;

@Path("/owners")
@Authenticated
public class VisitResource {
    
    @Inject
    TemplatesLocale templates;

    @Inject
    Validator validator;

    @GET
    @Path("{ownerId}/pets/{petId}/visits/new")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance createTemplate(@PathParam("petId") Long petId) {
        return templates.createOrUpdateVisitForm(Pet.findById(petId), null, new HashMap<>());
    }

    @POST
    @Path("{ownerId}/pets/{petId}/visits/new")
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public TemplateInstance processCreationForm(@PathParam("petId") Long petId,  @BeanParam Visit visit) {

        Pet pet = Pet.findById(petId);
        final Set<ConstraintViolation<Visit>> violations = validator.validate(visit);
        final Map<String, String> errors = new HashMap<>();
        if (!violations.isEmpty()) {
            
            for (ConstraintViolation<Visit> violation : violations) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }

            return templates.createOrUpdateVisitForm(pet, visit, errors);

        } else {
            
            visit.persist();

            pet.addVisit(visit);
            return templates.ownerDetails(pet.owner);
        }
    }

}
