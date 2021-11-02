
# Sobre a aplicação
Esta aplicação exemplifica a rotina de transações presente em uma operação de crédito.

Cada portador de cartão (cliente) possui uma conta com seus dados. A cada operação realizada pelo cliente uma transação é criada e associada à sua respectiva conta. Cada transação possui um tipo (compra à vista, compra parcelada, saque ou pagamento), um valor e uma data de criação. Transações de tipo compra e saque são registradas com valor negativo, enquanto transações de pagamento são registradas com valor positivo.


## Tecnologias utilizadas
Java 11, Spring Boot, Swagger-UI, MySQL e Docker.


## Sobre o modelo de dados
O modelo de dados é composto por quatro tabelas principais ('account', 'card', 'invoice', 'transaction') e uma tabela de relacionamento ('invoice_has_transactions'), conforme detalhados a seguir.

Detalhes da tabela "account":
| campo | tipo |
| ------ | ------ |
| id | bigint not null primary key auto_increment |
| doc_number | varchar(11) not null |
| card_id | bigint not null |
| credit_limit | decimal(15,2) not null |
| withdrawal_limit | decimal(15,2) not null |
| invoice_closing_day | integer not null |
| is_active | varchar(1) not null |

Detalhes da tabela "card":
| campo | tipo |
| ------ | ------ |
| id | bigint not null primary key auto_increment |
| card_number | varchar(16) not null |
| is_active | varchar(1) not null |

Detalhes da tabela "invoice":
| campo | tipo |
| ------ | ------ |
| id | bigint not null primary key auto_increment |
| invoice_number | varchar(16) not null |
| payment_due | decimal(15,2) not null |
| payment_due_date | DATE not null |
| invoice_status | varchar(20) not null |
| account_id | bigint not null |

Detalhes da tabela "invoice_has_transactions":
| campo | tipo |
| ------ | ------ |
| invoice_id | bigint not null |
| transaction_id | bigint not null |

Detalhes da tabela "transaction":
| campo | tipo |
| ------ | ------ |
| id | bigint not null primary key auto_increment |
| amount | decimal(15,2) not null |
| description | varchar(100) not null |
| event_date | TIMESTAMP not null |
| operation_type | varchar(20) not null |
| account_id | bigint not null |

## Sobre o diretório "/mysql"
O diretório "/mysql" presente na raiz do projeto possui 2 sub-diretórios.
* (initdb) Contém um arquivo "dbclientes.sql" para criação da tabela e inserção da carga inicial dos dados.
* (storage) Poderá ser referenciado como "volume" pelo container do MySQL.

## Sobre o arquivo "Dockerfile"
O arquivo "Dockerfile" presente na raiz do projeto é responsável pela criação da "imagem" a ser utilizada pelo docker para instanciar o container da aplicação (Java/SpringBoot). Este arquivo está sendo referenciado pelo "docker-compose.yml".

## Sobre o arquivo "docker-compose.yml"
O arquivo "docker-compose.yml" contém as configurações necessárias para construir e orquestrar os 2 containers (mysql_db, credit-account) necessários para o funcionamento desta aplicação.


## Instalação

OS X & Linux & Windows:

```sh
mvn clean install
docker-compose up -d
```

## End-points relacionados com a conta do usuário (account)

#### [ POST ] /v1/accounts
O endpoint abaixo é responsável pela criação da conta/cartão do usuário.

```json
Request Body {
  "docNumber": "29645636086",
  "creditLimit": 10000.00,
  "withdrawalLimit": 1000.00,
  "invoiceClosingDay": 15
}
 ```
 
```json
Response Body {
  "id": 1,
  "docNumber": "29645636086",
  "creditLimit": 10000,
  "withdrawalLimit": 1000,
  "invoiceClosingDay": 15,
  "card": {
    "id": 1,
    "cardNumber": "5100921398143993",
    "isActive": true
  },
  "isActive": true
}
 ```

#### [ GET ] /v1/accounts/1
Através deste endpoint é possível recuperar os dados da conta/cartão do usuário através do código (ID) da conta.

```json
Response Body {
  "id": 1,
  "docNumber": "29645636086",
  "creditLimit": 10000,
  "withdrawalLimit": 1000,
  "invoiceClosingDay": 15,
  "card": {
    "id": 1,
    "cardNumber": "5100921398143993",
    "isActive": true
  },
  "isActive": true
}
 ```


