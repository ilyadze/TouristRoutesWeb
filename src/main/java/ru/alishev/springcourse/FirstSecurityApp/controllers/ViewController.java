package ru.alishev.springcourse.FirstSecurityApp.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.alishev.springcourse.FirstSecurityApp.models.*;
import ru.alishev.springcourse.FirstSecurityApp.models.enums.Category;
import ru.alishev.springcourse.FirstSecurityApp.models.enums.Role;
import ru.alishev.springcourse.FirstSecurityApp.projection.RouteProjection;
import ru.alishev.springcourse.FirstSecurityApp.repositories.*;
import ru.alishev.springcourse.FirstSecurityApp.services.PersonService;
import ru.alishev.springcourse.FirstSecurityApp.services.PointService;
import ru.alishev.springcourse.FirstSecurityApp.services.RegistrationService;
import ru.alishev.springcourse.FirstSecurityApp.services.RouteService;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Controller
public class ViewController {
    private final RouteRepository routeRepository;
    private final RouteService routeService;
    private final RegistrationService registrationService;

    private final PersonService personService;
    private final CommentRepository commentRepository;
    private final TopicRepository topicRepository;
    private final MessageRepository messageRepository;
    private final PeopleRepository peopleRepository;
    private final RatingRepository ratingRepository;
    private final PointRepository pointRepository;

    private final PointService pointService;

    public ViewController(RouteRepository routeRepository, RouteService routeService, RegistrationService registrationService, PersonService personService,
                          CommentRepository commentRepository,
                          TopicRepository topicRepository,
                          MessageRepository messageRepository,
                          PeopleRepository peopleRepository,
                          RatingRepository ratingRepository,
                          PointRepository pointRepository, PointService pointService) {
        this.routeRepository = routeRepository;
        this.routeService = routeService;
        this.registrationService = registrationService;
        this.personService = personService;
        this.commentRepository = commentRepository;
        this.topicRepository = topicRepository;
        this.messageRepository = messageRepository;
        this.peopleRepository = peopleRepository;
        this.ratingRepository = ratingRepository;
        this.pointRepository = pointRepository;
        this.pointService = pointService;
    }

