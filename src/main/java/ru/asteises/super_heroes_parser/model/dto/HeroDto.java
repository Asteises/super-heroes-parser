package ru.asteises.super_heroes_parser.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HeroDto {

    private String h1Name;
    private String solarSystem;
    private String creator;
    private String universe;
    private String fullName;
    private String aliases;
    private String placeOfBirth;
    private String firstAppearance;
    private String alignment;
    private String intelligence;
    private String strength;
    private String speed;
    private List<MainPowerDto> mainPowersDtos;
}
