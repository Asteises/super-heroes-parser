package ru.asteises.super_heroes_parser.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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

    private String content;

    private String portraitUrl;
    private String h1Name;
    private String h2Name;
    private String solarSystem;

    private String creator;
    private String universe;

    private String fullName;
    private String aliases;
    private String placeOfBirth;
    private String firstAppearance;
    private String alignment;

    private String intelligence;
    private String strength;
    private String speed;

    @OneToMany(mappedBy = "hero")
    private Set<Tab> tabs;
    // TODO Прикрутить топ 3 батлов
    //    private List<String> top3battle;
    @ManyToMany
    @JoinTable(name = "Heroes_powers",
            joinColumns = @JoinColumn(name = "hero_id"),
            inverseJoinColumns = @JoinColumn(name = "power_id"))
    private Set<Power> powers;

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
}
