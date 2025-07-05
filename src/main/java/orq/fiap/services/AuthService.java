package orq.fiap.services;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.logging.Log;
import io.quarkus.security.ForbiddenException;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

import orq.fiap.dto.UserCreateRequest;
import orq.fiap.entity.Processamento;
import orq.fiap.entity.Usuario;
import orq.fiap.repository.ProcessamentoRepository;
import orq.fiap.repository.UsuarioRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AuthService {

    @Inject
    UsuarioService usuarioService;

    @Inject
    ProcessamentoRepository processamentoRepository;

    @Inject
    JsonWebToken jwt;

    public Long signUp(UserCreateRequest request) {
        Long userId = usuarioService.add(request);

        Log.infof("Generated user id: {}", userId);

        return userId;
    }

    public String signIn(String username, String password) {
        Usuario usuario = usuarioService.findByUsername(username);
        if (BcryptUtil.matches(password, usuario.getPassword())) {

            return Jwt.issuer("https://fiapstream.fiap.org")
                    .upn(usuario.getUsername())
                    .groups(new HashSet<>(List.of("user")))
                    .claim(Claims.sub, usuario.getUsername())
                    .claim("userId", usuario.getId())
                    .sign();
        }

        throw new NotFoundException("Invalid username or password");
    }

    public Processamento validarUsuario(String uuid) {
        Processamento processamento = processamentoRepository.findById(uuid);

        if (!processamento.getUsuario().getId().equals(Long.valueOf(jwt.getClaim("userId").toString()))) {
            throw new ForbiddenException("Usuário não autorizado a acessar este processamento.");
        }

        return processamento;
    }

    public Integer retornarUserId() {
        return jwt.getClaim("userId");
    }
}
