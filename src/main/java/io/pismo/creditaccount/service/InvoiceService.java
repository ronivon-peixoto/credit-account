package io.pismo.creditaccount.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import io.pismo.creditaccount.model.entity.Invoice;
import io.pismo.creditaccount.model.repository.InvoiceRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class InvoiceService {

	private InvoiceRepository invoiceRepository;

	public Optional<Invoice> findById(Long id) {
		return invoiceRepository.findById(id);
	}

}
