package fr.unice.polytech.repositories;

import fr.unice.polytech.entities.client.Client;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class ClientRepository extends BasicRepositoryImpl<Client, UUID> {
}
