package ru.alishev.springcourse.FirstSecurityApp.projection;

import ru.alishev.springcourse.FirstSecurityApp.models.Route;
import ru.alishev.springcourse.FirstSecurityApp.models.enums.Category;

import java.util.Set;

public interface RouteProjection {
    int getId();
    String getName();
    double getDistance();
    String getDescription();
    Set<Category> getCategory();

    default Route toRoute() {
        return new Route(getId(), getName(), getDistance(), getDescription(), getCategory());
    }
}
