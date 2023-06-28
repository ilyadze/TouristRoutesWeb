package ru.alishev.springcourse.FirstSecurityApp.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Point")
public class Point {

    @Id
    @Column(name = "point_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
//    @NotEmpty(message = "Название точки не должно быть пустым")
    private String name;
    @Column(name = "latitude")
//    @NotEmpty(message = "Координаты не должны быть пустыми")
    private double latitude;
    @Column(name = "longitude")
//    @NotEmpty(message = "Координаты не должны быть пустыми")
    private double longitude;
//    @ManyToMany(mappedBy = "points")
//    private List<Route> routes;
    @ManyToOne
    @JoinColumn(name = "route_id", referencedColumnName = "route_id")
    private Route route;
    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;

    private String description;

    public Point() {}


    public Point(int id, String name, double latitude, double longitude, Route route, Person person) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.route = route;
        this.person = person;
    }

    public Point(double latitude, double longitude, Route route, Person person) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.route = route;
        this.person = person;
    }

    public Point(double latitude, double longitude, Person person) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.person = person;
    }

    public Point(int id, String name, double latitude, double longitude, Route route, Person person, String description) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.route = route;
        this.person = person;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    @Override
    public String toString() {
        return "Point{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", person=" + person +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return id == point.id && Double.compare(point.latitude, latitude) == 0 && Double.compare(point.longitude, longitude) == 0 && Objects.equals(name, point.name) && Objects.equals(route, point.route) && Objects.equals(person, point.person);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, latitude, longitude, route, person);
    }
}
