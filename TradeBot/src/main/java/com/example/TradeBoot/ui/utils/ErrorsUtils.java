package com.example.TradeBoot.ui.utils;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.Set;



public class ErrorsUtils{

    public static <T> void AddErrors(BindingResult recipient, Set<ConstraintViolation<T>> violations){

        var errorDonor =  ErrorsUtils.toBindingResult(violations, recipient.getTarget(), recipient.getObjectName());

        for (ObjectError objectError : errorDonor.getAllErrors()) {
            recipient.addError(objectError);
        }
    }
    private static <T> BindingResult toBindingResult(Set<ConstraintViolation<T>> violations, Object object, String objectName) {
        BindingResult bindingResult = new BeanPropertyBindingResult(object, objectName);
        new AddConstraintViolationsToErrors().addConstraintViolations(violations, bindingResult);
        return bindingResult;
    }

    private static class AddConstraintViolationsToErrors extends SpringValidatorAdapter {
        public AddConstraintViolationsToErrors() {
            super(Validation.buildDefaultValidatorFactory().getValidator()); // Validator is not actually used
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        public <T> void addConstraintViolations(Set<ConstraintViolation<T>> violations, Errors errors) {
            // Using raw type since processConstraintViolations specifically expects ConstraintViolation<Object>
            super.processConstraintViolations((Set) violations, errors);
        }
    }
}
