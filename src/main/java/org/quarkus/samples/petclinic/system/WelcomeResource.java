package org.quarkus.samples.petclinic.system;

import java.util.Locale;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated;

@Path("/")
public class WelcomeResource {
    
    @Inject
    TemplatesLocale templates;

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Authenticated
    public TemplateInstance get() {
        return templates.welcome();
    }

}
