CREATE TABLE HistoricoProcessamento
(
    id              BIGINT AUTO_INCREMENT   NOT NULL,
    uuid            VARCHAR(255)            NOT NULL,
    estado          INT                     NOT NULL,
    descricaoEstado VARCHAR(500) NULL,
    webhookUrl      VARCHAR(255) NULL,
    timestamp       timestamp DEFAULT NOW() NOT NULL,
    userId          BIGINT                  NOT NULL,
    CONSTRAINT pk_historicoprocessamento PRIMARY KEY (id)
);

CREATE TABLE Usuario
(
    id       BIGINT AUTO_INCREMENT NOT NULL,
    username VARCHAR(255) NULL,
    password VARCHAR(255) NULL,
    email    VARCHAR(255) NULL,
    `role`   VARCHAR(255) NULL,
    CONSTRAINT pk_usuario PRIMARY KEY (id)
);

ALTER TABLE Usuario
    ADD CONSTRAINT uc_usuario_email UNIQUE (email);

ALTER TABLE HistoricoProcessamento
    ADD CONSTRAINT FK_HISTORICOPROCESSAMENTO_ON_USERID FOREIGN KEY (userId) REFERENCES Usuario (id);