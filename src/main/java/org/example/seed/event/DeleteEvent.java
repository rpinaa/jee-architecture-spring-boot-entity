package org.example.seed.event;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by Ricardo Pina Arellano on 30/11/2016.
 */
@Data
@AllArgsConstructor
public abstract class DeleteEvent {
  private String id;
}
