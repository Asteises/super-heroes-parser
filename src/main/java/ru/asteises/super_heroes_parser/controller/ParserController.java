package ru.asteises.super_heroes_parser.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.asteises.super_heroes_parser.model.Page;
import ru.asteises.super_heroes_parser.service.ParserService;
import ru.asteises.super_heroes_parser.util.Endpoints;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(Endpoints.API)
public class ParserController {

    private final ParserService parserService;

    //https://www.superherodb.com/adam-strange/10-626/
    @PostMapping(Endpoints.PARSE_PAGE)
    public ResponseEntity<Page> parsePage(@RequestParam String path) {
        return new ResponseEntity<>(parserService.parsePage(path), HttpStatus.CREATED);
    }
}
