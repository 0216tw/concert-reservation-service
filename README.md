# 콘서트 예매 서비스 
- keyword : TDD , 단위/통합 테스트 , 대기열 , JWT , REST API , API명세 , swagger , logging , redis , 캐싱 

<br> 

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

   
   


> 2024.07.24 5주차까지 소스 반영완료 

> 2024.07.28 6주차까지 소스 반영완료  

> 7주차 목표(now) : 자주 조회되는 DB 접근 캐싱 & redis로 대기열 리팩토링


<details> 
<summary> 6주차까지 내용 </summary>
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

</details>






















