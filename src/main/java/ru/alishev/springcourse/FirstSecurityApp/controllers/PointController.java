package ru.alishev.springcourse.FirstSecurityApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.alishev.springcourse.FirstSecurityApp.models.Point;
import ru.alishev.springcourse.FirstSecurityApp.models.Route;
import ru.alishev.springcourse.FirstSecurityApp.repositories.PeopleRepository;
import ru.alishev.springcourse.FirstSecurityApp.repositories.RouteRepository;
import ru.alishev.springcourse.FirstSecurityApp.services.PersonService;
import ru.alishev.springcourse.FirstSecurityApp.services.PointService;
import ru.alishev.springcourse.FirstSecurityApp.services.RouteService;
import ru.alishev.springcourse.FirstSecurityApp.util.PointErrorResponse;
import ru.alishev.springcourse.FirstSecurityApp.util.PointNotFoundException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/points")
public class PointController {

    private final PointService pointService;
    private final RouteService routeService;
    private  final PersonService personService;

    @Autowired
    public PointController(PointService pointService,
                           PeopleRepository peopleRepository, RouteRepository routeRepository, RouteService routeService, PersonService personService) {
        this.pointService = pointService;
        this.routeService = routeService;
        this.personService = personService;
    }

    @GetMapping()
    public List<Point> getPoints() {
        List<Point> points = pointService.index();
        points.forEach(point -> point.setRoute(null));
        points.forEach(point -> point.setPerson(null));
        return points;
    }

    @GetMapping("/{id}")
    public List<Point> getPoint(@PathVariable("id") int id) {
        List<Point> points = pointService.index();

        List<Point> result = points.stream().filter(point -> point.getRoute().getId() == id).collect(Collectors.toList());
        points.forEach(point -> point.setRoute(null));
        points.forEach(point -> point.setPerson(null));
        return result;
//        Point point = pointService.show(id);
//        point.setRoute(null);
//        point.setPerson(null);
//        return point;
    }

    @PostMapping("/{id}")
    public void processPoints(@PathVariable("id") Integer id, @RequestBody List<List<Double>> points, Principal principal) {
        // Преобразование полученных данных из JSON в объекты класса Point
        List<Point> processedPoints = new ArrayList<>();
        List<Route> routes = routeService.index();
        List<Point> allPoints = pointService.index();

        List<Point> result = allPoints.stream().filter(point -> point.getRoute().getId() == id).collect(Collectors.toList());
//        System.out.println(routes);
//        return routeService.show(id);
        for(Point el :result) {
            pointService.delete(el.getId());
        }
        for (List<Double> point : points) {
            double latitude = point.get(0);
            double longitude = point.get(1);
            System.out.println("lat - " + latitude + "  long - " + longitude);
            Point processedPoint = new Point(latitude, longitude, routes.stream()
                    .filter(route -> route.getId() == id)
                    .findFirst().get(), personService.getUserByPrincipal(principal));

//            Point processedPoint = new Point(latitude, longitude, personService.getUserByPrincipal(principal));


            processedPoints.add(processedPoint);
        }

        // Действия с объектами Point
        // ...
        System.out.println(processedPoints);
        processedPoints.stream().forEach(pointService::save);

    }
    @GetMapping("/new")
    public String newPerson(@ModelAttribute("point") Point point) {
        return "people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("point")  Point point,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "people/new";

        pointService.save(point);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit( @PathVariable("id") int id) {
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("point") Point point, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "people/edit";

        pointService.update(point);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        pointService.delete(id);
        return "redirect:/people";
    }

    @ExceptionHandler
    private ResponseEntity<PointErrorResponse> handleException(PointNotFoundException e) {
        PointErrorResponse response = new PointErrorResponse(
                "Point with this id wasn't found",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}

