package ru.alishev.springcourse.FirstSecurityApp.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.alishev.springcourse.FirstSecurityApp.models.Person;
import ru.alishev.springcourse.FirstSecurityApp.models.Route;
import ru.alishev.springcourse.FirstSecurityApp.services.RouteService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/routes")
public class RouteController {

    private final RouteService routeService;

    @Autowired
    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping()
    public List<Route> getRoutes() {
//        System.out.println(routeService.index());
        List<Route> routes = routeService.index();
//        System.out.println(routes);
        return routes;
    }

    @GetMapping("/{id}")
    public Route getRoute(@PathVariable("id")int id){
        List<Route> routes = routeService.index();
//        System.out.println(routes);
//        return routeService.show(id);
        return routes.stream()
                .filter(route -> route.getId() == id)
                .findFirst().get();
    }

    @GetMapping("/new")
    public String newRoute(@ModelAttribute("route") Route route) {
        return "people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("route")  Route route,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "people/new";

        routeService.save(route);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit( @PathVariable("id") int id) {
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("route") Route route, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "people/edit";

        routeService.update(route);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        routeService.delete(id);
        return "redirect:/people";
    }

}
