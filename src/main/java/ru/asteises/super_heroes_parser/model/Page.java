package ru.asteises.super_heroes_parser.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Page {

    private UUID id;
    private String url;

    private String content;

    private String portraitUrl;
    private String h1Name;
    private String h2Name;
    private String solarSystem;
    private List<String> top3battle;
    private List<String> tabs;

    private String intelligence;
    private String strength;
    private String speed;

    private List<String> powers;
}
