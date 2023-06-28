package ru.alishev.springcourse.FirstSecurityApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alishev.springcourse.FirstSecurityApp.models.Point;

import java.util.List;

@Repository
public interface PointRepository extends JpaRepository<Point, Integer> {

    List<Point> findByRouteId(int id);

}
