package org.example.seed.constraint;

import org.example.seed.constraint.impl.RfcImpl;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by PINA on 08/06/2017.
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RfcImpl.class)
@Documented
public @interface Rfc {

  String message() default "must match RFC pattern";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
