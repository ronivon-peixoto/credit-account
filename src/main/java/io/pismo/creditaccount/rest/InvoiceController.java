package io.pismo.creditaccount.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.pismo.creditaccount.data.vo.InvoiceVO;
import io.pismo.creditaccount.service.InvoiceService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/v1/invoices")
public class InvoiceController {

	private final InvoiceService invoiceService;

	@GetMapping("{id}")
	public InvoiceVO findById(@PathVariable Long id) {
		return invoiceService.findById(id)
				.map(InvoiceVO::create)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro não encontrado!"));
	}

}
