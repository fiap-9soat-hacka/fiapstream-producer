package orq.fiap.resource;

import java.net.URI;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import orq.fiap.dto.AuthSignInRequest;
import orq.fiap.dto.UserCreateRequest;
import orq.fiap.services.AuthService;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    AuthService authService;

    @Path("/sign-up")
    @Transactional
    @POST
    public Response signUp(@Valid UserCreateRequest userCreateRequest) {
        Long userId = authService.signUp(userCreateRequest);

        return Response.created(URI.create("/user/" + userId)).build();
    }

    @Path("/sign-in")
    @Transactional
    @POST
    public Response signIn(@Valid AuthSignInRequest signInRequest) {
        return Response.ok(authService.signIn(signInRequest.username(), signInRequest.password())).build();
    }
}
