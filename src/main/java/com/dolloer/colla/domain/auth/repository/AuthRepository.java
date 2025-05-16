package com.dolloer.colla.domain.auth.repository;

import com.dolloer.colla.domain.auth.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Member,Long> {

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);
}
