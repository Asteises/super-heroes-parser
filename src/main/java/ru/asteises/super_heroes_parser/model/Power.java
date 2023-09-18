package ru.asteises.super_heroes_parser.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
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
    // TODO Enum
    private String tier;
    // TODO Enum
    private String score;
    private String description;
    @ManyToMany(mappedBy = "powers")
    private Set<Hero> heroes;
}
