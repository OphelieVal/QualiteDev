package org.ormi.priv.tfa.orderflow.productregistry.infra.web.dto;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.ormi.priv.tfa.orderflow.productregistry.application.ProductCommand.RegisterProductCommand;
import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.write.RegisterProductCommandDto;
import org.ormi.priv.tfa.orderflow.kernel.product.SkuIdMapper;

/**
 * MapStruct mapper convertissant entre le DTO HTTP et la commande de domaine
 * `RegisterProductCommand`.
 */

@Mapper(
    componentModel = "cdi",
    builder = @Builder(disableBuilder = true),
    uses = { SkuIdMapper.class },
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CommandDtoMapper {
    
    /**
     * Convertit un DTO HTTP en commande de domaine.
     * @param dto
     * @return
     */
    RegisterProductCommand toCommand(RegisterProductCommandDto dto);

    /**
     * Convertit une commande de domaine en DTO HTTP.
     * @param command
     * @return
     */
    RegisterProductCommandDto toDto(RegisterProductCommand command);
}
