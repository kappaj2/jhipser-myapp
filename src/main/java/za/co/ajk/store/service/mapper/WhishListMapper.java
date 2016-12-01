package za.co.ajk.store.service.mapper;

import za.co.ajk.store.domain.*;
import za.co.ajk.store.service.dto.WhishListDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity WhishList and its DTO WhishListDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface WhishListMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    WhishListDTO whishListToWhishListDTO(WhishList whishList);

    List<WhishListDTO> whishListsToWhishListDTOs(List<WhishList> whishLists);

    @Mapping(source = "userId", target = "user")
    WhishList whishListDTOToWhishList(WhishListDTO whishListDTO);

    List<WhishList> whishListDTOsToWhishLists(List<WhishListDTO> whishListDTOs);
}
