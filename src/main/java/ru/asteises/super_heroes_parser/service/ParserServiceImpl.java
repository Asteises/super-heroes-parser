package ru.asteises.super_heroes_parser.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import ru.asteises.super_heroes_parser.mapper.MainPowerMapper;
import ru.asteises.super_heroes_parser.model.Hero;
import ru.asteises.super_heroes_parser.model.HeroPage;
import ru.asteises.super_heroes_parser.model.MainPower;
import ru.asteises.super_heroes_parser.model.Tab;
import ru.asteises.super_heroes_parser.model.dto.HeroDto;
import ru.asteises.super_heroes_parser.model.dto.MainPowerDto;
import ru.asteises.super_heroes_parser.storage.HeroPagesRepo;
import ru.asteises.super_heroes_parser.storage.HeroRepo;
import ru.asteises.super_heroes_parser.storage.MainPowerRepo;
import ru.asteises.super_heroes_parser.storage.TabRepo;

import java.io.File;
import java.io.IOException;
import java.rmi.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class ParserServiceImpl implements ParserService {

    private final HeroRepo heroRepo;
    private final MainPowerRepo mainPowerRepo;
    private final TabRepo tabRepo;
    private final HeroPagesRepo heroPagesRepo;

    public Long createAllHeroes() {
        List<HeroPage> heroPages = heroPagesRepo.findAll();
        List<Hero> heroes = heroRepo.findAll();
        List<String> actualLinks = new ArrayList<>();
        for (HeroPage heroPage : heroPages) {
            actualLinks.add(heroPage.getUrl());
        }
        for (Hero hero : heroes) {
            actualLinks.remove(hero.getUrl());
        }
        log.info("Осталось спарсить: " + actualLinks.size());
        for (String url : actualLinks) {
            parsePage(url);
        }
        return heroRepo.count();
    }

    // TODO ADD MAPPERS
    public HeroDto getHero(String url) {
        Hero hero = parsePage(url);
        List<MainPowerDto> mainPowerDtos = MainPowerMapper.INSTANCE.toDto(hero.getMainPowers().stream().toList());

        return HeroDto.builder()
                .h1Name(hero.getH1Name())
                .solarSystem(hero.getSolarSystem())
                .creator(hero.getCreator())
                .universe(hero.getUniverse())
                .fullName(hero.getFullName())
                .aliases(hero.getAliases())
                .placeOfBirth(hero.getPlaceOfBirth())
                .firstAppearance(hero.getFirstAppearance())
                .alignment(hero.getAlignment())
                .intelligence(hero.getIntelligence())
                .strength(hero.getStrength())
                .speed(hero.getSpeed())
                .mainPowersDtos(mainPowerDtos)
                .build();
    }

    @Override
    public Hero parsePage(String url) {
        Document document = getDocument(url);
        Hero hero = createNewHero();
        hero = parsePageData(document, hero);
        hero.setUrl(url);
        return saveHero(hero);
    }

    @Override
    public String imageParser() {
        List<Hero> heroes = heroRepo.findAllByPortraitUrlNotNullAndMainImageIsNull();
        log.info("heroes with portrait url: {}", heroes.size());
        for (Hero hero : heroes) {
            log.info("parse image for hero: {}", hero.getH1Name());
            parseMainImage(hero);
        }
        return "OK";
    }

    @Override
    public String parseMainImage(Hero hero) {
        String filePath = createImageFilePath(hero);
        try {
            Connection.Response image = Jsoup
                    .connect(hero.getPortraitUrl())
                    .ignoreContentType(true)
                    .execute();

            FileUtils.writeByteArrayToFile(new File(filePath), image.bodyAsBytes());
        } catch (IOException e) {
            if (e instanceof ConnectException) {
                try {
                    wait(5000);
                    parseMainImage(hero);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        return "imageUrl";
    }

    //src/main/resources/heroes/L/Lady_Shiva/Prime_DC_Comics_Universe/Lady_Shiva.jpg
    private String createImageFilePath(Hero hero) {
        String mainUrl = "src/main/resources/heroes/";
        String firstChar = String.valueOf(hero.getH1Name().charAt(0));
        String heroName = hero.getH1Name()
                .replaceAll(" ", "_")
                .replaceAll("[^A-Za-zА-Яа-я0-9-_]", "");
        String solarSystem;
        if (hero.getSolarSystem() != null) {
            solarSystem = hero.getSolarSystem()
                    .replace(" ", "_")
                    .replaceAll("[^a-zA-Z0-9-_]", "g");;
        } else {
            solarSystem = "unknown";
        }
        String fileName = heroName.toLowerCase() + ".jpg";
        String resultPath = mainUrl + firstChar + "/" + heroName + "/" + solarSystem + "/" + fileName;

        hero.setMainImage(resultPath);
        heroRepo.save(hero);

        log.info("result path: {}", resultPath);
        return resultPath;
    }

    public Hero createNewHero() {
        Hero hero = new Hero();
        hero.setId(UUID.randomUUID());
        return saveHero(hero);
    }

    public Hero saveHero(Hero hero) {
        heroRepo.save(hero);
        log.info("hero saved: {}", hero);
        return hero;
    }

    public Hero parsePageData(Document document, Hero hero) {
        String mainLink = "http://www.superherodb.com";

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

//        List<String> top3battlesLinks = new ArrayList<>();
//        Elements battles = elements.select("div.block.battle");
//        for (Element element : battles) {
//            String link = element.select("a").attr("href");
//            top3battlesLinks.add(mainLink + link);
//        }
//        hero.setTop3battle(top3battlesLinks);

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
        if (alignment != null) {
            hero.setAlignment(alignment.select("td:eq(1)").text());
            log.info(hero.getAlignment());
        } else {
            hero.setAlignment("unknown");
        }

        Set<Tab> tabs = parseTabs(elements, hero, mainLink);

        for (Tab tab : tabs) {
            if (tab.getTitle().contains("history")) {
                String history = parseHistory(tab.getUrl());
                hero.setContent(history);
            } else if (tab.getTitle().contains("powers")) {
                Set<MainPower> mainPowers = parseMainPowers(tab.getUrl(), hero);
                hero.setMainPowers(mainPowers);

                Map<String, String> stats = parseStats(tab.getUrl());
                hero.setIntelligence(stats.get("Intelligence"));
                hero.setStrength(stats.get("Strength"));
                hero.setSpeed(stats.get("Speed"));
            }
        }
        return hero;
    }

    public Set<Tab> parseTabs(Elements elements, Hero hero, String mainLink) {
        Set<Tab> tabSet = new HashSet<>();
        Elements tabs = elements.select("li.tab-item");
        for (Element element : tabs) {
            String link = element.select("a").attr("href");
            if (link.contains(hero.getH1Name().toLowerCase()) && !link.contains("edit")) {
                String title = element.select("a").attr("title");

                Tab tab = new Tab();
                tab.setId(UUID.randomUUID());
                tab.setHero(hero);
                tab.setUrl(mainLink + link);
                tab.setTitle(title);

                tabSet.add(tab);
            }
        }
        log.info(tabSet.toString());
        tabRepo.saveAll(tabSet);
        return tabSet;
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

    public Set<MainPower> parseMainPowers(String url, Hero hero) {
        Document document = getDocument(url);
        Elements elements = document.select("div.column.col-8.col-md-12").select("h4");
        log.info("Elements with size: {} and main powers: {}", elements.size(), elements.text());
        Set<MainPower> powers = new HashSet<>();
        for (Element element : elements) {
            log.info("Element h4: {}", element.text());
            String powerName = element.text();

            MainPower mainPower = new MainPower();
            mainPower.setId(UUID.randomUUID());
            mainPower.setName(powerName);
            mainPower.setHero(hero);

            powers.add(mainPower);
        }
        log.info("Parse main powers: {}", powers);
        mainPowerRepo.saveAll(powers);
        return powers;
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