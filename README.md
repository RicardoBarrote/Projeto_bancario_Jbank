# Sistema Bancário Digital - JBank

## Visão Geral
Sistema bancário digital que permite criação de carteiras virtuais, depósitos e transferências entre usuários, com registro completo de transações para extrato.

## Tecnologias Utilizadas
- **Backend**: Java 21, Spring Boot 3.x
- **Banco de Dados**: MySQL (com suporte a UUID)
- **Segurança**: Validação de IP, Auditoria de requisições
- **Exception**: Problem Details for HTTP APIs (RFC 7807)

## Funcionalidades Principais
- Criação e exclusão de carteiras virtuais
- Depósitos com registro de IP de origem
- Transferências entre usuários com verificação de saldo
- Extrato com paginação e classificação de operações (crédito/débito)
- Auditoria completa de todas as operações

## Diagrama de Arquitetura
```mermaid
classDiagram
    direction TB
    
    class Wallet {
        +UUID walletId
        +String cpf
        +String email
        +String name
        +BigDecimal balance
        +Long version
    }
    
    class Deposit {
        +UUID depositId
        +BigDecimal depositValue
        +LocalDateTime depositDateTime
        +String ipAddress
    }
    
    class Transfer {
        +UUID transferId
        +BigDecimal transferValue
        +LocalDateTime transferDateTime
    }
    
    class StatementView {
        +String getStatementId()
        +String getType()
        +BigDecimal getStatementValue()
        +String getWalletReceiver()
        +String getWalletSender()
        +LocalDateTime getStatementDateTime()
    }
    
    Wallet "1" *-- "0..*" Deposit : has
    Wallet "1" *-- "0..*" Transfer : as sender
    Wallet "1" *-- "0..*" Transfer : as receiver
    
    class WalletService {
        +createWallet()
        +deleteWallet()
        +deposit()
        +getStatement()
    }
    
    class TransferService {
        +transferMoney()
    }
    
    WalletService --> WalletRepository
    WalletService --> DepositRepository
    TransferService --> TransferRepository
    TransferService --> WalletRepository
    
    class WalletRepository {
        +findByCpfOrEmail()
        +findStatements()
    }
    
    class DepositRepository
    class TransferRepository
    
    class WalletController {
        +createWallet()
        +deposit()
        +deleteWallet()
        +getStatement()
    }
    
    class TransferController {
        +transfer()
    }
    
    WalletController --> WalletService
    TransferController --> TransferService
    
    class AuditInterceptor {
        +preHandle()
        +postHandle()
        +afterCompletion()
    }
    
    class IpFilter {
        +doFilter()
    }
    
    class GlobalExceptionHandler {
        +handleJbankException()
        +handleMethodArgumentNotValidException()
    }
    
    note for Wallet "Entidade principal que armazena\ndados do usuário e saldo"
    note for Deposit "Registra depósitos com\nIP de origem"
    note for Transfer "Registra transferências\nentre carteiras"
    note for StatementView "Interface para visualização\nunificada de extrato"
```

## Endpoints Principais

### Carteiras
- **POST /wallets - Cria nova carteira**

- **DELETE /wallets/{walletId} - Remove carteira (se saldo zero)**

- **POST /wallets/{walletId}/deposits - Realiza depósito**

- **GET /wallets/{walletId}/statements - Obtém extrato**

## Transferências
- **POST /transfers - Realiza transferência entre carteiras**

## Tratamento de Erros
### O sistema utiliza Problem Details (RFC 7807) para respostas de erro, com:

- **Detecção automática de campos inválidos**

- **Mensagens claras para erros de negócio**

- **Hierarquia de exceções customizadas**
