package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UsersService;
import java.security.Principal;



@Controller
@RequestMapping()
public class UsersController {

    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService, RoleService roleService) {
        this.usersService = usersService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/user")
    public String getUserPage(Model model, Principal principal) {
        model.addAttribute("user",
                usersService.getUserById(usersService.getUserByUsername(principal.getName()).getId()));
        return "show_user";
    }
}
