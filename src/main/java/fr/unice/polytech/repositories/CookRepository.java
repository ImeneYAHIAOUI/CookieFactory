package fr.unice.polytech.repositories;

import fr.unice.polytech.entities.store.Cook;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class CookRepository extends BasicRepositoryImpl<Cook, UUID> {
}
