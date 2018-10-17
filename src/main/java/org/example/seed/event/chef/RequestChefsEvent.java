package org.example.seed.event.chef;

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
public class RequestChefsEvent {

  private int page;
  private int limit;
}
