package org.ormi.priv.tfa.orderflow.productregistry.read.application;

import org.ormi.priv.tfa.orderflow.kernel.product.ProductId;

/**
 * Représente des requêtes applicatives liées aux produits (lecture).
 *
 * Définit les types de requêtes supportées : récupération par id, liste paginée,
 * recherche par motif SKU.
 */

public sealed interface ProductQuery {
    public record GetProductByIdQuery(ProductId productId) implements ProductQuery {
    }

    public record ListProductQuery(int page, int size) implements ProductQuery {
    }

    public record ListProductBySkuIdPatternQuery(String skuIdPattern, int page, int size) implements ProductQuery {
    }
}
