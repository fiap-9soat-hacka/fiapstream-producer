ALTER TABLE Processamento
ADD COLUMN presignedUrl VARCHAR(2048) NULL;
ALTER TABLE HistoricoProcessamento
ADD COLUMN presignedUrl VARCHAR(2048) NULL;