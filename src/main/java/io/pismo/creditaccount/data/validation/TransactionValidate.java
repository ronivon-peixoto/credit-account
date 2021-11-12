package io.pismo.creditaccount.data.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { TransactionValidator.class })
public @interface TransactionValidate {

	String message() default "Invalid transaction!";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}