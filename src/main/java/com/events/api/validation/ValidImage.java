package com.events.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = ValidImageValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidImage {
    String message() default "The image must be a non-empty JPEG, PNG, or WebP file up to 5MB";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    long maxSize() default 5_242_880L;

    String[] allowedContentTypes() default {"image/jpeg", "image/png", "image/webp"};
}
