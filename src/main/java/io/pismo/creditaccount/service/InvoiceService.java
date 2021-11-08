package io.pismo.creditaccount.service;

import java.util.Optional;

import io.pismo.creditaccount.model.entity.Account;
import io.pismo.creditaccount.model.entity.Invoice;

public interface InvoiceService {

	Optional<Invoice> findById(Long id);

	Invoice processInvoiceByAccount(Account account);

}
