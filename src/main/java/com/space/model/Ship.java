package com.space.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ship")
public class Ship {
    @Id //Указатель на PRIMARY KEY
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Стратегия генерации первичных ключей
    @Column(name = "id") //необязательно, если имя колонки совпадает с именем поля
    private Long id;

    @Column(name = "name") //необязательно, если имя колонки совпадает с именем поля
    private String name;

    @Column(name = "planet") //необязательно, если имя колонки совпадает с именем поля
    private String planet;

    @Enumerated(EnumType.STRING)
    @Column(name = "shipType") //необязательно, если имя колонки совпадает с именем поля
    private ShipType shipType;

    @Temporal(TemporalType.DATE)
    @Column(name = "prodDate") //необязательно, если имя колонки совпадает с именем поля
    private Date prodDate;

    @Column(name = "isUsed") //необязательно, если имя колонки совпадает с именем поля
    private Boolean isUsed;

    @Column(name = "speed") //необязательно, если имя колонки совпадает с именем поля
    private Double speed;

    @Column(name = "crewSize") //необязательно, если имя колонки совпадает с именем поля
    private Integer crewSize;

    @Column(name = "rating") //необязательно, если имя колонки совпадает с именем поля
    private Double rating;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlanet() {
        return planet;
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public void setShipType(ShipType shipType) {
        this.shipType = shipType;
    }

    public Date getProdDate() {
        return prodDate;
    }

    public void setProdDate(Date prodDate) {
        this.prodDate = prodDate;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Integer getCrewSize() {
        return crewSize;
    }

    public void setCrewSize(Integer crewSize) {
        this.crewSize = crewSize;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
