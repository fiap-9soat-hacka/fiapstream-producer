package orq.fiap.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "Clientes")
public class ClientesEntity {

    @Id
    @Column(name = "codigo_cliente")
    private Integer codigoCliente;

    @Column(name = "email_cliente")
    private String emailCliente;
}
