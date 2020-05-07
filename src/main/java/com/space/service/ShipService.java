package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;

import java.util.List;

public interface ShipService {

    List<Ship> getAllShips(
            String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed, Double minSpeed, Double maxSpeed,
            Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating, ShipOrder order, Integer pageNumber, Integer pageSize);

    Long getCountOfShips(
            String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed, Double minSpeed, Double maxSpeed,
            Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating);

    Ship getShip(String id);

    Ship addShip(Ship ship);

    Ship updateShip(String id, Ship ship);

    void delete(String id);
}
