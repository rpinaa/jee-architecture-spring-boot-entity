package org.example.seed.constraint.impl;

import org.example.seed.constraint.Denomination;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

/**
 * Created by PINA on 30/06/2017.
 */
public class DenominationImpl implements ConstraintValidator<Denomination, String> {

  private Denomination denomination;

  @Override
  public void initialize(final Denomination denomination) {
    this.denomination = denomination;
  }

  @Override
  public boolean isValid(final String value, final ConstraintValidatorContext constraintValidatorContext) {
    return Optional.of(value)
      .map(v -> v.matches("^([a-z]+[,.]?[ ]?|[a-z]+['-]?)+$"))
      .orElse(true);
  }
}