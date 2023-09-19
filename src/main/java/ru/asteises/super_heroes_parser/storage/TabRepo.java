package ru.asteises.super_heroes_parser.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.asteises.super_heroes_parser.model.Tab;

import java.util.UUID;

@Repository
public interface TabRepo extends JpaRepository<Tab, UUID> {
}
