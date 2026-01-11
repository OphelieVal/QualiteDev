package org.ormi.priv.tfa.orderflow.productregistry.infra.jpa;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.ormi.priv.tfa.orderflow.kernel.Product;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductIdMapper;
import org.ormi.priv.tfa.orderflow.kernel.product.SkuIdMapper;

/**
 * MapStruct mapper pour convertir entre l'entité JPA et l'agrégat de domaine.
 *
 * Fournit des méthodes pour :
 * - créer l'entité depuis le domaine (`toEntity`),
 * - mettre à jour une entité existante (`updateEntity`),
 * - recréer l'agrégat domaine depuis l'entité (`toDomain`).
 */

@Mapper(
    componentModel = "cdi",
    builder = @Builder(disableBuilder = false),
    uses = { ProductIdMapper.class, SkuIdMapper.class },
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class ProductJpaMapper {
    /**
     * Convertit une entité JPA en agrégat de domaine.
     * 
     * @param entity
     * @return Product
     */
    public abstract Product toDomain(ProductEntity entity);
    
    /**
     * Met à jour une entité JPA existante avec les données de l'agrégat de domaine.
     * 
     * @param product
     * @param entity
     */
    public abstract void updateEntity(Product product, @MappingTarget ProductEntity entity);

    /**
     * Convertit un agrégat de domaine en entité JPA.
     * 
     * @param product
     * @return ProductEntity
     */
    public abstract ProductEntity toEntity(Product product);
}