## End-points relacionados com a fatura (invoice)

#### [ GET ] /v1/invoices/process-account/1
Através deste endpoint é possível chamar a rotina de processamento das transações e geração da fatura. Esta mesma rotina possui um agendamento para execução diária, à 00:00h .

```json
Response Body {
  "id": 1,
  "status": "AGUARDANDO_PAGAMENTO",
  "invoiceNumber": "5157777594808025",
  "paymentDueDate": "12/11/2021",
  "paymentDue": 1328.21,
  "transactions": [
    {
      "id": 1,
      "type": "COMPRA_A_VISTA",
      "description": "ACADEMIA XYZ",
      "amount": -100,
      "eventDate": "02/11/2021 18:31:22"
    },
    {
      "id": 2,
      "type": "COMPRA_PARCELADA",
      "description": "LIVRARIA XYZ  (1/4)",
      "amount": -228.21,
      "eventDate": "02/11/2021 18:35:05"
    },
    {
      "id": 6,
      "type": "SAQUE",
      "description": "Saque Banco 24h",
      "amount": -1000,
      "eventDate": "02/11/2021 18:47:40"
    }
  ]
}
 ```

#### [ GET ] /v1/invoices/1
Este endpoint recupera os dados da fatura, através do código (ID).

 ```json
Response Body {
  "id": 1,
  "status": "PAGO_TOTALMENTE",
  "invoiceNumber": "5157777594808025",
  "paymentDueDate": "12/11/2021",
  "paymentDue": 1328.21,
  "transactions": [
    {
      "id": 1,
      "type": "COMPRA_A_VISTA",
      "description": "ACADEMIA XYZ",
      "amount": -100,
      "eventDate": "02/11/2021 18:31:22"
    },
    {
      "id": 2,
      "type": "COMPRA_PARCELADA",
      "description": "LIVRARIA XYZ  (1/4)",
      "amount": -228.21,
      "eventDate": "02/11/2021 18:35:05"
    },
    {
      "id": 6,
      "type": "SAQUE",
      "description": "Saque Banco 24h",
      "amount": -1000,
      "eventDate": "02/11/2021 18:47:40"
    },
    {
      "id": 7,
      "type": "PAGAMENTO",
      "description": "PAGAMENTO TOTAL DA FATURA | PAGAMENTO LOTÉRICA XYZ",
      "amount": 1328.21,
      "eventDate": "02/11/2021 18:54:11"
    }
  ]
}
 ```


## End-points relacionados com as transações (transaction)

#### [ POST ] /v1/transactions
Através deste endpoint é possível criar diversos tipos de transações.

```json
Request Body {
  "type": "COMPRA_PARCELADA",
  "cardNumber": "5100921398143993",
  "amount": 912.85,
  "description": "LIVRARIA XYZ",
  "installments": 4
}
 ```
 
```json
Request Body {
  "type": "COMPRA_A_VISTA",
  "cardNumber": "5100921398143993",
  "amount": 150.00,
  "description": "ACADEMIA XYZ"
}
 ```
 
```json
Request Body {
  "type": "SAQUE",
  "cardNumber": "5100921398143993",
  "amount": 1000.00,
  "description": "Saque Banco 24h"
}
 ```
 
```json
Request Body {
  "type": "PAGAMENTO",
  "invoiceNumber": "5157777594808025",
  "amount": 1328.21,
  "description": "Pagamento Lotérica XYZ"
}
 ```
 
#### [ GET ] /v1/transactions/1
Este endpoint recupera os dados da transação através do código.

```json
Response Body {
  "id": 1,
  "type": "COMPRA_PARCELADA",
  "description": "LIVRARIA XYZ  (1/4)",
  "amount": -228.21,
  "eventDate": "02/11/2021 18:35:05"
}
 ```


## Testando a aplicação através do Swagger-UI
Esta aplicação poderá ser testada a partir da interface (Swagger UI): [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)


## Testando a aplicação através do Postman
Existe um diretório "/postman" na raiz do projeto, contendo uma coleção do Postman com a configuração das chamadas aos end-points expostos pela API.

