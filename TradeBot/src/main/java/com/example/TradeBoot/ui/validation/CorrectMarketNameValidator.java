package com.example.TradeBoot.ui.validation;

import com.example.TradeBoot.configs.ServiceUtils;
import com.example.TradeBoot.trade.services.FinancialInstrumentService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class CorrectMarketNameValidator implements ConstraintValidator<CorrectMarketName, Object> {

    FinancialInstrumentService financialInstrumentService;

    @Override
    public void initialize(CorrectMarketName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.financialInstrumentService = ServiceUtils.getFinancialInstrumentService();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid = false;

        if (object == null) { // This should be validated by the not null validator (@NotNull).
            isValid = true;
        } else if (object instanceof String) {
            String targetName = String.valueOf(object);

            var allNames = financialInstrumentService.getAllNames();

            isValid = allNames.stream().anyMatch(financialInstrumentName -> financialInstrumentName.equals(targetName));

            if (!isValid) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate("Market name does not match any coin or future name").addConstraintViolation();
            }
        }

        return isValid;
    }


}
