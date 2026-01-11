package org.ormi.priv.tfa.orderflow.cqrs.infra.persistence;

import org.ormi.priv.tfa.orderflow.cqrs.EventEnvelope;
import org.ormi.priv.tfa.orderflow.cqrs.infra.jpa.EventLogEntity;

/**
 * Répertoire pour l'append-only event log : enregistre des enveloppes d'événements
 * sous forme d'`EventLogEntity` pour persistance et traçabilité.
 */

public interface EventLogRepository {
    EventLogEntity append(EventEnvelope<?> eventLog);
}
