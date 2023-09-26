package ru.asteises.super_heroes_parser.service;

import org.springframework.stereotype.Service;
import ru.asteises.super_heroes_parser.model.Hero;
import ru.asteises.super_heroes_parser.model.dto.HeroDto;

import java.util.List;

@Service
public interface ParserService {

    Long createAllHeroes();

    HeroDto getHero(String url);

    Hero parsePage(String url);

    String imageParser();

    String parseMainImage(Hero hero);

}
