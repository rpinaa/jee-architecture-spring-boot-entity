package org.example.seed.event.client;

import lombok.*;
import org.example.seed.domain.Client;
import org.example.seed.event.CreateEvent;
import org.example.seed.group.client.ClientRegisterGroup;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@XmlRootElement
public class RegisterClientEvent extends CreateEvent {

  @Valid
  @NotNull(groups = {ClientRegisterGroup.class})
  private Client client;
}
