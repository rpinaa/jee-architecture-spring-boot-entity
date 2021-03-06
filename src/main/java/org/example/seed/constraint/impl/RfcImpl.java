package org.example.seed.constraint.impl;

import org.example.seed.constraint.Rfc;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by PINA on 08/06/2017.
 */
public class RfcImpl implements ConstraintValidator<Rfc, String> {

  private Rfc rfc;

  @Override
  public void initialize(final Rfc rfc) {
    this.rfc = rfc;
  }

  @Override
  public boolean isValid(final String value, final ConstraintValidatorContext constraintValidatorContext) {
    return value == null || value.matches("[A-Z]{4}[0-9]{6}[A-Z0-9]{3}$");
  }
}
