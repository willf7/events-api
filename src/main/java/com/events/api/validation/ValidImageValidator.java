package com.events.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidImageValidator implements ConstraintValidator<ValidImage, MultipartFile> {
    private long maxSize;
    private Set<String> allowedContentTypes;

    @Override
    public void initialize(ValidImage constraintAnnotation) {
        maxSize = constraintAnnotation.maxSize();
        allowedContentTypes = Arrays.stream(constraintAnnotation.allowedContentTypes())
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null) {
            return true;
        }

        if (file.isEmpty() || file.getSize() > maxSize) {
            return false;
        }

        String contentType = file.getContentType();
        return contentType != null && allowedContentTypes.contains(contentType.toLowerCase());
    }
}
