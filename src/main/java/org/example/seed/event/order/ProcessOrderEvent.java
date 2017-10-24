package org.example.seed.event.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.seed.domain.Order;

import javax.validation.Valid;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.TimeZone;

/**
 * Created by PINA on 30/06/2017.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
public class ProcessOrderEvent {

  @Valid
  private Order order;
  private String idChef;
  private String idClient;
  private TimeZone timeZone;
}
