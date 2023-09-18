package ru.asteises.super_heroes_parser.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Powers {

    private UUID id;
    private String url;

    // TODO Enum
    private String tier;
    // TODO Enum
    private String score;
    private String description;

}
