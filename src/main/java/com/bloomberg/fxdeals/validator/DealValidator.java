package com.bloomberg.fxdeals.validator;

import com.bloomberg.fxdeals.dao.DealRepo;
import com.bloomberg.fxdeals.dto.DealRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Currency;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DealValidator  implements Validator {

    private final DealRepo dealRepo;
    public DealValidator(DealRepo dealRepo){
        this.dealRepo=dealRepo;
    }

    private static final Set<String> VALID_CURRENCY_CODES = Currency.getAvailableCurrencies()
            .stream()
            .map(Currency::getCurrencyCode)
            .collect(Collectors.toSet());

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    @Override
    public boolean supports(Class<?> clazz) {
        return DealRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        DealRequest dealRequest = (DealRequest) target;

        if (dealRepo.existsByDealUniqueId(dealRequest.getDealUniqueId())) {
            errors.rejectValue("dealUniqueId", "dealUniqueId.duplicate", "Deal with the same ID already exists you cant add the same deal more than once");
            return;
        }

        if (dealRequest.getFromCurrency().equals(dealRequest.getToCurrency())) {
            errors.rejectValue("toCurrency", "currencies.equal", "You Cant Exchange to the same currency");
            return;
        }

        BigDecimal amount=new BigDecimal(dealRequest.getDealAmount().toString());

        validateDealAmount(amount,"dealAmount",errors);
        validateCurrency(dealRequest.getFromCurrency(),"fromCurrency",errors);
        validateCurrency(dealRequest.getToCurrency(),"toCurrency",errors);
        validateDealTimestamp(dealRequest.getDealTimestamp(),"dealTimestamp",errors);

    }

    private void validateCurrency(String currency, String field, Errors errors) {
        if (currency == null) {
            errors.rejectValue(field, "currency.null", field + " must not be null");
            return;
        }
        if(!VALID_CURRENCY_CODES.contains(currency)){
            errors.rejectValue(field, "currency.null", field + " Please use a valid currency code");
        }
    }

    private void validateDealTimestamp(String dealTimestamp, String field, Errors errors) {
        if(dealTimestamp != null) {
            try {
                LocalDateTime.parse(dealTimestamp, DATE_TIME_FORMATTER);
            } catch (DateTimeParseException ex) {
                errors.rejectValue(field, "dealTimestamp.invalid", "Invalid time format");
            }
        }
    }

    private void validateDealAmount(BigDecimal dealAmount, String field, Errors errors) {
        if (dealAmount.compareTo(BigDecimal.ZERO)<=0) {
            errors.rejectValue(field, "dealAmount.invalid", "Deal amount must be greater than 0");
        }
    }
}
