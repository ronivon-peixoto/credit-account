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

import io.pismo.creditaccount.data.vo.TransactionVO;
import io.pismo.creditaccount.data.vo.form.TransactionFormVO;
import io.pismo.creditaccount.service.TransactionService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/v1/transactions")
public class TransactionController {

	private final TransactionService transactionService;

	@GetMapping("{id}")
	public TransactionVO findById(@PathVariable Long id) {
		return transactionService.findById(id)
				.map(TransactionVO::create)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro não encontrado!"));
	}

	@PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@Valid @RequestBody TransactionFormVO form) {
		transactionService.save(form);
    }

}
