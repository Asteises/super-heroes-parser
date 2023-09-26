package ru.asteises.super_heroes_parser.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.asteises.super_heroes_parser.model.HeroPage;
import ru.asteises.super_heroes_parser.model.dto.HeroDto;
import ru.asteises.super_heroes_parser.service.PaginationPagesParser;
import ru.asteises.super_heroes_parser.service.ParserService;
import ru.asteises.super_heroes_parser.util.Endpoints;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(Endpoints.API)
public class ParserController {

    private final ParserService parserService;
    private final PaginationPagesParser pagesParser;

    @PostMapping(Endpoints.PARSE_ALL_HEROES)
    public ResponseEntity<Long> parseAllHeroes() {
        return new ResponseEntity<>(parserService.createAllHeroes(), HttpStatus.CREATED);
    }

    @PostMapping(Endpoints.PARSE_PAGE)
    public ResponseEntity<HeroDto> parsePage(@RequestParam String path) {
        return new ResponseEntity<>(parserService.getHero(path), HttpStatus.CREATED);
    }

    @PostMapping(Endpoints.PARSE_PAGINATION_PAGE)
    public ResponseEntity<List<String>> parsePaginationPage(@RequestParam String path) {
        return new ResponseEntity<>(pagesParser.allPaginationPagesParse(path), HttpStatus.CREATED);
    }

    @PostMapping(Endpoints.PARSE_HEROES_PAGINATION_PAGE)
    public ResponseEntity<List<HeroPage>> parseHeroesPaginationPages(@RequestParam String path) {
        return new ResponseEntity<>(pagesParser.heroesByPageList(path), HttpStatus.CREATED);
    }

    @PostMapping(Endpoints.PARSE_HEROES_PAGES)
    public ResponseEntity<Long> parseHeroesPages(@RequestParam String path) {
        return new ResponseEntity<>(pagesParser.setData(path), HttpStatus.CREATED);
    }

    @PostMapping(Endpoints.PARSE_HERO_MAIN_IMAGE)
    public ResponseEntity<String> parseHeroMainImage() {
        return new ResponseEntity<>(parserService.imageParser(), HttpStatus.CREATED);
    }
}
