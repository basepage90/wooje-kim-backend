# Remit Server by Hexagonal Architecture

간단한 송금 서버를 헥사고날 아키텍쳐의 정석에 가깝게 설계하였습니다.  
실무에 적용한다면 일부 레이어의 DTO를 통합하여,  
보일러플레이트 코드를 줄이는 방식으로 트레이드 오프 하여 사용 할 수 있겠습니다. 

## 환경
- java 21
- spring boot 3.5.6
- gradle
- docker

## 라이브러리
- H2
  - 인메모리 DB
- Lombok
  - 코드 가독성
- jpa, queryDsl
  - type safe한 jpa 쿼리
- OpenAPI(Swagger)
  - api 명세 및 테스트
- junit
  - 테스트 코드

## 실행

```bash
  docker compose up -d 
```

```bash
  docker compose down
```

## H2 Console

**JDBC URL 변경 필수**

- http://localhost:8080/h2-console
    - Driver Class: org.h2.Driver
    - JDBC URL: jdbc:h2:mem:remitdb
    - User Name: sa
    - Password: (빈값)

## Swagger

- http://localhost:8080/swagger-ui/index.html

---

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
  - 응답 - TransactionHistoryResponse

---
### Project Tree Structure

```
src
  └── main
        ├── java.com.wire.remit
        │     ├── adapter 
        │     │   ├── in
        │     │   │   └── web
        │     │   │       ├── advice
        │     │   │       ├── controller
        │     │   │       ├── dto
        │     │   │       │   ├── request
        │     │   │       │   └── response
        │     │   │       └── mapper
        │     │   └── out
        │     │       ├── persistence
        │     │       │   ├── config
        │     │       │   ├── entity
        │     │       │   ├── mapper
        │     │       │   ├── queryRepository
        │     │       │   │   └── impl
        │     │       │   └── repository
        │     │       └── portimpl
        │     ├── application
        │     │   ├── dto
        │     │   │   ├── command
        │     │   │   ├── query
        │     │   │   └── result
        │     │   ├── mapper
        │     │   ├── port
        │     │   │   ├── in
        │     │   │   └── out
        │     │   │       └── contract
        │     │   └── service
        │     ├── domain
        │     │   ├── account
        │     │   │   ├── model
        │     │   │   └── service
        │     │   ├── common
        │     │   └── transaction
        │     │       ├── model
        │     │       └── service
        │     └── infrastructure
        │          └── config
        └── resources
```