package org.ormi.priv.tfa.orderflow.productregistry.infra.jpa;

import java.util.UUID;

import org.ormi.priv.tfa.orderflow.kernel.product.ProductLifecycle;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entité JPA représentant l'agrégat `Product` dans le schéma de domaine.
 *
 * Stocke les champs persistés : nom, description, SKU, statut et version.
 */

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table(
    schema = "domain",
    name = "products",
    indexes = {
        @Index(name = "ux_products_sku", columnList = "sku", unique = true)
    }
)
public class ProductEntity {

    private static final int LENGTH = 9;

    /** Identifiant UUID de l'entité. */
    @Id
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "uuid")
    private UUID id;

    /** Nom du produit. */
    @Column(name = "name", nullable = false, columnDefinition = "text")
    private String name;

    /** Description du produit. */
    @Column(name = "description", nullable = false, columnDefinition = "text")
    private String description;

    /** Identifiant SKU du produit (conserver la longueur maximale définie). */
    @Column(name = "sku_id", nullable = false, updatable = false, length = LENGTH, unique = true, columnDefinition = "varchar")
    private String skuId;

    /** Statut du produit (ex : ACTIVE, RETIRED). */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "text")
    private ProductLifecycle status;

    /** Version optimistic locking. */
    @Column(name = "version", nullable = false, columnDefinition = "bigint")
    private Long version;
}
