package org.quarkus.samples.petclinic.vet;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;
import org.quarkus.samples.petclinic.system.TemplatesLocale;

import java.util.List;

import io.quarkus.qute.TemplateInstance;

@Path("/")
public class VetResource {

    private static final Logger LOG = Logger.getLogger(VetResource.class);

    @Inject
    TemplatesLocale templates;

    @GET
    @Path("/vets.html")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance showResourcesVetPage() {
        List<Vet> vets = Vet.listAll();
        LOG.debugf("Vets: %s", vets);
        return templates.vetList(vets);
    }

    @GET
    @Path("/vets")
    public Vets showResourcesVetList() {
        Vets vets = new Vets();
        vets.getVetList().addAll(Vet.listAll());
        return vets;
    }

}
