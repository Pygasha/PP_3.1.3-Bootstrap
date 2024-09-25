package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.services.RegistrationService;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import javax.validation.Valid;


@Controller()
@RequestMapping("/admin")
public class AdminController {


    private final RegistrationService registrationService;
    private final UserService userService;
    private final RoleService roleService;


    @Autowired
    public AdminController( RegistrationService registrationService, UserService userService, RoleService roleService) {

        this.registrationService = registrationService;
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String adminPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        model.addAttribute("principal", user);
        model.addAttribute("users", userService.findAll());
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("newUser", new User());
        return "admin";
    }

    @PostMapping("/new")
    public String registrationPost(@ModelAttribute("newUser")  User user, @AuthenticationPrincipal UserDetails userDetails) {


        registrationService.saveUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/update/{id}")
    public String patchAdminRedactor( @ModelAttribute("user")  User user, @PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails) {
        userService.refactorUser(user, id);
        return "redirect:/admin";
    }

    @PostMapping("/delete/{id}")
    public String adminDelete(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }


}