package org.ormi.priv.tfa.orderflow.cqrs;

import java.time.Instant;
import java.util.UUID;

/**
 * Conteneur pour un événement de domaine avec métadonnées de séquence et horodatage.
 *
 * Utilisé pour persister et propager des événements de manière traçable.
 */

public class EventEnvelope<E extends DomainEvent> {
    private final E event;
    private final Long sequence;
    private final Instant timestamp;

    public EventEnvelope(E event, Long sequence, Instant timestamp) {
        this.event = event;
        this.sequence = sequence;
        this.timestamp = timestamp;
    }

    public UUID aggregateId() {
        return event.aggregateId();
    }
    public String aggregateType() {
        return event.aggregateType();
    }
    public E event() {
        return event;
    }
    public Long sequence() {
        return sequence;
    }
    public Instant timestamp() {
        return timestamp;
    }

    public static <E extends DomainEvent> EventEnvelope<E> with(E event, Long sequence) {
        return new EventEnvelope<>(event, sequence, Instant.now());
    }
}
