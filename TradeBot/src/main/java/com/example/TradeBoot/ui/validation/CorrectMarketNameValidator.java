package com.example.TradeBoot.ui.validation;

import com.example.TradeBoot.trade.services.FinancialInstrumentService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CorrectMarketNameValidator implements ConstraintValidator<CorrectMarketName, Object> {

    @Autowired
    FinancialInstrumentService financialInstrumentService;

    @Override
    public void initialize(CorrectMarketName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid = false;

        if (object == null) { // This should be validated by the not null validator (@NotNull).
            isValid = true;
        } else if (object instanceof String) {
            String targetName = new String(object.toString());


            isValid = financialInstrumentService.getAllNames().stream().anyMatch(financialInstrumentName -> equals(targetName));

            if (!isValid) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate("Market name does not match any coin or future name").addConstraintViolation();
            }
        }

        return isValid;
    }


}
