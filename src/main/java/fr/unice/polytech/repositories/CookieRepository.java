package fr.unice.polytech.repositories;

import fr.unice.polytech.entities.recipe.Cookie;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class CookieRepository extends BasicRepositoryImpl<Cookie, UUID> {
}
