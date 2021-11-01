-- --------------------------------------------------------

--
-- 
--
drop table if exists account;
drop table if exists card;
drop table if exists invoice_has_transactions;
drop table if exists invoice;
drop table if exists transaction;

create table account (
   id bigint not null auto_increment,
    credit_limit decimal(15,2) not null,
    doc_number varchar(11) not null,
    is_active varchar(1) not null,
    withdrawal_limit decimal(15,2) not null,
    card_id bigint not null,
    primary key (id)
) engine=InnoDB;

create table card (
   id bigint not null auto_increment,
    card_number varchar(16) not null,
    is_active varchar(1) not null,
    primary key (id)
) engine=InnoDB;

create table invoice (
   id bigint not null auto_increment,
    invoice_number varchar(48) not null,
    payment_due decimal(15,2) not null,
    payment_due_date DATE not null,
    invoice_status varchar(20) not null,
    account_id bigint not null,
    primary key (id)
) engine=InnoDB;

create table invoice_has_transactions (
   invoice_id bigint not null,
    transaction_id bigint not null
) engine=InnoDB;

create table transaction (
   id bigint not null auto_increment,
    amount decimal(15,2) not null,
    description varchar(45) not null,
    event_date TIMESTAMP not null,
    operation_type varchar(20) not null,
    account_id bigint not null,
    primary key (id)
) engine=InnoDB;

alter table invoice_has_transactions 
   add constraint UK_qb0ka332unj3ji2woxmairea6 unique (transaction_id);

alter table account 
   add constraint FKbwa0i03rcu97g5vonxdmslc35 
   foreign key (card_id) 
   references card (id);

alter table invoice 
   add constraint FKoevv8h8t2qgym9s0cn7oh069b 
   foreign key (account_id) 
   references account (id);

alter table invoice_has_transactions 
   add constraint FKi3bivhs3wxtjunipgtyr60ep4 
   foreign key (transaction_id) 
   references transaction (id);

alter table invoice_has_transactions 
   add constraint FKb21qf1btvw8xr57d27imww9ii 
   foreign key (invoice_id) 
   references invoice (id);

alter table transaction 
   add constraint FK6g20fcr3bhr6bihgy24rq1r1b 
   foreign key (account_id) 
   references account (id);



--
-- TESTES
--

INSERT INTO `card` (id, card_number, is_active) VALUES (1, "0123456789101112", "S");

INSERT INTO `account` (id, credit_limit, doc_number, is_active, withdrawal_limit, card_id) 
	VALUES(1, 0.0, "12345678900", "S", 0.0, 1);

INSERT INTO `invoice` (id, invoice_number, payment_due, payment_due_date, invoice_status, account_id) 
	VALUES (1, "321654987159548785874585632165498715954878", 0.0, CURRENT_DATE(), "AGUARDANDO_PAGAMENTO", 1);

INSERT INTO `transaction` (id, amount, description, event_date, operation_type, account_id) 
	VALUES (1, 0.0, "Supermercado XYZ", CURRENT_TIMESTAMP(), "COMPRA_A_VISTA", 1);

INSERT INTO `transaction` (id, amount, description, event_date, operation_type, account_id) 
	VALUES (2, 0.0, "Academia XYZ", CURRENT_TIMESTAMP(), "COMPRA_A_VISTA", 1);

INSERT INTO `invoice_has_transactions` (invoice_id, transaction_id) VALUES (1, 1);
INSERT INTO `invoice_has_transactions` (invoice_id, transaction_id) VALUES (1, 2);


