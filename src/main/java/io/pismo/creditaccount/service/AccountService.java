package io.pismo.creditaccount.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import io.pismo.creditaccount.data.vo.form.AccountFormVO;
import io.pismo.creditaccount.model.entity.Account;
import io.pismo.creditaccount.model.repository.AccountRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccountService {

	private AccountRepository accountRepository;

	public Optional<Account> findById(Long id) {
		return accountRepository.findById(id);
	}

	public Account save(AccountFormVO form) {
		// TODO Auto-generated method stub
		return null;
	}

}
