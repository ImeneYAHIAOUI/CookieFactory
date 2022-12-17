package fr.unice.polytech.repositories;

import fr.unice.polytech.entities.recipe.cookies.Cookie;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class SuggestedCookieRepository extends BasicRepositoryImpl<Cookie, UUID>
{
}

