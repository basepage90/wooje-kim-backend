# Remit Server by Hexagonal Architecture

## 실행

```bash
  docker compose up -d 
```

```bash
  docker compose down
```

## H2 Console

- http://localhost:8080/h2-console
    - Driver Class: org.h2.Driver
    - JDBC URL: jdbc:h2:mem:remitdb
    - User Name: sa
    - Password: (빈값)

## Swagger

- http://localhost:8080/swagger-ui/index.html

## API

### 계좌(Account)
- 계좌 등록 `POST /api/accounts`  
  - 요청 - RegisterAccountRequest  
  - 응답 - AccountResponse
- 계좌 삭제 `DELETE /api/accounts/{accountId}`  
  - 요청 - accountId  
  - 응답 - AccountResponse

#### 거래(Transaction)
- 입금 `POST /api/transactions/deposit`  
  - 요청 - DepositRequest  
  - 응답 - TransactionResponse
- 출금 `POST /api/transactions/withdraw`  
  - 요청 - WithdrawRequest  
  - 응답 - TransactionResponse
- 이체 `POST /api/transactions/transfer`  
  - 요청 - TransferRequest  
  - 응답 - TransactionResponse
- 거래내역 조회 `GET /api/transactions/{transactionId}`  
  - 요청 - transactionId  
  - 응답 - TransactionResponse
