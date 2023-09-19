package ru.asteises.super_heroes_parser.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.asteises.super_heroes_parser.model.MainPower;
import ru.asteises.super_heroes_parser.model.dto.MainPowerDto;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.FIELD,
        imports = UUID.class)
public interface MainPowerMapper {

    MainPowerMapper INSTANCE = Mappers.getMapper(MainPowerMapper.class);

    MainPowerDto toDto(MainPower mainPower);

    List<MainPowerDto> toDto(List<MainPower> mainPowers);
}
