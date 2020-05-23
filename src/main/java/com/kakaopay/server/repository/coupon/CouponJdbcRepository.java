package com.kakaopay.server.repository.coupon;

import com.kakaopay.server.domain.coupon.dao.CouponDao;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class CouponJdbcRepository {

  private final int BATCH_SIZE = 10000;
  @Autowired
  private JdbcTemplate jdbcTemplate;

  public void createCoupon(List<CouponDao> couponDaoList) {
    int batchCount = 0;
    List<CouponDao> subItems = new ArrayList<>();
    for (int i = 0; i < couponDaoList.size(); i++) {
      subItems.add(couponDaoList.get(i));
      if ((i + 1) % BATCH_SIZE == 0) {
        batchCount = batchInsert(BATCH_SIZE, batchCount, subItems);
      }
    }
    if (!subItems.isEmpty()) {
      batchCount = batchInsert(BATCH_SIZE, batchCount, subItems);
    }
    log.debug("batchCount : {}", batchCount);
    System.out.println("batchCount : " + batchCount);
  }

  private int batchInsert(int batchSize, int batchCount, List<CouponDao> subItems) {
    jdbcTemplate.batchUpdate("INSERT INTO COUPON (`STATUS`, `EXPIRE_DATE`, `CREATE_DATE`) VALUES (?, ?, ?)",
        new BatchPreparedStatementSetter() {
          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setString(1, subItems.get(i).getStatus());
            ps.setString(2, subItems.get(i).getExpireDate().toString());
            ps.setString(3, LocalDateTime.now().toString());
          }

          @Override
          public int getBatchSize() {
            return subItems.size();
          }
        });
    subItems.clear();
    return subItems.size();
  }

}
