package com.madm.learnroute.technology.spring.custom;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LengthOfByteValidator.class)
public @interface LengthOfByte {
    int min() default 0;

    int max() default Integer.MAX_VALUE;

    String message() default "{com.madm.learnroute.spring.custom.LengthOfByte.java.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
