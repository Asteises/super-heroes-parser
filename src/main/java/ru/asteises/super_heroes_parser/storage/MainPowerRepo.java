package ru.asteises.super_heroes_parser.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.asteises.super_heroes_parser.model.MainPower;

import java.util.UUID;

@Repository
public interface MainPowerRepo extends JpaRepository<MainPower, UUID> {
}
