ALTER TABLE Videos
ADD UNIQUE (id_video_s3);
ALTER TABLE Clientes
ADD UNIQUE (email_cliente);