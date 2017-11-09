package org.example.seed.util;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

public final class PhoneGenUtil {

  private static final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

  private PhoneGenUtil() { }

  public static Optional<PhoneDto> map(final String phoneNumber, final String country) {
    try {
      final Phonenumber.PhoneNumber rawPhoneNumber = phoneNumberUtil.parse(phoneNumber, country);

      return Optional.of(PhoneDto.builder()
        .lada(Integer.toString(rawPhoneNumber.getCountryCode()))
        .phoneNumber(Long.toString(rawPhoneNumber.getNationalNumber()))
        .build());
    } catch (final NumberParseException e) {
      return Optional.empty();
    }
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class PhoneDto {
    private String lada;
    private String phoneNumber;
  }
}
