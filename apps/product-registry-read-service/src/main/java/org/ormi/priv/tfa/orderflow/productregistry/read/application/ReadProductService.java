package org.ormi.priv.tfa.orderflow.productregistry.read.application;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.read.ProductStreamElementDto;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductId;
import org.ormi.priv.tfa.orderflow.kernel.product.persistence.ProductViewRepository;
import org.ormi.priv.tfa.orderflow.kernel.product.views.ProductView;

import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Service de lecture exposant les opérations sur les vues produit : recherche,
 * récupération par id et streaming d'événements liés aux produits.
 */

@ApplicationScoped
public class ReadProductService {

    private final ProductViewRepository repository;
    private final ProductEventBroadcaster productEventBroadcaster;

    @Inject
    public ReadProductService(
        final ProductViewRepository repository,
        final ProductEventBroadcaster productEventBroadcaster) {
        this.repository = repository;
        this.productEventBroadcaster = productEventBroadcaster;
    }

    public Optional<ProductView> findById(final ProductId productId) {
        return repository.findById(productId);
    }

    public SearchPaginatedResult searchProducts(final String skuIdPattern, final int page, final int size) {
        return new SearchPaginatedResult(
                repository.searchPaginatedViewsOrderBySkuId(skuIdPattern, page, size),
                repository.countPaginatedViewsBySkuIdPattern(skuIdPattern));
    }

    public Multi<ProductStreamElementDto> streamProductEvents(final ProductId productId) {
        return productEventBroadcaster.stream()
                .select().where(e -> e.productId().equals(productId.value().toString()));
    }

    public Multi<ProductStreamElementDto> streamProductListEvents(final String skuIdPattern, final int page, final int size) {
        final List<ProductView> products = searchProducts(skuIdPattern, page, size).page();
        final List<UUID> productIds = products.stream()
                .map(p -> p.getId().value())
                .toList();
        return productEventBroadcaster.stream()
                .select().where(e -> productIds.contains(UUID.fromString(e.productId())));
    }

    public record SearchPaginatedResult(List<ProductView> page, long total) {
    }
}
