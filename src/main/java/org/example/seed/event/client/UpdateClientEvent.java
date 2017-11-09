package org.example.seed.event.client;

import lombok.*;
import org.example.seed.domain.Client;
import org.example.seed.event.UpdateEvent;
import org.example.seed.group.client.ClientUpdateGroup;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by PINA on 22/05/2017.
 */

@Data
@Builder
@XmlRootElement
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UpdateClientEvent extends UpdateEvent {

  @Valid
  @NotNull(groups = {ClientUpdateGroup.class})
  private Client client;
}
