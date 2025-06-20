package orq.fiap.enums;

import lombok.Getter;

@Getter
public enum EstadoProcessamento {
    PENDENTE("Pendente"),
    PROCESSANDO("Processando"),
    CONCLUIDO("Concluído"),
    ERRO("Erro");

    private final String descricao;

    EstadoProcessamento(String descricao) {
        this.descricao = descricao;
    }

}
