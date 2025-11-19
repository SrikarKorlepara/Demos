package com.stockstreaming.demo.mapper;


import com.stockstreaming.demo.dto.DealerGroupResponseDto;
import com.stockstreaming.demo.dto.DealerLocationCreateRequestDto;
import com.stockstreaming.demo.dto.DealerLocationRequestDto;
import com.stockstreaming.demo.dto.DealerLocationResponseDto;
import com.stockstreaming.demo.model.DealerGroup;
import com.stockstreaming.demo.model.DealerLocation;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DealerLocationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dealerGroup", ignore = true)
    DealerLocation toCreateEntity(DealerLocationCreateRequestDto dto);

    @Mapping(target = "dealerGroup", ignore = true)
    DealerLocation toEntity(DealerLocationRequestDto dto);

    @Named("simpleLocationMapper")
    @Mapping(target = "dealerGroup", ignore = true)
    DealerLocationResponseDto toSimpleResponseDto(DealerLocation dealerLocation);

    @Mapping(target = "dealerGroup", qualifiedByName = "simpleGroupMapper")
    DealerLocationResponseDto toResponseDto(DealerLocation dealerLocation);

    @Named("simpleGroupMapper")
    @Mapping(target = "dealerLocations", ignore = true)
    DealerGroupResponseDto toGroupResponseDto(DealerGroup dealerGroup);

    @Mapping(target = "dealerGroup", ignore = true)
    List<DealerLocationResponseDto> toResponseDtoList(List<DealerLocation> dealerLocations);

    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "dealerGroup", ignore = true)
    void partialUpdate(DealerLocationRequestDto dto, @org.mapstruct.MappingTarget DealerLocation dealerLocation);

}
