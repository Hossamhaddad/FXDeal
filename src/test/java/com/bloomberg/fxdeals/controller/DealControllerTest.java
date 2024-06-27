package com.bloomberg.fxdeals.controller;

import com.bloomberg.fxdeals.dto.DealRequest;
import com.bloomberg.fxdeals.exception.DealProcessingException;
import com.bloomberg.fxdeals.service.DealService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class DealControllerTest {

    @Mock
    private DealService dealService;

    @InjectMocks
    private DealController dealController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateDeal_Successful() {

        DealRequest dealRequest = new DealRequest("12345", "USD", "EUR", "2024-06-27T19:00:32", BigDecimal.valueOf(1000.00));

        doNothing().when(dealService).processDeal(dealRequest);

        ResponseEntity<String> responseEntity = dealController.createDeal(dealRequest);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo("deal is saved");

        verify(dealService, times(1)).processDeal(dealRequest);
    }

    @Test
    public void testCreateDeal_DealProcessingException() {

        DealRequest dealRequest = new DealRequest("12345", "USD", "EUR", "2024-06-27T19:00:32", BigDecimal.valueOf(1000.00));

        doThrow(new DealProcessingException("Deal processing failed")).when(dealService).processDeal(dealRequest);
        ResponseEntity<String> responseEntity = dealController.createDeal(dealRequest);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(responseEntity.getBody()).isEqualTo("Deal processing failed");

        verify(dealService, times(1)).processDeal(dealRequest);
    }

    @Test
    public void testCreateDeal_IllegalArgumentException() {

        DealRequest dealRequest = new DealRequest("12345", "US", "EUR", "2024-06-27T19:00:32", BigDecimal.valueOf(1000.00));
        doThrow(new IllegalArgumentException("Please use a valid currency code")).when(dealService).processDeal(dealRequest);

        ResponseEntity<String> responseEntity = dealController.createDeal(dealRequest);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isEqualTo("Please use a valid currency code");

        verify(dealService, times(1)).processDeal(dealRequest);
    }

}
