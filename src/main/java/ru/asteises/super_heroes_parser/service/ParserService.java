package ru.asteises.super_heroes_parser.service;

import org.springframework.stereotype.Service;
import ru.asteises.super_heroes_parser.model.Hero;
import ru.asteises.super_heroes_parser.model.dto.HeroDto;

@Service
public interface ParserService {

    HeroDto getHero(String url);
    Hero parsePage(String url);

}
