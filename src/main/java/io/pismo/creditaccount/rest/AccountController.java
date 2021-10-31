package io.pismo.creditaccount.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.pismo.creditaccount.data.vo.AccountVO;
import io.pismo.creditaccount.model.repository.AccountRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

	private final AccountRepository repository;

	@GetMapping("{id}")
	public AccountVO acharPorId(@PathVariable Long id) {
		return repository
				.findById(id)
				.map(AccountVO::create)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

}
