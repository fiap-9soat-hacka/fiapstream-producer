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
INSERT INTO `Usuario` (id, username, password, email, `role`)
VALUES(
        1,
        'teste',
        '$2a$10$.O.15R6M4VjPVmi5W754C.6TnLEOcbnq.de96xv8blAT.NV.lFdFC',
        'du_ikei@hotmail.com',
        'user'
    );
INSERT INTO `HistoricoProcessamento` (
        id,
        uuid,
        estado,
        descricaoEstado,
        webhookUrl,
        `timestamp`,
        filename,
        userId
    )
VALUES(
        1,
        'testevideo - Copia (2).mp4-ffe66072-e11f-4161-aabc-de6ed09addef',
        1,
        NULL,
        'https://localhost:8081/processador',
        '2025-06-25 18:14:53',
        'testevideo - Copia (2).mp4',
        1
    );
INSERT INTO `HistoricoProcessamento` (
        id,
        uuid,
        estado,
        descricaoEstado,
        webhookUrl,
        `timestamp`,
        filename,
        userId
    )
VALUES(
        2,
        'testevideo - Copia (2).mp4-e133ae7d-76b9-47e0-8472-e828d74e9c22',
        1,
        NULL,
        'https://localhost:8081/processador',
        '2025-06-25 18:15:23',
        'testevideo - Copia (2).mp4',
        1
    );
INSERT INTO `HistoricoProcessamento` (
        id,
        uuid,
        estado,
        descricaoEstado,
        webhookUrl,
        `timestamp`,
        filename,
        userId
    )
VALUES(
        3,
        'testevideo - Copia (2).mp4-eed58040-1989-4cbf-ae6e-092db7c3ef43',
        1,
        NULL,
        'https://localhost:8081/processador',
        '2025-06-25 18:18:19',
        'testevideo - Copia (2).mp4',
        1
    );
INSERT INTO `HistoricoProcessamento` (
        id,
        uuid,
        estado,
        descricaoEstado,
        webhookUrl,
        `timestamp`,
        filename,
        userId
    )
VALUES(
        4,
        'testevideo - Copia (2).mp4-faa1a860-b339-4a76-9eae-6a0f46b04b34',
        1,
        NULL,
        'http://localhost:8081/processador',
        '2025-06-25 18:19:05',
        'testevideo - Copia (2).mp4',
        1
    );
INSERT INTO `HistoricoProcessamento` (
        id,
        uuid,
        estado,
        descricaoEstado,
        webhookUrl,
        `timestamp`,
        filename,
        userId
    )
VALUES(
        5,
        'testevideo - Copia (2).mp4-098104cb-5eb9-48fb-ba25-93403d2be28d',
        1,
        NULL,
        'http://localhost:8081/processador',
        '2025-06-25 18:21:58',
        'testevideo - Copia (2).mp4',
        1
    );
INSERT INTO `HistoricoProcessamento` (
        id,
        uuid,
        estado,
        descricaoEstado,
        webhookUrl,
        `timestamp`,
        filename,
        userId
    )
VALUES(
        6,
        'testevideo - Copia (2).mp4-96c69a19-8b00-43fd-8f80-0c020fdb481f',
        1,
        NULL,
        'http://localhost:8081/processador',
        '2025-06-25 18:22:57',
        'testevideo - Copia (2).mp4',
        1
    );
INSERT INTO `HistoricoProcessamento` (
        id,
        uuid,
        estado,
        descricaoEstado,
        webhookUrl,
        `timestamp`,
        filename,
        userId
    )
VALUES(
        7,
        'testevideo - Copia (2).mp4-96c69a19-8b00-43fd-8f80-0c020fdb481f',
        3,
        NULL,
        'http://localhost:8081/processador',
        '2025-06-25 18:22:58',
        'testevideo - Copia (2).mp4',
        1
    );
INSERT INTO `HistoricoProcessamento` (
        id,
        uuid,
        estado,
        descricaoEstado,
        webhookUrl,
        `timestamp`,
        filename,
        userId
    )
VALUES(
        8,
        'testevideo - Copia (2).mp4-96c69a19-8b00-43fd-8f80-0c020fdb481f',
        2,
        NULL,
        'http://localhost:8081/processador',
        '2025-06-25 18:22:58',
        'testevideo - Copia (2).mp4',
        1
    );
