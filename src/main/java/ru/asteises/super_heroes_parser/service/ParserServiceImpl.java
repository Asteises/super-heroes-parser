package ru.asteises.super_heroes_parser.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import ru.asteises.super_heroes_parser.model.Page;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class ParserServiceImpl implements ParserService {

    @Override
    public Page parsePage(String url) {
        Document document = getDocument(url);
        return parsePageData(document);
    }

    public Page parsePageData(Document document) {
        Page page = new Page();
        String mainLink = "http://www.superherodb.com";
        page.setId(UUID.randomUUID());

        Elements elements = document.getAllElements();

        Elements divH = elements.select("div.column.col-10.col-md-9.col-sm-12");
        String h1 = divH.select("h1").text();
        String h2 = divH.select("h2").text();
        String h3 = divH.select("h3").text();

        if (!h1.isEmpty()) {
            page.setH1Name(h1);
        }
        if (!h2.isEmpty()) {
            page.setH2Name(h2);
        }
        if (!h3.isEmpty()) {
            page.setSolarSystem(h3);
        }

        Elements divPortrait = elements.select("div.portrait.user");
        String portraitUrl = divPortrait.select("img").attr("src");
        if (!portraitUrl.isEmpty()) {
            page.setPortraitUrl(mainLink + portraitUrl);
        }

        List<String> top3battlesLinks = new ArrayList<>();
        Elements battles = elements.select("div.block.battle");
        for (Element element : battles) {
            String link = element.select("a").attr("href");
            top3battlesLinks.add(mainLink + link);
        }
        page.setTop3battle(top3battlesLinks);

        Map<String, String> tabsMap = new HashMap<>();
        Elements tabs = elements.select("li.tab-item");
        for (Element element : tabs) {
            String link = element.select("a").attr("href");
            if (link.contains(h1.toLowerCase()) && !link.contains("edit")) {
                String title = element.select("a").attr("title");
                tabsMap.put(title, mainLink + link);
            }
        }
        page.setTabs(tabsMap.values().stream().toList());

        String history = parseHistory(tabsMap.get("history"));
        page.setContent(history);

        List<String> powers = parsePowers(tabsMap.get("powers"));
        page.setPowers(powers);

        Map<String, String> stats = parseStats(tabsMap.get("powers"));
        page.setIntelligence(stats.get("Intelligence"));
        page.setStrength(stats.get("Strength"));
        page.setSpeed(stats.get("Speed"));

        return page;
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