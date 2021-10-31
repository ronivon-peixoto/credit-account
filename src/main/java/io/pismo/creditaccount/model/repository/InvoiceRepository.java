package io.pismo.creditaccount.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.pismo.creditaccount.model.entity.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

}
