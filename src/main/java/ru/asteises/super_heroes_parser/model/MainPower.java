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
@Table(name = "Main_powers")
public class MainPower {

    @Id
    private UUID id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "hero_id", nullable = false)
    private Hero hero;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MainPower mainPower = (MainPower) o;
        return id == mainPower.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "MainPower{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", hero=" + hero +
                '}';
    }
}
