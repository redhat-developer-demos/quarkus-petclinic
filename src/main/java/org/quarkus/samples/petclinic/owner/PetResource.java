package org.quarkus.samples.petclinic.owner;


import org.quarkus.samples.petclinic.system.Templates;

import java.time.LocalDate;

import javax.transaction.Transactional;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.quarkus.qute.TemplateInstance;

@Path("/owners")
public class PetResource {
    
    @GET
    @Path("{ownerId}/pets/new")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance createTemplate(@PathParam("ownerId") Long ownerId) {
        return Templates.createOrUpdatePetForm(Owner.findById(ownerId), null, PetType.listAll());
    }

    @GET
    @Path("{ownerId}/pets/{petId}/edit")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance editTemplate(@PathParam("ownerId") Long ownerId, @PathParam("petId") Long petId) {
        return Templates.createOrUpdatePetForm(Owner.findById(ownerId), Pet.findById(petId), PetType.listAll());
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
        pet.persist();

        owner.addPet(pet);
        return Templates.ownerDetails(owner);
    }

    @POST
    @Path("{ownerId}/pets/{petId}/edit")
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public TemplateInstance processUpdateForm(@PathParam("ownerId") Long ownerId, @PathParam("petId") Long petId, @FormParam("name") String name, @FormParam("birthDate") LocalDate birthDate, @FormParam("type") String type) {
        Pet pet = Pet.findById(petId);
        pet.birthDate = birthDate;
        pet.name = name;
        pet.type = PetType.parse(type);

        return Templates.ownerDetails(pet.owner);
    }

}
