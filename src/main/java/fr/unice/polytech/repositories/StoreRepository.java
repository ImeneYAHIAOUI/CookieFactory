package fr.unice.polytech.repositories;

import fr.unice.polytech.entities.store.Store;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class StoreRepository extends BasicRepositoryImpl<Store, UUID> {
}
