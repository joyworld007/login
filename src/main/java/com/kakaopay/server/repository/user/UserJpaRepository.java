package com.kakaopay.server.repository.user;

import com.kakaopay.server.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, String> {

  Optional<User> findByUserIdAndPass(String userId, String pass);
}
