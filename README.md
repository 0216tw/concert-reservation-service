# 콘서트 예매 서비스 
- keyword : TDD , 단위/통합 테스트 , 대기열 , JWT , REST API , API명세 , swagger , logging , redis , 캐싱 , 인덱스 , 이벤트 

<br> 

# chap 15. 쿼리 부하 테스트 및 index로 성능 개선 보고서 <br> 

콘서트 예매 서비스는 총 6개의 테이블을 가지고 있습니다. <br>
queue 테이블은 Redis 기능으로 전환했기에 테이블을 삭제했습니다. <br> 
성능 개선을 위해 concert_seat 테이블에 state 컬럼을 제거하여 빈번한 update 이슈를 제거합니다. <br> 


<img src="https://github.com/user-attachments/assets/2b28fc18-42d6-4479-a8ab-67161ff7062e" width="1000px" height="500px"/> 

### 테이블 CRUD 빈도 <br> 

테이블 | INSERT  | DELETE  | UPDATE | SELECT | 
| ------ | ------ | -----------| -------|  ---- | 
| CONCERT |  적음 | 적음| 적음 | **많음** | 
| CONCERT_SCHEDULE |  적음 |적음| 적음 | **많음** | 
| CONCERT_SEAT |  보통 | 적음 | 적음 | **많음** | 
| RESERVATION |  보통 | 적음 | 적음 | **매우 많음** | 
| PAYMENT |  보통 | 적음 | 적음 | **많음** | 
| USER |  **많음** | 적음 | **많음** | **많음** | 

<details> 
  
<summary> CRUD 빈도 근거 </summary>

  - CONCERT
     - INSERT : 관리자가 콘서트 정보 발생시에만 등록한다고 판단 
     - DELETE : 정보 기록을 위해 자주 삭제하지 않는다고 판단
     - UPDATE : 관리자가 수정 필요시에만 변경한다고 판단
     - **SELECT : 모든 사용자들이 최소 1번 이상 조회한다고 판단**
       
   - CONCERT_SCHEDULE
     - INSERT : 관리자가 콘서트 정보 발생시에만 등록한다고 판단 
     - DELETE : 정보 기록을 위해 자주 삭제하지 않는다고 판단
     - UPDATE : 관리자가 수정 필요시에만 변경한다고 판단 
     - **SELECT : 모든 사용자들이 최소 1번 이상 조회한다고 판단**
       
   - CONCERT_SEAT
     - INSERT : 관리자가 콘서트 정보 발생시에만 등록함. 단, 좌석수만큼 등록하므로 INSERT 가 CONCERT , CONCERT_SCHEDULE보다는 높음
     - DELETE : 정보 기록을 위해 자주 삭제하지 않는다고 판단
     - UPDATE : 관리자가 수정 필요시에만 변경한다고 판단 
     - **SELECT : 모든 사용자들이 최소 1번 이상 조회한다고 판단**
       
   - RESERVATION
     - INSERT : 사용자가 좌석 예약에 성공하면 발생합니다. SELECT로 사전 체크를 해서 존재하지 않으면 INSERT 합니다.
     - DELETE : 정보 기록을 위해 자주 삭제하지 않는다고 판단
     - UPDATE : 변경될 경우 없다고 판단  
     - **SELECT : 기 예약 여부 확인을 위한 조회 + 결제 이전에 예약 유효성 검증을 위한 조회**
       
   - PAYMENT
     - INSERT : 관리자가 콘서트 정보 발생시에만 등록한다고 판단 
     - DELETE : 정보 기록을 위해 자주 삭제하지 않는다고 판단
     - UPDATE : 변경될 경우가 없다고 판단 
     - **SELECT : 결제 전 해당 예약번호가 이미 결제되었는지 조회**
       
   - USER
     - **INSERT : 대기열 토큰이 ACTIVATE 되는 시점에 사용자 정보 등록**
     - DELETE : 정보 기록을 위해 자주 삭제하지 않는다고 판단
     - **UPDATE : 금액 충전 및 사용이 자주 발생된다고 판단** 
     - **SELECT : 사용자 존재 여부 및 금액 조회를 위해 자주 조회된다고 판단**  

</details> 
<br>
확인 결과, <br> 
모든 테이블에 대해 전반적으로 조회가 자주 발생했으며 <br> 
특히 RESERVATION의 경우 예약 정보 INSERT 전에 검증을 위한 SELECT , PAYMENT 결제 전에 검증을 위해 최소 2회 이상 조회가 발생한다고 판단했습니다. <br> 

<br><br> 

### 테스트용 데이터 개수 정의

