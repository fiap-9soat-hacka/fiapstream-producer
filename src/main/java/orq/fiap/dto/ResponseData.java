package orq.fiap.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import orq.fiap.enums.EstadoProcessamento;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseData {
    String key;
    String filename;
    EstadoProcessamento estado;
}