package org.example.seed.event.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.seed.domain.Order;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;


/**
 * Created by PINA on 22/05/2017.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
public class CatalogOrderEvent {

    private long total;
    private List<Order> orders;
}
