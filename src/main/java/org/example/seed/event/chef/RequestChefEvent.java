package org.example.seed.event.chef;

import lombok.Builder;
import org.example.seed.event.RequestEvent;

/**
 * Created by PINA on 22/05/2017.
 */
public class RequestChefEvent extends RequestEvent {

    @Builder
    public RequestChefEvent(final String id) {
        super(id);
    }
}
