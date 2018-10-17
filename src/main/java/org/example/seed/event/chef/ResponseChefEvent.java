package org.example.seed.event.chef;

import lombok.*;
import org.example.seed.domain.Chef;

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
public class ResponseChefEvent {

  private Chef chef;
}
