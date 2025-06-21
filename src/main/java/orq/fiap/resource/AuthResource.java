package orq.fiap.resource;

import io.quarkus.security.Authenticated;
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
import orq.fiap.dto.UserCreateRequest;
import orq.fiap.services.AuthService;

import java.net.URI;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    AuthService authService;

    @Path("sign-up")
    @Transactional
    @POST
    public Response signUp(@Valid UserCreateRequest userCreateRequest){
        authService.signUp(userCreateRequest);

        return Response.created(URI.create("/user")).build();
    }

    @Path("test")
    @GET
    @Authenticated
    public Response test(@Context SecurityContext securityContext){
        return Response.ok(securityContext).build();
    }
}
