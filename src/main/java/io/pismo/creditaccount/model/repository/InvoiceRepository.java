package io.pismo.creditaccount.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.pismo.creditaccount.model.entity.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

	@Query("SELECT i FROM Invoice i WHERE i.invoiceNumber = :number")
	Optional<Invoice> findByInvoiceNumber(@Param("number") String number);

}
