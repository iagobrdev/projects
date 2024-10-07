# Sobre o Projeto

Este projeto é uma aplicação desenvolvida em **Java 17** com **Spring Boot**. Ele utiliza **JSP** para renderizar páginas, com suporte a **HTML**, **CSS** e **JavaScript** para a interface do usuário. O banco de dados utilizado é o **PostgreSQL 16**. O projeto também integra o **Swagger** para documentação da API.

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot**
- **JSP**
- **HTML**
- **CSS**
- **JavaScript**
- **PostgreSQL 16**
- **Docker**
- **Swagger**

## Links Importantes

- **Swagger UI**: A documentação da API pode ser acessada em [http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/).
- **Servidor da Aplicação**: A aplicação roda em [http://localhost:8080/home](http://localhost:8080/home).

## Como Executar a Aplicação

### 1. Pré-requisitos

Certifique-se de que as seguintes ferramentas estão instaladas em sua máquina:

- [Docker](https://www.docker.com/get-started)
- [Docker Compose](https://docs.docker.com/compose/)

### 2. Configuração do Ambiente

Para executar a aplicação, você pode usar o comando:

```bash
docker-compose up --build
```