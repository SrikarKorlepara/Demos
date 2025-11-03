package com.stockstreaming.demo.mapper;

import com.stockstreaming.demo.dto.DealerGroupCreateRequestDto;
import com.stockstreaming.demo.dto.DealerGroupRequestDto;
import com.stockstreaming.demo.dto.DealerGroupResponseDto;
import com.stockstreaming.demo.model.DealerGroup;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DealerGroupMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dealerLocations", ignore = true)
    DealerGroup toCreateEntity(DealerGroupCreateRequestDto dto);

    DealerGroupResponseDto toResponseDto(DealerGroup dealerGroup);

    List<DealerGroupResponseDto> toResponseDtoList(List<DealerGroup> dealerGroups);

    DealerGroup toEntity(DealerGroupRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(DealerGroupRequestDto dto, @MappingTarget DealerGroup dealerGroup);
}
