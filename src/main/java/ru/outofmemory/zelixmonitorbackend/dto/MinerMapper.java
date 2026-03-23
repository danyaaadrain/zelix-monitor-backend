package ru.outofmemory.zelixmonitorbackend.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.outofmemory.zelixmonitorbackend.miner.BaseMiner;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface MinerMapper {
    @Mapping(target = "minerType", source = "type.name")
    MinerResponseDto toMinerResponseDto(BaseMiner miner);
}
