package org.ormi.priv.tfa.orderflow.kernel.product;

/**
 * État de cycle de vie d'un produit.
 *
 * ACTIVE : produit disponible; RETIRED : produit retiré du catalogue.
 */

public enum ProductLifecycle {
    ACTIVE,
    RETIRED
}
