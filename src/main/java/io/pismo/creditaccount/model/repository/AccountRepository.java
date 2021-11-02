package io.pismo.creditaccount.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.pismo.creditaccount.model.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

	@Query("select a from Account a join a.card c where c.cardNumber = :number")
	Optional<Account> findAccountByCardNumber(@Param("number") String number);

	@Query("select a from Account a where a.invoiceClosingDay = :dayOfMonth")
	List<Account> listByInvoiceClosingDay(Integer dayOfMonth);

}
