CREATE SCHEMA IF NOT EXISTS FIAPSTREAM;
CREATE TABLE `Usuario` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `username` varchar(255) DEFAULT NULL,
    `password` varchar(255) DEFAULT NULL,
    `email` varchar(255) DEFAULT NULL,
    `role` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uc_usuario_email` (`email`)
);
CREATE TABLE `HistoricoProcessamento` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `uuid` varchar(255) NOT NULL,
    `estado` int NOT NULL,
    `descricaoEstado` varchar(500) DEFAULT NULL,
    `webhookUrl` varchar(255) DEFAULT NULL,
    `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `filename` varchar(255) NOT NULL,
    `userId` bigint NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_HISTORICOPROCESSAMENTO_ON_USERID` (`userId`),
    CONSTRAINT `FK_HISTORICOPROCESSAMENTO_ON_USERID` FOREIGN KEY (`userId`) REFERENCES `Usuario` (`id`)
);
CREATE TABLE `Processamento` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `uuid` varchar(255) NOT NULL,
    `estado` int NOT NULL,
    `descricaoEstado` varchar(500) DEFAULT NULL,
    `webhookUrl` varchar(255) DEFAULT NULL,
    `filename` varchar(255) NOT NULL,
    `userId` bigint NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_PROCESSAMENTO_ON_USERID` (`userId`),
    CONSTRAINT `FK_PROCESSAMENTO_ON_USERID` FOREIGN KEY (`userId`) REFERENCES `Usuario` (`id`)
);