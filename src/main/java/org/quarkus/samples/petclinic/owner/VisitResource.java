package org.quarkus.samples.petclinic.owner;

import org.quarkus.samples.petclinic.system.Templates;
import org.quarkus.samples.petclinic.visit.Visit;

import javax.transaction.Transactional;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.quarkus.qute.TemplateInstance;

@Path("/owners")
public class VisitResource {
    
    @GET
    @Path("{ownerId}/pets/{petId}/visits/new")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance createTemplate(@PathParam("petId") Long petId) {
        return Templates.createOrUpdateVisitForm(Pet.findById(petId), null);
    }

    @POST
    @Path("{ownerId}/pets/{petId}/visits/new")
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public TemplateInstance processCreationForm(@PathParam("petId") Long petId,  @BeanParam Visit visit) {

        Pet pet = Pet.findById(petId);
        visit.persist();

        pet.addVisit(visit);
        return Templates.ownerDetails(pet.owner);
    }

}
