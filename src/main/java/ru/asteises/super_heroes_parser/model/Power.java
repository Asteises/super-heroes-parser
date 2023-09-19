package ru.asteises.super_heroes_parser.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "Powers")
public class Power {

    @Id
    private UUID id;
    private String url;
    private String name;
    // TODO Enum
    private String tier;
    // TODO Enum
    private String score;
    private String description;

//    @ManyToMany(mappedBy = "powers")
//    private Set<Hero> heroes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Power power = (Power) o;
        return id.equals(power.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Power{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", tier='" + tier + '\'' +
                ", score='" + score + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
