ALTER TABLE UsuarioProcessamento
DROP
FOREIGN KEY fk_processamento;

ALTER TABLE UsuarioProcessamento
DROP
FOREIGN KEY fk_usuario;

ALTER TABLE HistoricoProcessamento
    ADD userId BIGINT NULL;

ALTER TABLE HistoricoProcessamento
    MODIFY userId BIGINT NOT NULL;

ALTER TABLE Processamento
    ADD userId BIGINT NULL;

ALTER TABLE Processamento
    MODIFY userId BIGINT NOT NULL;

ALTER TABLE HistoricoProcessamento
    ADD CONSTRAINT FK_HISTORICOPROCESSAMENTO_ON_USERID FOREIGN KEY (userId) REFERENCES Usuario (id);

ALTER TABLE Processamento
    ADD CONSTRAINT FK_PROCESSAMENTO_ON_USERID FOREIGN KEY (userId) REFERENCES Usuario (id);

DROP TABLE UsuarioProcessamento;

ALTER TABLE HistoricoProcessamento
    MODIFY filename VARCHAR (255) NOT NULL;

ALTER TABLE Processamento
    MODIFY filename VARCHAR (255) NOT NULL;
