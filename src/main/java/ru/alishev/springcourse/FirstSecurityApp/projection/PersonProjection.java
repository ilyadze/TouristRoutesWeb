package ru.alishev.springcourse.FirstSecurityApp.projection;

import ru.alishev.springcourse.FirstSecurityApp.models.Person;
import ru.alishev.springcourse.FirstSecurityApp.models.Route;

public interface PersonProjection {
    public int getId();
    public String getUsername();
    public String getEmail();

    default Person toPerson() {
        return new Person(getId(), getUsername(), getEmail());
    }
}
