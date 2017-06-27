package org.example.seed.event.client;

import lombok.Builder;
import org.example.seed.event.DeleteEvent;

/**
 * Created by PINA on 22/05/2017.
 */
public class DeleteClientEvent extends DeleteEvent {

    @Builder
    public DeleteClientEvent(final String id) {
        super(id);
    }
}
