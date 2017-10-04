package org.example.seed.event.client;

import lombok.Builder;
import org.example.seed.event.DeleteEvent;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by PINA on 22/05/2017.
 */
@XmlRootElement
public class DeleteClientEvent extends DeleteEvent {

  @Builder
  public DeleteClientEvent(final String id) {
    super(id);
  }
}
