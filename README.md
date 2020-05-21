
100억건의 데이터를 저장하기 위해서는 RDB로는 한계가 있다고 판단 redis db를 사용하기로 판단 하였습니다.

 Redis 리스트 타입의 경우 약 42억건 정도 데이터 저장이 가능 합니다.
  * ( Maximum length of a list is 232 - 1 elements (4294967295, more than 4 billion of elements per list )
최적의 조회 성능을 위해 rang base 샤딩 전략으로 10,000건씩 키를 구성하여 나눠서 저장 하였습니다.
		
redis key 전략
 - 쿠폰 ( coupon:{range} )
 - 발급 된 쿠폰 ( coupon:issue:{range} )

쿠폰 리스트 ( key : coupon , type : list)
쿠폰 총 갯수 ( key : coupon:total, type : long )

쿠폰 ( key : coupon:couponNumber)
   쿠폰번호 ( long )
   만료일
   생성일


발급된 쿠폰 ( key : coupon:issue:{range}, type : list )
발급된 쿠폰 총 갯수 ( key : coupon:issue:total, type : long )

쿠폰 발급( key 쿠폰 번호 coupon:issue:couponNumber)
   쿠폰번호
   사용자ID
   생성일

발급 받은 쿠폰 만료일 조회용 key( coupon:expiration:yyyymmdd, type : list )
