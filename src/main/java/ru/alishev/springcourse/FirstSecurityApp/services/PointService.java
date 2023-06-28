package ru.alishev.springcourse.FirstSecurityApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alishev.springcourse.FirstSecurityApp.models.Point;
import ru.alishev.springcourse.FirstSecurityApp.repositories.PointRepository;

import java.util.List;

@Service
public class PointService {

    private final PointRepository pointRepository;

    @Autowired
    public PointService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    public List<Point> index() {
        return pointRepository.findAll();
    }

    public Point show(int id) {
        return pointRepository.getById(id);
    }

    public void delete(int id) {
        pointRepository.deleteById(id);
    }

    public void save(Point point) {
        pointRepository.save(point);
    }

    public void update(Point point) {
        pointRepository.save(point);
    }

    public List<Point> findByRouteId(Integer id) {
        return pointRepository.findByRouteId(id);
    }

}