INSERT INTO `HistoricoProcessamento` (
        id,
        uuid,
        estado,
        descricaoEstado,
        webhookUrl,
        `timestamp`,
        filename,
        userId
    )
VALUES(
        9,
        'testevideo - Copia (2).mp4-50d87ef9-6489-4b62-afdc-b552117071fe',
        1,
        NULL,
        'http://localhost:8081/processador',
        '2025-06-25 18:23:40',
        'testevideo - Copia (2).mp4',
        1
    );
INSERT INTO `HistoricoProcessamento` (
        id,
        uuid,
        estado,
        descricaoEstado,
        webhookUrl,
        `timestamp`,
        filename,
        userId
    )
VALUES(
        10,
        'testevideo - Copia (2).mp4-05a7c27b-0535-407b-85d2-3148763d3440',
        1,
        NULL,
        'http://localhost:8081/processador',
        '2025-06-25 18:24:12',
        'testevideo - Copia (2).mp4',
        1
    );
INSERT INTO `Processamento` (
        id,
        uuid,
        estado,
        descricaoEstado,
        webhookUrl,
        filename,
        userId
    )
VALUES(
        1,
        'testevideo - Copia (2).mp4-ffe66072-e11f-4161-aabc-de6ed09addef',
        1,
        NULL,
        'https://localhost:8081/processador',
        'testevideo - Copia (2).mp4',
        1
    );
INSERT INTO `Processamento` (
        id,
        uuid,
        estado,
        descricaoEstado,
        webhookUrl,
        filename,
        userId
    )
VALUES(
        2,
        'testevideo - Copia (2).mp4-e133ae7d-76b9-47e0-8472-e828d74e9c22',
        1,
        NULL,
        'https://localhost:8081/processador',
        'testevideo - Copia (2).mp4',
        1
    );
INSERT INTO `Processamento` (
        id,
        uuid,
        estado,
        descricaoEstado,
        webhookUrl,
        filename,
        userId
    )
VALUES(
        3,
        'testevideo - Copia (2).mp4-eed58040-1989-4cbf-ae6e-092db7c3ef43',
        1,
        NULL,
        'https://localhost:8081/processador',
        'testevideo - Copia (2).mp4',
        1
    );
INSERT INTO `Processamento` (
        id,
        uuid,
        estado,
        descricaoEstado,
        webhookUrl,
        filename,
        userId
    )
VALUES(
        4,
        'testevideo - Copia (2).mp4-faa1a860-b339-4a76-9eae-6a0f46b04b34',
        1,
        NULL,
        'http://localhost:8081/processador',
        'testevideo - Copia (2).mp4',
        1
    );
INSERT INTO `Processamento` (
        id,
        uuid,
        estado,
        descricaoEstado,
        webhookUrl,
        filename,
        userId
    )
VALUES(
        5,
        'testevideo - Copia (2).mp4-098104cb-5eb9-48fb-ba25-93403d2be28d',
        1,
        NULL,
        'http://localhost:8081/processador',
        'testevideo - Copia (2).mp4',
        1
    );
INSERT INTO `Processamento` (
        id,
        uuid,
        estado,
        descricaoEstado,
        webhookUrl,
        filename,
        userId
    )
VALUES(
        6,
        'testevideo - Copia (2).mp4-96c69a19-8b00-43fd-8f80-0c020fdb481f',
        1,
        NULL,
        'http://localhost:8081/processador',
        'testevideo - Copia (2).mp4',
        1
    );
INSERT INTO `Processamento` (
        id,
        uuid,
        estado,
        descricaoEstado,
        webhookUrl,
        filename,
        userId
    )
VALUES(
        7,
        'testevideo - Copia (2).mp4-50d87ef9-6489-4b62-afdc-b552117071fe',
        1,
        NULL,
        'http://localhost:8081/processador',
        'testevideo - Copia (2).mp4',
        1
    );
INSERT INTO `Processamento` (
        id,
        uuid,
        estado,
        descricaoEstado,
        webhookUrl,
        filename,
        userId
    )
VALUES(
        8,
        'testevideo - Copia (2).mp4-05a7c27b-0535-407b-85d2-3148763d3440',
        1,
        NULL,
        'http://localhost:8081/processador',
        'testevideo - Copia (2).mp4',
        1
    );