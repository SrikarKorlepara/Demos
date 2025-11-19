package com.stockstreaming.demo.mapper;

import com.stockstreaming.demo.dto.DealerGroupCreateRequestDto;
import com.stockstreaming.demo.dto.DealerGroupRequestDto;
import com.stockstreaming.demo.dto.DealerGroupResponseDto;
import com.stockstreaming.demo.model.DealerGroup;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring" , uses = DealerLocationMapper.class , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DealerGroupMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dealerLocations", ignore = true)
    DealerGroup toCreateEntity(DealerGroupCreateRequestDto dto);

    @Mapping(target = "dealerLocations", ignore = true)
    DealerGroup toEntity(DealerGroupRequestDto dto);

    @Mapping(target = "dealerLocations", qualifiedByName = "simpleLocationMapper")
    DealerGroupResponseDto toResponseDto(DealerGroup dealerGroup);

    @Named("simpleGroupMapper")
    @Mapping(target = "dealerLocations", ignore = true)
    DealerGroupResponseDto toSimpleResponseDto(DealerGroup dealerGroup);

    @Mapping(target = "dealerLocations", ignore = true)
    List<DealerGroupResponseDto> toResponseDtoList(List<DealerGroup> dealerGroups);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "dealerLocations", ignore = true)
    void partialUpdate(DealerGroupRequestDto dto, @MappingTarget DealerGroup dealerGroup);
}
