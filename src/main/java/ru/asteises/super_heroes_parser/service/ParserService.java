package ru.asteises.super_heroes_parser.service;

import org.springframework.stereotype.Service;
import ru.asteises.super_heroes_parser.model.Hero;

@Service
public interface ParserService {

    Hero parsePage(String url);

}
