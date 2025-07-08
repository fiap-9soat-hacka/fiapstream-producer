# fiapstream-producer

[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=fiap-9soat-hacka_fiapstream-processador&metric=coverage)](https://sonarcloud.io/summary/new_code?id=fiap-9soat-hacka_fiapstream-processador)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=fiap-9soat-hacka_fiapstream-processador&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=fiap-9soat-hacka_fiapstream-processador)

## Objetivo
API para o projeto de Hackaton da pós-tech FIAP, turma 9SOAT.  
Essa API é responsável por orquestrar os pedidos de processamento de vídeo recebidos, encaminhando os pedidos ao serviço de processamento 
e realizando o controle de estados e alarmes.


## Configuração

Para realizar a configuração do projeto, é necessário ter a CLI do `docker` com o plugin `docker compose` instalado.  
Primeiramente, inicialize um arquivo `.env` na pasta raiz do projeto, e insira esse conteúdo:
```
#opcional, usuario do sistema de email
EMAIL_SENDER=meugmail@gmail.com 
#opcional, senha do sistema de email
EMAIL_PASSWORD=minhasenha@123 
# access ID da AWS
AWS_ACCESS_KEY_ID=213921d... 
# secret_key da AWS
AWS_SECRET_ACCESS_KEY=12391239123... 
# session_token da AWS
AWS_SESSION_TOKEN=12319d12d.. 
# a url onde o endpoint de websocket é exposto, https://localhost:8084 por padrão
# recomendamos manter o valor padrão para essa propriedade.
WEBSOCKET_URI=https://localhost:8084
# Usuario do MySQL, 'fiap' por padrão
MYSQL_USER=fiap
# Senha do MySQL 'fiap' por padrão
MYSQL_PASSWORD=fiap
# Nome do bucket criado no serviço de S3, 'fiap-9soat-bucket' por padrão.
S3_BUCKET_NAME=fiap-9soat-bucket
# Credenciais do RabbitMQ. Substituia após a configuração da sua instância!
RABBITMQ_USER=guest
RABBITMQ_PASSWORD=guest
```
As credenciais da AWS podem ser obtidas atráves do arquivo `~/.aws/credentials`, caso você tenha o `AWS CLI` configurado.

### Deploy

Com a configuração feita, basta inicializar o deploy com o `docker compose`:
```shell
docker compose -f docker-compose.prod.yml up -d
```

É importante que seja especificado o arquivo `docker-compose.prod.yml` na subida, já que essa versão sobe e expõe todos os serviços 
necessários. Esse deploy vai expor a aplicação principal na porta `:8080`, e o endereço do Websocket na porta `:8084`.

### Configuração

Antes de realizar chamadas nas aplicações, é necessário terminar a configuração dos componentes:

#### S3
É necessário configurar o serviço de S3 para que seja acessível com as credenciais especificadas.  
Siga [esse guia](https://docs.aws.amazon.com/AmazonS3/latest/userguide/GetStartedWithS3.html) para realizar a configuração inicial do S3 do bucket na AWS.  
Ao término da configuração, lembre-se de criar os endpoints de gateway necessários para viabilizar o acesso externo ao bucket, referência [nesse guia](https://docs.aws.amazon.com/pt_br/vpc/latest/privatelink/vpc-endpoints-s3.html).  

Recomendamos utilizar o nome `fiap-9soat-bucket` na criação do bucket. Caso outro nome tenha sido especificado, lembre-se de atualizar a variavel `S3_BUCKET_NAME` no arquivo `.env`.

#### RabbitMQ
Por padrão, inicializamos as credenciais de inicialização do RabbitMQ (`guest`). Recomendamos que seja criado um novo usuario e senha após a subida do projeto, atráves da interface gráfica do RabbitMQ (`http://localhost:15672/`).  
Após criação do usuario, lembre-se de atualizar as variaveis `RABBITMQ_USER` e `RABBITMQ_PASSWORD` corretamente.