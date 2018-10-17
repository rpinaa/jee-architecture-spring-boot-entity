package org.example.seed.event.order;

import lombok.*;
import org.example.seed.domain.Order;

import javax.validation.Valid;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.TimeZone;

/**
 * Created by PINA on 30/06/2017.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
public class ProcessOrderEvent {

  @Valid
  private Order order;
  private TimeZone timeZone;
}
