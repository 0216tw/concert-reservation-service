# 콘서트 예매 서비스 
- keyword : TDD , 단위/통합 테스트 , 대기열 , JWT , REST API , API명세 , swagger , logging , redis , 캐싱 

> 2024.07.24 5주차까지 소스 반영완료 

> 2024.07.28 6주차까지 소스 반영완료  

> 7주차 목표(now) : 자주 조회되는 DB 접근 캐싱 & redis로 대기열 리팩토링

<br> 

**상세 일정** <br>

7/28 (일) : 과제 분석 <br> 
7/29 (월) : DB I/O 줄이기 위한 캐싱 레이어 추가 및 테스트 (외부 redis 활용) <br> 
7/30 (화) : redis 연동 및 대기열 리팩토링 <br> 
7/31 (수) : 스케줄러 추가 구현 <br> 
 8/1 (목) : 부하 테스트 및 보고서 작성 <br> 
 8/2 (금) : 과제 제출 <br> 
 <br> 


## Step13. 캐싱 적용 및 DB I/O 을 줄여보기. (인덱스 적용 x) 

### caching 적용 대상

**후보 1 ) 콘서트 목록 조회**  <br> 
자주 조회되는가? O  <br> 
거의 변경되지 않는가? O  <br> 
DB와 캐싱 동기화 전략 : DB 정보 업데이트 시 캐시에 바로 반영하자 <br> 
TTL : 길게 주자 (24시간 줘도 되지 않을까??) <br> 
결정 : 캐싱하자 <br> 


**후보 2) 콘서트 예약 가능 일자 조회**  <br> 
자주 조회되는가? O  <br> 
거의 변경되지 않는가? △ ( 예매초반에는 자주 변할 수 있음 , 연산이 발생하기 때문, 이용가능 좌석 하나라도 있으면 날짜 표출 , 없으면 날짜 리스트에서 제거 ) <br> 
<img src = "https://github.com/user-attachments/assets/4c6f5a96-8f49-4744-ad67-d635ca045093" width="400px" height="300px"/> <br> 
DB와 캐싱 동기화 전략 : 고민입니다.. 초반 10분 정도는 캐시 TTL을 1초로 잡고 이후에는 Write-Through 전략으로 TTL을 길게 하는게 좋을지.. <br> 
TTL : 고민중.. <br> 
결정 : 고민중..<br> 

**후보 3) 콘서트 예약 가능 좌석 조회** <br> 
위와 동일한 이유로 고민중입니다. <br> 

**후보 4) 잔액 조회** <br> 
자주 조회되는가? X <br> 
결정 : 캐싱 사용하지 않음 <br> 

**후보 5) 토큰 대기열 조회** 
자주 조회되는가? O <br> 
하지만 이 경우 캐싱이 아니라 redis 대기열 구현파트로 진행할 예정이므로 따로 캐싱개념을 적용하지 않으려고 합니다. <br> 
결정 : 캐싱이 아니라 대기열 구현으로 진행  <br> 


## Step14. 대기열을 Redis 로 리팩토링 <br> 

대략적인 예상안 <br><br> 
1. 토큰 발급 및 상태 검증을 redis 와 연동한다 (기존 DB와의 연동 제거) <br> 
2. DB에 저장했었던 큐 정보 등은 제거하고, 사용자 등록만 한다 (userId , balance) <br> 
3. 실제 적재 및 검증 , TTL 을 검증한다. <br> 
4. 부하 테스트를 진행해본다.  <br>
5. 비즈니스 로직에서 큐 , 대기열 관련 로직을 분리해 제거한다. <br> 

<br><br> 



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






















