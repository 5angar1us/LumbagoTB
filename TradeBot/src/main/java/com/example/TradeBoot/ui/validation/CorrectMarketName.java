package com.example.TradeBoot.ui.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = CorrectMarketNameValidator.class)
@Documented
public @interface CorrectMarketName {
    public String message() default "{java.math.BigDecimal.range.error}";
    public Class<?>[] groups() default {};
    public Class<? extends Payload>[] payload() default {};
}
