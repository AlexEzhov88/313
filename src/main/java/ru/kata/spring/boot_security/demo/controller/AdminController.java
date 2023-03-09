package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UsersService;
import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UsersService usersService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UsersService usersService, RoleService roleService) {
        this.usersService = usersService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String getAdminPage(Model model) {
        model.addAttribute("users", usersService.getAllUsers());
        return "admin_page";
    }

    @GetMapping("/new")
    public String getUserCreateForm(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getRoles());
        return "new_user";
    }

    @PostMapping("/createNew")
    public String createUser(ModelMap model, @ModelAttribute("user") @Valid User user,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.getRoles());
            return "new_user";
        }
        usersService.saveUser(user);
        return "redirect:/admin";
    }

    @PatchMapping(value = "/{id}/edit")
    public String getUserEditForm(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("user", usersService.getUserById(id));
        model.addAttribute("roles", roleService.getRoles());
        return "edit_user";
    }

    @PatchMapping(value = "/{id}")
    public String updateUser(@ModelAttribute("user") User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "edit_user";
        }
        usersService.updateUser(user);
        return "redirect:/admin";
    }


    @DeleteMapping("/{id}/delete")
    public String removeUserById(@PathVariable("id") Integer id) {
        usersService.removeUserById(id);
        return "redirect:/admin";
    }
}
