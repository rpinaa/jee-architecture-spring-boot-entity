package org.example.seed.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.seed.group.client.ClientUpdateGroup;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode
public class Point {

  @NotNull(groups = {ClientUpdateGroup.class})
  private Double latitude;

  @NotNull(groups = {ClientUpdateGroup.class})
  private Double longitude;
}
