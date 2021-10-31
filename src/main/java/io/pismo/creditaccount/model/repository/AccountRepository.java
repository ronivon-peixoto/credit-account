package io.pismo.creditaccount.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.pismo.creditaccount.model.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
