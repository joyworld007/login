# 카카오 페이 서버 개발 과제
## Rest API 기반 쿠폰시스템

## Specifications

````
 OpenJDK11
 Spring BOOT 2.3.0.RELEASE
 SPRING DATA REDIS
 SPRING MVC
````

## Reading

- 100억건의 데이터를 저장하기 위해서 RDB 대신 redis 저장소를 이용하였습니다.
- Redis 리스트 타입의 경우 약 42억건 정도 데이터 저장이 가능 합니다.
  * ( Maximum length of a list is 232 - 1 elements (4294967295, more than 4 billion of elements per list )
- 최적의 성능을 위해 list type의 경우 10,000건 이상 저장을 권장하지 않으므로, rang base 샤딩 전략으로 10,000건씩 키를 구성하여 나눠서 저장 하였습니다.
		
## Entity
```
쿠폰(Coupon) 
   쿠폰번호
   만료일
   생성일

쿠폰발급(CouponIssue)
   쿠폰번호
   사용여부
   사용자ID
   생성일
```


## Redis key 전략
````
 - 쿠폰 ( key : coupon:couponNumber, type : Coupon )
 - 쿠폰발급 ( key coupon:issue:couponNumber , type : CouponIssue )
 
 - 쿠폰 list ( key : coupon:{range}, type : Sorted sets )
 - 쿠폰 총 갯수 ( key : coupon:total, type : long ) 
 
 - 쿠폰 발급 ( key : coupon:issue:{range}, type : Sorted sets )
 - 쿠폰 발급 총 갯수 ( key : coupon:issue:total, type : long )
 
 - 특정 사용자에게 지급한 쿠폰 ( key : coupon:issue:{user}, type : Sorted sets ) 
 - 특정 사용자에게 지급한 쿠폰 총 갯수 ( key : coupon:issue:{user}:total, type : long )

 - 특정일이 만료일인 발급 쿠폰 list ( key : coupon:expiration:{yyyymmdd}:{range}, type : Sorted sets )
 - 특정일이 만료일인 발급 쿠폰 총 카운트 ( key : coupon:expiration:{yyyymmdd}:total, type : long )
   해당 일자에 만료되는 발급된 쿠폰은 --> total 기준으로 range 키만큼 조회
````







