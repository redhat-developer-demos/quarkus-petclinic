package org.quarkus.samples.petclinic.owner;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.quarkus.samples.petclinic.system.TemplatesLocale;
import org.quarkus.samples.petclinic.visit.Visit;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import io.quarkus.qute.TemplateInstance;

@Path("/owners")
public class VisitResource {

    @Inject
    TemplatesLocale templates;

    @Inject
    Validator validator;

    @GET
    @Path("{ownerId}/pets/{petId}/visits/new")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance createTemplate(@PathParam("ownerId") Long ownerId, @PathParam("petId") Long petId) {
        return templates.createOrUpdateVisitForm(findPet(ownerId, petId), null, Map.of());
    }

    @POST
    @Path("{ownerId}/pets/{petId}/visits/new")
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public TemplateInstance processCreationForm(@PathParam("ownerId") Long ownerId, @PathParam("petId") Long petId,
            @BeanParam Visit visit) {

        Pet pet = findPet(ownerId, petId);
        final Set<ConstraintViolation<Visit>> violations = validator.validate(visit);
        if (!violations.isEmpty()) {
            return templates.createOrUpdateVisitForm(pet, visit, violations.stream().collect(Collectors
                    .toMap(v -> v.getPropertyPath().toString(), ConstraintViolation::getMessage, (a, b) -> a)));
        }

        visit.persist();
        pet.addVisit(visit);
        return templates.ownerDetails(pet.owner);
    }

    private static Pet findPet(Long ownerId, Long petId) {
        Pet pet = Pet.findById(petId);
        if (pet == null || pet.owner == null || !ownerId.equals(pet.owner.id)) {
            throw new NotFoundException("Pet not found: " + petId);
        }
        return pet;
    }

}
