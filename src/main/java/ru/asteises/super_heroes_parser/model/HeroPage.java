package ru.asteises.super_heroes_parser.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Hero_page")
public class HeroPage {

    @Id
    private UUID id;
    private String url;
    private String title;
    @Column(name = "suffix_level_1")
    private String suffixLevel1;
    @Column(name = "suffix_level_2")
    private String suffixLevel2;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HeroPage heroPage = (HeroPage) o;
        return id.equals(heroPage.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "HeroPage{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", suffixLevel1='" + suffixLevel1 + '\'' +
                ", suffixLevel2='" + suffixLevel2 + '\'' +
                '}';
    }
}
