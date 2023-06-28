package ru.alishev.springcourse.FirstSecurityApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alishev.springcourse.FirstSecurityApp.models.Person;
import ru.alishev.springcourse.FirstSecurityApp.models.Route;
import ru.alishev.springcourse.FirstSecurityApp.projection.PersonProjection;
import ru.alishev.springcourse.FirstSecurityApp.projection.RouteProjection;
import ru.alishev.springcourse.FirstSecurityApp.repositories.PeopleRepository;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final PeopleRepository peopleRepository;


    @Autowired
    public PersonService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> index() {

        List<PersonProjection> personProjections = peopleRepository.findAllBy();
        return personProjections.stream()
                .map(PersonProjection::toPerson)
                .collect(Collectors.toList());
    }

    public Person findByUsername(String username) {
//        Optional<Person> person =
        System.out.println(peopleRepository.findByUsername(username));
        return peopleRepository.findByUsername(username).get();
    }

    public Person getUserByPrincipal(Principal principal) {
        if (principal == null) return new Person();
        return peopleRepository.findByUsername(principal.getName()).get();
    }
}
