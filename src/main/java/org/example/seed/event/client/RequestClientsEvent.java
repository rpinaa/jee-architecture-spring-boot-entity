package org.example.seed.event.client;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by PINA on 22/05/2017.
 */
@Getter
@Setter
@Builder
@XmlRootElement
@NoArgsConstructor
@AllArgsConstructor
public class RequestClientsEvent {

  private int page;
  private int limit;
}
