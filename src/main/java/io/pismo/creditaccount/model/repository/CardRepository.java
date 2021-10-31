package io.pismo.creditaccount.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.pismo.creditaccount.model.entity.Card;

public interface CardRepository extends JpaRepository<Card, Long> {

}
