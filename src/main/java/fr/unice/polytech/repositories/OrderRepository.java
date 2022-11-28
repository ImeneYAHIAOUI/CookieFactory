package fr.unice.polytech.repositories;

import fr.unice.polytech.entities.order.Order;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class OrderRepository extends BasicRepositoryImpl<Order, UUID> {
}
