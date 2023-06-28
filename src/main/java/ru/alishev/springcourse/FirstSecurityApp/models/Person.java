package ru.alishev.springcourse.FirstSecurityApp.models;

import org.hibernate.annotations.Cascade;
import ru.alishev.springcourse.FirstSecurityApp.models.enums.Role;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Entity
@Table(name = "Person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 100, message = "Имя должно быть от 2 до 100 символов длиной")
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();
    @OneToMany(mappedBy = "person")
    private List<Route> routes;
    @OneToMany(mappedBy = "person")
    private List<Point> points;

    @Column(name = "email")
    private String email;

    @OneToOne
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private Comment comment;

    // Конструктор по умолчанию нужен для Spring
    public Person() {
    }

    public Person(int id, String username, String password, HashSet<Role> role, List<Route> routes, List<Point> points, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = role;
        this.routes = routes;
        this.points = points;
        this.email = email;
    }

    public Person(int id, String username, String password, Set<Role> roles, List<Route> routes, List<Point> points, String email, Comment comment) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.routes = routes;
        this.points = points;
        this.email = email;
        this.comment = comment;
    }

    public Person(int id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public Person(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public List<Route> getRoutes() {
        return routes;
    }


    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public List<String> Roles() {
        return roles.stream()
                .map(Role::getAuthority)
                .collect(Collectors.toList());
    }

}