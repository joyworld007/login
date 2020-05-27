package com.kakaopay.server.repository.user;

import com.kakaopay.server.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, String> {

}
