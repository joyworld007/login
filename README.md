# 카카오 페이 서버 개발 과제
## Rest API 기반 쿠폰시스템

## Specifications
````
 OpenJDK11
 Spring Boot 2.3.0.RELEASE
 Spring Data Jpa
 Spring Data Jdbc(Batch Update)
 Swagger2
 Domain Driven Design
 CQRS Pettern
````

## Reading (strategy)
````
BASE 
- DDD를 적용하기 위해 DB Entity는 한개지만 쿠폰과, 쿠폰발급 도메인으로 비지니스 로직을 분리 [완료]
- 쿠폰 생성 시 대용량 Insert 를 위해 jpa와 별개로 jdbc batch update를 적용 [완료]
- RESTFUL 지향 API 구현 [완료]

기본 API 구현하기
- 랜덤한 코드의 쿠폰을 N개 생성하여 데이터베이스에 보관 [완료]
- 생성된 쿠폰중 하나를 사용자에게 지급 [완료]
- 사용자에게 지급된 쿠폰을 조회 [완료]
- 지급된 쿠폰중 하나를 사용 (쿠폰 재사용은 불가) [완료]
- 지급된 쿠폰중 하나를 사용 취소 (취소된 쿠폰 재사용 가능) [완료]
- 발급된 쿠폰중 당일 만료된 전체 쿠폰 목록을 조회

TODO : EMBED REDIS를 이용하여 CQRS(Command and Query Responsibility Segregation) 패턴 구현하기
- EMBED REDIS 연동
- 만료 일자를 기준으로 분류하여 발급된    쿠폰 저장
- 당일 만료된 전체 쿠폰 목록을 REDIS에서 조회 
- 만료 3일전 사용자에게 메시지 전송

TODO : JWT 웹 토큰을 통한 인증하기
- JWT 웹 토큰을 이용한 회원가입, 로그인, API 인증 구현

TODO : 성능 테스트 결과서 만들기
- nGrinder 성능 테스트 결과

TODO : 10만개 이상 벌크 Insert 구현하기
- csv 파일 읽고 jdbc batch insert 구현 

TODO : 테스트 코드 작성
````

## Domain 설계
```
쿠폰(Coupon) 
   쿠폰번호
   쿠폰상태
   만료일
   생성일

쿠폰발급(CouponIssue)
   쿠폰번호
   유저아이디
   사용일
   발급일

유저(User)
   유저아이디
   패스워드
   토큰 
```

## Entity 설계
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

## Redis Key
````
- 쿠폰 ( key : coupon:{couponNumber}, type : Coupon )
- 만료일 기준 쿠폰 발급 list ( key : coupon:expiration:{yyyymmdd}, type : list[Coupon] )
- API 인증을 위한 user 정보 ( key : user:{id}, type : User )  
````

## Explanation of REST
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


## Api Feature list
```
- 랜덤한 코드의 쿠폰을 N개 생성하여 데이터베이스에 보관
- 생성된 쿠폰중 하나를 사용자에게 지급
- 사용자에게 지급된 쿠폰을 조회
- 지급된 쿠폰중 하나를 사용  (쿠폰 재사용은 불가) 
- 지급된 쿠폰중 하나를 사용 취소 (취소된 쿠폰 재사용 가능)
- 발급된 쿠폰중 당일 만료된 전체 쿠폰 목록을 조회
``` 

## Api Endpoint
```
EndPoint : /coupons
Method : POST 
Description : 랜덤한 코드의 쿠폰을 N개 생성하여 데이터베이스에 보관(쿠폰 코드 1씩 자동증가 : 초기값 1) 
              만료일을 테스트 하기 위해 10000단위 레코드 별로 만료일 +1 day 증가
Return value: HTTP status 201 (Created) 
Payload Example (required parameters)
{
    "size": "20000"
}

EndPoint : /coupons/{id}
Method : PUT 
Description : 1. 생성된 쿠폰중 하나를 사용자에게 지급 
              2. 사용자에게 지급한 쿠폰을 사용
              3. 사용된 쿠폰을 사용 취소
Return value: HTTP status 200 (OK), 404 (NOT_FOUND)
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

쿠폰을 사용자 에게 지급할 때 
Payload Example (required parameters)
{
    "status" : "ISSUED",
    "userId" : "joyworld007"
}

쿠폰을 사용으로 처리 할때 
Payload Example (required parameters)
{
    "status" : "USED"
}

쿠폰을 사용 취소 할때 
Payload Example (required parameters)
{
    "status" : "ISSUED"
}

EndPoint : /coupons
Method : GET
Description : 사용자에게 지급된 쿠폰을 조회
Return value: HTTP status 200 (OK) 

|-----------|--------------|---------------------------------------------------|---------------|
| Parameter |Parameter Type| Description                                       | Default value |
|-----------|--------------|---------------------------------------------------|---------------|
| userId    | @QueryParam  | UserId (required = true)                          |               |
|-----------|--------------|---------------------------------------------------|---------------|
| pageable  | @Pageable    | Pageable Object(size, page)                       |               |
|-----------|--------------|---------------------------------------------------|---------------|

EndPoint : /coupons/expired-coupons
Method : GET
Description : 만료일자가 오늘인 쿠폰 전체 조회
Return value: HTTP status 200 (OK) 

|-----------|--------------|---------------------------------------------------|---------------|
| Parameter |Parameter Type| Description                                       | Default value |
|-----------|--------------|---------------------------------------------------|---------------|
| pageable  | @Pageable    | Pageable Object(size, page)                       |               |
|-----------|--------------|---------------------------------------------------|---------------|
