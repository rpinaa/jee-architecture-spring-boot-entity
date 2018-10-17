package org.example.seed.event.client;

import lombok.*;
import org.example.seed.domain.Client;

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
public class ResponseClientEvent {

  private Client client;
}
