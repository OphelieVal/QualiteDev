package org.ormi.priv.tfa.orderflow.productregistry.infra.api;

import java.net.URI;
import java.util.UUID;

import org.jboss.resteasy.reactive.RestResponse;
import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.write.RegisterProductCommandDto;
import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.write.UpdateProductDescriptionParamsDto;
import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.write.UpdateProductNameParamsDto;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductId;
import org.ormi.priv.tfa.orderflow.productregistry.application.ProductCommand.RetireProductCommand;
import org.ormi.priv.tfa.orderflow.productregistry.application.ProductCommand.UpdateProductDescriptionCommand;
import org.ormi.priv.tfa.orderflow.productregistry.application.ProductCommand.UpdateProductNameCommand;
import org.ormi.priv.tfa.orderflow.productregistry.application.RegisterProductService;
import org.ormi.priv.tfa.orderflow.productregistry.application.RetireProductService;
import org.ormi.priv.tfa.orderflow.productregistry.application.UpdateProductService;
import org.ormi.priv.tfa.orderflow.productregistry.infra.web.dto.CommandDtoMapper;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;

/**
 * Ressource REST exposant les API de commande pour le product-registry
 * (enregistrement, retrait et mise à jour des produits).
 */

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
public class ProductRegistryCommandResource {

    // Dépendances requises
    private final CommandDtoMapper mapper;
    private final RegisterProductService registerProductService;
    private final RetireProductService retireProductService;
    private final UpdateProductService updateProductService;

    @Inject
    public ProductRegistryCommandResource(
            final CommandDtoMapper mapper,
            final RegisterProductService registerProductService,
            final RetireProductService retireProductService,
            final UpdateProductService updateProductService) {
        this.mapper = mapper;
        this.registerProductService = registerProductService;
        this.retireProductService = retireProductService;
        this.updateProductService = updateProductService;
    }

    /**
     * Enregistre un nouveau produit.
     * @param cmd
     * @param uriInfo
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public RestResponse<Void> registerProduct(final RegisterProductCommandDto cmd,
            final @Context UriInfo uriInfo) {
        final ProductId productId = registerProductService.handle(mapper.toCommand(cmd));
        final URI location = uriInfo.getAbsolutePathBuilder()
            .path("/products/" + productId.value())
            .build();
        return RestResponse.created(URI.create(location.toString()));
    }

    /**
     * Retire un produit existant.
     * @param productId
     * @return
     */
    @DELETE
    @Path("/{id}")
    public RestResponse<Void> retireProduct(final @PathParam("id") String productId) {
        retireProductService.retire(
            new RetireProductCommand(new ProductId(UUID.fromString(productId)))
        );
        return RestResponse.noContent();
    }

    /**
     * Met à jour le nom d'un produit existant.
     * @param productId
     * @param params
     * @return
    */
    @PATCH
    @Path("/{id}/name")
    @Consumes(MediaType.APPLICATION_JSON)
    public RestResponse<Void> updateProductName(final @PathParam("id") String productId,
            final UpdateProductNameParamsDto params) {
        updateProductService.handle(
            new UpdateProductNameCommand(new ProductId(UUID.fromString(productId)), params.name())
        );
        return RestResponse.noContent();
    }

    /**
     * Met à jour la description d'un produit existant.
     * @param productId
     * @param params
     * @return
    */
    @PATCH
    @Path("/{id}/description")
    @Consumes(MediaType.APPLICATION_JSON)
    public RestResponse<Void> updateProductDescription(final @PathParam("id") String productId,
            final UpdateProductDescriptionParamsDto params) {
        updateProductService.handle(
            new UpdateProductDescriptionCommand(
                new ProductId(UUID.fromString(productId)),
                params.description()
            )
        );
        return RestResponse.noContent();
    }
}
