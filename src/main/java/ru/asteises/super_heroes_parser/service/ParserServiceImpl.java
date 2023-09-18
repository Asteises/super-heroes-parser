package ru.asteises.super_heroes_parser.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import ru.asteises.super_heroes_parser.model.Hero;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ParserServiceImpl implements ParserService {

    @Override
    public Hero parsePage(String url) {
        Document document = getDocument(url);
        Hero hero = parsePageData(document);
        hero.setUrl(url);
        return hero;
    }

    public Hero parsePageData(Document document) {
        Hero hero = new Hero();
        String mainLink = "http://www.superherodb.com";
        hero.setId(UUID.randomUUID());

        Elements elements = document.getAllElements();

        Elements divH = elements.select("div.column.col-10.col-md-9.col-sm-12");
        String h1 = divH.select("h1").text();
        String h2 = divH.select("h2").text();
        String h3 = divH.select("h3").text();

        if (!h1.isEmpty()) {
            hero.setH1Name(h1);
        }
        if (!h2.isEmpty()) {
            hero.setH2Name(h2);
        }
        if (!h3.isEmpty()) {
            hero.setSolarSystem(h3);
        }

        Elements divPortrait = elements.select("div.portrait.user");
        String portraitUrl = divPortrait.select("img").attr("src");
        if (!portraitUrl.isEmpty()) {
            hero.setPortraitUrl(mainLink + portraitUrl);
        }

        List<String> top3battlesLinks = new ArrayList<>();
        Elements battles = elements.select("div.block.battle");
        for (Element element : battles) {
            String link = element.select("a").attr("href");
            top3battlesLinks.add(mainLink + link);
        }
//        hero.setTop3battle(top3battlesLinks);

        Map<String, String> tabsMap = new HashMap<>();
        Elements tabs = elements.select("li.tab-item");
        for (Element element : tabs) {
            String link = element.select("a").attr("href");
            if (link.contains(h1.toLowerCase()) && !link.contains("edit")) {
                String title = element.select("a").attr("title");
                tabsMap.put(title, mainLink + link);
            }
        }
        hero.setTabs(tabsMap.values());

        String history = parseHistory(tabsMap.get("history"));
        hero.setContent(history);

        List<String> powers = parsePowers(tabsMap.get("powers"));
        hero.setPowers(powers);

        Map<String, String> stats = parseStats(tabsMap.get("powers"));
        hero.setIntelligence(stats.get("Intelligence"));
        hero.setStrength(stats.get("Strength"));
        hero.setSpeed(stats.get("Speed"));

        Elements originInfo = elements
                .select("div.column.col-8.col-md-7.col-sm-12")
                .select("table.table.profile-table")
                .select("tbody");

        Element creator = originInfo.select("tr:eq(0)").first();
        if (creator != null) {
            hero.setCreator(creator.select("td:eq(1)").text());
            log.info(hero.getCreator());
        }

        Element universe = originInfo.select("tr:eq(1)").first();
        if (universe != null) {
            hero.setUniverse(universe.select("td:eq(1)").text());
            log.info(hero.getUniverse());
        }

        Element fullName = originInfo.select("tr:eq(2)").first();
        if (fullName != null) {
            hero.setFullName(fullName.select("td:eq(1)").text());
            log.info(hero.getFullName());
        }

        Element aliases = originInfo.select("tr:eq(4)").first();
        if (aliases != null) {
            hero.setAliases(aliases.select("td:eq(1)").text());
            log.info(hero.getAliases());
        }

        Element placeOfBirth = originInfo.select("tr:eq(5)").first();
        if (placeOfBirth != null) {
            hero.setPlaceOfBirth(placeOfBirth.select("td:eq(1)").text());
            log.info(hero.getPlaceOfBirth());
        }

        Element firstAppearance = originInfo.select("tr:eq(6)").first();
        if (firstAppearance != null) {
            hero.setFirstAppearance(firstAppearance.select("td:eq(1)").text());
            log.info(hero.getFirstAppearance());
        }

        Element alignment = originInfo.select("tr:eq(7)").first();
        if (aliases != null) {
            hero.setAlignment(alignment.select("td:eq(1)").text());
            log.info(hero.getAlignment());
        }

        return hero;
    }

    // TODO Нужно красиво распарсить текст
    public String parseHistory(String url) {
        Document document = getDocument(url);
//        document.outputSettings(new Document.OutputSettings().prettyPrint(false));
//        document.select("br").append("\\n");
//        document.select("p").prepend("\\n\\n");
//        String s = document.html().replaceAll("\\\\n", "\n");
//        return Jsoup.clean(s, "", Safelist.none(), new Document.OutputSettings().prettyPrint(false));
        Elements elements = document.getAllElements();
        return elements.select("div.column.col-12.text-columns-2").text();
    }

    public List<String> parsePowers(String url) {
        Document document = getDocument(url);
        Elements elements = document.select("div.column.col-8.col-md-12");
        List<String> powersNames = new ArrayList<>();
        for (Element element : elements.select("h4")) {
            String powerName = element.text();
            powersNames.add(powerName);
        }
        return powersNames;
    }

    public Map<String, String> parseStats(String url) {
        Map<String, String> statsMap = new HashMap<>();
        String intValue = "";
        String strValue = "";
        String speedValue = "";

        Document document = getDocument(url);
        Elements stats = document.select("table.table.profile-table").select("tbody");

        Element intelligence = stats.select("tr:eq(0)").first();
        if (intelligence != null) {
            intValue = intelligence.select("td:eq(1)").text();
            statsMap.put("Intelligence", intValue);
        }

        Element strength = stats.select("tr:eq(1)").first();
        if (strength != null) {
            strValue = strength.select("td:eq(1)").text();
            statsMap.put("Strength", strValue);
        }

        Element speed = stats.select("tr:eq(2)").first();
        if (speed != null) {
            speedValue = speed.select("td:eq(1)").text();
            statsMap.put("Speed", speedValue);
        }
        log.info(intValue);
        log.info(strValue);
        log.info(speedValue);
        return statsMap;
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