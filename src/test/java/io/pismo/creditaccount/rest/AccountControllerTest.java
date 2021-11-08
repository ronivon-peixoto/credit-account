package io.pismo.creditaccount.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.pismo.creditaccount.data.vo.form.AccountFormVO;
import io.pismo.creditaccount.model.entity.Account;
import io.pismo.creditaccount.model.entity.Card;
import io.pismo.creditaccount.service.AccountService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AccountControllerTest {

	private static final Long ACCOUNT_ID = 1L;
	private static final String ACCOUNT_DOC_NUMBER = "06067381001";
	private static final BigDecimal ACCOUNT_CREDIT_LIMIT = BigDecimal.valueOf(10000.0);
	private static final BigDecimal ACCOUNT_WITHDRAWAL_LIMIT = BigDecimal.valueOf(1000.0);;
	private static final Integer ACCOUNT_INVOICE_CLOSING_DAY = 15;
	private static final Boolean ACCOUNT_IS_ACTIVE = Boolean.TRUE;

	private static final Long CARD_ID = 1L;
	private static final String CARD_NUMBER = "5100921398143993";
	private static final Boolean CARD_IS_ACTIVE = Boolean.TRUE;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private AccountService accountService;

	@Test
	void testFindById_NotFound() throws Exception {
		given(accountService.findById(ACCOUNT_ID)).willReturn(Optional.empty());

		mockMvc.perform(get("/v1/accounts/{id}", ACCOUNT_ID)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void testFindById() throws Exception {
		Card card = Card.builder()
				.id(CARD_ID)
				.cardNumber(CARD_NUMBER)
				.isActive(CARD_IS_ACTIVE)
				.build();

		Account account = Account.builder()
				.id(ACCOUNT_ID)
				.docNumber(ACCOUNT_DOC_NUMBER)
				.creditLimit(ACCOUNT_CREDIT_LIMIT)
				.withdrawalLimit(ACCOUNT_WITHDRAWAL_LIMIT)
				.invoiceClosingDay(ACCOUNT_INVOICE_CLOSING_DAY)
				.isActive(ACCOUNT_IS_ACTIVE)
				.card(card)
				.build();

		given(accountService.findById(ACCOUNT_ID)).willReturn(Optional.of(account));

		mockMvc.perform(get("/v1/accounts/{id}", ACCOUNT_ID)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(ACCOUNT_ID.intValue())))
				.andExpect(jsonPath("$.docNumber", is(ACCOUNT_DOC_NUMBER)))
				.andExpect(jsonPath("$.creditLimit", is(ACCOUNT_CREDIT_LIMIT.doubleValue())))
				.andExpect(jsonPath("$.withdrawalLimit", is(ACCOUNT_WITHDRAWAL_LIMIT.doubleValue())))
				.andExpect(jsonPath("$.invoiceClosingDay", is(ACCOUNT_INVOICE_CLOSING_DAY)))
				.andExpect(jsonPath("$.isActive", is(ACCOUNT_IS_ACTIVE)))
				.andExpect(jsonPath("$.card").exists())
				.andExpect(jsonPath("$.card.id", is(CARD_ID.intValue())))
				.andExpect(jsonPath("$.card.cardNumber", is(CARD_NUMBER)))
				.andExpect(jsonPath("$.card.isActive", is(CARD_IS_ACTIVE)));
	}

	@Test
	void testSave_DadosInvalidos() throws Exception {
		AccountFormVO form = AccountFormVO.builder()
				.docNumber(ACCOUNT_DOC_NUMBER)
				.build();

		mockMvc.perform(post("/v1/accounts")
				.content(objectMapper.writeValueAsString(form))
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").exists())
				.andExpect(jsonPath("$.timestamp", is(notNullValue())))
				.andExpect(jsonPath("$.status", is(400)))
				.andExpect(jsonPath("$.errors").isArray())
				.andExpect(jsonPath("$.errors", hasSize(3)))
				.andExpect(jsonPath("$.errors", hasItem("O campo creditLimit é obrigatório.")))
				.andExpect(jsonPath("$.errors", hasItem("O campo withdrawalLimit é obrigatório.")))
				.andExpect(jsonPath("$.errors", hasItem("O campo invoiceClosingDay é obrigatório.")));

		verify(accountService, times(0)).save(any(AccountFormVO.class));
	}

	@Test
	void testSave() throws Exception {
		Card card = Card.builder()
				.id(CARD_ID)
				.cardNumber(CARD_NUMBER)
				.isActive(CARD_IS_ACTIVE)
				.build();

		Account account = Account.builder()
				.id(ACCOUNT_ID)
				.docNumber(ACCOUNT_DOC_NUMBER)
				.creditLimit(ACCOUNT_CREDIT_LIMIT)
				.withdrawalLimit(ACCOUNT_WITHDRAWAL_LIMIT)
				.invoiceClosingDay(ACCOUNT_INVOICE_CLOSING_DAY)
				.isActive(ACCOUNT_IS_ACTIVE)
				.card(card)
				.build();

		AccountFormVO form = AccountFormVO.builder()
				.docNumber(ACCOUNT_DOC_NUMBER)
				.creditLimit(ACCOUNT_CREDIT_LIMIT)
				.withdrawalLimit(ACCOUNT_WITHDRAWAL_LIMIT)
				.invoiceClosingDay(ACCOUNT_INVOICE_CLOSING_DAY)
				.build();

		given(accountService.save(form)).willReturn(account);

		mockMvc.perform(post("/v1/accounts")
				.content(objectMapper.writeValueAsString(form))
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id", is(ACCOUNT_ID.intValue())))
				.andExpect(jsonPath("$.docNumber", is(ACCOUNT_DOC_NUMBER)))
				.andExpect(jsonPath("$.creditLimit", is(ACCOUNT_CREDIT_LIMIT.doubleValue())))
				.andExpect(jsonPath("$.withdrawalLimit", is(ACCOUNT_WITHDRAWAL_LIMIT.doubleValue())))
				.andExpect(jsonPath("$.invoiceClosingDay", is(ACCOUNT_INVOICE_CLOSING_DAY)))
				.andExpect(jsonPath("$.isActive", is(ACCOUNT_IS_ACTIVE)))
				.andExpect(jsonPath("$.card").exists())
				.andExpect(jsonPath("$.card.id", is(CARD_ID.intValue())))
				.andExpect(jsonPath("$.card.cardNumber", is(CARD_NUMBER)))
				.andExpect(jsonPath("$.card.isActive", is(CARD_IS_ACTIVE)));

		verify(accountService, times(1)).save(any(AccountFormVO.class));
	}

}
