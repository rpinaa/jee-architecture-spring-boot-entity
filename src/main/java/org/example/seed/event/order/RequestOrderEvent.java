package org.example.seed.event.order;

import lombok.Builder;
import org.example.seed.event.RequestEvent;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by PINA on 22/05/2017.
 */
@XmlRootElement
public class RequestOrderEvent extends RequestEvent {

  @Builder
  public RequestOrderEvent(final String id) {
    super(id);
  }
}
