package com.madm.learnroute.technology.spring.custom;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.nio.charset.StandardCharsets;

/**
 * @author madongming
 */
@Slf4j
public class LengthOfByteValidator implements ConstraintValidator<LengthOfByte, CharSequence> {

    private int min;
    private int max;

    @Override
    public void initialize(LengthOfByte parameters) {
        min = parameters.min();
        max = parameters.max();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        int bytes = value.toString().getBytes(StandardCharsets.UTF_8).length;
        if (min > bytes || max < bytes || max < min) {
            log.info("");
            return false;
        }
        return true;
    }

}
