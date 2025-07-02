package orq.fiap.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import orq.fiap.enums.EstadoProcessamento;

@Converter(autoApply = true)
public class EstadoProcessamentoConverter implements AttributeConverter<EstadoProcessamento, Integer> {
    @Override
    public Integer convertToDatabaseColumn(EstadoProcessamento estado) {
        return estado != null ? estado.getCodigoEstado() : null;
    }

    @Override
    public EstadoProcessamento convertToEntityAttribute(Integer dbCode) {
        return dbCode != null ? EstadoProcessamento.fromCode(dbCode) : null;
    }
}
