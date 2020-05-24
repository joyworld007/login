# 카카오 페이 서버 개발 과제
## Rest API 기반 쿠폰시스템

## Specifications
````
 OpenJDK11
 Spring Boot 2.3.0.RELEASE
 Spring Data Jpa
 Spring Data Jdbc
 Swagger2
 Domain Driven Design
 CQRS 
````

## Reading
* CQRS(Command and Query Responsibility Segregation) Pattern을 적용하여 Write, 
Update 모듈은 Embed H2 DB로, Read 모듈은 Embed Redis를 통해 엑세스 하도록 구현하였습니다.
* DDD를 적용하여 DB Entity는 한개지만 쿠폰과, 쿠폰발급 도메인으로 비지니스 로직을 분리 하였습니다.
* 쿠폰 생성 시 대용량 Insert 를 위해 jpa와 별개로 jdbc batch update를 적용 하였습니다.
* 특정일에 만료 되는 쿠폰을 빠르게 조회하기 위해 Redis에 만료일을 key로 데이터를 분류하여 저장 하였습니다.
* RESTFUL한 API를 구현하기 위해 Resource, Method, Status Code를 적절히 활용하였습니다.
* jwt 토큰을 이용해 회원 가입, 로그인, token을 통한 API 인증을 구현하였습니다. 

## Domain 설계
```
쿠폰(Coupon) 
   쿠폰번호
   발급여부
   만료일
   생성일

쿠폰발급(CouponIssue)
   쿠폰발급
   쿠폰번호
   사용자ID
   사용여부
   사용일
   발급일

사용자(User)
   id ( 사용자 아이디 )
   pass ( 사용자 패스워드 )
   token ( 발급받은 토큰 )
```

## Entity 설계
```
쿠폰(Coupon) 
   쿠폰번호
   쿠폰상태
   사용자ID
   만료일
   생성일   
   사용일
   발급일

사용자(User)
   id ( 사용자 아이디 )
   pass ( 사용자 패스워드 )
   token ( 발급받은 토큰 )
```

## Redis Key
````
- 쿠폰 ( key : coupon:{couponNumber}, type : Coupon )
- 만료일 기준 쿠폰 발급 list ( key : coupon:expiration:{yyyymmdd}, type : list[Coupon] )
- json token 저장 ( key : user:{id}, type : User )  
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
- 지급된 쿠폰중 하나를 사용  (쿠폰 재사용은 불가) - 지급된 쿠폰중 하나를 사용 취소 (취소된 쿠폰 재사용 가능)
- 발급된 쿠폰중 당일 만료된 전체 쿠폰 목록을 조회
``` 

## Api Endpoint
```
EndPoint : /coupons
Method : POST 
Description : 쿠폰을 N개 생성하여 데이터베이스에 보관 ( 쿠폰 코드 1씩 자동증가 : 초기값 1)
              만료일을 테스트 하기 위해 10000단위 레코드 별로 만료일 +1 day 증가
Return value: HTTP status 201 (Created) 
Payload Example (required parameters)
{
    "size": "20000"
}

|-----------|--------------|---------------------------------------------------|---------------|
| Parameter |Parameter Type| Description                                       | Default value |
|-----------|--------------|---------------------------------------------------|---------------|
| size      | @QueryParam  | Number of coupons to be created                   |               |
|-----------|--------------|---------------------------------------------------|---------------|


EndPoint : /coupons/{id}
Method : PUT 
Description : 쿠폰을 사용자에게 지급
Return value: HTTP status 200 (OK) 
성공시 
{
    "code": "COUPON_EXPIRED",
    "message": "Coupon is Expired"
}              
쿠폰 만료 시  
{
    "code": "COUPON_EXPIRED",
    "message": "Coupon is Expired"
}
Payload Example (required parameters)
{
    "status" : "ISSUED",
    "userId" : "joyworld007"
}
```