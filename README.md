# ticket-reservation
 콘서트 티켓 예약 시스템

### 개발 기술 스택
- **Language** : Java17
- **Framework** : Spring Boot 3.3.4
- **Database** : MySQL
- **ORM** : JPA + QueryDSL
- **API docs** : Swagger
- **TEST** : Junit5


### 시스템 요구사항
<details>
  <summary>API 요구 스펙</summary>

**1️⃣ `주요`유저 대기열 토큰 기능**

- 서비스를 이용할 토큰을 발급받는 API를 작성합니다.
- 토큰은 유저의 UUID 와 해당 유저의 대기열을 관리할 수 있는 정보 ( 대기 순서 or 잔여 시간 등 ) 를 포함합니다.
- 이후 모든 API 는 위 토큰을 이용해 대기열 검증을 통과해야 이용 가능합니다.

> 기본적으로 폴링으로 본인의 대기열을 확인한다고 가정하며, 다른 방안 또한 고려해보고 구현해 볼 수 있습니다.
>

**2️⃣ `기본` 예약 가능 날짜 / 좌석 API**

- 예약가능한 날짜와 해당 날짜의 좌석을 조회하는 API 를 각각 작성합니다.
- 예약 가능한 날짜 목록을 조회할 수 있습니다.
- 날짜 정보를 입력받아 예약가능한 좌석정보를 조회할 수 있습니다.

> 좌석 정보는 1 ~ 50 까지의 좌석번호로 관리됩니다.
>

3️⃣ **`주요` 좌석 예약 요청 API**

- 좌석 예약과 동시에 해당 좌석은 그 유저에게 약 5분간 임시 배정됩니다. ( 시간은 정책에 따라 자율적으로 정의합니다. )
- 날짜와 좌석 정보를 입력받아 좌석을 예약 처리하는 API 를 작성합니다.
- 만약 배정 시간 내에 결제가 완료되지 않는다면 좌석에 대한 임시 배정은 해제되어야 하며 임시배정 상태의 좌석에 대해 다른 사용자는 예약할 수 없어야 한다.

4️⃣ **`기본`**  **잔액 충전 / 조회 API**

- 결제에 사용될 금액을 API 를 통해 충전하는 API 를 작성합니다.
- 사용자 식별자 및 충전할 금액을 받아 잔액을 충전합니다.
- 사용자 식별자를 통해 해당 사용자의 잔액을 조회합니다.

5️⃣ **`주요` 결제 API**

- 결제 처리하고 결제 내역을 생성하는 API 를 작성합니다.
- 결제가 완료되면 해당 좌석의 소유권을 유저에게 배정하고 대기열 토큰을 만료시킵니다.
</details>

### [개발 마일스톤](https://github.com/users/maiorem/projects/2)

