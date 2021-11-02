package io.pismo.creditaccount.scheduler;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.pismo.creditaccount.service.AccountService;
import io.pismo.creditaccount.service.InvoiceService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InvoiceProcessorTask {

	@Autowired
	private AccountService accountService;

	@Autowired
	private InvoiceService invoiceService;

	@Scheduled(cron = "0 0 0 * * ?") // every day
	public void process() {
		accountService.listByInvoiceClosingDay(LocalDate.now().getDayOfMonth()).forEach(account -> {
			try {
				log.info("Starting account processing :: " + account.getId());
				invoiceService.processInvoicesByAccount(account);
			} catch (Exception e) {
				log.error("Unexpected error processing account :: " + account.getId(), e);
			} finally {
				log.info("Finishing account processing :: " + account.getId());
			}
		});
	}

}
