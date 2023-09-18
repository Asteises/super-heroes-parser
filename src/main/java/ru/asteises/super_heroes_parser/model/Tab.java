package ru.asteises.super_heroes_parser.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Tabs")
public class Tab {

    @Id
    private UUID id;
    private String url;
    @ManyToOne
    @JoinColumn(name = "hero_page_id", nullable = false)
    private Hero hero;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tab tab = (Tab) o;
        return id.equals(tab.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
