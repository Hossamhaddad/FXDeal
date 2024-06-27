package com.bloomberg.fxdeals.service;

import com.bloomberg.fxdeals.dao.DealRepo;
import com.bloomberg.fxdeals.dto.DealRequest;
import com.bloomberg.fxdeals.exception.DealProcessingException;
import com.bloomberg.fxdeals.model.Deal;
import com.bloomberg.fxdeals.validator.DealValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;

@Service
public class DealService {

    private static final Logger logger = LoggerFactory.getLogger(DealService.class);

    private final DealRepo dealRepo;
    private final DealValidator dealValidator;

    public DealService(DealRepo dealRepo, DealValidator dealValidator ){
        this.dealRepo=dealRepo;
        this.dealValidator=dealValidator;
    }

    @Transactional
    public void processDeal(DealRequest dealRequest) {

        Errors errors = new  BeanPropertyBindingResult(dealRequest, "dealRequest");
        dealValidator.validate(dealRequest, errors);

        if (errors.hasErrors()) {
            for (FieldError fieldError : errors.getFieldErrors()) {
                String fieldName = fieldError.getField();
                String errorMessage = fieldError.getDefaultMessage();
                if (fieldName.equals("dealUniqueId")) {
                    throw new DealProcessingException(errorMessage);
                } else {
                    throw new IllegalArgumentException(errorMessage);
                }
            }
        }

        try {
            Deal deal = convertToDeal(dealRequest);
            dealRepo.save(deal);
            logger.info("Deal has been saved successfully");
        }catch (Exception ex) {
            logger.error("Error processing deal: {}", dealRequest.getDealUniqueId());
        }
    }

    private Deal convertToDeal(DealRequest dealRequest) {
        Deal deal = new Deal();
        deal.setDealUniqueId(dealRequest.getDealUniqueId());
        deal.setFromCurrency(dealRequest.getFromCurrency());
        deal.setToCurrency(dealRequest.getToCurrency());
        deal.setDealTimestamp(LocalDateTime.parse(dealRequest.getDealTimestamp().replace(" ", "T")));
        deal.setDealAmount(dealRequest.getDealAmount());

        return deal;
    }

}
