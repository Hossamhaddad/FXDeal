package com.bloomberg.fxdeals.service;

import com.bloomberg.fxdeals.dao.DealRepo;
import com.bloomberg.fxdeals.dto.DealRequest;
import com.bloomberg.fxdeals.exception.DealProcessingException;
import com.bloomberg.fxdeals.model.Deal;
import com.bloomberg.fxdeals.validator.DealValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DealServiceTest {

    @Mock
    private DealRepo dealRepo;

    @Mock
    private DealValidator dealValidator;

    @InjectMocks
    private DealService dealService;

    private DealRequest dealRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        dealRequest = new DealRequest();
        dealRequest.setDealUniqueId("12345");
        dealRequest.setFromCurrency("USD");
        dealRequest.setToCurrency("EUR");
        dealRequest.setDealTimestamp("2023-06-01 12:00:00");
        dealRequest.setDealAmount(new BigDecimal("1000.0"));
    }

    @Test
    public void testProcessDeal_Success() {
        doNothing().when(dealValidator).validate(any(), any());
        doNothing().when(dealRepo).save(any(Deal.class));

        dealService.processDeal(dealRequest);

        verify(dealValidator, times(1)).validate(any(), any());
        verify(dealRepo, times(1)).save(any(Deal.class));
    }

    @Test
    public void testProcessDeal_ValidationError() {
        Errors errors = new BeanPropertyBindingResult(dealRequest, "dealRequest");
        errors.rejectValue("dealUniqueId", "error.dealUniqueId", "Deal unique ID error");

        doAnswer(invocation -> {
            ((Errors) invocation.getArgument(1)).rejectValue("dealUniqueId", "error.dealUniqueId", "Deal unique ID error");
            return null;
        }).when(dealValidator).validate(any(), any());

        Exception exception = assertThrows(DealProcessingException.class, () -> {
            dealService.processDeal(dealRequest);
        });

        assertEquals("Deal unique ID error", exception.getMessage());
        verify(dealValidator, times(1)).validate(any(), any());
        verify(dealRepo, never()).save(any(Deal.class));
    }

    @Test
    public void testProcessDeal_OtherValidationError() {
        Errors errors = new BeanPropertyBindingResult(dealRequest, "dealRequest");
        errors.rejectValue("fromCurrency", "error.fromCurrency", "From currency error");

        doAnswer(invocation -> {
            ((Errors) invocation.getArgument(1)).rejectValue("fromCurrency", "error.fromCurrency", "From currency error");
            return null;
        }).when(dealValidator).validate(any(), any());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            dealService.processDeal(dealRequest);
        });

        assertEquals("From currency error", exception.getMessage());
        verify(dealValidator, times(1)).validate(any(), any());
        verify(dealRepo, never()).save(any(Deal.class));
    }

    @Test
    public void testProcessDeal_Exception() {
        doNothing().when(dealValidator).validate(any(), any());
        doThrow(new RuntimeException("Database error")).when(dealRepo).save(any(Deal.class));

        dealService.processDeal(dealRequest);

        verify(dealValidator, times(1)).validate(any(), any());
        verify(dealRepo, times(1)).save(any(Deal.class));
    }
}
