package com.stockstreaming.demo.mapper;

import com.stockstreaming.demo.dto.DealerGroupCreateRequestDto;
import com.stockstreaming.demo.dto.DealerGroupRequestDto;
import com.stockstreaming.demo.dto.DealerGroupResponseDto;
import com.stockstreaming.demo.model.DealerGroup;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring" , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DealerGroupMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dealerLocations", ignore = true)
    DealerGroup toCreateEntity(DealerGroupCreateRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "businessId", ignore = true)
    @Mapping(target = "dealerLocations", ignore = true)
    DealerGroup toEntity(DealerGroupRequestDto dto);

    DealerGroupResponseDto toResponseDto(DealerGroup dealerGroup);

    List<DealerGroupResponseDto> toResponseDtoList(List<DealerGroup> dealerGroups);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(DealerGroupRequestDto dto, @MappingTarget DealerGroup dealerGroup);

    @Mapping(target = "businessId", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateEntity(DealerGroupRequestDto dto, @MappingTarget DealerGroup dealerGroup);
}
