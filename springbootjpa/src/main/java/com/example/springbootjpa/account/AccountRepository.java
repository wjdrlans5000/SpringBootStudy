package com.example.springbootjpa.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

//    @Query(nativeQuery = true, value = "select * from USER where username='{0}'")
    Optional<Account> findByUsername(String username);
}
