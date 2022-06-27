package com.example.TradeBoot.ui.utils;

import javax.validation.ConstraintViolation;
import javax.validation.ElementKind;
import javax.validation.Path;
import javax.validation.Validator;
import javax.validation.metadata.ConstraintDescriptor;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ValidorUtil {
    public  static <T> Set<ConstraintViolation<T>> validateModels(Validator validator, List<T> models){
        Set<ConstraintViolation<T>> errors = new HashSet<>();
        for (T model :  models) {
            if (errors != null) errors.addAll(validator.validate(model));
            else errors = validator.validate(model);
        }
        return errors;
    }
}
