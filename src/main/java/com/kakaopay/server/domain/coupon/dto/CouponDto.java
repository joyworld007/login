package com.kakaopay.server.domain.coupon.dto;

import com.kakaopay.server.domain.coupon.CouponStatus;
import com.kakaopay.server.domain.coupon.entity.Coupon;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("coupon")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CouponDto {

  @Id
  private Long id;
  private CouponStatus status;
  private LocalDateTime expireDate;
  private LocalDateTime createDate;
  private String userId;
  private LocalDateTime useDate;
  private LocalDateTime issueDate;

  protected CouponDto(Coupon entity) {
    this.id = entity.getId();
    this.status = entity.getStatus();
    this.expireDate = entity.getExpireDate();
    this.createDate = entity.getCreateDate();
    Optional.ofNullable(entity.getCouponIssue()).ifPresent(t -> {
      this.userId = entity.getCouponIssue().getUserId();
      this.useDate = entity.getCouponIssue().getUseDate();
      this.issueDate = entity.getCouponIssue().getIssueDate();
    });
  }

  public static CouponDto ofEntity(Coupon entity) {
    return new CouponDto(entity);
  }

}
