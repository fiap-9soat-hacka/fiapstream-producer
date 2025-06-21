ALTER TABLE Processamento
ADD COLUMN filename VARCHAR(255) NULL;
ALTER TABLE HistoricoProcessamento
ADD COLUMN filename VARCHAR(255) NULL;