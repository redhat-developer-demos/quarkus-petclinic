package org.quarkus.samples.petclinic.system;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.logging.Logger;

@Provider
public class ErrorExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger LOG = Logger.getLogger(ErrorExceptionMapper.class);

    @Inject
    TemplatesLocale templates;

    @Override
    public Response toResponse(Exception exception) {
        LOG.error("Interal application error", exception);
        return Response.ok(templates.error(exception.getMessage())).build();
    }

}
