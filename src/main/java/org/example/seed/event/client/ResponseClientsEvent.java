package org.example.seed.event.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.seed.domain.Client;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;


/**
 * Created by PINA on 22/05/2017.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
public class ResponseClientsEvent {

  private long total;
  private List<Client> clients;
}
