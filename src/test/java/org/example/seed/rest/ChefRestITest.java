package org.example.seed.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.seed.Application;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import static org.example.seed.rest.ChefRest.CHEF_ROOT_PATH;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(
  classes = Application.class,
  properties = {"spring.cloud.config.profile:local"},
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class ChefRestITest {

  @LocalServerPort
  private int PORT;

  @Value("${server.servlet.context-path}")
  private String CONTEXT;

  private static final String HOST = "http://localhost:";

  private final HttpHeaders headers = new HttpHeaders();
  private final TestRestTemplate restTemplate = new TestRestTemplate();

  @Test
  public void getChefsShouldReturnAPageOfChefsUsingAPageAs1AndALimitAs1() throws JSONException {

    final HttpEntity<String> entity = new HttpEntity<>(null, headers);

    final ResponseEntity<String> response = restTemplate.exchange(
      createURLWithPort(CHEF_ROOT_PATH + "?page=1&limit=1"), HttpMethod.GET, entity, String.class
    );

    final String jsonExpected = new BufferedReader(
      new InputStreamReader(TypeReference.class.getResourceAsStream("/chef/getChefs.json"))
    )
      .lines()
      .collect(Collectors.joining("\n"));

    assertEquals(jsonExpected, response.getBody(), false);
  }

  @Test(expected = AssertionError.class)
  public void getChefsShouldReturnAPageOfChefsUsingAPageAs0AndALimitAs1() throws JSONException {

    final HttpEntity<String> entity = new HttpEntity<>(null, headers);

    final ResponseEntity<String> response = restTemplate.exchange(
      createURLWithPort(CHEF_ROOT_PATH + "?page=0&limit=1"), HttpMethod.GET, entity, String.class
    );

    final String jsonExpected = new BufferedReader(
      new InputStreamReader(TypeReference.class.getResourceAsStream("/chef/getChefs.json"))
    )
      .lines()
      .collect(Collectors.joining("\n"));

    assertEquals(jsonExpected, response.getBody(), false);
  }

  private String createURLWithPort(final String uri) {

    return HOST + PORT + CONTEXT + uri;
  }
}
