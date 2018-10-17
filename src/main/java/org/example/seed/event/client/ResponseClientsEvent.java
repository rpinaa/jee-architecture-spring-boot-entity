package org.example.seed.event.client;

import lombok.*;
import org.example.seed.domain.Client;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;


/**
 * Created by PINA on 22/05/2017.
 */

@Getter
@Setter
@Builder
@XmlRootElement
@NoArgsConstructor
@AllArgsConstructor
public class ResponseClientsEvent {

  private long total;
  private List<Client> clients;
}
