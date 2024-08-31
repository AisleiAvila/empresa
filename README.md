# Empresa Application

## Introdução
Esta é uma aplicação Java Spring Boot para gerenciar perfis e usuários. A aplicação utiliza JPA para persistência de dados e JWT para autenticação.

## Pré-requisitos
- Java 17 ou superior
- Maven 3.6.0 ou superior
- Banco de dados PostgreSQL

## Instalação
1. Clone o repositório:
    ```bash
    git clone https://github.com/seu-usuario/empresa.git
    cd empresa
    ```

2. Configure o banco de dados PostgreSQL:
    - Crie um banco de dados chamado `empresa`.
    - Atualize as configurações de conexão no arquivo `src/main/resources/application.properties`.

3. Compile e instale as dependências do projeto:
    ```bash
    mvn clean install
    ```

## Execução
Para iniciar a aplicação, execute o seguinte comando:
```bash
mvn spring-boot:run
