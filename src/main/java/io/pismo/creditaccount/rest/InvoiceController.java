package io.pismo.creditaccount.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.pismo.creditaccount.data.vo.InvoiceVO;
import io.pismo.creditaccount.model.repository.InvoiceRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

	private final InvoiceRepository repository;

	@GetMapping("{id}")
	public InvoiceVO acharPorId(@PathVariable Long id) {
		return repository
				.findById(id)
				.map(InvoiceVO::create)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

}
