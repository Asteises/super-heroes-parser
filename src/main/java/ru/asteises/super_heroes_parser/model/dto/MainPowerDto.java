package ru.asteises.super_heroes_parser.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MainPowerDto {

    private String name;

    @Override
    public String toString() {
        return "MainPowerDto{" +
                "name='" + name + '\'' +
                '}';
    }
}
