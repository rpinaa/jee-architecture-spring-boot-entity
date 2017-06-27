package org.example.seed.event.chef;

import lombok.Builder;
import org.example.seed.event.DeleteEvent;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by PINA on 22/05/2017.
 */
@XmlRootElement
public class DeleteChefEvent extends DeleteEvent {

    @Builder
    public DeleteChefEvent(final String id) {
        super(id);
    }
}
