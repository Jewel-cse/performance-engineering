package com.rana.performance_engineering_1.repository;

import com.rana.performance_engineering_1.model.TotpUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TotpUserRepository extends JpaRepository<TotpUser, Long> {
    Optional<TotpUser> findByUsername(String username);
}
