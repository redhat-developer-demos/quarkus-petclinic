package org.quarkus.samples.petclinic.login;

import com.sun.security.auth.UserPrincipal;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.IdentityProvider;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.AuthenticationRequest;
import io.quarkus.security.identity.request.UsernamePasswordAuthenticationRequest;
import io.quarkus.security.runtime.QuarkusPrincipal;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.auth.authentication.UsernamePasswordCredentials;
import org.apache.sshd.common.config.keys.loader.openssh.kdf.BCrypt;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.UserTransaction;
import java.util.function.Supplier;

@ApplicationScoped
public class CustomIdentityProvider implements IdentityProvider<UsernamePasswordAuthenticationRequest> {


    @Override
    public Class<UsernamePasswordAuthenticationRequest> getRequestType() {
        return UsernamePasswordAuthenticationRequest.class;
    }

    @Override
    public Uni<SecurityIdentity> authenticate(UsernamePasswordAuthenticationRequest usernamePasswordAuthenticationRequest, AuthenticationRequestContext authenticationRequestContext) {
        Supplier<SecurityIdentity> s = () -> new QuarkusSecurityIdentity.Builder().setPrincipal(new QuarkusPrincipal(usernamePasswordAuthenticationRequest.getUsername())).addRole("User")
                .build();
        return authenticationRequestContext.runBlocking(s);

    }
}
