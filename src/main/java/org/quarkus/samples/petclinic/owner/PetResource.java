package org.quarkus.samples.petclinic.owner;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.quarkus.samples.petclinic.system.TemplatesLocale;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import io.quarkus.qute.TemplateInstance;

@Path("/owners")
public class PetResource {

    @Inject
    TemplatesLocale templates;

    @Inject
    Validator validator;

    @GET
    @Path("{ownerId}/pets/new")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance createTemplate(@PathParam("ownerId") Long ownerId) {
        return templates.createOrUpdatePetForm(findOwner(ownerId), null, PetType.listAll(), Map.of());
    }

    @GET
    @Path("{ownerId}/pets/{petId}/edit")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance editTemplate(@PathParam("ownerId") Long ownerId, @PathParam("petId") Long petId) {
        Pet pet = findPet(ownerId, petId);
        return templates.createOrUpdatePetForm(pet.owner, pet, PetType.listAll(), Map.of());
    }

    @POST
    @Path("{ownerId}/pets/new")
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public TemplateInstance processCreationForm(@PathParam("ownerId") Long ownerId, @FormParam("name") String name,
            @FormParam("birthDate") LocalDate birthDate, @FormParam("type") String type) {
        Owner owner = findOwner(ownerId);

        Pet pet = new Pet();
        pet.birthDate = birthDate;
        pet.name = name;
        pet.type = PetType.parse(type);

        final Set<ConstraintViolation<Pet>> violations = validator.validate(pet);
        if (!violations.isEmpty()) {
            return templates.createOrUpdatePetForm(owner, null, PetType.listAll(), toErrorMap(violations));
        }

        pet.persist();
        owner.addPet(pet);
        return templates.ownerDetails(owner);
    }

    @POST
    @Path("{ownerId}/pets/{petId}/edit")
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public TemplateInstance processUpdateForm(@PathParam("ownerId") Long ownerId, @PathParam("petId") Long petId,
            @FormParam("name") String name, @FormParam("birthDate") LocalDate birthDate,
            @FormParam("type") String type) {
        Pet existingPet = findPet(ownerId, petId);
        Pet updatedPet = new Pet();
        updatedPet.id = petId;
        updatedPet.birthDate = birthDate;
        updatedPet.name = name;
        updatedPet.type = PetType.parse(type);

        final Set<ConstraintViolation<Pet>> violations = validator.validate(updatedPet);
        if (!violations.isEmpty()) {
            return templates.createOrUpdatePetForm(existingPet.owner, existingPet, PetType.listAll(),
                    toErrorMap(violations));
        }

        existingPet.birthDate = updatedPet.birthDate;
        existingPet.name = updatedPet.name;
        existingPet.type = updatedPet.type;
        return templates.ownerDetails(existingPet.owner);
    }

    private static Owner findOwner(Long ownerId) {
        Owner owner = Owner.findById(ownerId);
        if (owner == null) {
            throw new NotFoundException("Owner not found: " + ownerId);
        }
        return owner;
    }

    private static Pet findPet(Long ownerId, Long petId) {
        Pet pet = Pet.findById(petId);
        if (pet == null || pet.owner == null || !ownerId.equals(pet.owner.id)) {
            throw new NotFoundException("Pet not found: " + petId);
        }
        return pet;
    }

    private static <T> Map<String, String> toErrorMap(Set<ConstraintViolation<T>> violations) {
        return violations.stream().collect(
                Collectors.toMap(v -> v.getPropertyPath().toString(), ConstraintViolation::getMessage, (a, b) -> a));
    }

}
