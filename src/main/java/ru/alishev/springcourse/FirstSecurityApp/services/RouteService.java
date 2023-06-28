package ru.alishev.springcourse.FirstSecurityApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alishev.springcourse.FirstSecurityApp.models.Route;
import ru.alishev.springcourse.FirstSecurityApp.projection.RouteProjection;
import ru.alishev.springcourse.FirstSecurityApp.repositories.RouteRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RouteService {

    private final RouteRepository routeRepository;

    @Autowired
    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public List<Route> index() {

        List<RouteProjection> routeProjections = routeRepository.findAllBy();
        return routeProjections.stream()
                .map(RouteProjection::toRoute)
                .collect(Collectors.toList());
    }

    public List<Route> findByPersonId(Integer id) {

        List<RouteProjection> routeProjections = routeRepository.findAllByPersonId(id);
        return routeProjections.stream()
                .map(RouteProjection::toRoute)
                .collect(Collectors.toList());
    }

    public Route show(int id) {
        RouteProjection route = routeRepository.findById(id);
//        System.out.println(route);
        return route.toRoute();
    }

    public void delete(int id) {
        routeRepository.deleteById(id);
    }

    public void save(Route route) {
        routeRepository.save(route);
    }

    public void update(Route route) {
        routeRepository.save(route);
    }
}