| 테이블 | ROW 수 | 비고 
| ------ | ------ | ----| 
| CONCERT |  50 |  | 
| CONCERT_SCHEDULE | 1,500 | 모든 콘서트는 30일씩 진행됨 (50 * 30) | 
| CONCERT_SEAT |  15,000,000 | 모든 콘서트는 좌석이 1만건씩 존재. ( 50 * 30 * 10000 ) |
| RESERVATION |  19,980,000 | 대부분의 콘서트 좌석이 예약된 경우 ( 특정 콘서트가 딱 이틀간 모든 좌석 예약 가능)  + 예약기간 만료 5000000건 , 20000건은 테스트용 |
| PAYMENT |  보통 | 14,980,000 | 대부분의 콘서트 좌석 결제 성공 (나머지 20000건은 테스트용) |
| USER |  **많음** | active횟수만큼 | 토큰 ACTIVE 전환된 시점 횟수만큼 USER 등록 | 

<br> 

### 사용 DBMS : JPA + Oracle 



<details> 
  
<summary> 테스트 SQL </summary>

</details> 


<br>
<br> 


### 내가 생각했던 DB key관리 정책



# chap 16. 기존 트랜잭션 범위 분석 및 서비스 분리를 통한 결과 개선 보고서 <br> 







> 2024.07.24 5주차까지 소스 반영완료 

> 2024.07.28 6주차까지 소스 반영완료  

> 7주차 목표 : 자주 조회되는 DB 접근 캐싱 & redis로 대기열 리팩토링

> 8주차 목표(now) : index 활용 및 대용량 쿼리 개선안 정리 & 개발 기능 분리 (이벤트 사용) & 로직추가 : 전달 및 이력 데이터 저장



<details> 
<summary> 7 주차까지 내용 </summary>
<br>

## 간트 차트(6/29 ~ 7/19) 

<details>
  
<summary>전체 일정</summary>

