# ticket-reservation
 콘서트 티켓 예약 시스템

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
  <summary>콘서트 예매 시스템 각 API 시퀀스 다이어그램</summary>

#### 유저 토큰 발급
```mermaid
sequenceDiagram
    participant User
    participant 유저토큰발급API
    participant UserService
    participant Queue Table
    participant User Table
    participant UserToken Table

    %% 대기열 등록
    User->>유저토큰발급API: 유저 토큰 발급 요청
    유저토큰발급API->>UserService: 사용자 정보 조회
    UserService->>User Table: 사용자 정보 요청
    User Table-->>UserService: 사용자 정보 반환
    UserService-->>유저토큰발급API: 사용자 정보 반환
    유저토큰발급API->>Queue Table: 대기열 등록
    Queue Table-->>유저토큰발급API: 대기열 등록 완료

    %% 대기열 순서 확인 및 유저 토큰 발급
    유저토큰발급API->>Queue Table: 대기열 순서 확인 요청
    Queue Table-->>유저토큰발급API: 대기열 순서 정보 반환
    유저토큰발급API -->> User: 대기 순서 반환
    
    alt 대기가 종료됨
        유저토큰발급API->>UserToken Table: 유저 토큰 생성
        UserToken Table-->>유저토큰발급API: 유저 토큰 발급 완료
        유저토큰발급API-->>User: 유저 토큰 반환
    end


```

#### 예약 가능 날짜 및 선택 가능 좌석 조회
```mermaid
sequenceDiagram
    participant 사용자
    participant 예약가능날짜조회 API
    participant Concert Table
    participant ConcertDate Table
    participant Seat Table


    사용자->>예약가능날짜조회 API: 예약 가능 날짜 및 좌석 요청
    예약가능날짜조회 API->>Concert Table: 콘서트 정보 조회
    Concert Table->>예약가능날짜조회 API: 콘서트 정보 반환
    예약가능날짜조회 API->>ConcertDate Table: 예약 가능 날짜 조회
    ConcertDate Table-->>예약가능날짜조회 API: 예약 가능 날짜 반환
    예약가능날짜조회 API->>Seat Table: 예약 가능 좌석 조회
    Seat Table->>예약가능날짜조회 API: 예약 가능 좌석 반환
    예약가능날짜조회 API-->>사용자: 예약 가능 날짜 및 좌석 수 반환

```

#### 좌석 예약 요청
```mermaid
sequenceDiagram
    participant User
    participant 좌석예약API
    participant Seat Table


    %% 좌석 선택 및 임시 예약
    User->>좌석예약API: 선택한 날짜 및 좌석으로 예약 요청
    좌석예약API->>Seat Table: 임시 예약 시간 및 상태 생성
    Seat Table-->>좌석예약API: 임시 예약 완료
    좌석예약API->>User: 임시예약완료 응답

```

#### 잔액 조회
```mermaid
sequenceDiagram
    participant User
    participant 잔액 조회 API
    participant UserService
    participant User Table

    %% 잔액 조회
    User->>잔액 조회 API: 결제 가능 금액 조회 요청
    잔액 조회 API->>UserService: 사용자 잔액 조회
    UserService->>User Table: 잔액 정보 요청
    User Table-->>UserService: 잔액 정보 반환
    UserService-->>잔액 조회 API: 잔액 정보 반환
    잔액 조회 API-->>User: 결제 가능 금액 반환

```
</details>