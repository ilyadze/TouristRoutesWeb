package ru.alishev.springcourse.FirstSecurityApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.alishev.springcourse.FirstSecurityApp.models.Person;
import ru.alishev.springcourse.FirstSecurityApp.models.Route;
import ru.alishev.springcourse.FirstSecurityApp.projection.RouteProjection;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Integer> {
    List<RouteProjection> findAllBy();

    RouteProjection findById(int id);

    List<RouteProjection> findAllByPersonId(Integer personId);

    List<RouteProjection> findAllByPerson(Person person);


    @Query("SELECT r.name FROM Route r WHERE r.person.id = :personId")
    List<String> findRoutesByPersonId(@Param("personId") Integer personId);
}
