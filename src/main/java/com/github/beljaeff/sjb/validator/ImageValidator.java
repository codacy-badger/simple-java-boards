package com.github.beljaeff.sjb.validator;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.github.beljaeff.sjb.util.Utils.isImage;

public class ImageValidator implements ConstraintValidator<Image, MultipartFile> {

    @Override
    public void initialize(Image constraintAnnotation) {}

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        return !(file == null || file.isEmpty() || !isImage(file.getContentType()));
    }
}