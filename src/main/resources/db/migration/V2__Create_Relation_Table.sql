ALTER TABLE HistoricoProcessamento DROP FOREIGN KEY FK_HISTORICOPROCESSAMENTO_ON_USERID;
ALTER TABLE HistoricoProcessamento DROP COLUMN userId;
CREATE TABLE Processamento(
    id BIGINT AUTO_INCREMENT NOT NULL,
    uuid VARCHAR(255) NOT NULL,
    estado INT NOT NULL,
    descricaoEstado VARCHAR(500) NULL,
    webhookUrl VARCHAR(255) NULL,
    CONSTRAINT pk_processamento PRIMARY KEY (id)
);
CREATE TABLE UsuarioProcessamento (
    usuarioId BIGINT NOT NULL,
    processamentoId BIGINT NOT NULL,
    CONSTRAINT pk_usuario_processamento PRIMARY KEY (usuarioId, processamentoId),
    CONSTRAINT fk_usuario FOREIGN KEY (usuarioId) REFERENCES Usuario (id),
    CONSTRAINT fk_processamento FOREIGN KEY (processamentoId) REFERENCES Processamento (id)
);