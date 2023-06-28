package ru.alishev.springcourse.FirstSecurityApp.models;

import ru.alishev.springcourse.FirstSecurityApp.models.enums.Category;
import ru.alishev.springcourse.FirstSecurityApp.models.enums.Role;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "Route")
public class Route {

    @Id
    @Column(name = "route_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
//    @NotEmpty(message = "Название маршрута не должно быть пустым")
    private String name;

    @OneToMany(mappedBy = "route",cascade = CascadeType.ALL)
    private List<Point> points;
    @Column(name = "distance")
//    @NotEmpty(message = "Название маршрута не должно быть пустым")
    private double distance;
    @Column(name = "description")
    private String description;



    @ElementCollection(targetClass = Category.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "route_category",
            joinColumns = @JoinColumn(name = "route_id"))
    @Enumerated(EnumType.STRING)
    private Set<Category> category = new HashSet<>();
    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;



//    @Transient
    @OneToMany(mappedBy = "route",cascade = CascadeType.ALL)
    private List<Comment> comments;

//    @OneToMany(mappedBy = "route")
//    private Comment comment;


    public Route() {}

    public Route(int id, String name, List<Point> points, double distance, String description, Set<Category> category, Person person, Comment comment) {
        this.id = id;
        this.name = name;
        this.points = points;
        this.distance = distance;
        this.description = description;
        this.category = category;
        this.person = person;
//        this.comment = comment;
    }

    public Route(String name, double distance, String description, Set<Category> category) {
        this.name = name;
        this.distance = distance;
        this.description = description;
        this.category = category;
    }

    public Route(int id, String name, List<Point> points, double distance, String description, Set<Category> category, Person person, List<Comment> comments) {
        this.id = id;
        this.name = name;
        this.points = points;
        this.distance = distance;
        this.description = description;
        this.category = category;
        this.person = person;
        this.comments = comments;
    }

    public Route(int id, String name, double distance, String description, Set<Category> category) {
        this.id = id;
        this.name = name;
        this.distance = distance;
        this.description = description;
        this.category = category;
    }


    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Category> getCategory() {
        return category;
    }

    public void setCategory(Set<Category> category) {
        this.category = category;
    }

    //    public String getCategory() {
//        return category;
//    }
//
//    public void setCategory(String category) {
//        this.category = category;
//    }

    public Person getUser() {
        return person;
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

//    public Comment getComment() {
//        return comment;
//    }
//
//    public void setComment(Comment comment) {
//        this.comment = comment;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return id == route.id && Double.compare(route.distance, distance) == 0 && Objects.equals(name, route.name) && Objects.equals(points, route.points) && Objects.equals(description, route.description) && Objects.equals(category, route.category) && Objects.equals(person, route.person);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, points, distance, description, category, person);
    }

    @Override
    public String toString() {
        return "Route{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", distance=" + distance +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", person=" + person +
                '}';
    }

    public List<String> categorys() {
        return category.stream()
                .map(Category::toString)
                .collect(Collectors.toList());
    }

}
