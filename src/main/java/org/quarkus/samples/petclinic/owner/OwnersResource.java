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
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.quarkus.samples.petclinic.system.TemplatesLocale;
import org.quarkus.samples.petclinic.visit.Visit;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import io.quarkus.qute.TemplateInstance;

@Path("/owners")
public class OwnersResource {

    @Inject
    TemplatesLocale templates;

    @Inject
    Validator validator;

    @GET
    @Path("/find")
    @Produces(MediaType.TEXT_HTML)
    /**
     * Renders the findOwners.html
     *
     * @return
     */
    public TemplateInstance findTemplate() {
        return templates.findOwners(List.of());
    }

    @GET
    @Path("new")
    @Produces(MediaType.TEXT_HTML)
    /**
     * Renders the createOrUpdateOwnerForm.html
     *
     * @return
     */
    public TemplateInstance createTemplate() {
        return templates.createOrUpdateOwnerForm(null, Map.of());
    }

    @GET
    @Path("{ownerId}/edit")
    @Produces(MediaType.TEXT_HTML)
    /**
     * Renders the createOrUpdateOwnerForm.html
     *
     * @return
     */
    public TemplateInstance editTemplate(@PathParam("ownerId") Long ownerId) {
        return templates.createOrUpdateOwnerForm(findOwner(ownerId), Map.of());
    }

    @GET
    @Path("{ownerId}")
    @Produces(MediaType.TEXT_HTML)
    /**
     * Renders the createOrUpdateOwnerForm.html
     *
     * @return
     */
    public TemplateInstance showOwner(@PathParam("ownerId") Long ownerId) {
        return templates.ownerDetails(findOwner(ownerId));
    }

    @POST
    @Path("new")
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    /**
     * Renders the createOrUpdateOwnerForm.html
     *
     * @return
     */
    public TemplateInstance processCreationForm(@BeanParam Owner owner) {
        final Set<ConstraintViolation<Owner>> violations = validator.validate(owner);
        if (!violations.isEmpty()) {
            return templates.createOrUpdateOwnerForm(null, toErrorMap(violations));
        }
        owner.persist();
        return templates.ownerDetails(owner);
    }

    @POST
    @Path("{ownerId}/edit")
    @Transactional
    @Produces(MediaType.TEXT_HTML)
    /**
     * Renders the createOrUpdateOwnerForm.html
     *
     * @return
     */
    public TemplateInstance processUpdateOwnerForm(@BeanParam Owner owner, @PathParam("ownerId") Long ownerId) {
        final Set<ConstraintViolation<Owner>> violations = validator.validate(owner);
        if (!violations.isEmpty()) {
            return templates.createOrUpdateOwnerForm(owner, toErrorMap(violations));
        }
        // We need to reattach the Owner object. Since method is transactional, the
        // update occurs automatically.
        return templates.ownerDetails(owner.attach());
    }

    private static <T> Map<String, String> toErrorMap(Set<ConstraintViolation<T>> violations) {
        return violations.stream().collect(
                Collectors.toMap(v -> v.getPropertyPath().toString(), ConstraintViolation::getMessage, (a, b) -> a));
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    /**
     * Process the findOwners form
     */
    public TemplateInstance processFindForm(@QueryParam("lastName") String lastName) {

        // allow parameterless GET request for /owners to return all records
        Collection<Owner> owners = (lastName == null || lastName.isBlank()) ? Owner.listAll()
                : Owner.findByLastName(lastName);

        // find owners by last name
        if (owners.isEmpty()) {
            // no owners found
            return templates.findOwners(List.of("lastName not found"));
        }
        if (owners.size() == 1) {
            // 1 owner found
            Owner owner = owners.iterator().next();
            return templates.ownerDetails(setVisits(owner));
        }

        return templates.ownersList(owners);

    }

    protected Owner setVisits(Owner owner) {
        for (Pet pet : owner.pets) {
            pet.setVisitsInternal(Visit.findByPetId(pet.id));
        }
        return owner;
    }

    private static Owner findOwner(Long ownerId) {
        Owner owner = Owner.findById(ownerId);
        if (owner == null) {
            throw new NotFoundException("Owner not found: " + ownerId);
        }
        return owner;
    }

    @Path("/api/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Owner> listOwners() {
        return Owner.listAll();
    }

}
