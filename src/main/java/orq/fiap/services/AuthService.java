package orq.fiap.services;

import io.quarkus.logging.Log;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.Claims;
import org.jboss.logging.Logger;
import org.jboss.logmanager.LogManager;
import orq.fiap.dto.UserCreateRequest;
import orq.fiap.repository.UsuarioRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@ApplicationScoped
public class AuthService {

    @Inject
    UsuarioRepository usuarioRepository;

    public void createUser(UserCreateRequest request){
//        Long userId = usuarioRepository.add(request.username(), request.password(), request.email());

        String token = Jwt.issuer("https://fiapstream.fiap.org")
                .upn(request.username())
                .groups(new HashSet<>(List.of("User")))
                .claim(Claims.sub, request.username())
                .sign();

//        Log.infof("Generated user id: {}", userId);
        Log.infof("Generated JWT: %s", token);
    }

}
