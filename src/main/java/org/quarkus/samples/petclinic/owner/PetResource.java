package org.quarkus.samples.petclinic.owner;


import io.quarkus.security.Authenticated;
import org.quarkus.samples.petclinic.system.Templates;
import org.quarkus.samples.petclinic.system.TemplatesLocale;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.quarkus.qute.TemplateInstance;

@Path("/owners")
@Authenticated
public class PetResource {
    
    @Inject
    TemplatesLocale templates;

    @Inject
    Validator validator;

    @GET
    @Path("{ownerId}/pets/new")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance createTemplate(@PathParam("ownerId") Long ownerId) {
        return templates.createOrUpdatePetForm(Owner.findById(ownerId), null, PetType.listAll(), new HashMap<>());
    }

    @GET
    @Path("{ownerId}/pets/{petId}/edit")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance editTemplate(@PathParam("ownerId") Long ownerId, @PathParam("petId") Long petId) {
        return templates.createOrUpdatePetForm(Owner.findById(ownerId), Pet.findById(petId), PetType.listAll(), new HashMap<>());
    }

    @POST
    @Path("{ownerId}/pets/new")
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public TemplateInstance processCreationForm(@PathParam("ownerId") Long ownerId, @FormParam("name") String name, @FormParam("birthDate") LocalDate birthDate, @FormParam("type") String type) {
        Owner owner = Owner.findById(ownerId);
        
        Pet pet = new Pet();
        pet.birthDate = birthDate;
        pet.name = name;
        pet.type = PetType.parse(type);


        final Set<ConstraintViolation<Pet>> violations = validator.validate(pet);
        final Map<String, String> errors = new HashMap<>();
        if (!violations.isEmpty()) {
            
            for (ConstraintViolation<Pet> violation : violations) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }

            return templates.createOrUpdatePetForm(Owner.findById(ownerId), null, PetType.listAll(), errors);

        } else {

            pet.persist();
            owner.addPet(pet);
            return templates.ownerDetails(owner);
        }
    }

    @POST
    @Path("{ownerId}/pets/{petId}/edit")
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public TemplateInstance processUpdateForm(@PathParam("ownerId") Long ownerId, @PathParam("petId") Long petId, @FormParam("name") String name, @FormParam("birthDate") LocalDate birthDate, @FormParam("type") String type) {
        Pet pet = new Pet();
        pet.id = petId;
        pet.birthDate = birthDate;
        pet.name = name;
        pet.type = PetType.parse(type);

        final Set<ConstraintViolation<Pet>> violations = validator.validate(pet);
        final Map<String, String> errors = new HashMap<>();
        if (!violations.isEmpty()) {
            
            for (ConstraintViolation<Pet> violation : violations) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }

            Pet oldPet = Pet.findById(petId);
            return templates.createOrUpdatePetForm(oldPet.owner, oldPet, PetType.listAll(), errors);

        } else {
            return templates.ownerDetails(pet.attach().owner);
        }
    }

}
