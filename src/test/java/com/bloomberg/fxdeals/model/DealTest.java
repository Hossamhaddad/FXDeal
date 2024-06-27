package com.bloomberg.fxdeals.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.MILLIS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@DataJpaTest
public class DealTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testDealEntityMapping() {

        LocalDateTime time=LocalDateTime.now();
        Deal deal = new Deal("123456", "USD", "EUR", time, new BigDecimal("1000.00"));

        entityManager.persist(deal);
        entityManager.flush();

        entityManager.clear();

        Deal found = entityManager.find(Deal.class, "123456");

        assertThat(found).isNotNull();
        assertThat(found.getDealUniqueId()).isEqualTo(deal.getDealUniqueId());
        assertThat(found.getFromCurrency()).isEqualTo(deal.getFromCurrency());
        assertThat(found.getToCurrency()).isEqualTo(deal.getToCurrency());
        assertThat(found.getDealTimestamp()).isCloseTo(deal.getDealTimestamp(), within(1, MILLIS));
        assertThat(found.getDealAmount()).isEqualTo(deal.getDealAmount());
    }
}
