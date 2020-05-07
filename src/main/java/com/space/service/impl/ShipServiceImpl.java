package com.space.service.impl;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import com.space.service.exception.BadRequestException;
import com.space.service.exception.ShipNotFoundException;
import com.space.service.ShipService;
import com.space.service.specification.ShipSpecificationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.List;

@Service
public class ShipServiceImpl implements ShipService {
    private static final int CURRENT_YEAR = 3019;

    @Autowired
    private ShipRepository shipRepository;

    @Override
    public List<Ship> getAllShips(String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed, Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating, ShipOrder order, Integer pageNumber, Integer pageSize) {
        Specification specification = ShipSpecificationBuilder.getSpecification(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);
        return shipRepository.findAll(specification, PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()))).getContent();
    }

    @Override
    public Long getCountOfShips(String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed, Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating) {
        Specification specification = ShipSpecificationBuilder.getSpecification(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);
        return shipRepository.count(specification);
    }

    @Override
    public Ship getShip(String id) {
        Long lonId = parseId(id);
        return shipRepository.findById(lonId).orElseThrow(() -> new ShipNotFoundException(id));
    }

    @Override
    public Ship addShip(Ship ship) {
        checkData(ship);
        if (ship.getUsed() == null) ship.setUsed(false);
        setRating(ship);
        return shipRepository.save(ship);
    }

    @Override
    public Ship updateShip(String id, Ship newShip) {
        Ship currentShip = getShip(id);
        if (newShip.getName() != null) currentShip.setName(newShip.getName());
        if (newShip.getPlanet() != null) currentShip.setPlanet(newShip.getPlanet());
        if (newShip.getShipType() != null) currentShip.setShipType(newShip.getShipType());
        if (newShip.getProdDate() != null) currentShip.setProdDate(newShip.getProdDate());
        if (newShip.getUsed() != null) currentShip.setUsed(newShip.getUsed());
        if (newShip.getSpeed() != null) currentShip.setSpeed(newShip.getSpeed());
        if (newShip.getCrewSize() != null) currentShip.setCrewSize(newShip.getCrewSize());
        checkData(currentShip);
        setRating(currentShip);
        return shipRepository.save(currentShip);
    }

    @Override
    public void delete (String id) {
        shipRepository.delete(getShip(id));
    }

    private boolean checkId (Long id) {
        return id > 0;
    }

    //Return true if all good
    private boolean checkString (String s) {
        return s != null && !s.isEmpty();
    }

    private Long parseId (String id) {
        Long longId = 0L;
        if (checkString(id) && !id.contains(",") && !id.contains(".")) {
            try {
                longId = Long.parseLong(id);
                if (checkId(longId)) return longId;
            }
            catch (NumberFormatException e) { }
        }
        throw  new BadRequestException("ID doesn't correct.");
    }

    private  void setRating (Ship ship) {
        //Получаем год выпуска
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ship.getProdDate());
        int year = calendar.get(Calendar.YEAR);

        //Получаем коэфф.
        double k = ship.getUsed() ? 0.5 : 1;

        //Считаем рейтинг
        BigDecimal rating = new BigDecimal((80 * ship.getSpeed() * k) / (CURRENT_YEAR - year + 1));

        //Округляем до сотых
        rating = rating.setScale(2, RoundingMode.HALF_UP);

        //Устанавливаем рейтинг
        ship.setRating(rating.doubleValue());
    }

    private void checkData (Ship ship) {
        if (!checkString(ship.getName()) || ship.getName().length() > 50)
            throw new BadRequestException("Incorrect Ship.name");

        if (!checkString(ship.getPlanet()) || ship.getPlanet().length() > 50)
            throw new BadRequestException("Incorrect Ship.planet");

        if (ship.getCrewSize() == null || ship.getCrewSize() < 1 || ship.getCrewSize() > 9999)
            throw new BadRequestException("Incorrect Ship.crewSize");

        if (ship.getSpeed() == null || ship.getSpeed() < 0.01D || ship.getSpeed() > 0.99D)
            throw new BadRequestException("Incorrect Ship.speed");

        if (ship.getProdDate() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(ship.getProdDate());
            if (cal.get(Calendar.YEAR) < 2800 || cal.get(Calendar.YEAR) > CURRENT_YEAR)
                throw new BadRequestException("Incorrect Ship.date");
        }
    }
}
