package org.example.seed.util;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.net.InetAddress;

public final class GeoGenUtil {

  private static final DatabaseReader databaseReader;

  static {
    try (final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
      databaseReader = new DatabaseReader.Builder(context
        .getResource("classpath:GeoLite2-Country.mmdb")
        .getFile())
        .build();
    } catch (final IOException e) {
      throw new RuntimeException("ERROR-10000");
    }
  }

  private GeoGenUtil() { }

  public static String map(final String ip) {
    try {
      return databaseReader.enterprise(InetAddress.getByName(ip)).getCountry().getIsoCode();
    } catch (final IOException | GeoIp2Exception e) {
      return null;
    }
  }
}
