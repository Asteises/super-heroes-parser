package ru.asteises.super_heroes_parser.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import ru.asteises.super_heroes_parser.model.HeroPage;
import ru.asteises.super_heroes_parser.storage.HeroPagesRepo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaginationPagesParserImpl implements PaginationPagesParser {

    private final HeroPagesRepo heroPagesRepo;
    @Override
    public List<String> allPaginationPagesParse(String path) {
        String mainLink = "http://www.superherodb.com/characters/";
        Map<String, String> pagesMap = new HashMap<>();
        Document document = getDocument(path);
        Elements elements = document.getAllElements();

        Elements paginationPages = elements.select("ul.pagination.text-center");
        Element firstPageData = paginationPages.select("li.page-item.active").first();
        pagesMap.put(firstPageData.text(), mainLink + firstPageData.select("a").attr("href"));

        Elements otherPages = paginationPages.select("li.page-item");
        for (Element element : otherPages) {
            String href = element.select("a").attr("href");
            if (href.contains("page")) {
                pagesMap.put(element.text(), mainLink + href);
            }
        }
        return pagesMap.values().stream().toList();
    }

    public Long setData(String path) {
        List<String> urls = allPaginationPagesParse(path);

        for (String url: urls) {
            if (url != null && !url.isEmpty()) {
                heroesByPageList(url);
            }
        }
        return heroPagesRepo.count();
    }

    public List<HeroPage> heroesByPageList(String path) {
        String mainLink = "http://www.superherodb.com";
        List<HeroPage> heroPages = new ArrayList<>();
        Document document = getDocument(path);
        Elements elements = document.getAllElements();
        Elements nextElements = elements.select("div.column.col-12").select("ul.list.list-md");
        for (Element element: nextElements.select("li")) {
            String href = element.select("a").attr("href");
            String title = element.select("a").attr("title");
            String suffixLevel1 = element.select("a").select("span.suffix.level-1").text();
            String suffixLevel2 = element.select("a").select("span.suffix.level-2").text();
            log.info(href);
            log.info(title);
            log.info(suffixLevel1);
            log.info(suffixLevel2);

            HeroPage heroPage = HeroPage.builder()
                    .id(UUID.randomUUID())
                    .url(mainLink + href)
                    .title(title)
                    .suffixLevel1(suffixLevel1)
                    .suffixLevel2(suffixLevel2)
                    .build();
            heroPages.add(heroPage);
        }
        heroPagesRepo.saveAll(heroPages);
        return heroPages;
    }

    private Document getDocument(String url) {
        Document document;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return document;
    }
}
