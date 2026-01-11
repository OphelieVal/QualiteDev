package org.ormi.priv.tfa.orderflow.kernel.product;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

/**
 * Identifiant de produit (UUID) encapsulé en tant que value object.
 *
 * Fournit une méthode utilitaire pour générer de nouveaux identifiants.
 */

public record ProductId(@NotNull UUID value) {
    public static ProductId newId() {
        return new ProductId(UUID.randomUUID());
    }
}
