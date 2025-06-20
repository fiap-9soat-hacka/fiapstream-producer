CREATE TABLE Videos (
    codigo_video INT AUTO_INCREMENT NOT NULL,
    id_video_s3 VARCHAR(255) NOT NULL,
    codigo_cliente SMALLINT NOT NULL,
    nome_arquivo VARCHAR(255) NOT NULL,
    estado_processamento SMALLINT NOT NULL,
    webhook_url VARCHAR(500) NOT NULL,
    ts_alteracao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_videos PRIMARY KEY (codigo_video)
);
CREATE TABLE Clientes(
    codigo_cliente SMALLINT NOT NULL,
    email_cliente VARCHAR(100) NOT NULL,
    CONSTRAINT pk_clientes PRIMARY KEY (codigo_cliente)
);
ALTER TABLE Videos
ADD CONSTRAINT fk_videos_clientes FOREIGN KEY (codigo_cliente) REFERENCES Clientes(codigo_cliente);