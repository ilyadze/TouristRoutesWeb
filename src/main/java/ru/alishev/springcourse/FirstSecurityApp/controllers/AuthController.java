package ru.alishev.springcourse.FirstSecurityApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.alishev.springcourse.FirstSecurityApp.models.Person;
import ru.alishev.springcourse.FirstSecurityApp.services.PersonService;
import ru.alishev.springcourse.FirstSecurityApp.services.RegistrationService;
import ru.alishev.springcourse.FirstSecurityApp.util.PersonValidator;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final PersonValidator personValidator;
    private final RegistrationService registrationService;

    private final PersonService personService;

    @Autowired
    public AuthController(PersonValidator personValidator, RegistrationService registrationService, PersonService personService) {
        this.personValidator = personValidator;
        this.registrationService = registrationService;
        this.personService = personService;
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(name = "error", required = false) String error, Model model) {
        model.addAttribute("error", error);
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("person") Person person) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("person")@Valid Person person, BindingResult bindingResult) {
        personValidator.validate(person, bindingResult );

        if (bindingResult.hasErrors()) {
            return "auth/registration";
        }

        registrationService.register(person);

        return "redirect:/auth/login";
    }

    @GetMapping("/logout")
    public String logout() {
        // Получаем текущую аутентификацию и осуществляем выход
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(null);
        }

        // Перенаправляем пользователя на страницу выхода или на другую страницу, если требуется
        return "redirect:/auth/login?logout";
    }

    @GetMapping("/users")
    @ResponseBody
    public List<Person> getPersons() {
        return personService.index();
    }


}
