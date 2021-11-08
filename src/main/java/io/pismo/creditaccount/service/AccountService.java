package io.pismo.creditaccount.service;

import java.util.List;
import java.util.Optional;

import io.pismo.creditaccount.data.vo.form.AccountFormVO;
import io.pismo.creditaccount.model.entity.Account;

public interface AccountService {

	Optional<Account> findById(Long id);

	List<Account> listByInvoiceClosingDay(Integer dayOfMonth);

	Account save(AccountFormVO form);

}
