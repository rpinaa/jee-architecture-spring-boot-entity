package org.example.seed.domain;

import lombok.Getter;
import lombok.Setter;
import org.example.seed.group.client.ClientUpdateGroup;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class Point {

  @NotNull(groups = {ClientUpdateGroup.class})
  private Double latitude;

  @NotNull(groups = {ClientUpdateGroup.class})
  private Double longitude;
}
