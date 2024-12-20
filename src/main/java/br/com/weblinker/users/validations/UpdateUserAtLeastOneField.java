package br.com.weblinker.users.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UpdateUserAtLeastOneFieldValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdateUserAtLeastOneField {
    String message() default "At least one field must be provided";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
