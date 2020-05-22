# 카카오 페이 서버 개발 과제
## Rest API 기반 쿠폰시스템

## Specifications

````
 OpenJDK11
 Spring Boot 2.3.0.RELEASE
 Embed Redis
````

## Reading

- 100억건의 데이터를 빠르게 저장하기 위해서 RDB 대신 redis 저장소를 이용하였습니다.
- 특정일에 만료 되는 쿠폰 리스트를 저장하기 위해 list type을 사용하여 저장하였습니다.

- Api feature list



		
## Entity
```
쿠폰(Coupon) 
   쿠폰번호
   발급여부
   만료일
   생성일
   발급일

쿠폰발급(CouponIssue)
   쿠폰번호
   사용여부
   사용자ID
   발급일
   사용일

사용자(User)
   id ( 사용자 아이디 )
   pass ( 사용자 패스워드 )
   token ( 발급받은 토큰 )
```


## Redis key 전략
````
 - 쿠폰 ( key : coupon:{couponNumber}, type : Coupon )
 - 쿠폰발급 ( key coupon:issue:{couponNumber}, type : CouponIssue )
 
 - 만료일 기준 쿠폰 발급 list ( key : coupon:expiration:{yyyymmdd}, type : list )
 - json token 저장 ( key : user:{id}, type : User )  

````







