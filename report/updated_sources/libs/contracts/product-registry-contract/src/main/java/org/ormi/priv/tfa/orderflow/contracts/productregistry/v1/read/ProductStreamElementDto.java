package org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.read;

import java.time.Instant;

/**
 * DTO représentant un élément du flux d'événements produit (utilisé pour le streaming).
 *
 * Contient le type d'événement, l'identifiant du produit et la date d'occurrence.
 */

public record ProductStreamElementDto(
    String type,
    String productId,
    Instant occuredAt
) {
}
