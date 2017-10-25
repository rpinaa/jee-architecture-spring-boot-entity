package org.example.seed.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public final class KeyGenUtil {

  private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

  private KeyGenUtil() { }

  public static byte[] encode(final String credential) {
    return bCryptPasswordEncoder.encode(credential).getBytes();
  }

  public static boolean isMatched(final String rawCredential, final String encodeCredential) {
    return bCryptPasswordEncoder.matches(rawCredential, encodeCredential);
  }
}
