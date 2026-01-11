package org.ormi.priv.tfa.orderflow.kernel.product.persistence;

import java.util.List;
import java.util.Optional;

import org.ormi.priv.tfa.orderflow.kernel.product.ProductId;
import org.ormi.priv.tfa.orderflow.kernel.product.SkuId;
import org.ormi.priv.tfa.orderflow.kernel.product.views.ProductView;

/**
 * Répertoire d'accès pour la vue produit (read model).
 *
 * Fournit des méthodes pour sauvegarder et rechercher des `ProductView` ainsi que
 * des opérations de recherche paginée par motif de SKU.
 */

public interface ProductViewRepository {
    void save(ProductView productView);
    Optional<ProductView> findById(ProductId id);
    Optional<ProductView> findBySkuId(SkuId skuId);
    long countPaginatedViewsBySkuIdPattern(String skuIdPattern);
    List<ProductView> searchPaginatedViewsOrderBySkuId(String skuIdPattern, int page, int size);
}
