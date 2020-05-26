package com.kakaopay.server.domain.coupon.dto;

import java.util.Set;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("coupon:expire")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CouponExpire {

  @Id
  private String id; //yyyyMMdd

  private Set<Long> couponIdSet;

}
