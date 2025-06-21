package orq.fiap.repository;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import orq.fiap.entity.Usuario;

import java.util.Optional;

@ApplicationScoped
public class UsuarioRepository implements PanacheRepository<Usuario> {
    /**
     * Adds a new user to the database with default role of 'user'
     * @param username the username
     * @param password the unencrypted password (it is encrypted with bcrypt)
     */
    public Long add(String username, String password, String email) {
        return this.add(username, password, email, "user");
    }

    /**
     * Adds a new user to the database
     * @param username the username
     * @param password the unencrypted password (it is encrypted with bcrypt)
     * @param role the comma-separated roles
     */
    public Long add(String username, String password, String email, String role) {
        Usuario usuario = new Usuario();
        usuario.username = username;
        usuario.password = BcryptUtil.bcryptHash(password);
        usuario.role = role;
        usuario.email = email;

        persist(usuario);

        return usuario.id;
    }

    public Optional<Usuario> findByUsername(String username){
        return find("username", username).singleResultOptional();
    }
}

