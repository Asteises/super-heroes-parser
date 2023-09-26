package ru.asteises.super_heroes_parser.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Heroes")
public class Hero {
    @Id
    private UUID id;
    private String url;

    @Column(name = "main_image")
    private String mainImage;

    private String content;

    @Column(name = "portrait_url")
    private String portraitUrl;
    @Column(name = "h_1_name")
    private String h1Name;
    @Column(name = "h_2_name")
    private String h2Name;
    @Column(name = "solar_system")
    private String solarSystem;

    private String creator;
    private String universe;

    @Column(name = "full_name")
    private String fullName;
    private String aliases;
    @Column(name = "place_of_birth")
    private String placeOfBirth;
    @Column(name = "first_appearance")
    private String firstAppearance;
    private String alignment;

    private String intelligence;
    private String strength;
    private String speed;

    @OneToMany(mappedBy = "hero")
    private Set<Tab> tabs;
    // TODO Прикрутить топ 3 батлов
    //    private List<String> top3battle;

    @OneToMany(mappedBy = "hero")
    private Set<MainPower> mainPowers;

//    @ManyToMany
//    @JoinTable(name = "Heroes_powers",
//            joinColumns = @JoinColumn(name = "hero_id"),
//            inverseJoinColumns = @JoinColumn(name = "power_id"))
//    private Set<Power> powers;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hero hero = (Hero) o;
        return id.equals(hero.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Hero{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", content='" + content + '\'' +
                ", portraitUrl='" + portraitUrl + '\'' +
                ", h1Name='" + h1Name + '\'' +
                ", h2Name='" + h2Name + '\'' +
                ", solarSystem='" + solarSystem + '\'' +
                ", creator='" + creator + '\'' +
                ", universe='" + universe + '\'' +
                ", fullName='" + fullName + '\'' +
                ", aliases='" + aliases + '\'' +
                ", placeOfBirth='" + placeOfBirth + '\'' +
                ", firstAppearance='" + firstAppearance + '\'' +
                ", alignment='" + alignment + '\'' +
                ", intelligence='" + intelligence + '\'' +
                ", strength='" + strength + '\'' +
                ", speed='" + speed + '\'' +
                ", tabs=" + tabs +
                ", mainPowers=" + mainPowers +
                '}';
    }
}