    @GetMapping("/main")
    public String main(Model model, Authentication authentication) {
        model.addAttribute("role", authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN")));
        model.addAttribute("routes", routeRepository.findAllBy());
        return "index";
    }

    @GetMapping("/profile")
    public String account(Model model, Authentication authentication1, Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        model.addAttribute("role", authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN")));
//        model.addAttribute("user", personService.findByUsername(authentication.getName()));
//        System.out.println(personService.getUserByPrincipal(principal));
//        System.out.println(authentication.getName());
//        System.out.println(personService.findByUsername(authentication.getName()));
        model.addAttribute("user", personService.findByUsername(authentication.getName()));
        model.addAttribute("thisAccount", true);
        return "accountPage";
    }

    @GetMapping("/routesPage/{id}")
    public String routesPageId(@PathVariable("id")Integer id, Model model, Authentication authentication) {
        model.addAttribute("role", authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN")));
        List<Route> routes = routeService.index();
        model.addAttribute("route", routes.stream().filter(route -> route.getId() == id).findFirst().get());
        return "routePage";
    }

    @PostMapping("/routeEdit/{id}")
    public String editRoute(Model model, @PathVariable("id")Integer id, @RequestParam("name")String name, @RequestParam("distance")Double distance, @RequestParam("category")String category, @RequestParam("description")String description, Principal principal) {
        Set<Category> categorys = new HashSet<>();
        categorys.add(Category.valueOf(category));
//        Route route = routeRepository.findById(id).get();
        List<RouteProjection> allRoutes = routeRepository.findAllBy();
        Route route = allRoutes.stream().filter(routeProjection -> routeProjection.getId()==id).findFirst().get().toRoute();
//        System.out.println(route);
//        route.setPerson(personService.getUserByPrincipal(principal));
        System.out.println(route);
        route.setDescription(description);
        route.setDistance(distance);
        route.setName(name);
        route.setCategory(categorys);
        routeService.save(route);

//        model.addAttribute("route", route);
        return "redirect:/routesPage/" + id;
    }

    @GetMapping("/route/{id}")
    public String routesInfo(@PathVariable("id")Integer id, Model model, Authentication authentication) {
        model.addAttribute("role", authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN")));

        List<Route> routes = routeService.index();
        List<Comment> comments = commentRepository.findAll();
//        System.out.println(routes.stream().filter(route -> route.getId() == id).findFirst());
        model.addAttribute("comments", comments.stream()
                .filter(comment -> comment.getRoute().getId() == id)
                .collect(Collectors.toList()));
        model.addAttribute("route", routes.stream().filter(route -> route.getId() == id).findFirst().get());
        return "route";
    }


    @GetMapping("/routesPage")
    public String routesPage(Model model, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("role", isAdmin);

        if (isAdmin) {
            model.addAttribute("routes", routeRepository.findAllBy());
        }
        else {
            List<Route> routes = routeRepository.findAllBy().stream().map(routeProjection -> routeProjection.toRoute()).collect(Collectors.toList());
            int id = peopleRepository.findByUsername(authentication.getName()).get().getId();
            List<String> all = routeRepository.findRoutesByPersonId(id);
            List<Route> result = all.stream()
                    .map(name -> routes.stream()
                            .filter(route -> route.getName().equals(name))
                            .findFirst()
                            .orElse(null))
                    .collect(Collectors.toList());
            model.addAttribute("routes", result);
        }
        return "routes";
    }

    @GetMapping("/usersPage")
    public String usersPage(Model model, Authentication authentication) {
        model.addAttribute("role", authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN")));
        model.addAttribute("users", registrationService.findAll());
        return "users";
    }

    @GetMapping("/usersPage/{id}")
    public String usersPageId(@PathVariable("id")Integer id, Model model, Authentication authentication) {
        model.addAttribute("role", authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN")));
        List<Person> users = registrationService.findAll();
        Person person = personService.findByUsername(authentication.getName());
        model.addAttribute("user", users.stream().filter(user -> user.getId() == id).findFirst().get());
        model.addAttribute("thisAccount", person == users.stream().filter(user -> user.getId() == id).findFirst().get());
        return "accountPage";
    }
    @GetMapping("/admin")
    public String adminPage() {
        return "admin";
    }


    @GetMapping("/forum")
    public String forum(Model model) {
        model.addAttribute("topics", topicRepository.findAll());
        return "forum";
    }

    @GetMapping("/forum/{id}")
    public String forum(@PathVariable("id")Long id, Model model) {
        model.addAttribute("topics", topicRepository.findAll());
        model.addAttribute("messages", messageRepository.findMessagesByTopic_Id(id));
        return "forum";
    }

    @GetMapping("/route/add")
    public String routeAdd(Model model, Authentication authentication) {
        model.addAttribute("route", new Route());
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("role", isAdmin);

        return "routeAdd";
    }

    @PostMapping("/route/add")
    public String performRegistration(@RequestParam("name")String name, @RequestParam("distance")Double distance, @RequestParam("category")String category, @RequestParam("description")String description, Principal principal) {
        Set<Category> categorys = new HashSet<>();
        categorys.add(Category.valueOf(category));

        Route route = new Route(name, distance, description, categorys);

        System.out.println(route);
        route.setPerson(personService.getUserByPrincipal(principal));
        System.out.println(route);
        routeService.save(route);
        int id = routeService.index().stream().filter(route1 -> route1.getName()==name).findFirst().get().getId();
        return "redirect:/routesPage/" + id;
    }

    @PostMapping("/message")
    public String addMessage(@RequestParam("content") String content, @RequestParam("id") Long id, Principal principal) {
//        System.out.println(content);
//        System.out.println(id);
        Message message = new Message();
        Topic topic = topicRepository.findById(id).get();
        Person person = personService.getUserByPrincipal(principal);
//        System.out.println(topic);
        message.setTopic(topic);
        message.setPerson(person);
        message.setContent(content);
        messageRepository.save(message);
//        System.out.println(message);
        return "redirect:/forum/" + id;
    }


    @PostMapping("/comment")
    public String addComment(@RequestParam("rating") Integer rating, @RequestParam("comment") String comment, @RequestParam("id") Integer id, Principal principal) {
//        System.out.println(rating);
//        System.out.println(comment);
//        System.out.println(id);
        Rating newRating = new Rating(rating);
        Comment newComment = new Comment(personService.getUserByPrincipal(principal),  newRating, routeRepository.findById(id).get(),comment);
//        System.out.println(newRating);
//        System.out.println(newComment);
        commentRepository.save(newComment);
        return "redirect:/route/" + id;
    }

    @GetMapping("/route-delete/{id}")
    public String deleteRoute(@PathVariable("id")int id) {
        List<Point> allPoints = pointRepository.findAll();
        List<Point> result = allPoints.stream().filter(point -> point.getRoute().getId() == id).collect(Collectors.toList());
        for(Point el :result) {
            pointService.delete(el.getId());
        }
//        List<Rating> ratings = ratingRepository.findAll().stream().filter().collect(Collectors.toList());
//        List<Comment> comments = commentRepository.findAll();
//        comments = comments.stream().filter(comment -> comment.getRoute().getId() == id).collect(Collectors.toList());

//        System.out.println(comments);
//        for(Comment comment:comments) {
//            Rating rating = ratingRepository.findAll().stream().filter(rating1 -> rating1.getComment().getId() == comment.getId()).findFirst().get();
////            System.out.println(rating);
////            ratingRepository.deleteById(rating.getId());
//            commentRepository.delete(comment);
//        }
//        routeRepository.deleteById(id);
//        System.out.println(id);
        return "redirect:/routesPage";
    }


    @PostMapping("/edit")
    public String editUser( @RequestParam("name")String name, @RequestParam("email")String email, @RequestParam("role") String role, Principal principal) {
//        List<String> taskParam = Arrays.asList(stringTask.split(","));
//        Task task = new Task().parseToTask(taskParam);
//        User user = userService.getUserByPrincipal(principal);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println(role);
        Person person = personService.findByUsername(name);
        person.setEmail(email);
        person.setUsername(name);
        Set<Role> roles = new HashSet<>();
        roles.add(Role.valueOf(role));
        person.setRoles(roles);
//        System.out.println(person);
        peopleRepository.save(person);
//        task.setUser(user);
//        taskService.saveTask(task);
        if(name == authentication.getName()) {
            return "redirect:/profile";
        }
        return "redirect:/usersPage/" + person.getId();
    }
}
