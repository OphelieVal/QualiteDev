package org.ormi.priv.tfa.orderflow.productregistry.infra.jpa;

import java.util.Optional;
import java.util.UUID;

import org.ormi.priv.tfa.orderflow.kernel.Product;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductId;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductIdMapper;
import org.ormi.priv.tfa.orderflow.kernel.product.SkuId;
import org.ormi.priv.tfa.orderflow.kernel.product.SkuIdMapper;
import org.ormi.priv.tfa.orderflow.kernel.product.persistence.ProductRepository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

/**
 * Implémentation JPA du repository `Product` utilisant Panache.
 * Gère la conversion entre l'entité JPA et l'agrégat métier et fournit
 * les opérations de recherche et de persistance.
 */

@ApplicationScoped
public class JpaProductRepository implements PanacheRepositoryBase<ProductEntity, UUID>, ProductRepository {

    private ProductJpaMapper mapper;
    private ProductIdMapper productIdMapper;    
    private SkuIdMapper skuIdMapper;

    @Inject
    public JpaProductRepository(final ProductJpaMapper mapperParam,
            final ProductIdMapper productIdMapperParam,
            final SkuIdMapper skuIdMapperParam) {
        this.mapper = mapperParam;
        this.productIdMapper = productIdMapperParam;
        this.skuIdMapper = skuIdMapperParam;
    }

    public ProductJpaMapper getMapper() {
        return mapper;
    }

    public void setMapper(final ProductJpaMapper mapper) {
        this.mapper = mapper;
    }

    public ProductIdMapper getProductIdMapper() {
        return productIdMapper;
    }

    public void setProductIdMapper(final ProductIdMapper productIdMapper) {
        this.productIdMapper = productIdMapper;
    }

    public SkuIdMapper getSkuIdMapper() {
        return skuIdMapper;
    }

    public void setSkuIdMapper(final SkuIdMapper skuIdMapper) {
        this.skuIdMapper = skuIdMapper;
    }

    /**
     * Sauvegarde ou met à jour un produit dans la base de données.
     * 
     * @param product
     * @throws IllegalArgumentException 
     * @throws PersistenceException
     */
    @Override
    @Transactional
    public void save(final Product product) {
        findByIdOptional(productIdMapper.map(product.getId()))
                .ifPresentOrElse(e -> {
                    mapper.updateEntity(product, e);
                }, () -> {
                    ProductEntity newEntity = mapper.toEntity(product);
                    getEntityManager().merge(newEntity);
                });
    }

    /**
     * Recherche un produit par son identifiant.
     * @param id
     * @return
     */
    @Override
    public Optional<Product> findById(final ProductId id) {
        return findByIdOptional(productIdMapper.map(id))
                .map(mapper::toDomain);
    }

    /**
     * Vérifie l'existence d'un produit par son SKU.
     * @param skuId
     * @return
     */
    @Override
    public boolean existsBySkuId(final SkuId skuId) {
        return count("skuId", skuIdMapper.map(skuId)) > 0;
    }
}
