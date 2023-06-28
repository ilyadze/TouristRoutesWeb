package ru.alishev.springcourse.FirstSecurityApp.models;

import org.hibernate.annotations.Cascade;
import ru.alishev.springcourse.FirstSecurityApp.models.enums.Role;
import ru.alishev.springcourse.FirstSecurityApp.models.Route;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;

    @OneToOne(cascade = CascadeType.ALL)
    private Rating rating;

    @ManyToOne
    @JoinColumn(name = "route_id", referencedColumnName = "route_id")
    private Route route;


    private String text;




    public Comment() {}

    public Comment(int id, Person person) {
        this.id = id;
        this.person = person;
    }

    public Comment(Person person, Rating rating, Route route, String text) {
        this.person = person;
        this.rating = rating;
        this.route = route;
        this.text = text;
    }

    public Comment(int id, Person person, Rating rating, String text) {
        this.id = id;
        this.person = person;
        this.rating = rating;
        this.text = text;
    }

    public Comment(int id, Person person, Rating rating, Route route, String text) {
        this.id = id;
        this.person = person;
        this.rating = rating;
        this.route = route;
        this.text = text;
    }


    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Comment(int id, Person person, Rating rating) {
        this.id = id;
        this.person = person;
        this.rating = rating;
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

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }


    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", person=" + person +
                ", route=" + route +
                ", text='" + text + '\'' +
                '}';
    }
}
