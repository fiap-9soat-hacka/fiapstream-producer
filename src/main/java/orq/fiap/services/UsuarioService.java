package orq.fiap.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import orq.fiap.dto.UserCreateRequest;
import orq.fiap.entity.Usuario;
import orq.fiap.repository.UsuarioRepository;

@RequestScoped
public class UsuarioService {

    @Claim(standard = Claims.sub)
    String username;

    @Inject
    UsuarioRepository usuarioRepository;

    /**
     * Recupera o usuario logado atualmente.
     * Não funciona para endpoints não protegidos.
     */
    public Usuario getAuthUser() {
        return findByUsername(username);
    }

    public Usuario findByUsername(String username) {
        return usuarioRepository.findByUsername(username).orElseThrow(NotFoundException::new);
    }

    public Long add(UserCreateRequest request) {
        return usuarioRepository.add(request.username(), request.password(), request.email());
    }

}
