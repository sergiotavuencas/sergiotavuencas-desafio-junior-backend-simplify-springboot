# Desafio Lista de Tarefas pela Simplify

## Documentação de Execução
Este documento fornece um passo a passo para auxiliar na execução do projeto **Desafio Lista de Tarefas pela Simplify**.

## Pré-requisitos
Antes de tudo, certifique-se de ter o [Git](https://git-scm.com) e [Docker](https://docs.docker.com/engine/install/)
instalados e configurados em sua máquina.

## Passo a Passo

### 1. Clonando o Repositório
Clone o repositório no diretório de sua preferência utilizando o comando abaixo:
```
$ git clone https://github.com/sergiotavuencas/desafio-simplify.git
```

### 2. Acessando o Diretório do Projeto
Acesse o repositório em sua máquina local com o comando:
```
$ cd desafio-simplify
```

### 3. Executando o Projeto
Agora basta executar o seguinte comando para iniciar o projeto:
```
$ docker compose up -d
```


### 4. Acessando a Aplicação
Certifique-se de que os contêineres `todolist_api` e `todolist_db` em ***NAMES*** estão em execução. Você pode
verificar isso com o comando:
```
$ docker ps
```
Em seguida, acesse a aplicação através do seguinte link: [Swagger UI](http://localhost:8080/swagger-ui/index.html).

## Importante

Não é necessário ter o Java e PostgreSQL instalado em sua máquina, pois o Docker se encarrega de fornecer todas as dependências
necessárias para a execução da aplicação.