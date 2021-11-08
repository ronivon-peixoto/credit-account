package io.pismo.creditaccount.rest;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.pismo.creditaccount.data.vo.AccountVO;
import io.pismo.creditaccount.data.vo.form.AccountFormVO;
import io.pismo.creditaccount.service.AccountService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/v1/accounts")
public class AccountController {

	private final AccountService accountService;

	@GetMapping("{id}")
	public AccountVO findById(@PathVariable Long id) {
		return accountService.findById(id)
				.map(AccountVO::create)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Record not found!"));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public AccountVO save(@Valid @RequestBody AccountFormVO form) {
		return AccountVO.create(accountService.save(form));
	}

}
