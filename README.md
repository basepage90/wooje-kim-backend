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
- 계좌(Account)
  - 계좌 등록
  - 계좌 삭제
- 거래(Transaction)
  - 입금
  - 출금
  - 이체
  - 거래 내역 조회
