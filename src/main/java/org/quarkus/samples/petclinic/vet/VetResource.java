package org.quarkus.samples.petclinic.vet;

import org.quarkus.samples.petclinic.system.Templates;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.qute.TemplateInstance;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class VetResource {
    
    @GET
    @Path("/vets.html")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance showResourcesVetPage() {
        List<Vet> vets = Vet.listAll();
        System.out.println(vets);
        return Templates.vetList(vets);
    }

    @GET
    @Path("/vets")
    public Vets showResourcesVetList() {
        Vets vets = new Vets();
		vets.getVetList().addAll(Vet.listAll());
		return vets;
    }

}
