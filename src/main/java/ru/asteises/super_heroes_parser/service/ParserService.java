package ru.asteises.super_heroes_parser.service;

import org.springframework.stereotype.Service;
import ru.asteises.super_heroes_parser.model.Page;

@Service
public interface ParserService {

    Page parsePage(String url);

}
