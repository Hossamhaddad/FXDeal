package com.bloomberg.fxdeals.controller;

import com.bloomberg.fxdeals.dto.DealRequest;
import com.bloomberg.fxdeals.exception.DealProcessingException;
import com.bloomberg.fxdeals.service.DealService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DealController {

    private final DealService dealService;


    public DealController(DealService dealService){
        this.dealService=dealService;
    }


    @RequestMapping(value = "/deals",method = RequestMethod.POST)
    public ResponseEntity<String> createDeal(  @RequestBody DealRequest dealRequest) {

        try {
            dealService.processDeal(dealRequest);
            return new ResponseEntity<>("deal is saved",HttpStatus.OK);
        } catch (DealProcessingException dealProcessingException){
            return new ResponseEntity<>(dealProcessingException.getMessage(),HttpStatus.CONFLICT);
        }catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

}
