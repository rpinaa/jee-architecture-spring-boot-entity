package org.example.seed.event.order;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by PINA on 22/05/2017.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
public class RequestOrdersEvent {

  private int page;
  private int limit;
  private String chefId;
}
