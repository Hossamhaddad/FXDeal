package com.bloomberg.fxdeals.validator;

import com.bloomberg.fxdeals.dao.DealRepo;
import com.bloomberg.fxdeals.dto.DealRequest;
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

public class DealValidatorTest {

    @Mock
    private DealRepo dealRepo;

    @InjectMocks
    private DealValidator dealValidator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidate_ValidDealRequest() {

        DealRequest dealRequest = new DealRequest("12345", "USD", "EUR", "2024-06-27T19:00:32", new BigDecimal("1000.00"));
        when(dealRepo.existsByDealUniqueId("12345")).thenReturn(false);
        Errors errors = new BeanPropertyBindingResult(dealRequest, "dealRequest");
        dealValidator.validate(dealRequest, errors);

        assertFalse(errors.hasErrors());
    }

    @Test
    public void testValidate_DuplicateDealUniqueId() {

        DealRequest dealRequest = new DealRequest("12345", "USD", "EUR", "2024-06-27T19:00:32", new BigDecimal("1000.00"));
        when(dealRepo.existsByDealUniqueId("12345")).thenReturn(true);

        Errors errors = new BeanPropertyBindingResult(dealRequest, "dealRequest");
        dealValidator.validate(dealRequest, errors);

        assertTrue(errors.hasFieldErrors("dealUniqueId"));
        assertEquals("Deal with the same ID already exists you cant add the same deal more than once", errors.getFieldError("dealUniqueId").getDefaultMessage());
    }

    @Test
    public void testValidate_SameCurrency() {
        DealRequest dealRequest = new DealRequest("12345", "USD", "USD", "2024-06-27T19:00:32", new BigDecimal("1000.00"));
        Errors errors = new BeanPropertyBindingResult(dealRequest, "dealRequest");

        dealValidator.validate(dealRequest, errors);
        assertTrue(errors.hasFieldErrors("toCurrency"));
        assertEquals("You Cant Exchange to the same currency", errors.getFieldError("toCurrency").getDefaultMessage());
    }

}
