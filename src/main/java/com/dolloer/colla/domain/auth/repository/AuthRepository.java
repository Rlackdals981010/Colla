package com.dolloer.colla.domain.auth.repository;

import com.dolloer.colla.domain.auth.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<Member,Long> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