### API 
[API 명세서](https://docs.google.com/spreadsheets/d/1ClRDu49AR7M-U53IVL--cpwFlNVCGPH9BHyfGmiLfE0/edit?usp=sharing)



### 콘서트 티켓예매 시스템 플로우 차트
```mermaid
graph TD
    A[사용자 요청] --> B[대기열 등록]
    B --> D[유저 토큰 발급]
    
    D --> F[예약가능날짜 및 좌석 조회]
    F --> G[좌석 선택]
    
    G --> |좌석 예약 가능| H[임시 예약 5분 설정]
    H --> K[잔액 조회]
    
    K --> I[잔액 부족 시 충전 요청]
    I --> J[잔액 충전 완료]
    K --> L[결제 요청]
    J --> L
    
    L --> |결제 성공| M[좌석 예약 완료]
    L --> |결제 실패| N[결제 실패 알림]
    
    M --> E[대기열 토큰 만료]
    E --> O[예매 완료 내역 조회]
    
    G --> |이미 선택한 좌석| P[예매 불가 알림]
    P --> G
    
    N --> AD[결제 재시도]
    AD --> K

    H --> |5분 경과| Q[좌석 예약 이탈]
    Q --> Z[대기열 토큰 만료]
```


### 시퀀스 다이어그램 

<details>
  <summary>유저 토큰 발급</summary>

```mermaid
sequenceDiagram
    actor  User
    participant 유저토큰발급API
    participant Service
    participant DB

    %% 대기열 등록
    User->>유저토큰발급API: 유저 토큰 발급 요청
    유저토큰발급API->>Service: 사용자 정보 조회
    Service->>DB: 사용자 정보 조회
    DB-->>Service: 사용자 정보 반환
    
    Service->>DB: 토큰 및 대기열 순서 조회
    DB-->>Service: 토큰 및 대기열 순서 정보 반환
    Service-->>유저토큰발급API: 토큰 및 대기열 순서 정보 반환
    alt 기존토큰 존재하지 않으면
        유저토큰발급API->>Service:토큰 및 대기열 생성
        Service->>DB:토큰 및 대기열 생성
        DB-->>Service:토큰 및 대기열 순서 반환
        Service-->>유저토큰발급API:토큰 및 대기열 순서 반환
    end
    
    alt 대기가 종료됨
        유저토큰발급API->>Service: 유저 토큰 생성
        Service->>DB: 유저 토큰 생성
        DB-->>Service: 유저 토큰 발급 완료
        Service-->>유저토큰발급API: 유저 토큰 반환
        유저토큰발급API-->>User: 유저 토큰 반환
    end
```
</details>

<details>
  <summary>예약 가능 날짜 및 선택 가능 좌석 조회</summary>

```mermaid
sequenceDiagram
    actor User
    participant 예약가능날짜조회 API
    participant Service
    participant DB

    User->>예약가능날짜조회 API: 예약 가능 날짜 및 좌석 요청
    예약가능날짜조회 API->>Service: 콘서트 정보 조회
    Service->>DB: 콘서트 정보 조회 요청
    DB -->> Service:콘서트 정보 반환

    Service->>DB: 예약 가능 날짜 조회
    DB-->>Service: 예약 가능 날짜 반환
    Service->>DB: 날짜에 해당하는 예약가능좌석 요청
    DB-->>Service: 예약 가능 좌석 반환
    Service-->예약가능날짜조회 API:예약가능 날짜 및 좌석 정보 반환
    예약가능날짜조회 API-->>User: 예약 가능 날짜 및 좌석 정보 반환

```
</details>

<details>
  <summary>좌석 예약 요청</summary>

```mermaid
sequenceDiagram
    actor User
    participant 좌석예약API
    participant Service
    participant DB

    User->>좌석예약API: 선택한 날짜 및 좌석으로 예약 요청
    좌석예약API->>Service:예약 요청
    Service->>DB: 예약 시간 및 좌석 상태 업데이트
    DB-->>Service: 예약 시간 및 좌석 상태 반환
    Service-->>좌석예약API: 임시 예약 완료
    좌석예약API->>User: 임시 예약 완료 응답

```
</details>

<details>
  <summary>잔액 조회</summary>

```mermaid
sequenceDiagram
    actor User
    participant 잔액 조회 API
    participant Service
    participant DB

    %% 잔액 조회
    User->>잔액 조회 API: 결제 가능 금액 조회 요청
    잔액 조회 API->>Service: 사용자 잔액 조회
    Service->>DB: 잔액 정보 요청
    DB-->>Service: 잔액 정보 반환
    Service-->>잔액 조회 API: 잔액 정보 반환
    잔액 조회 API-->>User: 결제 가능 금액 반환

```
</details>

<details>
  <summary>잔액 충전</summary>

```mermaid
sequenceDiagram
    actor User
    participant 잔액충전API
    participant Service
    participant DB

    User->>잔액충전API: 잔액 충전 요청
    잔액충전API->>Service: 사용자 잔액 업데이트
    Service->>DB: 사용자 잔액 업데이트
    DB-->>Service: 업데이트 내역 반환
    Service-->>잔액충전API: 충전 내역 확인
    잔액충전API-->>User: 충전 내역 확인

```
</details>

<details>
  <summary>예매확정</summary>

```mermaid
sequenceDiagram
    actor User
    participant 예매확정API
    participant Service
    participant DB

    User->>예매확정API: 예매확정 처리 요청
    예매확정API->>Service: 예매 확정 정보 생성
    Service->>DB: 예매 확정 정보 생성
    DB-->>Service: 예매 정보 반환
    Service->>DB: 토큰 만료 업데이트 요청
    DB-->>Service: 토큰 상태 반환
    Service-->>예매확정API: 예매 정보 반환
    예매확정API-->>User: 예매 정보 반환

```
</details>



### ERD
<details>
  <summary>데이터베이스 설계</summary>

```mermaid
erDiagram
    User {
        INT user_id PK
        STRING uuid
        STRING username
        DECIMAL charge_amount
        DATETIME created_at
        DATETIME updated_at
    }

    UserToken {
        INT token_id PK
        INT user_id FK
        STRING uuid
        BOOLEAN is_active
        DATETIME token_expiry
        DATETIME created_at
        DATETIME updated_at
    }

    Concert {
        INT concert_id PK
        STRING concert_name
        STRING location
        INT total_seats
        INT run_time
        DATETIME created_at
        DATETIME updated_at
    }

    ConcertDate {
        INT concert_date_id PK
        INT concert_id FK
        DATETIME concert_date
        INT available_seats
    }

    Seat {
        INT seat_id PK 
        INT concert__id FK 
        INT concert_date_id FK
        STRING seat_number
        STRING status
        DECIMAL ticket_price  
        DATETIME reservated_date
        DATETIME created_at
        DATETIME updated_at
    }

    Queue {
        INT queue_id PK
        INT user_id FK
        INT sequence
        STRING status
        DATETIME created_at
        DATETIME deleted_at
    }
    
    Reservation {
        INT reservation_id PK
        INT user_id FK
        INT concert_id FK
        INT seat_id FK
        STRING reservation_status
        DATETIME reservation_date
        DATETIME created_at
        DATETIME updated_at
    }

    Payment {
        INT payment_id PK
        INT user_id FK
        INT concert_id FK
        DECIMAL amount
        DATETIME payment_date
        STRING payment_status
        DATETIME created_at
        DATETIME updated_at
    }


    User ||--o| UserToken : ""
    User ||--o{ Payment : ""
    User ||--o| Queue : ""
    User ||--o{ Reservation : ""
    Concert ||--o{ ConcertDate : ""
    Concert ||--o{ Seat : ""
    Concert ||--o{ Reservation : ""
    Seat ||--o{ Reservation : ""
    ConcertDate ||--o{ Seat : "" 
    Concert ||--o{ Payment : ""

``` 

#### User : 사용자 테이블
- uuid 사용자 식별키
- username 사용자 이름
- charge_amount 잔액

#### UserToken : 유저 토큰 테이블
- uuid 사용자 식별키
- is_active 토큰 활성화 여부
- token_expiry 토큰 만료 시간

#### Concert : 콘서트 테이블
- concert_name 공연명
- location 공연장소
- total_seat 전체 좌석 수
- run_time 런타임

#### ConcertDate : 콘서트날짜 테이블
- concert_date 날짜
- available_seats 남은 좌석 수

#### Seat : 좌석 테이블
- seat_number 좌석번호
- status 좌석 예약 상태
- ticket_price 티켓 가격
- reservated_date 예약일시

#### Queue : 대기열 테이블
- sequence 순서
- status 상태

#### Reservation : 예약 테이블
- reservation_status 예약 상태
- reservation_date 예약일시

#### Payment : 결제 테이블
- amount 결제금액
- payment_status 결제 상태
- payment_date 결제일시


</details>

### 패키지 구조 설계
```
└─src
    ├─main
    │  ├─java
    │  │  └─com
    │  │      └─hhplus
    │  │          └─io
    │  │              ├─common
    │  │              │  ├─config
    │  │              │  ├─exception
    │  │              │  └─response
    │  │              ├─concert
    │  │              │  ├─application
    │  │              │  ├─domain
    │  │              │  ├─persistence
    │  │              │  └─web
    │  │              │      └─response
    │  │              ├─reservation
    │  │              │  ├─application
    │  │              │  ├─domain
    │  │              │  ├─persistence
    │  │              │  └─web
    │  │              └─user
    │  │                  ├─application
    │  │                  ├─domain
    │  │                  ├─persistence
    │  │                  └─web
    │  │                      ├─request
    │  │                      └─response
    │  └─resources
    └─test
        └─java
            └─com
                └─hhplus
                    └─io

```

