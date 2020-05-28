# 카카오 페이 서버 개발 과제
## Rest API 기반 쿠폰시스템

## 목차
* [Specifications](#chapter-1)
* [Strategy](#chapter-2)
* [Domain](#chapter-3)
* [Entity](#chapter-4)
* [Redis Key](#chapter-5)
* [Explanation of REST](#chapter-6)
* [Api Feature list](#chapter-7)
* [Api Endpoint](#chapter-8)
* [Performance Test](#chapter-9)
* [How to run](#chapter-10)


### <a name="chapter-1"></a>Specifications 
````
 OpenJDK11
 Spring Boot 2.3.0.RELEASE
 Spring Data Jpa
 Spring Data Jdbc(Batch Update)
 Spring Data Redis
 Swagger2
 Domain Driven Design
 CQRS Pettern
````

### <a name="chapter-2"></a>Strategy 
````
---------------------------------------------- 기본 과제 ---------------------------------------------
기본 전략 
- DDD를 적용하기 위해 DB Entity는 한개지만 쿠폰과, 쿠폰발급 도메인으로 비지니스 로직을 분리 
- 쿠폰 생성 시 대용량 Insert를 위해 jdbc batch update를 구현 
- RESTFUL API 구현  

TODO : 기본 API 구현 [완료] 
- 쿠폰을 하나 생성
- 랜덤한 코드의 쿠폰을 N개 생성하여 데이터베이스에 보관
- 생성된 쿠폰중 하나를 사용자에게 지급
- 사용자에게 지급된 쿠폰을 조회
- 지급된 쿠폰중 하나를 사용 (쿠폰 재사용은 불가)
- 지급된 쿠폰중 하나를 사용 취소 (취소된 쿠폰 재사용 가능)
- 발급된 쿠폰중 당일 만료된 전체 쿠폰 목록을 조회
- 만료 N일전 쿠폰을 조회하여 알림 

단위 테스트 코드 작성 [완료]
- MockBean을 이용한 CouponService 비지니스 로직 테스트
- JacocotestReport 코드 커버리지 체크

---------------------------------------------- 기본 과제 ---------------------------------------------

---------------------------------------------- 옵션 과제 ---------------------------------------------

TODO : JWT 웹 토큰을 통한 인증하기 [완료]
- JWT 웹 토큰을 이용한 회원가입, 로그인, API 인증 구현
- 회원가입 구현 ( 패스워드 SHA-512 단방향 암호화 하여 저장 )
- 로그인 구현 ( 로그인 후 토큰값 리턴 ) 
- API 호출 시 token 값 유효성 여부 검증 하는 Interceptor 구현 

TODO : 트래픽(성능)을 고려한 설계 (쿠폰 데이터 100억개 이상, API TPS 10K이상 ) [완료] 
전략 : EMBED REDIS를 이용하여 CQRS(Command and Query Responsibility Segregation) 패턴 구현하기
- Coupon 정보를 id를 키로 embed redis에 저장 ( 쿠폰 조회시 사용 ) 

TODO : 만료 N일전 쿠폰을 조회하여 알림 [완료]
전략 : 만료일을 키로 Redis에 저장 조회된 쿠폰 정보를 기준 System.out 출력

TODO : 10만개 이상 벌크 Insert 구현하기 [완료]
전략 : csv 파일을 읽고(load file) DB 저장(jdbc batch update) 구현

TODO : 성능 테스트 결과서 만들기 [완료]
전략 : nGrinder를 이용해 성능 테스트 
 
---------------------------------------------- 옵션 과제 ---------------------------------------------
````

### <a name="chapter-3"></a>Domain 
```
쿠폰(Coupon) 
   쿠폰번호
   쿠폰상태
   만료일
   생성일

쿠폰발급(CouponIssue)
   유저아이디
   사용일
   발급일

유저(User)
   유저아이디
   패스워드
   토큰 
```

## <a name="chapter-4"></a>Entity
```
쿠폰(Coupon) 
   쿠폰번호
   쿠폰상태
   유저아이디
   만료일
   생성일   
   사용일
   발급일

유저(User)
   유저아이디
   패스워드
   토큰 
```

### <a name="chapter-5"></a>Redis Key
````
- 쿠폰( key : 쿠폰 ID, type : 쿠폰 Object)  
- 해당 일자에 만료되는 쿠폰(key : 만료일자, type : 쿠폰 ID 리스트)
````

### <a name="chapter-6"></a>Explanation of REST 
```
데이터에 액세스하는 표준 방법을 제공하기 위해 API는 REST를 사용합니다.
|-------------|-----------------------------------|
| HTTP Method |  Usage                            |
|-------------|-----------------------------------|
| GET         | Receive a read-only data          |
| PUT         | Overwrite an existing resource    |
| POST        | Creates a new resource            |
| DELETE      | Deletes the given resource        |
|-------------|-----------------------------------|
```


### <a name="chapter-7"></a>Api Feature list 
```
--회원가입
--로그인
- 쿠폰을 하나 생성
- 랜덤한 코드의 쿠폰을 N개 생성하여 데이터베이스에 보관
- CSV 파일을 읽고 10만개 쿠폰 정보 Insert
- 생성된 쿠폰중 하나를 사용자에게 지급
- 사용자에게 지급된 쿠폰을 조회
- 지급된 쿠폰중 하나를 사용  (쿠폰 재사용은 불가) 
- 지급된 쿠폰중 하나를 사용 취소 (취소된 쿠폰 재사용 가능)
- 발급된 쿠폰중 당일 만료된 전체 쿠폰 목록을 조회
- 만료 N일전 쿠폰을 조회하여 알림
- 쿠폰 코드를 이용해 쿠폰 정보를 조회
``` 

### <a name="chapter-8"></a>Api Endpoint
```
API 실행 절차
1. 회원가입을 합니다 
2. 로그인 후 인증 토큰을 받습니다
3. 헤더에 userId, token 값을 넣고 각 Coupon API를 호출합니다

EndPoint : /v1/users/signUp
Method : POST 
Description : 회원가입
Return value: HTTP status 201 (Created) 
Payload Example (required parameters)
{
	"userId" : "joyworld007", 
	"password" : "12345"
}

----------------------------------------------------------------------------------------------------

EndPoint : /v1/users/signIn
Method : GET
Description : 로그인
Return value: HTTP status 200 (OK) 

|-----------|--------------|---------------------------------------------------|
| Parameter |Parameter Type| Description                                       |
|-----------|--------------|---------------------------------------------------|
| userId    | @QueryParam  | user id                                           |
|-----------|--------------|---------------------------------------------------|
| password  | @QueryParam  | user passowrd                                     |
|-----------|--------------|---------------------------------------------------|

----------------------------------------------------------------------------------------------------

EndPoint : /v1/coupons
Method : POST 
Description : 쿠폰을 하나 생성
Return value: HTTP status 201 (Created) 
Payload Example (required parameters)
{
	"expireDate" : "2020-05-26T00:00:00"
}

----------------------------------------------------------------------------------------------------

EndPoint : /v1/coupons/generate
Method : POST 
Description : 랜덤한 코드의 쿠폰을 N개 생성하여 데이터베이스에 보관
              (쿠폰 코드 1씩 자동증가 : 초기값 1) 
              만료일을 테스트 하기 위해 10000단위 레코드 별로 만료일 +1 day 증가
Return value: HTTP status 201 (Created) 
Payload Example (required parameters)
{
    "size": "20000"
}

----------------------------------------------------------------------------------------------------

EndPoint : /v1/coupons/generate/csv
Method : POST 
Description : CSV 파일을 읽고 10만개 쿠폰 정보 Insert
              (쿠폰 코드 1씩 자동증가 : 초기값 1) 
Return value: HTTP status 201 (Created) 

----------------------------------------------------------------------------------------------------

EndPoint : /v1/coupons/{id}
Method : PUT 
Description : 생성된 쿠폰중 하나를 사용자에게 지급 
              사용자에게 지급한 쿠폰을 사용
              사용된 쿠폰을 사용 취소
Return value: HTTP status 200 (OK), 404 (NOT_FOUND)

|-----------|--------------|---------------------------------------------------|
| Parameter |Parameter Type| Description                                       |
|-----------|--------------|---------------------------------------------------|
| id        | @PathParam   | Coupon id                                         |
|-----------|--------------|---------------------------------------------------|

Payload Example (required parameters)
생성된 쿠폰중 하나를 사용자에게 지급 
{
    "status" : "ISSUED",
    "userId" : "joyworld007"
}
사용자에게 지급한 쿠폰을 사용
{
    "status" : "USED"
}
사용된 쿠폰을 사용 취소
{
    "status" : "ISSUED"
}

Response Body example
성공시 
{
    "code": "SUCCESS",
    "message": "OK"
}            
쿠폰 만료 시  
{
    "code": "COUPON_EXPIRED",
    "message": "Coupon is Expired"
}
요청 조건이 맞지 않을시   
{
    "code": "BAD_REQUEST",
    "message": "Bad Request"
}

----------------------------------------------------------------------------------------------------

EndPoint : /v1/coupons
Method : GET
Description : 사용자에게 지급된 쿠폰을 조회
Return value: HTTP status 200 (OK) 

|-----------|--------------|---------------------------------------------------|
| Parameter |Parameter Type| Description                                       |
|-----------|--------------|---------------------------------------------------|
| userId    | @QueryParam  | UserId (required = true)                          |
|-----------|--------------|---------------------------------------------------|
| pageable  | @Pageable    | Pageable Object(size, page)                       |
|-----------|--------------|---------------------------------------------------|

----------------------------------------------------------------------------------------------------

EndPoint : /v1/coupons/today-expired-coupons
Method : GET
Description : 발급된 쿠폰중 당일 만료된 전체 쿠폰 목록을 조회
Return value: HTTP status 200 (OK) 

|-----------|--------------|---------------------------------------------------|
| Parameter |Parameter Type| Description                                       |
|-----------|--------------|---------------------------------------------------|
| pageable  | @Pageable    | Pageable Object(size, page)                       |
|-----------|--------------|---------------------------------------------------|

----------------------------------------------------------------------------------------------------

EndPoint : /v1/coupons/notify-expire-coupons
Method : GET
Description : 만료 day일전 쿠폰을 조회하여 logger info 알림
Return value: HTTP status 200 (OK) 

|-----------|--------------|---------------------------------------------------|
| Parameter |Parameter Type| Description                                       |
|-----------|--------------|---------------------------------------------------|
| day       | @QueryParam  | search expired plus day                           |
|-----------|--------------|---------------------------------------------------|

----------------------------------------------------------------------------------------------------

EndPoint : /v1/coupons/{id}
Method : GET
Description : 쿠폰 정보 조회 ( CQRS 성능 테스트 용 )
Return value: HTTP status 200 (OK) 

|-----------|--------------|---------------------------------------------------|
| Parameter |Parameter Type| Description                                       |
|-----------|--------------|---------------------------------------------------|
| id        | @PathParam   | Coupon id                                         |
|-----------|--------------|---------------------------------------------------|

```

### <a name="chapter-9"></a>Performance Test
Embed DB내 쿠폰 100000개 생성 테스트 결과 
<img src="src/docs/1.png" witdh="100%" height="100%">

조회 성능 테스트 결과 
Total 가상유저 : 99, 테스트 횟수 : 10, 스레드 : 3 
<img src="src/docs/2.png" witdh="100%" height="100%">

### <a name="chapter-10"></a>How to Run
```
1. 실행
./gradlew bootrun

2. Test 
./gradlew test
./gradlew jacocoTestReport

3. Swagger URI
http://localhost:8080/swagger-ui.html
```
 
 
