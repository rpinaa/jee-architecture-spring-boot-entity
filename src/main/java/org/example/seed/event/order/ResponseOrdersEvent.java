package org.example.seed.event.order;

import lombok.*;
import org.example.seed.domain.Order;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;


/**
 * Created by PINA on 22/05/2017.
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
public class ResponseOrdersEvent {

  private long total;
  private List<Order> orders;
}
