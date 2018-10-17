package org.example.seed.event.chef;

import lombok.*;
import org.example.seed.domain.Chef;
import org.example.seed.event.CreateEvent;
import org.example.seed.group.chef.ChefRegisterGroup;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
public class RegisterChefEvent extends CreateEvent {

  @Valid
  @NotNull(groups = {ChefRegisterGroup.class})
  private Chef chef;
}
