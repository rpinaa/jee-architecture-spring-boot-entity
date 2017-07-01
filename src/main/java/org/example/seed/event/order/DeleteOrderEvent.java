package org.example.seed.event.order;

import lombok.Builder;
import org.example.seed.event.DeleteEvent;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by PINA on 22/05/2017.
 */
@XmlRootElement
public class DeleteOrderEvent extends DeleteEvent {

    @Builder
    public DeleteOrderEvent(final String id) {
        super(id);
    }
}
