package ru.asteises.super_heroes_parser.service;

import org.springframework.stereotype.Service;
import ru.asteises.super_heroes_parser.model.HeroPage;

import java.util.List;

@Service
public interface PaginationPagesParser {

    Long setData(String path);

    List<String> allPaginationPagesParse(String path);

    List<HeroPage> heroesByPageList(String path);
}
