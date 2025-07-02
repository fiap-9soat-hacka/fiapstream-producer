package orq.fiap.enums;

public enum EstadoProcessamento {
    PENDENTE("Pendente", 1),
    PROCESSANDO("Processando", 2),
    CONCLUIDO("Concluído", 3),
    ERRO("Erro", -1);

    private final String descricao;
    private final Integer codigoEstado;

    EstadoProcessamento(String descricao, Integer codigoEstado) {
        this.descricao = descricao;
        this.codigoEstado = codigoEstado;
    }

    public String getDescricao() {
        return descricao;
    }

    public Integer getCodigoEstado() {
        return codigoEstado;
    }

    public static EstadoProcessamento fromCode(Integer codigo) {
        for (EstadoProcessamento estado : values()) {
            if (estado.getCodigoEstado().equals(codigo)) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Código de estado inválido: " + codigo);
    }
}