![image](https://github.com/0216tw/concert-reservation-system/assets/140934688/5d0cc672-54ca-4181-8437-aafea5c990a6)

</details>

### 4주차 일정 (7/7 ~ 7/12)  TODO 
![image](https://github.com/0216tw/concert-reservation-system/assets/140934688/56a9d2ec-9bd6-4593-8d5e-2562c4762614)


<br>

## 3. 요구사항 분석 

## 3-1) 플로우 차트 (전체 흐름) 

![image](https://github.com/0216tw/concert-reservation-system/assets/140934688/199162fe-a6bd-4fe8-9d63-643c33e2dfab)




<br> 

## 3-2) API 스펙 명세 (postman 활용)  TODO 

![image](https://github.com/0216tw/concert-reservation-system/assets/140934688/c801eca0-422b-404a-b9f0-073d58e3128c)

**<a href="https://documenter.getpostman.com/view/36732325/2sA3dyhB7q"> 👉 API 명세 보기 </a>**

<br>

## swagger  TODO 

![image](https://github.com/user-attachments/assets/7a81d77e-7e9d-49b7-ac55-3f4a5dcac123)

<br> 

## 3-3) 시퀀스 다이어그램 (유스케이스별)

### 사용자 토큰 발급 요청 

![토큰발급요청3](https://github.com/user-attachments/assets/48bbcff7-8931-436f-bfe6-ba197ce8dd91)

<br>

### 콘서트 예약 가능 날짜 조회 

![콘서트예약가능날짜조회](https://github.com/0216tw/concert-reservation-system/assets/140934688/d4e4f55f-f633-4dca-996e-6835fab8f852)

<br>

### 콘서트 예약 가능 좌석 조회 

![콘서트예약가능좌석조회](https://github.com/0216tw/concert-reservation-system/assets/140934688/7521551b-9d1e-4a93-8735-2855b6483ea0)

<br>

### 콘서트 좌석 예약 요청 

![좌석예약](https://github.com/0216tw/concert-reservation-system/assets/140934688/d279e6f5-260b-417b-9774-0275ef47f81f)

<br>

### 잔액 충전

![잔액 충전](https://github.com/0216tw/concert-reservation-system/assets/140934688/9544b719-47d4-46d1-a434-89ce6d68bd46)

<br>

#### 잔액 조회 

![잔액 조회](https://github.com/0216tw/concert-reservation-system/assets/140934688/b3c97833-b803-4cdd-955d-6de5b99d2527)

<br>

#### 결제 요청

![결제요청](https://github.com/0216tw/concert-reservation-system/assets/140934688/31b395d3-2b66-457a-b9af-e750c640fc3d)

## 3-4) ERD 

![ERD_20240710_3](https://github.com/user-attachments/assets/5e56b960-dd38-4b19-ac3e-10b99dd4fe7e)




## Step13. 캐싱 적용 결과서

### 조회성 쿼리 캐싱 적용 판단 기준 & 결과

| 조회 쿼리    | 조회가 자주 발생하는가?                           | 내용이 거의 변경되지 않는가? |  캐시 여부   |  사유 | 
| ------------ | ----------------------------------------------- | ---------------------------| -------- |  ------ | 
| `콘서트 목록 조회`             | O    | O              |      캐싱                           | 두 조건 모두 만족 | 
| `콘서트 예약 가능 일자 조회`   | O    | X               |     캐싱 안함                     | 현재 설계상 연산 이슈가 있음 | 
| `콘서트 예약 가능 좌석 조회`   | O    | X               |     캐싱 안함                            | 내용이 자주 변경되므로 캐싱의 의미가 없음 | 
| `사용자 잔액 조회`             |  X  | O |   캐싱 안함  |     조회가 자주 발생하지 않음             | 


### 콘서트 목록 조회 (캐싱 적용) <br>

**캐시 적용결과** <br> 

![image](https://github.com/user-attachments/assets/74a06990-8a1e-453e-9a59-f5ba04c4e8ca)

![image](https://github.com/user-attachments/assets/d1185e02-887e-4c4f-a263-a7c0b120b98e)

**추후 시도해 볼 방향** :  <br>
현재는 TTL을 10으로 적용, 10분 마다 조회가 된다는 전제로 캐시가 동기화됩니다. <br>
DB 변경시 바로 캐시 변경이 되도록 이벤트 방식을 고려하고자 합니다. <br> 

<br>

### 콘서트 예약 가능 일자 조회 설계 이슈  <br> 

<img src = "https://github.com/user-attachments/assets/4c6f5a96-8f49-4744-ad67-d635ca045093" width="400px" height="300px"/> <br> 

콘서트 예약이 가능한 일자를 조회하기 위해 내부적으로 "좌석의 상태를 조회하는" 연산 쿼리가 발생하고 있습니다.  <br> 
연산 시점의 일자 정보 유추가 어려우므로 캐싱 적용이 어려울 것으로 보입니다.  <br> 

**추후 시도해 볼 방향 :**  <br>
코치님의 조언에 따라 좌석 테이블에서 상태라는 조건을 제거할 예정입니다. <br> 
그렇게 되면 좌석 정보 자체도 캐싱 처리가 가능할 것으로 보입니다. <br> 

**<질문사항>**  <br> 
${\textsf{\color{magenta}콘서트 예약 일자도 캐싱으로 고정해놓고, 특정 일자 선택시 좌석 없으면 좌석없음" 이라고 하면 어떨까요?}}$ <br> 
${\textsf{\color{magenta}이 경우에는 예약 일자도 캐싱이 가능할 것 같다고 생각합니다.}}$ <br> 

<br><br>

## Step14. 대기열을 Redis 로 리팩토링 <br> 

기존 DB 대기열을 Redis로 이관하였습니다. <br><br>
![image](https://github.com/user-attachments/assets/d6cd8873-4d95-4ecb-97f3-f80f1af0cf6e)

### 리팩토링 설계 결과 <br> 

1. 50명 pool에 대해 제한을 없앴습니다.  <br>
2. DB의 큐 테이블을 삭제하고 redis 상에서 대기열 정보를 관리합니다. <br> 
3. 토큰 검증 관련 로직을 단순화 했습니다. <br> 
   -기존 : 토큰 유무 조회 -> 토큰 DB 조회 -> 토큰 상태값 조회 -> 상태에 따른 처리 (active 이면 진입 , wait이면 대기순서 반환 , expired면 만료 알림)  <br> 
   -변경 : 토큰 유무 조회 -> 토큰 Active 조회 or 토큰 Waiting 조회 -> 없으면 만료 처리 <br>
<br><br> 

### 고민했던 내용 <br> 
**token이 activeQueue로 전환될 때, TTL을 어떻게 줄 것인가?** <br> <br>
(1) 시도-1 ) redis레벨에서 TTL을 준다. (X) 전체가 아닌 각각의 토큰에 대해 10분 대기가 필요함 <br>  <br>
(2) 시도-2 ) 토큰을 생성하는 시점에 이미 날짜 정보를 붙여줌 (토큰:날짜)   (X) active token이 되는 순간부터 10분을 고려해야 하므로 실패<br> <br>
(3) 시도-3 ) 토큰이 active로 전환되는 시점에 (토큰:날짜) 처리를 함 ex) tokentoken:20240801121212 <br>  <br>
   => 하지만 사용자는 (토큰)만 가지고 있음 ex) tokentoken <br>  <br>
   => String의 contains 메서드와 시간비교 기능을 활용해 해결 (O)  <br>  <br>
![image](https://github.com/user-attachments/assets/703d86f4-3df3-45c0-8298-6a9ea9579310)

   


</details>






















