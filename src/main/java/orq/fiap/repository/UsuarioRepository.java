package orq.fiap.repository;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import orq.fiap.entity.Usuario;

public class UsuarioRepository implements PanacheRepository<Usuario> {
    /**
     * Adds a new user to the database with default role of 'user'
     * @param username the username
     * @param password the unencrypted password (it is encrypted with bcrypt)
     */
    public void add(String username, String password) {
        this.add(username, password, "user");
    }

    /**
     * Adds a new user to the database
     * @param username the username
     * @param password the unencrypted password (it is encrypted with bcrypt)
     * @param role the comma-separated roles
     */
    public void add(String username, String password, String role) {
        Usuario usuario = new Usuario();
        usuario.username = username;
        usuario.password = BcryptUtil.bcryptHash(password);
        usuario.role = role;

        persist(usuario);
    }
}
