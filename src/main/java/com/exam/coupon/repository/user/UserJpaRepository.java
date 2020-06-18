package com.exam.coupon.repository.user;

import com.exam.coupon.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, String> {

}
