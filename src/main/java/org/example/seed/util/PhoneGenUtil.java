package org.example.seed.util;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public final class PhoneGenUtil {

  private static final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

  private PhoneGenUtil() {}

  public static PhoneDto parse(final String phoneNumber) {
    try {
      Phonenumber.PhoneNumber rawPhoneNumber = phoneNumberUtil.parse(phoneNumber, "CH");

      return PhoneDto.builder()
        .lada(Integer.toString(rawPhoneNumber.getCountryCode()))
        .phoneNumber(Long.toString(rawPhoneNumber.getNationalNumber()))
        .build();
    } catch (final NumberParseException e) {
      throw new RuntimeException("ERROR-10000");
    }
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class PhoneDto {
    private String lada;
    private String phoneNumber;
  }
}
