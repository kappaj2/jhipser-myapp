package za.co.ajk.store.service.mapper;

import za.co.ajk.store.domain.*;
import za.co.ajk.store.service.dto.AndreDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Andre and its DTO AndreDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AndreMapper {

    AndreDTO andreToAndreDTO(Andre andre);

    List<AndreDTO> andresToAndreDTOs(List<Andre> andres);

    Andre andreDTOToAndre(AndreDTO andreDTO);

    List<Andre> andreDTOsToAndres(List<AndreDTO> andreDTOs);
}
